package cc.rinoux.server.mina.protocol;
/**
 * 服务器接收到device的非数据消息
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午9:48:00
 */
public class NonDataMsgType {
	/**
	 * 心跳包
	 */
	public static NonDataMsgType HEART_BEAT = new NonDataMsgType("HEART_BEAT");
	/**
	 * 数据空
	 */
	public static NonDataMsgType DATA_EMPTY = new NonDataMsgType("DATA_EMPTY");
	/**
	 * 数据满
	 */
	public static NonDataMsgType DATA_FULL = new NonDataMsgType("DATA_FULL");
	/**
	 * 包号
	 */
	public static NonDataMsgType SEQUENCE_COUNT = new NonDataMsgType("SEQUENCE_COUNT");
	/**
	 * 请求连接提示
	 */
	public static NonDataMsgType CONN_ACK_REQUEST = new NonDataMsgType("CONN_ACK_REQUEST");
	/**
	 * 未知
	 */
	public static NonDataMsgType UNKNOW_STATUS = new NonDataMsgType("UNKNOW_STATUS");
	private final String status;

	public NonDataMsgType(String status) {
		super();
		this.status = status;
	}
	
	@Override
	public String toString(){
		return status;
		
	}

}
