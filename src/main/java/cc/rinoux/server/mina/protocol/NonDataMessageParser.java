package cc.rinoux.server.mina.protocol;

import org.apache.mina.core.buffer.IoBuffer;

import cc.rinoux.server.mina.util.IoBufferTransUtil;
/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description <p><b>解析设备发送的数据 <br>
 * 	**
	 * 协议格式
	 * *******************包头（18位）********************|***数据**|**包尾**<br>
	 * 版本号 | 包类型 | 设备号 | 数据类型 | 发送者 | 数据长度  | 数据内容 |奇偶校验<br>
	 *    1  |   1   |   3   |    1    |   8   |    3    |    N    |   2    <br>
	***
 * @Datetime 2016年5月3日下午2:25:06
 */
public class NonDataMessageParser extends NonDataMessageProtocol {

	IoBuffer in;

	public NonDataMessageParser(IoBuffer in) {
		super();
		this.in = in;
	}

	@Override
	public byte getVersion() {
		//固定值为0x01
		return in.get(0);
	}

	@Override
	public byte getPacketType() {
		byte type = in.get(1);
		return type;
	}

	@Override
	public byte[] getDevice() {
		byte[] device = new byte[3];
		IoBuffer buffer = in.getSlice(2, 3);
		device = IoBufferTransUtil.ioBufferToByte(buffer);
		return device;
	}

	@Override
	public byte getSequence(){
		
		byte sequence = in.get(5);
		return sequence;
		
	}
	@Override
	public byte getHeaderCheck() {
		byte check = in.get(6);
		return check;
	}
}
