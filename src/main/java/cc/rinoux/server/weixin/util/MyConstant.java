package cc.rinoux.server.weixin.util;

public class MyConstant {

	// ////**********微信公众号信�?
	public static String APPID = "wx2411072ff7f4a0d2";
	public static String SECRET = "d4624c36b6795d1d99dcf0547af5443d";

	// ////************XML文件保存地址
	public static String XMLFILEPATH = "config";// ///XML文件路径
	public static String XMLFILENAME = "xml.xml";// ///XML文件文件�?

	// ////************从微信服务器下载资源保存地址
	public static String IMAGEFILEPATH = "downloadimage";// ///XML文件路径
	

	// //////********服务器URL
	public static String URLTHESERVER = "http://godyangpeng123.oicp.net/mojimojicup/";

	// ///******微信相关接口
	public static String URLDOWNLOADFILE = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	public static String UrlUPLOADFILE = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	public static String UrlGETACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static String UrlGETJSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

}
