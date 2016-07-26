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
 * @Datetime 2016年4月22日上午9:14:14
 */
public class PropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private PropertiesUtil() {
    }
    /**
     * 读取配置文件某属性
     */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            // 注意路径以 / 开始，没有则处理
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
     * 打印配置文件全部内容（filePath，配置文件名，如果有路径，props/test.properties）
     */
    public static void readProperties(String filePath) {
        Properties props = new Properties();
        try {
            // 注意路径以 / 开始，没有则处理
            if (!filePath.startsWith("/"))
                filePath = "/" + filePath;
            InputStream in = PropertiesUtil.class.getResourceAsStream(filePath);
            props.load(in);
            Enumeration<?> en = props.propertyNames();
            // 遍历打印
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
     * 将值写入配置文件
     */
    public static void writeProperties(String fileName, String parameterName, String parameterValue) throws Exception {
        // 本地测试特别注意，如果是maven项目，请到\target目录下查看文件，而不是源代码下
        // 注意路径不能加 / 了，加了则移除掉
        if (fileName.startsWith("/"))
            fileName.substring(1);
        String filePath = PropertiesUtil.class.getResource("/").getPath()+fileName;

        // 获取配置文件
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        in.close();
        OutputStream out = new FileOutputStream(filePath);
        // 设置配置名称和值
        pps.setProperty(parameterName, parameterValue);
        // comments 等于配置文件的注释
        pps.store(out, "Update " + parameterName + " name");
        logger.info("保存到文件成功");
        out.flush();
        out.close();
    }
    /**
     * 写入properties文件的另一种方法<br>
     * @param propFile
    * @param key
    * @param value
    * @throws IOException
     */
    public static void write2prop(String propFile, String key, String value) throws IOException {
        if (propFile.startsWith("/"))
            propFile.substring(1);
        //获取tomcat文件夹路径
        String path = System.getProperty("catalina.home") + "/" + "webapps/service/" +propFile;
        Properties prop = new Properties();
        FileOutputStream fos = new FileOutputStream(path, true);
        prop.put(key, value);
        prop.store(fos, "ip已保存");
        logger.info("保存成功");
        fos.close();	
	}
}
