package cc.rinoux.server.mapper;

import cc.rinoux.server.model.Cup;

public interface CupMapper {

    int deleteByPrimaryKey(Integer cupId);
    int insert(Cup record);
    int insertSelective(Cup record);
    
    Cup selectByPrimaryKey(Integer cupId);
    Cup selectByIp(String ip);
    
    int updateByPrimaryKeySelective(Cup record);
    int updateByPrimaryKey(Cup record);
}