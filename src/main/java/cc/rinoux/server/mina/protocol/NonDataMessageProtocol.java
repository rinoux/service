package cc.rinoux.server.mina.protocol;
/**
 * 非数据协议
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年4月21日下午1:09:31
 */
public abstract class NonDataMessageProtocol {
	
	
	/**获取版本号
	 * * @return
	 */
	public abstract byte getVersion();
	/**获取包类型
	 * * @return
	 */
	public abstract byte getPacketType();
	/**
	 * 获取设备号
	 * * @return
	 */
	public abstract byte[] getDevice();
	/**
	 * 获取包号数* @return
	 */
	public abstract byte getSequence();
	/**
	 * 获取包头校检* @return
	 */
	public abstract byte getHeaderCheck();

	
	
	

}
