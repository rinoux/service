package cc.rinoux.server.mina.server;

import java.io.IOException;

/**
 * 在单独线程监听socket<br>
 * 监听启动放在上下文启动时
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午10:13:00
 */

public class ServerSocketThread extends Thread {

	private static final int PORT = 10096;
	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final int IDLE_INTERVAL = 30;
	@Override
	public void run() {
		MinaSocketServer socketServer = new MinaSocketServer(PORT, BUFFER_SIZE, IDLE_INTERVAL);
		try {
			socketServer.startListen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
