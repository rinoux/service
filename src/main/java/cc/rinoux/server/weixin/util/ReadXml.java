package cc.rinoux.server.weixin.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXml {

	private static Logger log = LoggerFactory.getLogger(ReadXml.class);

	/**
	 * 读XML
	 * 
	 * @param fileName
	 *            文件路径文件�?
	 * 
	 * @return 解析XML得到的�??
	 */
	public static Map<String, String> xmlRead(String fileName) {

		Map<String, String> xmlreadMap = new HashMap<String, String>();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			NodeList employees = document.getChildNodes();

			for (int i = 0; i < employees.getLength(); i++) {

				Node employee = employees.item(i);
				NodeList employeeInfo = employee.getChildNodes();

				for (int j = 0; j < employeeInfo.getLength(); j++) {

					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();

					for (int k = 1; k < employeeMeta.getLength(); k++) {

						if (k % 2 == 1) {

							xmlreadMap.put(employeeMeta.item(k).getNodeName(),
									employeeMeta.item(k).getTextContent());
							log.info("XML文件解析内容�?" + employeeMeta.item(k).getNodeName() + ":"
									+ employeeMeta.item(k).getTextContent());
						}
					}
				}
			}
			log.info("XML文件解析成功");
		} catch (FileNotFoundException e) {
			log.error("XML文件解析异常" + e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error("XML文件解析异常" + e.getMessage());
		} catch (SAXException e) {
			log.error("XML文件解析异常" + e.getMessage());
		} catch (IOException e) {
			log.error("XML文件解析异常" + e.getMessage());
		}
		return xmlreadMap;
	}

}
