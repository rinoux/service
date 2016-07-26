package cc.rinoux.common.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class.getName());
	/**
	 * 图片文件保存在本地时的路径
	 */
	private static final String IMAGES_PATH_ROOT = System.getProperty("catalina.home") + "/" + "webapps/service/temp_images/";
	//private static final String IMAGES_PATH_ROOT = "${webApp.root}/WEB-INF/";
	/**
	 * 缩略图前缀
	 */
	private static final String THUMBNAIL_PREFIX = "thumb_";
	
	/**生成缩略图工具
	 * 
	 * @param originImage需要生产缩略图的源图片
	 * @throws IOException
	 */
	public static File scaleImage(File originImage, int width, int height) throws IOException{
		@SuppressWarnings("unused")
		String types = Arrays.toString(ImageIO.getReaderFormatNames());
		String suffix = null;
		if (originImage.getName().indexOf(".") > -1) {
			suffix = originImage.getName().substring(originImage.getName().indexOf(".") + 1).toLowerCase();			
		}
		if (suffix == null) {
			return null;
		}
		Image image = ImageIO.read(originImage);
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		Graphics graphics = bImage.getGraphics();
		graphics.drawImage(image, 0, 0, width, height, null);
		graphics.dispose();

		File thumb =  new File(IMAGES_PATH_ROOT + THUMBNAIL_PREFIX + originImage.getName());
		ImageIO.write(bImage, suffix,thumb);
		return thumb;
		
	}

	/**
	 * 处理从前端上传的图片，处理为bmp565格式<br>
	 * * @param inputStream图片输入流<br>
	/*** @return 返回处理好的图片文件（和路径）<br>
	/*** @throws InterruptedException
	/*** @throws IOException
	 */
	public static File handleInputImage(InputStream inputStream) throws InterruptedException, IOException {
		int hash = inputStream.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//图片命名为日期时间（yyyyMMddHHmmss）+文件流hash值
		String imageName = dateStr + "_" + hash;
		
		File rootPath = new File(IMAGES_PATH_ROOT);
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
		File out = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		byte[] tempFile = new byte[1024];
		@SuppressWarnings("resource")
		OutputStream outputStream = new FileOutputStream(out);
		try {
			while ((inputStream.read(tempFile))!= -1) {
				outputStream.write(tempFile);
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("读写图片临时文件出错", e);
		}

		//将已经将输入流处理保存的图片文件进行格式转换
		BufferedImage src = null;
		try {
			src = ImageIO.read(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int height = src.getHeight();
		int width = src.getWidth();

		int[] pixels = new int[height * width];
		PixelGrabber grabber = new PixelGrabber(src, 0, 0, width, height, pixels, 0, width);
		grabber.grabPixels();
		MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
		Image image = Toolkit.getDefaultToolkit().createImage(mis);
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		buff.createGraphics().drawImage(image, 0, 0, null);
		ImageIO.write(buff, "bmp", out);
	
		return out;
	}
	/**
	 * BMP===>byte[]<br>
	 * * @param bmpFile<br>
	/*** @return
	 */
	public static byte[] bmpToBytes(File bmpFile) {
		byte[] bmpContent = null;
		BufferedImage bi;
		try {
			bi = ImageIO.read(bmpFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bi, "bmp", bos);
			bmpContent = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmpContent;
		
	}
    
    /**处理输入图片为bmp<br>
     * * @param inputStream图片文件输入流<br>
    /*** @param temp 文件仅以临时文件保存<br>
    /*** @return 处理过后的bmp图片<br>
    /*** @throws InterruptedException<br>
    /*** @throws IOException
     */
	public static File handleInputImage(InputStream inputStream, boolean temp) throws InterruptedException, IOException {
		int hash = inputStream.hashCode();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(date);
		//图片命名为日期时间（yyyyMMddHHmmss）+文件流hash值
		String imageName = dateStr + "_" + hash;
		File dir = new File(IMAGES_PATH_ROOT);
		File out = null;
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (temp) {
			out = File.createTempFile(IMAGES_PATH_ROOT + imageName, ".bmp");
		}else {
			out = new File(IMAGES_PATH_ROOT + imageName + ".bmp");
		}
		
		byte[] tempFile = new byte[1024];
		@SuppressWarnings("resource")
		OutputStream outputStream = new FileOutputStream(out);
		try {
			while ((inputStream.read(tempFile))!= -1) {
				outputStream.write(tempFile);
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//将已经将输入流处理保存的图片文件进行格式转换
		BufferedImage src = null;
		try {
			src = ImageIO.read(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int height = src.getHeight();
		int width = src.getWidth();

		int[] pixels = new int[height * width];
		PixelGrabber grabber = new PixelGrabber(src, 0, 0, width, height, pixels, 0, width);
		grabber.grabPixels();
		MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
		Image image = Toolkit.getDefaultToolkit().createImage(mis);
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		buff.createGraphics().drawImage(image, 0, 0, null);
		ImageIO.write(buff, "bmp", out);		
		return out;
	}
	
}
