package cc.rinoux.server.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import cc.rinoux.server.mapper.ImageMapper;
import cc.rinoux.server.mapper.UserMapper;
import cc.rinoux.server.model.Image;
import cc.rinoux.server.model.User;
import cc.rinoux.server.pojo.RespModel;

//用户功能API，v1为版本
@Path("/users/v1")
@Produces("application/json;charset=UTF-8")
@Consumes("text/plain")
public class UserService {
	//使用@Autowired标注，当需要使用UserMapper时，Spring会自动装配注入
	@Autowired
	UserMapper UserMapper;
	@Autowired
	ImageMapper ImageMapper;
	
	/**
	 * 保留接口，根据id获取用户信息
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	public User selectUserById(@PathParam("id") Integer id){
		
		User user = new User();
		user = UserMapper.selectByPrimaryKey(id);
		System.out.println(user);
		return user;

		//return UserMapper.selectById(id);
		//return null;
	}

	/**
	 * 保留接口，获取所有用户
	 * @return
	 */
	@GET
	@Path("all")
	@Produces({MediaType.APPLICATION_JSON})
	public RespModel getAllUsers(){
		RespModel response = new RespModel();
		List<User> users;
		users = UserMapper.getUsers();
		int flag = 0;
		String mString = null;
		for (User user : users) {
			if (user != null) {
				flag = 0;
				mString = "not null";
			}else {
				flag = 1;
				mString = "no data found";
			}
			
		}
		
		response.code = flag;
		response.msg = mString;
		response.data = users;
		
		return response;
		
	}
	/**
	 * 根据用户id获取该用户下所有的图片
	 * @param uid
	 * @return
	 */
	@GET
	@Path("/{uid}/images")
	@Produces("application/json;charset=UTF-8")
	@Consumes("text/plain")
	public RespModel getImgs(@PathParam("uid") Integer uid){
		RespModel imgListRes = new RespModel();
		List<Image> imgList ;
		imgList = ImageMapper.selectImagesByUID(uid);
		for(Image image : imgList){
			if (!image.equals(null)) {
				imgListRes.code = 0;
				imgListRes.msg = "success!";
				imgListRes.data = imgList;
			}else {
				imgListRes.code = 1;
				imgListRes.msg = "no data found!";
			}
		}	
		return imgListRes;	
	}
	
	
	@GET
	@Path("user/{uid}")
	@Produces("application/json;charset=UTF-8")
	@Consumes("text/plain")
	
	public RespModel updateUser(@PathParam("uid") Integer uid){
		User user = UserMapper.selectByPrimaryKey(uid);
		user.setAvatar("no avator");
		int flag = UserMapper.updateByPrimaryKeySelective(user);
		if (flag > 0) {
			return new RespModel(0, "OK");
		}else {
			return new RespModel(1,"not_ok");
		}
	}
	
}
