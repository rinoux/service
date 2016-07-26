package cc.rinoux.server.mina.protocol;
/** 
* @author 杨锐
*         rinoux@foxmail.com
* @version 
* 发送给device的数据包模型
*/

public class DataMessageModel {
	/**
	 * 协议格式
	 * *******************包头（18位）********************|***数据**|**包尾**
	 * 版本号 | 包类型 | 设备号 | 数据类型 | 发送者 | 数据长度  | 数据内容 |奇偶校验
	 *    1  |   1   |   3   |    1    |   8   |    3    |    N    |   2   
	***/
	byte vesion;
	byte packetType;
	byte deviceNumber;
	byte dataType;
	byte packetOrder;
	byte[] sender;
	byte[] dataLength;
	byte[] content;
	byte parityValue;
	public byte getVesion() {
		return vesion;
	}
	public void setVesion(byte vesion) {
		this.vesion = vesion;
	}
	public byte getPacketType() {
		return packetType;
	}
	public void setPacketType(byte packetType) {
		this.packetType = packetType;
	}
	public byte getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(byte deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public byte getDataType() {
		return dataType;
	}
	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}
	public byte getPacketOrder() {
		return packetOrder;
	}
	public void setPacketOrder(byte packetOrder) {
		this.packetOrder = packetOrder;
	}
	public byte[] getSender() {
		return sender;
	}
	public void setSender(byte[] sender) {
		this.sender = sender;
	}
	public byte[] getDataLength() {
		return dataLength;
	}
	public void setDataLength(byte[] dataLength) {
		this.dataLength = dataLength;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public byte getParityValue() {
		return parityValue;
	}
	public void setParityValue(byte parityValue) {
		this.parityValue = parityValue;
	}
	public DataMessageModel(byte vesion, byte packetType, byte deviceNumber,
			byte dataType, byte packetOrder, byte[] sender, byte[] dataLength, byte[] content, byte parityValue) {
		super();
		this.vesion = vesion;
		this.packetType = packetType;
		this.deviceNumber = deviceNumber;
		this.dataType = dataType;
		this.packetOrder = packetOrder;
		this.sender = sender;
		this.dataLength = dataLength;
		this.content = content;
		this.parityValue = parityValue;
	}

	
}
