package cc.rinoux.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.rinoux.server.mina.server.ServerSocketThread;

/**
 * 继承servlet的上下文监听类，此类配置在web.xml中，服务器发布后自动开启socket的监听
 * @author Administrator
 *
 */


public class ServerSocketListener implements ServletContextListener {
	
	static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketListener.class.getName());
	Thread thread = new ServerSocketThread();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {		
		thread.start();
		LOGGER.info("服务端开始监听SOCKET连接！");		
	}
	
	/**
	 * 取代thread的stop()方法，以免造成线程死锁
	 */
	private void stop() {
		thread = null;
		
	}
	

}
