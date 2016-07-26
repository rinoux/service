package cc.rinoux.server.mina.coder;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cc.rinoux.common.util.SpringBeanManager;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.mina.helper.MessageSenderHelperI;
import cc.rinoux.server.mina.helper.SenderHelperPool;
import cc.rinoux.server.mina.protocol.NonDataMessageParser;
import cc.rinoux.server.mina.protocol.NonDataMsgType;
import cc.rinoux.server.mina.server.SessionCache;
import cc.rinoux.server.mina.util.ParityCheckUtil;
import cc.rinoux.server.mina.util.IntegerBytesUtil;
import cc.rinoux.server.mina.util.TrimedIpString;
import cc.rinoux.server.model.Cup;

/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 解码
 * @Datetime 2016年5月3日上午10:11:15
 */

@Component
public class MyMessageDecoder implements MessageDecoder {
	static final Logger logger = LoggerFactory.getLogger(MyMessageDecoder.class.getName());
	MessageSenderHelperI helper;
	//@Resource(name = "cupMapper")
	private CupMapper cupMapper;
	Cup record = new Cup();

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		//防止粘包
		IoBuffer buffer = IoBuffer.allocate(7).setAutoExpand(false);
		buffer = in.getSlice(0, 7);
		// 此处判断数据包是否发送完整，OK表示数据完整可以开始解码
		Boolean legal = ParityCheckUtil.oddParityCheck(buffer);
		if (legal) {
			logger.debug("解码成功！");
			return MessageDecoderResult.OK;
		}else {
			logger.info("解码失败！");
			return MessageDecoderResult.NOT_OK;
		}		
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		logger.debug("客户端发送的原始数据" + in.toString());		
		//logger.info("所在service:" + session.getService().toString());
		
		//由于无法从@autowired自动注入，采取手动注入
		cupMapper = (CupMapper) SpringBeanManager.getBean("cupMapper");
		
		/**
		IoService localService = session.getService();

		Iterator<Long> iterator = localService.getManagedSessions().keySet().iterator();
		while (iterator.hasNext()) {
			logger.info(iterator.next().toString());
		}
		
		*/
		//String ip = session.getRemoteAddress().toString();
		String ip = TrimedIpString.getTrimedIpString(session.getRemoteAddress().toString());
		IoBuffer buffer = in.getSlice(0, 7);		
		NonDataMessageParser parser = new NonDataMessageParser(buffer);
		byte status = parser.getPacketType();
		int sequence = parser.getSequence() & 0xff;
		//设备号
		//byte[] deviceID = nonDataMessageReq.getDevice();
		int cupId = IntegerBytesUtil.bytesToInteger(parser.getDevice());
		logger.debug("设备ID：" + cupId);
		//PropertiesUtil.write2prop("cupip.properties", "Device:No." + cupId, ip);		
		session.setAttribute("message", buffer);
		record = cupMapper.selectByPrimaryKey(cupId);
		//设置ip
		record.setIp(ip);
		
		switch (status) {
		case 0x00:
			logger.debug("心跳包！");
			out.write(NonDataMsgType.HEART_BEAT);
			break;
		case 0x02:
			logger.debug("满数据！");
			//设置cup的存储状态为满
			record.setMemoryStatus(NonDataMsgType.DATA_FULL.toString());
			out.write(NonDataMsgType.DATA_FULL);
			
			break;
		case 0x04:
			logger.debug("空数据！");
			//设置cup的存储状态为空
			record.setMemoryStatus(NonDataMsgType.DATA_EMPTY.toString());
			out.write(NonDataMsgType.DATA_EMPTY);
			break;
		case 0x06:
			logger.debug("请求连接提示！");
			out.write(NonDataMsgType.CONN_ACK_REQUEST);
			break;
		case 0x08:
			logger.debug("请求图片数据！");
			try {
				if (SessionCache.instance.getSession(ip) == null) {
					logger.info("device在未被缓存连接情况下请求未知数据");
					/**
					 * TODO 处理错误请求<br>
					 * 建议：发送错误码给设备，设备接受后重新发心跳
					 */
				}else {
					logger.debug("请求第" + sequence +"个包数据");
					//获取包序
					//根据ip查找相应的消息发送处理工具
					helper = SenderHelperPool.instance.getHelper(ip);
					if (helper == null) {
						logger.info("未找到helper，无法请求数据");
					}else {
						//收到继续发送第sequence个数据包的请求
						helper.receivedPacketRequest(sequence);
					}	
				}
			} catch (Exception e) {
				// TODO: handle exception
			}			
			break;			
		default:
			//设置cup的存储状态为未知
			record.setMemoryStatus(NonDataMsgType.UNKNOW_STATUS.toString());
			out.write(NonDataMsgType.UNKNOW_STATUS);
		}
		cupMapper.updateByPrimaryKeySelective(record);
		return MessageDecoderResult.OK;
	}
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
	}

}
