package cc.rinoux.server.mina.util;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description 奇偶校验工具
 * @Datetime 2016年5月3日下午3:19:33
 */

public class ParityCheckUtil {
	/**
	 * 获得输入byte[] 中“1”的个数
	 * * @param bytes
	/*** @return 返回个数int
	 */
	public static int getParity(byte[] bytes) {
		int oddCount = 0;
		if (bytes.length != 0) {
			StringBuilder sBuilder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sBuilder.append(Integer.toBinaryString(bytes[i] & 0xFF));				
			}
			char[] ch = sBuilder.toString().toCharArray();
			for (int j = 0; j < ch.length; j++) {
				if (ch[j] == '1') {					
					oddCount++;	
				}
			}
			return oddCount;
		}else {
			return 0;
		}
	}
	/**
	 * 
	 * @param bytes  除去包头校验的数据bytes
	 * @param givenCount 包头校验中给出的"1"的个！
	 * @return 实际奇个数是否和校验给出的一致，若一致返回true
	 * 
	 */
	public static boolean OddParityCheck(byte[] bytes,byte givenCount) {
		if (bytes.length != 0) {
			int gCount = givenCount & 0xFF;
			int realCount = 0;
			StringBuilder sBuilder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sBuilder.append(Integer.toBinaryString(bytes[i] & 0xFF));				
			}
			char[] ch = sBuilder.toString().toCharArray();
			for (int j = 0; j < ch.length; j++) {
				if (ch[j] == '1') {					
					realCount++;	
				}
			}
			if (gCount == realCount) {
				return true;
			}
		}
		return false;		
	}
	/**
	 * 奇偶校检
	 * @param in 输入IoBuffer对象（转成byte[]后一位是奇偶校验码）
	 * @return IoBuffer对象包含的数据和奇偶数是否一致
	 */
	public static boolean oddParityCheck(IoBuffer in) {
		if (in != null) {
			byte[] data = IoBufferTransUtil.ioBufferToByte(in);
			int len = data.length;
			byte[] content = new byte[len -1];
			for (int i = 0; i < content.length; i++) {
				content[i] = data[i];
			}
			byte givenCount = in.get(len - 1);
			return ParityCheckUtil.OddParityCheck(content, givenCount);
		}
		return false;
		
	}

}
