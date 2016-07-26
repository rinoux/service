package cc.rinoux.server.weixin.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cc.rinoux.server.weixin.util.MyConstant;

public class WeixinJsSdk {

	private static Logger log = LoggerFactory.getLogger(WeixinJsSdk.class);

	/**
	 * 获取sign
	 * 
	 * @param urlString
	 *            URL
	 * @return sign
	 * @throws IOException
	 */
	public Map<String, String> getSignMap(String urlString) {

		Map<String, String> signMap = new HashMap<String, String>();
		// ////************获取TICKET
		TokenTicket getTokenTicket = new TokenTicket();
		String tiketString = getTokenTicket.getTicketString();
		signMap = Sign.getSignFromTiket(tiketString, urlString);
		log.info("获取签名成功，签名signMap为：" + signMap);
		return signMap;
	}

	/**
	 * 从微信服务器下载图片
	 * 
	 */
	public String getImgUrl(String mediaId) {
		
		// //******项目根目�?		
		 String rootpath = "./webapps/mojimojicup/";
		
		
		//String classPath = getClass().getResource("/").getFile().toString();
		//String classPathtemp = classPath.substring(1, classPath.length());
		// //******项目根目�?
		//String rootpath = classPathtemp.substring(0,
				//classPathtemp.length() - 16);
		
		
		
		// //******配置文件目录
		// ////按日期建立文件夹
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");
		String imgagefilepathString = rootpath + MyConstant.IMAGEFILEPATH + "/" + matter1.format(dt);
		
		// /////*********判断下载文件夹是否存�?
		File file = new File(imgagefilepathString);
		// 如果文件夹不存在则创�?
		if (!file.exists() && !file.isDirectory()) {
			log.info("多媒体下载目录日期文件夹不存在，路径�?" + imgagefilepathString);
			file.mkdirs();
		} else {
			log.info("多媒体下载目录日期文件夹存在，路径：" + imgagefilepathString);
		}
		
		
		TokenTicket getTokenTicket = new TokenTicket();
		String accessTokenString = getTokenTicket.getTokenString();
		String ImgLocalPath = Download.downloadMedia(accessTokenString,
				mediaId, imgagefilepathString);
		log.info("IMAGE资源下载成功:服务器路径为�?" + ImgLocalPath);
		String ImgUrl = MyConstant.URLTHESERVER + MyConstant.IMAGEFILEPATH
				+ "/" + matter1.format(dt) + "/" + mediaId + ".jpg";
		log.info("IMAGE资源下载成功:IMAGE的URL为：" + ImgUrl);
		return ImgUrl;
	}

}
