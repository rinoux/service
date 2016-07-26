package cc.rinoux.server.mina.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 从IoSession获得IP
 * @Datetime 2016年5月3日下午3:20:57
 */

public class TrimedIpString {
	static final Logger LOGGER = LoggerFactory.getLogger(TrimedIpString.class.getName());
	/**
	 * 从IoSession获得IP<br>
	 * * @param sessionIp
	/*** @return
	 */
	public static String getTrimedIpString(String sessionIp) {
		if (!sessionIp.equals(null)) {
			String ip = null;
			int i = sessionIp.indexOf(":");
			if (i > -1) {
				ip = sessionIp.substring(1, i);
		 		return ip;
			}else {
				LOGGER.debug("invalid IP Sring!");
			}
		}
		return null;
	}

}
