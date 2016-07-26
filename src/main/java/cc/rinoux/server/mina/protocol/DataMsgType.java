package cc.rinoux.server.mina.protocol;
/**
 * 数据消息类型（从服务器发送到device）
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午9:47:27
 */
public class DataMsgType {

	public static final byte IMAGE_DATA = 0x02;
	public static final byte DATE_TIME_SYNCH_DATA = 0x04;
	public static final byte CALENDER_NOTES_DATA = 0x06;
	
}
