package cc.rinoux.server.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cc.rinoux.server.annotation.Restful;
import cc.rinoux.server.mapper.CupMapper;
import cc.rinoux.server.model.Cup;

/** 
* @author   杨锐
* @email    rinoux@foxmail.com
* @company  江苏南大五维科技有限公司
*
* @version  CupService.java
* @date     2016年4月1日 上午10:16:53 
* @function TODO
*/
@Restful
@Path("/cups/v1")
public class CupService {
	static final Logger logger = LoggerFactory.getLogger(CupService.class.getName());
	@Autowired
	CupMapper CupMapper;
	@POST
	@Path("/cup")
	@Produces("application/json")
	
	public Cup updateCup(@QueryParam("ip") String ip,
			@QueryParam("status") String status){
		Cup cup = CupMapper.selectByIp(ip);
		cup.setMemoryStatus(status);
		int i = CupMapper.updateByPrimaryKeySelective(cup);
		if (i>=1) {
			logger.info("查询成功");
			return cup;

		}
		logger.info("查询失败");
		return cup;		
	}
	
	@GET
	@Path("/cup/{cup_id}")
	@Produces("application/json")
	public Cup getCup(@PathParam("cup_id") Integer cupId){
		Cup cup = CupMapper.selectByPrimaryKey(cupId);
		return cup;
	}
}
