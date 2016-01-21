package cc.rinoux.server.restService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.rinoux.server.annotation.Restful;
import cc.rinoux.server.mapper.UserMapper;
import cc.rinoux.server.model.User;


@Path("/user/")
@Component
@Restful
//@Produces({MediaType.APPLICATION_JSON})
//@Consumes({MediaType.APPLICATION_JSON})
@Produces("application/json;charset=UTF-8")
@Consumes("text/plain")
public class UserService {
	//使用@Autowired标注，当需要使用UserMapper时，Spring会自动注入
	@Autowired
	UserMapper UserMapper;
	
	@GET
	@Path("{id}")
	public User selectUserById(@PathParam("id") Integer id){
		
		User user = new User();
		user = UserMapper.selectByPrimaryKey(id);
		System.out.println(user);
		return user;
	}

}
