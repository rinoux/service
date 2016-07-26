package cc.rinoux.server.aliyun.oss;

import com.aliyun.oss.OSSClient;

import cc.rinoux.common.util.PropertiesUtil;

public class OSSClientConfig {
	
	
	String prop = "ossconfig.properties";
	public String endpoint = PropertiesUtil.readValue(prop, "oss.endpoint");
	public String accessKeyId = PropertiesUtil.readValue(prop, "oss.accessKeyId");
	public String accessKeySecret = PropertiesUtil.readValue(prop, "oss.accessKeySecret");
	public String bucketName = PropertiesUtil.readValue(prop, "oss.bucketName");
	//public String bucketAccessUrl = "rinoux.oss-cn-hangzhou.aliyuncs.com";
	public String bucketAccessUrl = PropertiesUtil.readValue(prop, "oss.imgAceessurl");
	public String getEndpoint() {
		return endpoint;
	}
	public String getBucketName() {
		return bucketName;
	}
	public String getBucketAccessUrl() {
		return bucketAccessUrl;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	
	public OSSClient getOSSClient(){
		return new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}
}
