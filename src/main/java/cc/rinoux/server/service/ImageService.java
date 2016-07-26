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
 * @Datetime 2016��3��31������8:03:19
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
	 * ����ʽ�ӿڣ�������
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
	 * ����ͼƬ��ˮ��
	 * 
	 * @param uid �������û�id
	 * @param cupId ����Ŀ��ˮ����id
	 * @param fileInputStream ����ͼƬ��
	 * @param disposition ����ͼƬ�ı���Ϣ
	 * @return �Ƿ�ɹ�
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
		 * -->����ͼƬ,ת����ʽ����������ͼ 
		 * -->�ж��Ƿ���Է���
		 * -->�ҵ�socket���ӣ���cup_id��ص�ˮ������ͼƬ׼����Ϣ
		 * -->�洢��ͼƬ�������������ݿ����ͼƬ��Ϣ
		 * -->����ͼƬ����Դ��ַ���Ƿ��ͳɹ�����Ϣ
		 */
		try {
			image = ImageUtil.handleInputImage(fileInputStream, true);
		} catch (InterruptedException e) {
			logger.debug("ͼƬ�����ж�");
			e.printStackTrace();
		}
		thumbnail = ImageUtil.scaleImage(image, 40, 60);

		if (image.length() > 1048575) {
			return new RespModel(FAILED, RespDetail.IMAGE_OVERLARGE);
		}else {											
			//ȡ��ͼƬ����������
			byte[] msg = ImageUtil.bmpToBytes(image);	
			//����idȡ��ip		
			String ip = CupMapper.selectByPrimaryKey(cupId).getIp();		
			logger.info("�û�" + uid + "-->����Ŀ��ˮ����ip��" + ip + ",���ݴ�С" + msg.length);	
			//��ʼ�����Ͱ�������
			MessageSenderHelper helper = new MessageSenderHelper(uid, ip, msg, DataMsgType.IMAGE_DATA);
			//��ȡ���Ͱ��������
			SenderHelperPool helperPool = SenderHelperPool.instance;	
			//����µİ�������
			helperPool.addHelper(ip, helper);
			//��ȡ�ո���ӵİ�������
			helper = helperPool.getHelper(ip);	
			//���͵�0֡����
			try {
				helper.sendMessage(0);
				logger.info("���ڷ�������...");
			} catch (NullObjectException e) {
				image.deleteOnExit();
				thumbnail.deleteOnExit();
				return new RespModel(FAILED, RespDetail.DEVICE_OFFLINE);
			}
			try {
				//��ͼ������ͼ�ϴ���OSS
				imgUrl = OSSOperator.uploadFile(image);
				thumbUrl = OSSOperator.uploadFile(thumbnail);
			} catch (Exception e) {
				logger.info("�ϴ�ͼƬ�ļ���OSSʧ�ܣ�");
				e.printStackTrace();
			}	
			//�洢ͼƬ�����ݿ�
			Image img4Strore = new Image();	
			img4Strore.setuId(uid);
			img4Strore.setImgName(image.getName());
			img4Strore.setUrl(imgUrl);
			img4Strore.setThumbnail(thumbUrl);			
			ImageMapper.insertSelective(img4Strore);			
			Integer imgId = img4Strore.getImgId();
			//���뵽ˮ��-ͼƬ����
			int flag = CupImagesMapper.insert(new CupImages(cupId, imgId));
			if (flag != 0) {
				logger.info("�������ݿ�ɹ�");
			}
			return new RespModel(SUCCESS, RespDetail.UPLOAD_SUCCESS);
		}
		
	}
	/**
	 * ��ͼƬid��ȡͼƬ
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
			System.out.println("�޶���");
		}catch (Exception e) {
			// TODO: handle exception
		}

		return new RespModel(FAILED, RespDetail.UNKNOW);
	}
	/**
	 * ��ͼƬidɾ��ͼƬ
	 * @param imgID ͼƬID
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
			logger.debug("ɾ������ʧ�ܣ�");
		}
		return respPojo;	
	}
	/**
	 * ��ȡ�û��µ�����ͼƬ
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
