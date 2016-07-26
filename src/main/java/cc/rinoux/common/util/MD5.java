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
 * 生成文件的MD5值
 * @author Administrator
 *
 */

public class MD5 {
	
	/**
	 * 
	 * @param file 需要生成MD5值的文件
	 * @return MD5值（小写）
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
