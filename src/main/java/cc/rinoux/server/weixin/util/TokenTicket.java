package cc.rinoux.server.weixin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import cc.rinoux.server.weixin.util.MyConstant;

public class TokenTicket {

	private static Logger log = LoggerFactory.getLogger(TokenTicket.class);

	public static String tokenString = "";
	public static String tokenexpiresinString = "";
	public static String tokenTimeString = "";

	public static String tiketString = "";
	public static String tiketexpiresinString = "";
	public static String tiketTimeString = "";

	/**
	 * 获取Ticket
	 */
	public String getTicketString() {
		// TODO Auto-generated method stub
		// ///*********�?查token ticket的有效�??
		Checkvalidity();
		return tiketString;
	}

	/**
	 * 获取token
	 */
	public String getTokenString() {
		// TODO Auto-generated method stub
		// ///*********�?查token ticket的有效�??
		Checkvalidity();
		return tokenString;
	}

	/**
	 * �?查token ticket的有效�??
	 */
	public void Checkvalidity() {
		// TODO Auto-generated method stub
		
		// //******项目根目�?----webapps下的mojimojicup
		String rootpath = "./webapps/mojimojicup/";		
		
		// //******配置文件目录
		String xmlfilepathString = rootpath + MyConstant.XMLFILEPATH + "/";		
		
		// /////*********判断文件夹是否存�?
		File file = new File(xmlfilepathString);
		// 如果文件夹不存在则创�?
		if (!file.exists() && !file.isDirectory()) {
			log.info("配置目录不存在，路径�?" + xmlfilepathString);
			file.mkdirs();
			log.info("已经创建配置目录，路径：" + xmlfilepathString);
		} else {
			log.info("配置目录存在，路径：" + xmlfilepathString);
		}

		// /////配置文件文件�?
		String xmlfilenameString = xmlfilepathString + "/"
				+ MyConstant.XMLFILENAME;

		if (!isExists(xmlfilenameString)
				|| !isNormAndEffective(xmlfilenameString)) {
			// /////********如果XML不合�?
			log.info("XML文件不存在或者不符合规范，重新获取信息，生成XML");

			// ////**********获取ACCESSTOKEN
			getAccessToken(MyConstant.APPID, MyConstant.SECRET);

			// ////***********获取JSAPITICKET
			getJsapiTicket(tokenString);

			// ////******************将信息写进XML
			WirteXml.xmlWrite(xmlfilenameString, tokenString,
					tokenexpiresinString, tiketString, tiketexpiresinString);
		}
	}

	/**
	 * 判断XML文件是否存在
	 * 
	 * @param xmlfilenameString
	 */
	public static boolean isExists(String xmlfilenameString) {
		File file = new File(xmlfilenameString);
		if (!file.exists()) {
			log.info("XML文件不存在！");
			return false;
		} else {
			log.info("XML文件存在�?");
			return true;
		}
	}

	/**
	 * 判断XML文件是否合法 信息是否有效
	 * 
	 * @param xmlfilenameString
	 * 
	 */
	private static boolean isNormAndEffective(String xmlfilenameString) {
		// TODO Auto-generated method stub

		Map<String, String> ret = new HashMap<String, String>();
		ret = ReadXml.xmlRead(xmlfilenameString);

		tokenString = ret.get("Token");
		tokenexpiresinString = ret.get("Tokenexpires_in");
		tokenTimeString = ret.get("TokenTime");

		tiketString = ret.get("Tiket");
		tiketexpiresinString = ret.get("Tiketexpires_in");
		tiketTimeString = ret.get("TiketTime");

		if (tokenTimeString == null || tokenString == null
				|| tokenexpiresinString == "" || tiketTimeString == null
				|| tiketString == null || tiketexpiresinString == "") {
			log.info("XML文件不规范！");
			return false;
		} else {
			log.info("XML文件规范�?");
			int nowTime = (int) (System.currentTimeMillis() / 1000);

			if (nowTime - Integer.parseInt(tokenTimeString) < Integer
					.parseInt(tokenexpiresinString)
					&& nowTime - Integer.parseInt(tiketTimeString) < Integer
							.parseInt(tiketexpiresinString)) {
				log.info("XML文件信息有效�?");
				return true;
			} else {
				log.info("XML文件信息超时�?");
				return false;
			}
		}
	}

	/**
	 * 获取Jsapi_ticket*
	 * 
	 * @param Jsapi_ticket
	 * 
	 * @return Jsapi_ticket
	 */
	private static void getJsapiTicket(String access_token) {
		// TODO Auto-generated method stub
		String urlString = MyConstant.UrlGETJSAPITICKET;
		String requestUrlString = urlString.replace("ACCESS_TOKEN",
				access_token);
		log.info("获取ticket的URL为：" + requestUrlString);
		JSONObject jsonObject = httpRequest(requestUrlString);
		log.info("获取ticket返回的数据为�?" + jsonObject);
		if (null != jsonObject) {
			try {
				tiketString = jsonObject.getString("ticket");
				tiketexpiresinString = jsonObject.getString("expires_in");
				log.info("获取ticket成功�?");
			} catch (JSONException e) {
				int errcode = jsonObject.getInt("errcode");
				String errmsg = jsonObject.getString("errmsg");
				log.error("获取ticket异常：{errcode:" + errcode + "errmsg:" + errmsg
						+ "}");
			}
		}

	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 * 
	 * @param appsecret
	 * 
	 * @return AccessToken
	 */
	private static void getAccessToken(String aPPID, String sECRET) {
		// TODO Auto-generated method stub
		String access_token_url = MyConstant.UrlGETACCESSTOKEN;
		String requestUrlString = access_token_url.replace("APPID", aPPID)
				.replace("APPSECRET", sECRET);
		log.info("获取token的URL为：" + requestUrlString);
		JSONObject jsonObject = httpRequest(requestUrlString);
		log.info("获取token返回的数据为�?" + jsonObject);
		if (null != jsonObject) {
			try {
				tokenString = jsonObject.getString("access_token");
				tokenexpiresinString = jsonObject.getString("expires_in");
				log.info("获取token成功�?");
			} catch (JSONException e) {
				String errcode = jsonObject.getString("errcode");
				String errmsg = jsonObject.getString("errmsg");
				log.error("获取token异常：{errcode:" + errcode + "errmsg:" + errmsg
						+ "}");
			}
		}
	}

	/**
	 * 模拟http请求
	 * 
	 * @param requestUrl
	 *            URL
	 * @return
	 */
	private static JSONObject httpRequest(String requestUrl) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		String requestMethod = "GET";
		StringBuffer buffer = new StringBuffer();
		try {

			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("http请求数据失败�?" + ce.getMessage());
		} catch (Exception e) {
			log.error("http请求数据失败�?" + e.getMessage());
		}
		return jsonObject;
	}

}
