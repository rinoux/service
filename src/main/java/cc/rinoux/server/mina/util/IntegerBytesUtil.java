package cc.rinoux.server.mina.util;

/**
 * int 和 byte[]直接的转换
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年5月9日上午11:00:10
 */
public class IntegerBytesUtil {
	/**
	 * int转byte
	 * @param value
	 * @return
	 */
	  public static byte[] intTo4Bytes(int value){   		    
		  byte[] b = new byte[4];  		    
		  b[3] = (byte)((value>>24) & 0xFF);  		    
		  b[2] = (byte)((value>>16) & 0xFF);  		    
		  b[1] = (byte)((value>>8) & 0xFF);    		    
		  b[0] = (byte)(value & 0xFF);                  		    
		  return b;   
		} 
	  
	  public static byte[] intTo3Bytes(int value){   		    
		  byte[] b = new byte[3];  		     		    
		  b[2] = (byte)((value>>16) & 0xFF);  		    
		  b[1] = (byte)((value>>8) & 0xFF);    		    
		  b[0] = (byte)(value & 0xFF);                  		    
		  return b;   
		} 
	  public static byte[] intTo2Bytes(int value){   		    
		  byte[] b = new byte[3];  		     		    
		 // b[2] = (byte)((value>>16) & 0xFF);  		    
		  b[1] = (byte)((value>>8) & 0xFF);    		    
		  b[0] = (byte)(value & 0xFF);                  		    
		  return b;   
		} 

	  /**
	   * int转byte[]<br>
	   * 低位在前，高位在后
	   * @param value 待转int
	   * @param bitNum 转成的byte[]位数
	   * @return
	 * @throws Exception 
	   */
	  public static byte[] integerToBytes(int value, int bitNum) throws Exception {
 
			  byte[] bs = new byte[bitNum];
			  for (int i = bitNum -1; i >= 0; i--) {				
				  bs[i] = (byte)((value >> i * 8) & 0xFF);			  
			  }
			  return bs;
	
		  		
	}
		/**
		 * 把byte[] 按高低位转换成int<br>
		 * 低位在前，高位在后<br>
		 * 例如{0x00,0x00,0x01}  ==> int:65536
		 * @param b
		 * @return
		 */
	 
	  public static int bytesToInteger(byte[] b) {
		  int bits = b.length;
		  int[] temp = new int[bits];
		  int num = 0;
		  for (int i = 0; i < b.length; i++) {
			temp[i] = (b[i] & 0xff) << (i * 8);
			num = temp[i] | num;
		}		  
		  return num;		
	}
	  public static int bytesToInteger(byte[] b, int off, int len) {
		  int[] temp = new int[len];
		  int num = 0;
		  for (int i = 0; i < len; i++) {
			temp[i] = (b[i + off] & 0xff) << (i * 8);
			num = temp[i] | num;
		}		  
		  return num;		
	}


}
