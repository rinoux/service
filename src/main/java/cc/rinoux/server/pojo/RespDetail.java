package cc.rinoux.server.pojo;

/** 
* @author   杨锐
* @email    rinoux@foxmail.com
* @company  江苏南大五维科技有限公司
*
* @version  ReturnDescribe.java
* @date     2016年4月20日 下午1:34:44 
* @function 描述返回信息中的具体情况
*/

public class RespDetail {
	
	@SuppressWarnings("unused")
	private static final int FAILED = 1;
	@SuppressWarnings("unused")
	private static final int SUCCESS = 0;
	/**
	 * 文件大小超过1M
	 */
	public static final String  IMAGE_OVERLARGE = "error:the size of image is over 1M!";
	/**
	 * 没有找到文件
	 */
	public static final String NO_SUCH_FILE = "error:the file you request is not exist!";
	/**
	 * 查询成功
	 */
	public static final String QUERY_SUCCESS = "query success!";
	/**
	 * 上传文件为空
	 */
	public static final String NONE_FILE = "error:there is no data contained in your file uploaded!";
	/**
	 * 上传成功
	 */
	public static final String UPLOAD_SUCCESS = "you have uploaded the image successful";

	/**
	 * 无法确定的错误
	 */
	public static final String UNKNOW = "we don't know what's wrong !";
	/**
	 * 设备未在线
	 */
	public static final String DEVICE_OFFLINE = "you operation failed to complete,please check whether you device is online";
}
