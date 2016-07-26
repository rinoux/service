package cc.rinoux.server.mina.helper;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cc.rinoux.server.exception.RequestDataNotExist;
import cc.rinoux.server.mina.util.ParityCheckUtil;
import cc.rinoux.server.mina.protocol.DataMessageHeaderWrapper;
import cc.rinoux.server.mina.server.SessionCache;
import cc.rinoux.server.mina.util.IntegerBytesUtil;

/**
 * 消息发送帮助工具，根据发送序列分包发送
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午9:49:41
 */
public class MessageSenderHelper implements MessageSenderHelperI{
	
	static final Logger logger = LoggerFactory.getLogger(MessageSenderHelper.class.getName());

	/**
	 * 每包的长度
	 */
	static final int PACKET_LEN = 1300;
	/**
	 * 每包中像素数据的长度
	 */
	static final int PACKET_DATA_LEN = 1280;
	/**
	 * 发送者用户ID
	 */
	int uid;
	/**
	 * 发送目标的ip地址
	 */
	String ip;
	/**
	 * 发送消息用IoBuffer装载，大小为1M
	 */
	IoBuffer buffer = IoBuffer.allocate(1024 *1024);
	/**
	 * 待发送消息
	 */
	byte[] message = null;	
	/**
	 * 待发送数据类型
	 */
	byte sendDataType;
	/**
	 * 包头
	 */
	byte[] header;
	/**
	 * 待发送数据分包
	 */
	byte[] temp = new byte[PACKET_LEN];
	/**
	 * socket连接会话缓存池
	 */
	SessionCache sessionCache = SessionCache.instance;
	/**
	 * 发送者
	 */
	String userName = null;
	/**
	 * 发送后剩余数据
	 */
	int remainData = 0;
	
	/**
	 * 去除bmp图片的66位包头后的像素数据
	 */
	byte[] pixelData;
	/**
	 * 307200数据分包为240，包号239最后一包
	 */
	int packetCount = (320 * 480 * 16)/(8 * PACKET_DATA_LEN) - 1;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public MessageSenderHelper(String ip, byte[] message) {
		super();
		this.ip = ip;
		this.message = message;
	}

	/**
	 * @param uid用户id<br>
	 * @param ip水杯ip<br>
	 * @param message待发送数据<br>
	 * @param sendDataType待发送数据类型（图片、备忘录等）<br>
	 * @throws RequestDataNotExist
	 */
	public MessageSenderHelper(int uid, String ip, byte[] message, byte sendDataType) throws RequestDataNotExist {
		super();
		this.uid = uid;
		this.ip = ip;
		this.message = message;
		this.sendDataType = sendDataType;		
		pixelData = this.subBytes(message, 66, message.length - 66);
		
	}

	/**
	 * 封装协议数据
	 * @param sequence<br>
	 * @throws RequestDataNotExist 
	 */
	public void protocolAssembler(int sequence) throws RequestDataNotExist {

		//根据包序生成对应的包头
		header = headerAssembler(sequence);
		//根据包头生成奇偶校检中“1”的个数，放在一包数据的最末尾两位
		byte[] parityOdd = IntegerBytesUtil.intTo2Bytes(ParityCheckUtil.getParity(header));
		//装载包头到数据分包（18位）
		for (int i = 0; i < 18; i++) {
			temp[i] = header[i];			
		}
		//装载一帧图片像素数据到数据分包（1280位）
		for (int i = 0; i < 1280; i++) {	
			temp[i + 18] = this.subBytes(pixelData, sequence * PACKET_DATA_LEN, PACKET_DATA_LEN)[i];	
		}

		//装载奇偶校验到包尾（2位）
		for (int i = 0; i < 2; i++) {
			temp[i + 1298] = parityOdd[i];			
		}				
	}

	/**
	 * 封装单个包头<br>
	 *  @param sequence 包序<br>
	 * @return 返回包头<br>
	 */
	public byte[] headerAssembler(int sequence){
		//生成16进制的包序
		byte sequenceB = (byte)sequence;
		//测试用设备号，实际从设备id获取
		byte[] deviceNumberB = new byte[]{0x00,0x00,0x01};
		//测试用发送者（第8位0x06），实际从用户获取。时间校正（第8位0x02）和台历数据(第8位0x04)为固定值
		byte[] senderB = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x06};
		//测试用数据长度，暂时留空
		byte[] dataLengthB = new byte[]{0x00,0x00,0x00};
		//包装成包头
		DataMessageHeaderWrapper headerWrapper  = new DataMessageHeaderWrapper(
				(byte)0x0A, 
				deviceNumberB, 
				sendDataType, 
				sequenceB, 
				senderB, 
				dataLengthB);		
		return headerWrapper.getHeader();
	}
	/**
	 * 处理接受到的发送序列
	 */
	@Override
	public void receivedPacketRequest(int sequence) {
		boolean dataExist = false;
		//如果message图像数据全是0X00或者message为空，则不存在数据可用于发送
		if (ParityCheckUtil.getParity(message) == 0 || message == null) {
			dataExist = false;
		}else {
			dataExist = true;
		}
		if (dataExist) {
			logger.debug("准备发送序列为" + sequence + "的分包数据");
			sendMessage(sequence);
		}else {
			logger.info("没有可用于发送的数据");
		}
	}


	/**
	 * 发送数据
	 *@param sequence序列<br>
	 *@return 是否已发送<br>
	 */
	public boolean sendMessage(int sequence) {
		logger.debug("发送序列" + sequence);
		if (message.length < PACKET_LEN) {
			logger.info("发送数据有误，请检查");
			return false;
		}else {
			remainData = pixelData.length - sequence * PACKET_DATA_LEN;
			if (0 >= remainData) {
				logger.info("发送完成，无剩余数据");
				SenderHelperPool.instance.removeHelper(ip);
				return false;
			}else {
				try {
					protocolAssembler(sequence);
				} catch (RequestDataNotExist e) {
					logger.info("请求的包序超出数据包含的包数量");
					e.printStackTrace();
				}
				
				buffer.clear();
				buffer.put(temp);
				String[] key = {ip};
				buffer.flip();
				sessionCache.sendMessage(key, buffer);				
				//发送完毕后清空数据，并移除helper

				
				if (sequence == packetCount) {
					message = null;
					logger.info("发送完毕");
					SenderHelperPool.instance.removeHelper(ip);
				}
				return true;
			}
			
		}
	}
	/**
	 * 截取byte[]数组<br>
	* @param src被截取的数组<br>
	* @param begin起点<br>
	* @param count长度<br>
	* @return 截取到的数组<br>
	* @throws RequestDataNotExist 
	 */
    public byte[] subBytes(byte[] src, int begin, int count) throws RequestDataNotExist {
        byte[] bs = new byte[count];
        if (begin > src.length || (begin + count) > src.length || count < 0) {				
        	throw new RequestDataNotExist();
		}
        for (int i=begin; i<begin+count; i++) 
        	bs[i-begin] = src[i];
        return bs;
    }
}
