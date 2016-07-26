package cc.rinoux.server.weixin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.rinoux.server.weixin.util.MyConstant;

public class Download {

	private static Logger log = LoggerFactory.getLogger(Download.class);

	/**
	 * 获取媒体文件
	 * 
	 * @param accessToken
	 *            接口访问凭证
	 * @param media_id
	 *            媒体文件id
	 * @param savePath
	 *            文件在服务器上的存储路径
	 * */
	public static String downloadMedia(String accessToken, String mediaId,
			String savePath) {
		String filePath = null;
		// 拼接请求地址
		String requestUrl = MyConstant.URLDOWNLOADFILE;
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
				"MEDIA_ID", mediaId);
		log.info("下载多媒体URL" + requestUrl);

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (!savePath.endsWith("/")) {
				savePath += "/";
			}

			// 根据内容类型获取扩展�?
			String fileExt = getFileEndWitsh(conn
					.getHeaderField("Content-Type"));

			// 将mediaId作为文件�?
			filePath = savePath + mediaId + fileExt;

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();
			conn.disconnect();
			String info = String.format("下载媒体文件成功，filePath=" + filePath);
			log.debug(info);
		} catch (Exception e) {
			filePath = null;
			String error = String.format("下载媒体文件失败�?%s", e);
			log.error(error);
		}
		return filePath;
	}

	private static String getFileEndWitsh(String headerField) {

		// TODO Auto-generated method stub
		String fileEndWitsh = "";
		if ("image/jpeg".equals(headerField))
			fileEndWitsh = ".jpg";
		else if ("audio/mpeg".equals(headerField))
			fileEndWitsh = ".mp3";
		else if ("audio/amr".equals(headerField))
			fileEndWitsh = ".amr";
		else if ("video/mp4".equals(headerField))
			fileEndWitsh = ".mp4";
		else if ("video/mpeg4".equals(headerField))
			fileEndWitsh = ".mp4";
		return fileEndWitsh;
	}

}
