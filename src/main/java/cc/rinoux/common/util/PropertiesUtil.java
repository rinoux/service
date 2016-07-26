package cc.rinoux.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016��4��22������9:14:14
 */
public class PropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private PropertiesUtil() {
    }
    /**
     * ��ȡ�����ļ�ĳ����
     */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            // ע��·���� / ��ʼ��û������
            if (!filePath.startsWith("/"))
                filePath = "/" + filePath;
            InputStream in = PropertiesUtil.class.getResourceAsStream(filePath);
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            logger.error("-->" + e);
            return null;
        }
    }
    /**
     * ��ӡ�����ļ�ȫ�����ݣ�filePath�������ļ����������·����props/test.properties��
     */
    public static void readProperties(String filePath) {
        Properties props = new Properties();
        try {
            // ע��·���� / ��ʼ��û������
            if (!filePath.startsWith("/"))
                filePath = "/" + filePath;
            InputStream in = PropertiesUtil.class.getResourceAsStream(filePath);
            props.load(in);
            Enumeration<?> en = props.propertyNames();
            // ������ӡ
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty(key);
                logger.info(key + ":" + Property);
            }
        } catch (Exception e) {
            logger.error("-->" + e);
        }
    }
    /**
     * ��ֵд�������ļ�
     */
    public static void writeProperties(String fileName, String parameterName, String parameterValue) throws Exception {
        // ���ز����ر�ע�⣬�����maven��Ŀ���뵽\targetĿ¼�²鿴�ļ���������Դ������
        // ע��·�����ܼ� / �ˣ��������Ƴ���
        if (fileName.startsWith("/"))
            fileName.substring(1);
        String filePath = PropertiesUtil.class.getResource("/").getPath()+fileName;

        // ��ȡ�����ļ�
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        in.close();
        OutputStream out = new FileOutputStream(filePath);
        // �����������ƺ�ֵ
        pps.setProperty(parameterName, parameterValue);
        // comments ���������ļ���ע��
        pps.store(out, "Update " + parameterName + " name");
        logger.info("���浽�ļ��ɹ�");
        out.flush();
        out.close();
    }
    /**
     * д��properties�ļ�����һ�ַ���<br>
     * @param propFile
    * @param key
    * @param value
    * @throws IOException
     */
    public static void write2prop(String propFile, String key, String value) throws IOException {
        if (propFile.startsWith("/"))
            propFile.substring(1);
        //��ȡtomcat�ļ���·��
        String path = System.getProperty("catalina.home") + "/" + "webapps/service/" +propFile;
        Properties prop = new Properties();
        FileOutputStream fos = new FileOutputStream(path, true);
        prop.put(key, value);
        prop.store(fos, "ip�ѱ���");
        logger.info("����ɹ�");
        fos.close();	
	}
}
