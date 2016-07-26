package cc.rinoux.server.aliyun.oss;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import cc.rinoux.common.util.MD5;  
  
/**
 * OSS����������
 * ���ϴ��ļ�
 * ��ɾ���ļ�
 * ���������������ļ�
 * ���г�bucket
 * �ݻ�ȡָ����Object
 * @author Administrator
 *
 */


public class OSSOperator {  

	OSSClientConfig clientConfig = new OSSClientConfig();
	OSSClient client = clientConfig.getOSSClient();
	String bucketName = clientConfig.getBucketName();
	
	/**
	 * �ϴ��ļ�
	 * @param file
	 * @return �����ļ�url string
	 * @throws Exception
	 */
    public String uploadFile(File file) throws Exception{  

    	String MD5Value = MD5.getMD5ValueOfFile(file);


    	String name = file.getName().trim();
    	InputStream fileContent = new FileInputStream(file);
    	//����ǩ�����URL
    	Date expiration = new Date(new Date().getTime() + 3600 * 1000);
    	URL url = client.generatePresignedUrl(bucketName, file.getName(), expiration);
        //�����ϴ�Object��Metadata  
        ObjectMetadata objectMetadata=new ObjectMetadata();  
        objectMetadata.setContentLength(file.length());  
        objectMetadata.setCacheControl("no-cache");  
        objectMetadata.setHeader("Pragma", "no-cache");  
        objectMetadata.setContentType(contentType(file.getName().substring(file.getName().lastIndexOf("."))));  
        objectMetadata.setContentDisposition("inline;filename=" + file.getName());  
        //�ϴ��ļ�  
        PutObjectResult result = client.putObject(clientConfig.getBucketName(), name, fileContent, objectMetadata);

        if (MD5Value.equals(result.getETag().toString().toLowerCase())) {
        	return url.toString(); 
		}else {
	        return null;
		}
    } 
    /**
     * ɾ�������ļ�
     * @param fileName
     * @return trueΪɾ���ɹ���false�෴
     */
     public  boolean deleteFile(String fileName) {
    	 
		client.deleteObject(bucketName, fileName);
		if (client.getObject(bucketName, fileName) != null) {
			return false;
		}else {
			return true;
		}
	}
     
     public void deleteFiles() {
    	 DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
    	 
    	 client.deleteObjects(deleteObjectsRequest);
		
	}
      
     /** 
     * Description: �ж�OSS�����ļ��ϴ�ʱ�ļ���contentType 
     * @Version1.0 
     * @param FilenameExtension �ļ���׺ 
     * @return String  
     */  
     public static String contentType(String FilenameExtension){  
        if(FilenameExtension.equals("BMP")||FilenameExtension.equals("bmp")){return "image/bmp";}  
        if(FilenameExtension.equals("GIF")||FilenameExtension.equals("gif")){return "image/gif";}  
        if(FilenameExtension.equals("JPEG")||FilenameExtension.equals("jpeg")||  
           FilenameExtension.equals("JPG")||FilenameExtension.equals("jpg")||     
           FilenameExtension.equals("PNG")||FilenameExtension.equals("png")){return "image/jpeg";}  
        if(FilenameExtension.equals("HTML")||FilenameExtension.equals("html")){return "text/html";}  
        if(FilenameExtension.equals("TXT")||FilenameExtension.equals("txt")){return "text/plain";}  
        if(FilenameExtension.equals("VSD")||FilenameExtension.equals("vsd")){return "application/vnd.visio";}  
        if(FilenameExtension.equals("PPTX")||FilenameExtension.equals("pptx")||  
            FilenameExtension.equals("PPT")||FilenameExtension.equals("ppt")){return "application/vnd.ms-powerpoint";}  
        if(FilenameExtension.equals("DOCX")||FilenameExtension.equals("docx")||  
            FilenameExtension.equals("DOC")||FilenameExtension.equals("doc")){return "application/msword";}  
        if(FilenameExtension.equals("XML")||FilenameExtension.equals("xml")){return "text/xml";}  
        return "text/html";  
     }  
}  
