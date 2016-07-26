package cc.rinoux.server.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cc.rinoux.common.util.ImageUtil;
import cc.rinoux.server.aliyun.oss.OSSOperator;
import cc.rinoux.server.annotation.Restful;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.mapper.ImageMapper;
import cc.rinoux.server.mapper.UserMapper;
import cc.rinoux.server.mina.helper.MessageSenderHelper;
import cc.rinoux.server.mina.helper.SenderHelperPool;
import cc.rinoux.server.mina.protocol.DataMsgType;
import cc.rinoux.server.model.Cup;
import cc.rinoux.server.model.User;
import cc.rinoux.server.pojo.RespDetail;
import cc.rinoux.server.pojo.RespModel;
/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d-tech.com
 * @Description 测试API
 * @Datetime 2016年5月9日下午1:56:34
 */
@Path("/test/")
@Restful
@Produces("application/json;charset=UTF-8")
public class TestService {
	
	
	static final Logger LOGGER = LoggerFactory.getLogger(TestService.class.getName());
	
	private static final int FAILED = 1;
	private static final int SUCCESS = 0;
	@Autowired
	OSSOperator OSSOperationUtil;
	@Autowired
	ImageMapper ImageMapper;
	@Autowired
	CupMapper CupMapper;	
	@Autowired
	UserMapper userMapper;

	
	@DELETE
	@Path("/cup")
	public Cup deleteCup(@QueryParam("ip")String ip){
	
		Cup cup = CupMapper.selectByIp(ip);
		if (cup == null) {
			LOGGER.info("查询失败");
		}
		Integer cupId = cup.getCupId();
		System.out.println("待删除：" + cupId);
		int flag = CupMapper.deleteByPrimaryKey(cupId);
		if (flag>0) {
			LOGGER.info("--------->删除成功");
		}else {
			LOGGER.info("--------->删除失败");
		}
		return cup;
	}
	@POST
	@Path("/cup")
	public void addCup(@QueryParam("ip") String ip,
			@QueryParam("status") String status){
		Cup cup = CupMapper.selectByIp(ip);
		cup.setMemoryStatus(status);
		int flag = CupMapper.updateByPrimaryKeySelective(cup);
		if (flag>0) {
			LOGGER.info("--------->添加成功");
		}else {
			LOGGER.info("--------->添加失败");
		}
	}
	
	@GET
	@Path("/cup")
	@Produces("application/json")
	@Consumes(MediaType.TEXT_PLAIN)
	
	public Cup updateCup(@QueryParam("ip") String ip,
			@QueryParam("status") String status){
		Cup cup = CupMapper.selectByIp(ip);
		cup.setMemoryStatus(status);
		int i = CupMapper.updateByPrimaryKeySelective(cup);
		if (i>=1) {
			LOGGER.info("查询成功");
			return cup;

		}
		LOGGER.info("查询失败");
		return cup;		
	}
	@GET
	@Path("/getuser")
	@Produces("application/json")
	@Consumes(MediaType.TEXT_HTML)
	public User getUser(@QueryParam("uid") Integer uid){
		User user = userMapper.selectByPrimaryKey(uid);
		
		return user;
		
	}
	@POST
	@Path("{uid}/to/{cup_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	
	public RespModel sendImageToCup(@PathParam("uid") Integer uid,
			@PathParam("cup_id") Integer cupId,
			@FormDataParam("image") InputStream fileInputStream,
			@FormDataParam("image") FormDataContentDisposition disposition
			) throws Exception{
		/**
		 * ①找到socket连接，向cup_id相关的水杯发送图片准备信息
		 * ②判断是否可以发送
		 * ③接受图片
		 * ④转换格式、生成缩略图、存储到图片服务器、向数据库存入图片信息
		 * ⑤返回图片的资源地址，是否发送成功等信息
		 */

		
		//File image = ImageUtil.jpgToBmp(fileInputStream);
		//image = imageUtil.imgToBmp565(image);
		File image = ImageUtil.handleInputImage(fileInputStream);

		//ImageUtil.generateThumbnail(image);
		//Image insImage = new Image(uid, image.getPath(), image.getName());

		if (image.length() > 1048575) {
			return new RespModel(FAILED, RespDetail.IMAGE_OVERLARGE);
		}else {				
			String resStr = OSSOperationUtil.uploadFile(image);		
			byte[] msg = ImageUtil.bmpToBytes(image);	
		
			//根据id取出ip		
			String ip = CupMapper.selectByPrimaryKey(cupId).getIp();		
			LOGGER.info("发送目的水杯的ip：" + ip + ",数据大小" + msg.length);		
			//MessageSenderHelper helper = new MessageSenderHelper(ip,msg);
			MessageSenderHelper helper = new MessageSenderHelper(uid, ip, msg, DataMsgType.IMAGE_DATA);
			SenderHelperPool helperPool = SenderHelperPool.instance;		
			helperPool.addHelper(ip, helper);		
			helper = helperPool.getHelper(ip);		
			helper.sendMessage(0);	
			return new RespModel(SUCCESS, resStr);
		}
	}
	@Deprecated
	@POST
	@Path("{uid}/testo/{cup_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	
	public RespModel sendImageCup(@PathParam("uid") Integer uid,
			@PathParam("cup_id") Integer cupId,
			@FormDataParam("image") InputStream fileInputStream,
			@FormDataParam("image") FormDataContentDisposition disposition
			) throws Exception{
				
		
		BufferedInputStream bis = new BufferedInputStream(fileInputStream);
		//bis.skip(54);
		byte[] b = new byte[307200];
		bis.read(b);
		
		String ip = CupMapper.selectByPrimaryKey(cupId).getIp();
		LOGGER.info("发送目的水杯的ip：" + ip);
		
		MessageSenderHelper helper = new MessageSenderHelper(ip, b);
		SenderHelperPool helperPool = SenderHelperPool.instance;
		helperPool.addHelper(ip, helper);
		
		helper = helperPool.getHelper(ip);
		helper.sendMessage(0);
		return new RespModel(SUCCESS, RespDetail.QUERY_SUCCESS);
	
	}

}
