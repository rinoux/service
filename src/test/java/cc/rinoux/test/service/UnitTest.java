package cc.rinoux.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aliyun.oss.OSSClient;
import cc.rinoux.common.util.ImageUtil;
import cc.rinoux.common.util.MD5;
import cc.rinoux.server.aliyun.oss.OSSClientConfig;
import cc.rinoux.server.aliyun.oss.OSSOperator;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.mapper.ImageMapper;
import cc.rinoux.server.mina.util.IntegerBytesUtil;
import cc.rinoux.server.mina.util.StringTransUtil;
@Component
public class UnitTest {
	
	
	static final Logger logger = LoggerFactory.getLogger(UnitTest.class.getName());
	@Autowired()
	OSSClientConfig ClientConfig;
	
	@Autowired
	CupMapper CupMapper;

	//@Test
	public void testOSS() throws Exception{


		String pathname = "E:/uploadImage/20160311152538.bmp";
		
		//OSSClientConfig myOSSClient = new OSSClientConfig();
		OSSClient client = ClientConfig.getOSSClient();
		File file = new File(pathname);
		String md5 = MD5.getMD5ValueOfFile(file);
		System.out.println(md5);
		OSSOperator manageUtil = new OSSOperator();
		String url = manageUtil.uploadFile(file);
		System.out.println(url);
	}
	@Autowired
	ImageMapper ImageMapper;

	

	@Test
	public void testCupMapper() throws Exception{

		byte[] bs = IntegerBytesUtil.intTo3Bytes(65536);
		
		byte[] header = new byte[]{0x01,0x08,0x04,0x02,0x00,0x03,0x00,0x00,0x00,0x5e,0x23};
		logger.info("--->" + Integer.bitCount(100));
		logger.info("----->" + StringTransUtil.bytesToHexString(IntegerBytesUtil.integerToBytes(239, 1)));
		//logger.info("----->" +StringUtils.dumpAsHex(SimpleTypeConvertUtil.intTo3Bytes(100)),3);
		
		//logger.info("--->" + Integer.bitCount(10000));
		//logger.info("--->" + Integer.lowestOneBit(10000));
		
	}
	//@Test
	public void input(){
		File src = new File("‪E:/uploadImage/batterfly.jpg");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(src);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageUtil.handleInputImage(inputStream);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
