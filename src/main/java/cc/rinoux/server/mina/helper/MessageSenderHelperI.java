package cc.rinoux.server.mina.helper;
/**
 * 消息发送帮助工具接口
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午9:49:09
 */
public interface MessageSenderHelperI {
	/**
	 * 收到发送序列，请求发送本序列的分包数据
	 * * @param sequence分包序列
	 */
	public void receivedPacketRequest(int sequence);
}
