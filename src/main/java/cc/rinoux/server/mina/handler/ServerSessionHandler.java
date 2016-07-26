package cc.rinoux.server.mina.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cc.rinoux.server.mina.helper.SenderHelperPool;
import cc.rinoux.server.mina.server.SessionCache;
import cc.rinoux.server.mina.util.TrimedIpString;
/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 继承DemuxingIoHandler，DemuxingIoHandler继承和实现IoHandler(adaptor)
 * 用于对单个IoSession的状态进行回调处理，包括接受消息、建立连接、打开连接、关闭连接等
 * @Datetime 2016年5月3日下午2:07:40
 */
public class ServerSessionHandler extends DemuxingIoHandler {
	
	static Logger logger = LoggerFactory.getLogger(ServerSessionHandler.class);
	String ip = null;
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.debug(session.getRemoteAddress()+"发送的消息：" + message.toString());

		//使用独立线程来处理连接
		//初始化NonDataMessageProcessor
		NonDataMessageProcessor pooledHander = new NonDataMessageProcessor(session, message);
		//建立对非数据消息（从设备接受的消息）处理对象NonDataMessageProcessor的队列
		/**
		 * 使用LinkedBlockingQueue队列，容量根据需求设定
		 */
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(10);
		/**
		 * 建立线程池处理队列
		 */
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 120, 120000, TimeUnit.MILLISECONDS, queue);
		executor.execute(pooledHander);
		/*
		//若需要判断是否处理成功使用下段代码
		Future<?> future = executor.submit(pooledHander);
		logger.info("------------>消息处理成功");
		try {
			Object object = future.get();
		} catch (InterruptedException e) {
			// TODO: handle exception
		}catch (ExecutionException e) {
			// TODO: handle exception
		}finally {
			executor.shutdown();
		}
		*/
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.debug("和"+session.getRemoteAddress() + "发送消息完成");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("和"+session.getRemoteAddress() + "的连接已建立");
		ip = TrimedIpString.getTrimedIpString(session.getRemoteAddress().toString());
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("和"+session.getRemoteAddress() + "的连接已打开");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ip = TrimedIpString.getTrimedIpString(session.getRemoteAddress().toString());
		SessionCache sessionCache = SessionCache.instance;
		sessionCache.removeSession(ip);
		
		SenderHelperPool helperPool = SenderHelperPool.instance;
		helperPool.removeHelper(ip);
		logger.info("和"+session.getRemoteAddress() + "的连接已断开");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		//空闲时长，协议规定120s空闲后关闭session
		logger.debug(session.getRemoteAddress() + "已空闲" + session.getIdleCount(status) * 30 +"s");
		if (session != null) {
			if (session.getIdleCount(status) >= 12) {
				session.closeNow();
			}
		}
	}

}

