package cc.rinoux.server.pojo;

/** 
* @author   ����
* @email    rinoux@foxmail.com
* @company  �����ϴ���ά�Ƽ����޹�˾
*
* @version  ReturnDescribe.java
* @date     2016��4��20�� ����1:34:44 
* @function ����������Ϣ�еľ������
*/

public class RespDetail {
	
	@SuppressWarnings("unused")
	private static final int FAILED = 1;
	@SuppressWarnings("unused")
	private static final int SUCCESS = 0;
	/**
	 * �ļ���С����1M
	 */
	public static final String  IMAGE_OVERLARGE = "error:the size of image is over 1M!";
	/**
	 * û���ҵ��ļ�
	 */
	public static final String NO_SUCH_FILE = "error:the file you request is not exist!";
	/**
	 * ��ѯ�ɹ�
	 */
	public static final String QUERY_SUCCESS = "query success!";
	/**
	 * �ϴ��ļ�Ϊ��
	 */
	public static final String NONE_FILE = "error:there is no data contained in your file uploaded!";
	/**
	 * �ϴ��ɹ�
	 */
	public static final String UPLOAD_SUCCESS = "you have uploaded the image successful";

	/**
	 * �޷�ȷ���Ĵ���
	 */
	public static final String UNKNOW = "we don't know what's wrong !";
	/**
	 * �豸δ����
	 */
	public static final String DEVICE_OFFLINE = "you operation failed to complete,please check whether you device is online";
}
