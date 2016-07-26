package cc.rinoux.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

/**
 * @Author Rinoux
 * @Orgnization www.5d-tech.com
 * @Description TODO
 * @Datetime 2016年5月9日下午1:59:07
 */
public class JpgToBmp {
	
	private static final String IMAGES_PATH_ROOT = "${webApp.root}/WEB-INF/";
	
	/**将jpg图片转化成bmp图片
	 * 
	 * @param in 输入图片文件（仅jpg格式）
	 * @return 返回生成的bmp文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File fromFile(File in) throws FileNotFoundException, IOException {
		int hash = in.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//图片命名为日期时间（yyyyMMddHHmmss）+文件Hash值
		String imageName = dateStr + "_" + hash;				
		File file = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		FileImageInputStream fiis = new FileImageInputStream(in);
		FileImageOutputStream fios = new FileImageOutputStream(file);
		//设置输入文件
		ImageReader jpgReader = null;
		Iterator<ImageReader> iterator1 = ImageIO.getImageReadersByFormatName("jpg");
		if (iterator1.hasNext()) {
			jpgReader = iterator1.next();
		}
		jpgReader.setInput(fiis);
		
		//设置输出文件
		ImageWriter bmpWriter = null;
		Iterator<ImageWriter> iterator2 = ImageIO.getImageWritersByFormatName("bmp");
		if (iterator2.hasNext()) {
			bmpWriter = iterator2.next();
		}
		bmpWriter.setOutput(fios);
		
		//把
		BufferedImage bufferedImage = jpgReader.read(0);
		bmpWriter.write(bufferedImage);
		
		fiis.close();
		fios.close();
		return file;
	}
	/**
	 * 将jpg图片流转化为bmp图片
	 * @param inputStream 输入jpg图片流
	 * @return 输出bmp图片文件
	 * @throws IOException
	 */
	public static File fromInputstream(InputStream inputStream) throws IOException{
		int hash = inputStream.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//图片命名为日期时间（yyyyMMddHHmmss）+文件流hash值
		String imageName = dateStr + "_" + hash;
		File out = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		OutputStream outputStream = new FileOutputStream(out);
		byte[] bt = new byte[8192];
		while ((inputStream.read(bt))!= -1) {
			outputStream.write(bt);
		}
		FileImageInputStream fiis = new FileImageInputStream(out);
		FileImageOutputStream fios = new FileImageOutputStream(out);
		//设置输入文件
		ImageReader jpgReader = null;
		Iterator<ImageReader> iterator1 = ImageIO.getImageReadersByFormatName("jpg");
		if (iterator1.hasNext()) {
			jpgReader = iterator1.next();
		}
		jpgReader.setInput(fiis);
		
		//设置输出文件
		ImageWriter bmpWriter = null;
		Iterator<ImageWriter> iterator2 = ImageIO.getImageWritersByFormatName("bmp");

		if (iterator2.hasNext()) {
			bmpWriter = iterator2.next();
		}
		bmpWriter.setOutput(fios);
		
		//把
		BufferedImage bufferedImage = jpgReader.read(0);
		
		bmpWriter.write(bufferedImage);
		inputStream.close();
		outputStream.close();
		fiis.close();
		fios.close();
		
		return out;
		
	}

}
