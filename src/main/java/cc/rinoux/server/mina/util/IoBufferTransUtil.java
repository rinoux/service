package cc.rinoux.server.mina.util;

import org.apache.mina.core.buffer.IoBuffer;

public class IoBufferTransUtil {
	/**  
	* 将byte按ASCII码转换成相应的字！
	* 
	* @param butBuffer  
	* @return 返回每个byte对应的字符，并拼接成字符！
	*/  
	public static String bytesToString(byte [] b)   
	{   
	       StringBuffer stringBuffer = new StringBuffer();   
	       for (int i = 0; i < b.length; i++)   
	       {   
	           stringBuffer.append((char) b [i]);   
	       }   
	       return stringBuffer.toString();   
	}   
	  
	/**  
	* String->IoBuffer
	* @param str  
	*/  
	public static IoBuffer stringToIoBuffer(String str)   
	{   
	  
	       byte bt[] = str.getBytes();   
	  
	       IoBuffer ioBuffer = IoBuffer.allocate(bt.length);   
	       ioBuffer.put(bt, 0, bt.length);   
	       ioBuffer.flip();   
	       return ioBuffer;   
	}   
	/**  
	* byte[]->IoBuffer
	* @param str  
	*/  
	public static IoBuffer bytesToIoBuffer(byte[] bt,int length)   
	{   
	  
	       IoBuffer ioBuffer = IoBuffer.allocate(length);   
	       ioBuffer.put(bt, 0, length);   
	       ioBuffer.flip();   
	       return ioBuffer;   
	}   
	/**  
	* IoBuffer->byte[]    
	* @param str  
	*/  
	public static byte[] ioBufferToByte(Object message)   
	{   
	      if (!(message instanceof IoBuffer))   
	      {   
	          return null;   
	      }   
	      IoBuffer ioBuffer = (IoBuffer)message;   
	      byte[] b = new byte[ioBuffer.limit()];   
	      ioBuffer.get(b);   
	      return b;   
	}   
	/**  
	* IoBuffer->String
	* @param butBuffer  
	*/  
	public static String ioBufferToString(Object message)   
	{   
	      if (!(message instanceof IoBuffer))   
	      {   
	        return "";   
	      }   
	      IoBuffer ioBuffer = (IoBuffer) message;   
	      byte[] b = new byte [ioBuffer.limit()];   
	      ioBuffer.get(b);   
	      StringBuffer stringBuffer = new StringBuffer();   
	  
	      for (int i = 0; i < b.length; i++)   
	      {   
	  
	       stringBuffer.append((char) b [i]);   
	      }   
	       return stringBuffer.toString();   
	}  
	
}
