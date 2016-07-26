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
* @author   ����
* @email    rinoux@foxmail.com
* @company  �����ϴ���ά�Ƽ����޹�˾
*
* @version  CupService.java
* @date     2016��4��1�� ����10:16:53 
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
			logger.info("��ѯ�ɹ�");
			return cup;

		}
		logger.info("��ѯʧ��");
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
