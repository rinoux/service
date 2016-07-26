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
 * @Datetime 2016��5��9������1:59:07
 */
public class JpgToBmp {
	
	private static final String IMAGES_PATH_ROOT = "${webApp.root}/WEB-INF/";
	
	/**��jpgͼƬת����bmpͼƬ
	 * 
	 * @param in ����ͼƬ�ļ�����jpg��ʽ��
	 * @return �������ɵ�bmp�ļ�
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File fromFile(File in) throws FileNotFoundException, IOException {
		int hash = in.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//ͼƬ����Ϊ����ʱ�䣨yyyyMMddHHmmss��+�ļ�Hashֵ
		String imageName = dateStr + "_" + hash;				
		File file = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		FileImageInputStream fiis = new FileImageInputStream(in);
		FileImageOutputStream fios = new FileImageOutputStream(file);
		//���������ļ�
		ImageReader jpgReader = null;
		Iterator<ImageReader> iterator1 = ImageIO.getImageReadersByFormatName("jpg");
		if (iterator1.hasNext()) {
			jpgReader = iterator1.next();
		}
		jpgReader.setInput(fiis);
		
		//��������ļ�
		ImageWriter bmpWriter = null;
		Iterator<ImageWriter> iterator2 = ImageIO.getImageWritersByFormatName("bmp");
		if (iterator2.hasNext()) {
			bmpWriter = iterator2.next();
		}
		bmpWriter.setOutput(fios);
		
		//��
		BufferedImage bufferedImage = jpgReader.read(0);
		bmpWriter.write(bufferedImage);
		
		fiis.close();
		fios.close();
		return file;
	}
	/**
	 * ��jpgͼƬ��ת��ΪbmpͼƬ
	 * @param inputStream ����jpgͼƬ��
	 * @return ���bmpͼƬ�ļ�
	 * @throws IOException
	 */
	public static File fromInputstream(InputStream inputStream) throws IOException{
		int hash = inputStream.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//ͼƬ����Ϊ����ʱ�䣨yyyyMMddHHmmss��+�ļ���hashֵ
		String imageName = dateStr + "_" + hash;
		File out = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		OutputStream outputStream = new FileOutputStream(out);
		byte[] bt = new byte[8192];
		while ((inputStream.read(bt))!= -1) {
			outputStream.write(bt);
		}
		FileImageInputStream fiis = new FileImageInputStream(out);
		FileImageOutputStream fios = new FileImageOutputStream(out);
		//���������ļ�
		ImageReader jpgReader = null;
		Iterator<ImageReader> iterator1 = ImageIO.getImageReadersByFormatName("jpg");
		if (iterator1.hasNext()) {
			jpgReader = iterator1.next();
		}
		jpgReader.setInput(fiis);
		
		//��������ļ�
		ImageWriter bmpWriter = null;
		Iterator<ImageWriter> iterator2 = ImageIO.getImageWritersByFormatName("bmp");

		if (iterator2.hasNext()) {
			bmpWriter = iterator2.next();
		}
		bmpWriter.setOutput(fios);
		
		//��
		BufferedImage bufferedImage = jpgReader.read(0);
		
		bmpWriter.write(bufferedImage);
		inputStream.close();
		outputStream.close();
		fiis.close();
		fios.close();
		
		return out;
		
	}

}
