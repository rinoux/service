package cc.rinoux.server.mina.protocol;
/** 
* @author   ����
* @email    rinoux@foxmail.com
* @company  �����ϴ���ά�Ƽ����޹�˾
*
* @version  DataMessageHeaderWrapper.java
* @date     2016��4��21�� ����1:41:26 
* @function TODO
*/

public class DataMessageHeaderWrapper {

	byte vesion;
	byte packetType;
	byte[] deviceNumber;
	byte dataType;
	byte packetSequence;
	byte[] sender;
	byte[] dataLength;
	
	/**
	 * ��ͷ
	 */
	private byte[] header = new byte[18];
	
	public byte getPacketType() {
		return packetType;
	}
	public void setPacketType(byte packetType) {
		this.packetType = packetType;
	}
	public byte[] getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(byte[] deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public byte getDataType() {
		return dataType;
	}
	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}
	public byte getPacketSequence() {
		return packetSequence;
	}
	public void setPacketSequence(byte packetSequence) {
		this.packetSequence = packetSequence;
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
	public DataMessageHeaderWrapper(byte packetType, byte[] deviceNumber, byte dataType, byte packetSequence,
			byte[] sender, byte[] dataLength) {
		super();
		this.packetType = packetType;
		this.deviceNumber = deviceNumber;
		this.dataType = dataType;
		this.packetSequence = packetSequence;
		this.sender = sender;
		this.dataLength = dataLength;
	}
	
	public byte[] getHeader(){
		header[0] = 0x01;
		header[1] = packetType;
		for (int i = 0; i < 3; i++) {
			header[i + 2] = deviceNumber[i];
			
		}
		header[5] = dataType;
		header[6] = packetSequence;
		for (int i = 0; i < 8; i++) {
			header[i + 7] = sender[i];
			
		}
		for (int i = 0; i < 3; i++) {
			header[i + 15] = dataLength[i];
			
		}
			
		return header;
	}
}
