package cc.rinoux.server.weixin.util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WirteXml {
	
	private static Logger log = LoggerFactory.getLogger(WirteXml.class);

	private static Document document;

	/**
	 * 写XML
	 * 
	 * @param fileName
	 *            文件路径文件�?
	 * @param tokenString
	 *            token
	 * @param tokenexpires_in
	 *            token超时
	 * @param tiketString
	 *            tiket
	 * @param tiketexpiresin
	 *            tiket超时
	 * @return
	 */
	public static void xmlWrite(String fileName, String tokenString,
			String tokenexpiresin, String tiketString,
			String tiketexpiresin) {
		init();

		String nowTimeString = Long.toString(System.currentTimeMillis() / 1000);

		Element root = document.createElement("cacheTokenAndTiket"); // 创建根节�?
																		// ----cacheTokenAndTiket
		document.appendChild(root); // /添加根节�? ----cacheTokenAndTiket

		/* cacheToken */
		Element cacheTokenElement = document.createElement("cacheToken");

		Element tokenTimeElement = document.createElement("TokenTime");
		tokenTimeElement.appendChild(document.createTextNode(nowTimeString));

		Element tokenElement = document.createElement("Token");
		tokenElement.appendChild(document.createTextNode(tokenString));

		Element tokenexpires_inElement = document
				.createElement("Tokenexpires_in");
		tokenexpires_inElement.appendChild(document
				.createTextNode(String.valueOf(tokenexpiresin)));

		cacheTokenElement.appendChild(tokenTimeElement);

		cacheTokenElement.appendChild(tokenElement);

		cacheTokenElement.appendChild(tokenexpires_inElement);

		/* cacheTiket */
		Element cacheTiketElement = document.createElement("cacheTiket");

		Element tiketTimeElement = document.createElement("TiketTime");
		tiketTimeElement.appendChild(document.createTextNode(nowTimeString));

		Element tiketElement = document.createElement("Tiket");
		tiketElement.appendChild(document.createTextNode(tiketString));

		Element tiketexpires_inElement = document
				.createElement("Tiketexpires_in");
		tiketexpires_inElement.appendChild(document
				.createTextNode(String.valueOf(tiketexpiresin)));

		cacheTiketElement.appendChild(tiketTimeElement);
		cacheTiketElement.appendChild(tiketElement);
		cacheTiketElement.appendChild(tiketexpires_inElement);

		root.appendChild(cacheTokenElement);
		root.appendChild(cacheTiketElement);

		// 将DOM对象document写入到xml文件�?
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			FileOutputStream fileOutputStream = new FileOutputStream(fileName);
			PrintWriter pw = new PrintWriter(fileOutputStream);
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result); // 关键转换
			fileOutputStream.close();
			log.info("生成XML文件成功!");

		} catch (TransformerConfigurationException e) {
			log.error("生成XML文件失败�?" + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("生成XML文件失败�?" + e.getMessage());
		} catch (FileNotFoundException e) {
			log.error("生成XML文件失败�?" + e.getMessage());
		} catch (TransformerException e) {
			log.error("生成XML文件失败�?" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("生成XML文件失败�?" + e.getMessage());
		}
	}

	private static void init() {
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			//System.out.println(e.getMessage());
			log.error("生成XML初始化失败：" + e.getMessage());
		}

	}

}
