package cc.rinoux.server.mina.helper;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送数据的helper池
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午9:55:09
 */
public enum SenderHelperPool {
	/**
	 * 单例
	 */
	instance;
		
	static final Logger logger = LoggerFactory.getLogger(SenderHelperPool.class.getName());
	/**
	 * MessageSenderHelper池，从前端发送数据给device时建立，
	 * 发送完毕后销毁.key：ip, value:MessageSenderHelper
	 */
	private static Map<String, MessageSenderHelper> helper_map = new HashMap<String, MessageSenderHelper>();
	
	private SenderHelperPool(){
		
	}
	/**
	 * * @param ip 发送目的ip
	/*** @param handler帮助工具
	 */
	public void addHelper(String ip, MessageSenderHelper helper) {
		helper_map.put(ip, helper);		
		logger.debug("装入helper成功");
		
	}
	/**
	 * 取出helper
	 * * @param ip
	/*** @return
	 */
	public MessageSenderHelper getHelper(String ip) {
		
		if (helper_map.get(ip) == null) {
			logger.debug("未找到对应helper");
			return null;
		}else {
			logger.debug("取出helper成功！");
			return helper_map.get(ip);
		}
	}
	/**
	 * 销毁helper
	 * * @param ip
	 */
	public void removeHelper(String ip) {
		helper_map.remove(ip);
		logger.debug("销毁helper成功！");
	}
	
	

}
