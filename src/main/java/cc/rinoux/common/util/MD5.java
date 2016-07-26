package cc.rinoux.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
/**
 * �����ļ���MD5ֵ
 * @author Administrator
 *
 */

public class MD5 {
	
	/**
	 * 
	 * @param file ��Ҫ����MD5ֵ���ļ�
	 * @return MD5ֵ��Сд��
	 * @throws FileNotFoundException
	 */
    public static String getMD5ValueOfFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
    try {

        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(byteBuffer);
        BigInteger bi = new BigInteger(1, md5.digest());
        value = bi.toString(16);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
            if(in != null) {
                try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    return value;
    }

}
