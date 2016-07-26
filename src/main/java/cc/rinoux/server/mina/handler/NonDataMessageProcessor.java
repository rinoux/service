package cc.rinoux.server.mina.handler;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.mina.server.SessionCache;
import cc.rinoux.server.mina.util.TrimedIpString;

/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 用于处理非数据指令的类，多线程处理
 * @Datetime 2016年4月15日下午12:21:46
 */

@Component
class NonDataMessageProcessor implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(NonDataMessageProcessor.class.getName());
	private static final byte[] CONN_ACK_RESPONSE = new byte[]{0x01,0x06,0x00,0x00,0x00,0x00,0x03};
	IoSession session = null;
	Object message = null;
	
	byte[] connectAcknowledge= new byte[1300];

	@Autowired
	CupMapper cupMapper;
	public NonDataMessageProcessor() {
	}
	public NonDataMessageProcessor(IoSession session, Object message) {
		super();
		this.session = session;
		this.message = message;
	}	
	@Override
	public void run() {
		peocessMessage();		
	}

	private void peocessMessage(){		
		for (int i = 0; i < CONN_ACK_RESPONSE.length; i++) {
			connectAcknowledge[i] = CONN_ACK_RESPONSE[i];
			
		}
		//发送消息使用的IoBuffer
		IoBuffer buffer = IoBuffer.allocate(1024 * 1024);	
		String ip = TrimedIpString.getTrimedIpString(session.getRemoteAddress().toString());
		buffer.put(connectAcknowledge);
		
		buffer.flip();				
		String deviceStatus = message.toString();
		
		if (deviceStatus.equals("HEART_BEAT") || deviceStatus.equals("CONN_ACK_REQUEST")) {
			logger.debug("判断为心跳或者请求连接提示，准备发送连接提示！");
			//接收到心跳之后，发送连接提示给device，并缓存session			
			WriteFuture writeFuture = session.write(buffer);
			writeFuture.awaitUninterruptibly();
			if (writeFuture.isWritten()) {
				logger.debug("发送连接提示成功！");
				//以session的IP为key存储session到sessionMap
				SessionCache sessionCache = SessionCache.instance;
				sessionCache.addSession(ip, session);				
			}else {
				logger.info("发送连接提示失败！");
				session.closeNow();
			}			
		}else if (deviceStatus.equals("DATA_FULL")) {
			logger.debug("设备数据空间已满！");
		}else if (deviceStatus.equals("DATA_EMPTY")) {
			logger.debug("设备数据空间为空！");
		}else {
			logger.debug("无法获知设备数据空间状态！");
		}	
	}	
}

