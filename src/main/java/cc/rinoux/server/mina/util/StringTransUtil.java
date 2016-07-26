package cc.rinoux.server.mina.util;

import java.io.UnsupportedEncodingException;

/** 
* @author   杨锐
* @email    rinoux@foxmail.com
* @company  江苏南大五维科技有限公司
*
* @version  StringTransUtil.java
* @date     2016年5月9日 上午10:29:15 
* @function TODO
*/

public class StringTransUtil {
	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {	 
	    StringBuffer unicode = new StringBuffer();	 
	    for (int i = 0; i < string.length(); i++) {	 
	        // 取出每一个字符
	        char c = string.charAt(i);	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }	 
	    return unicode.toString();
	}

		
	public static String BinaryToHexString(byte[] bytes){  
        String hexStr = "0123456789ABCDEF";
        String result = "";  
        String hex = "";  
        for(int i=0;i<bytes.length;i++){  
            //字节高4位  
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
            //字节低4位  
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
            result +=hex+" ";  
        }  
        return result;  
    } 
	
	public static byte[] BinaryToHexByte(byte[] bytes) throws UnsupportedEncodingException{  
        String hexStr = "0123456789ABCDEF";
        String result = "";  
        String hex = "";  
        for(int i=0;i<bytes.length;i++){  
            //字节高4位  
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
            //字节低4位  
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
            result +=hex+" ";  
        }
        byte[] bs = new byte[result.length()];
        bs = result.getBytes("UTF8");
        return bs;  
    } 
	
	
	/**  
	* 将byte按ASCII码转换成相应的字！
	* 
	* @param butBuffer  
	* @return 返回每个byte对应的字符，并拼接成字符！
	*/  
	public static String bytesToString(byte[] b)   
	{   
	       StringBuffer stringBuffer = new StringBuffer();   
	       for (int i = 0; i < b.length; i++)   
	       {   
	           stringBuffer.append((char) b [i]);   
	       }   
	       return stringBuffer.toString();   
	}  
	
	/**
	 * byte！16进制数连成字符串
	 * @param b
	 * @return
	 */
	public static String bytesToHexString(byte[] b) {			  
		String ret = "";
		for (int i = 0; i < b.length; i++) {			   
			String hex = Integer.toHexString(b[i] & 0xFF);			   
			if (hex.length() == 1) {			   
				hex = '0' + hex;			   
			}			   
			ret += hex.toUpperCase();			  
		}			  
		return ret;			
	}


}
