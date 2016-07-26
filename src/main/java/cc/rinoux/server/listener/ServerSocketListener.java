package cc.rinoux.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.rinoux.server.mina.server.ServerSocketThread;

/**
 * �̳�servlet�������ļ����࣬����������web.xml�У��������������Զ�����socket�ļ���
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
		LOGGER.info("����˿�ʼ����SOCKET���ӣ�");		
	}
	
	/**
	 * ȡ��thread��stop()��������������߳�����
	 */
	private void stop() {
		thread = null;
		
	}
	

}
