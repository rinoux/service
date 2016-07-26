package cc.rinoux.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cc.rinoux.common.util.ImageUtil;
import cc.rinoux.common.util.JpgToBmp;
import cc.rinoux.server.aliyun.oss.OSSOperator;
import cc.rinoux.server.annotation.Restful;
import cc.rinoux.server.exception.NullObjectException;
import cc.rinoux.server.exception.RequestDataNotExist;
import cc.rinoux.server.mapper.CupImagesMapper;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.mapper.ImageMapper;
import cc.rinoux.server.mina.helper.MessageSenderHelper;
import cc.rinoux.server.mina.helper.SenderHelperPool;
import cc.rinoux.server.mina.protocol.DataMsgType;
import cc.rinoux.server.model.CupImages;
import cc.rinoux.server.model.Image;
import cc.rinoux.server.pojo.RespModel;
import cc.rinoux.server.pojo.RespDetail;
/**
 * 
 * @Author Rinoux
 * @Orgnization www.5d.com
 * @Description TODO
 * @Datetime 2016年3月31日下午8:03:19
 */


@Path("/images/v1/")
@Restful
@Produces("application/json")
public class ImageService {
	final static Logger logger = LoggerFactory.getLogger(ImageService.class.getName());
		
	private static final int FAILED = 1;
	private static final int SUCCESS = 0;
	
	File image = null;
	File thumbnail = null;
	String imgUrl = null;
	String thumbUrl = null;
	
	@Autowired
	ImageMapper ImageMapper;
	
	@Autowired
	OSSOperator OSSOperator;
	
	@Autowired
	CupMapper CupMapper;
	
	@Autowired
	CupImagesMapper CupImagesMapper;
	
	@Autowired
	RespModel respPojo;

	/**
	 * 非正式接口，测试用
	 * @param form
	 * @param uri
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("test_upload")
	@Produces("application/json")
	public String uploadTest(FormDataMultiPart form, 
			@Context UriInfo uri, 
			@Context HttpServletRequest request) 
					throws FileNotFoundException, IOException{
		FormDataBodyPart part = form.getField("image");
		File image = part.getEntityAs(File.class);
		System.out.println(image.getName());

		File file = JpgToBmp.fromFile(image);
		ImageUtil.scaleImage(file, 320, 480);
		String path = file.getPath();
		return path;

	}

	/**
	 * 发送图片到水杯
	 * 
	 * @param uid 发送者用户id
	 * @param cupId 发送目的水杯的id
	 * @param fileInputStream 发送图片流
	 * @param disposition 发送图片的表单信息
	 * @return 是否成功
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RequestDataNotExist 
	 */
	@POST
	@Path("{uid}/{cup_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public RespModel forwardImage(@PathParam("uid") Integer uid,
			@PathParam("cup_id") Integer cupId,
			@FormDataParam("image") InputStream fileInputStream,
			@FormDataParam("image") FormDataContentDisposition disposition
			) throws FileNotFoundException, IOException, RequestDataNotExist{	
		
		/**
		 * -->接受图片,转换格式、生成缩略图 
		 * -->判断是否可以发送
		 * -->找到socket连接，向cup_id相关的水杯发送图片准备信息
		 * -->存储到图片服务器、向数据库存入图片信息
		 * -->返回图片的资源地址，是否发送成功等信息
		 */
		try {
			image = ImageUtil.handleInputImage(fileInputStream, true);
		} catch (InterruptedException e) {
			logger.debug("图片处理中断");
			e.printStackTrace();
		}
		thumbnail = ImageUtil.scaleImage(image, 40, 60);

		if (image.length() > 1048575) {
			return new RespModel(FAILED, RespDetail.IMAGE_OVERLARGE);
		}else {											
			//取出图片的像素数据
			byte[] msg = ImageUtil.bmpToBytes(image);	
			//根据id取出ip		
			String ip = CupMapper.selectByPrimaryKey(cupId).getIp();		
			logger.info("用户" + uid + "-->发送目的水杯的ip：" + ip + ",数据大小" + msg.length);	
			//初始化发送帮助对象
			MessageSenderHelper helper = new MessageSenderHelper(uid, ip, msg, DataMsgType.IMAGE_DATA);
			//获取发送帮助对象池
			SenderHelperPool helperPool = SenderHelperPool.instance;	
			//添加新的帮助对象
			helperPool.addHelper(ip, helper);
			//获取刚刚添加的帮助对象
			helper = helperPool.getHelper(ip);	
			//发送第0帧数据
			try {
				helper.sendMessage(0);
				logger.info("正在发送数据...");
			} catch (NullObjectException e) {
				image.deleteOnExit();
				thumbnail.deleteOnExit();
				return new RespModel(FAILED, RespDetail.DEVICE_OFFLINE);
			}
			try {
				//大图和缩略图上传到OSS
				imgUrl = OSSOperator.uploadFile(image);
				thumbUrl = OSSOperator.uploadFile(thumbnail);
			} catch (Exception e) {
				logger.info("上传图片文件到OSS失败！");
				e.printStackTrace();
			}	
			//存储图片到数据库
			Image img4Strore = new Image();	
			img4Strore.setuId(uid);
			img4Strore.setImgName(image.getName());
			img4Strore.setUrl(imgUrl);
			img4Strore.setThumbnail(thumbUrl);			
			ImageMapper.insertSelective(img4Strore);			
			Integer imgId = img4Strore.getImgId();
			//存入到水杯-图片联表
			int flag = CupImagesMapper.insert(new CupImages(cupId, imgId));
			if (flag != 0) {
				logger.info("存入数据库成功");
			}
			return new RespModel(SUCCESS, RespDetail.UPLOAD_SUCCESS);
		}
		
	}
	/**
	 * 按图片id获取图片
	 * @param imgID
	 * @return
	 */
	@GET
	@Path("/{img_id}")
	@Produces("application/json")
	public RespModel getImg(@PathParam("img_id") Integer imgID){
		RespModel response;
		try {
			Image image = ImageMapper.selectByPrimaryKey(imgID);
			if (image.equals(null)) {
				response = new RespModel(FAILED, RespDetail.NONE_FILE);
				return response;
			}else {
				response = new RespModel(SUCCESS, RespDetail.UPLOAD_SUCCESS, image);
				return response;
				
			}

		} catch (NullObjectException e) {
			System.out.println("无对象");
		}catch (Exception e) {
			// TODO: handle exception
		}

		return new RespModel(FAILED, RespDetail.UNKNOW);
	}
	/**
	 * 按图片id删除图片
	 * @param imgID 图片ID
	 * @return
	 */
	@DELETE
	@Path("/{img_id}")
	@Produces("application/json")
	public RespModel deleteImage(@PathParam("img_id") Integer imgID){
		try {
			int flag = ImageMapper.deleteByPrimaryKey(imgID);
			if (flag == 1) {
				respPojo = new RespModel(SUCCESS, RespDetail.QUERY_SUCCESS);
				return respPojo;
			}
			else {
				respPojo =new RespModel(FAILED, RespDetail.NO_SUCH_FILE);
				return respPojo;
			}
		} catch (Exception e) {
			logger.debug("删除操作失败！");
		}
		return respPojo;	
	}
	/**
	 * 获取用户下的所有图片
	 * @return
	 */
	@GET
	@Path("/images/{uid}")
	@Produces("application/json")
	public RespModel getUserImagesList(
			@PathParam("uid") Integer uid,
			@QueryParam("start") Integer start,
			@QueryParam("capacity") Integer capacity){
		return null;		
	}
}
