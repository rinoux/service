package cc.rinoux.server.mina.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.rinoux.server.exception.NullObjectException;

/** 
* @author   杨锐
* @email    rinoux@foxmail.com
* @company  江苏南大五维科技有限公司
*
* @version  SessionCache.java
* @date     2016年4月5日 下午4:24:20 
* @function 单例模式，保存session连接
*/

public enum SessionCache {
	/**
	 * 单例
	 */
	instance;
	private static final Logger logger = LoggerFactory.getLogger(SessionCache.class.getName());
	/**
	 * 用于缓存session的map，使用hashmap
	 */
	//private static Map<String, IoSession>  SESSION_MAP = new HashMap<String, IoSession>();
	private static Map<String, IoSession>  SESSION_MAP = new ConcurrentHashMap<>();

	private SessionCache(){
		
	}
	/**保存session
	 * * @param key session的ip值（如"192.168.4.141"）<br>
	/*** @param session IoSession对象
	 */
	public void addSession(String key, IoSession session){
		synchronized(SESSION_MAP){
		SESSION_MAP.put(key, session);
		logger.info("保存session成功" + key);
		}
	}
	/**根据ip获取session
	 * * @param key ip值
	/*** @return
	 */
	public IoSession getSession(String key){
		IoSession session = SESSION_MAP.get(key);
		if (session == null) {
			logger.info("获取session失败!");
			//throw new NullObjectException();
			return null;		
		}else {
			logger.debug("获取session成功!");	
			return session;
		}		
	}
	
	public void removeSession(String key){
		if (SESSION_MAP.get(key) == null) {
			logger.info("未找到要移除的session");
			throw new NullObjectException();
		}else {
			SESSION_MAP.remove(key);
			logger.info("移除session操作成功");
		}
	}
	/**向多个客户端发送消息<br>（预留方式，当前是向一个设备发送）<br>
	 * @param keys 多客户端的nip字符串数组<br>
	 * @param message 要发送的信息
	 */
	public void sendMessage(String[] keys, Object message){
		for(String key : keys){
			IoSession session = getSession(key);
			if (session == null) {
				logger.debug("未找到相应session");
				throw new NullObjectException();
			}else {
				session.write(message);
			}
		}
	}
	/**
	 * 测试用代码<br>
	 * 显示所有保存的连接ip
	 */
	public static void printAll() {
		logger.info("所有连接：");
		for(String key : SESSION_MAP.keySet()){
			IoSession session = SESSION_MAP.get(key);			
			logger.debug(session.getRemoteAddress().toString());
		}
	}
}
