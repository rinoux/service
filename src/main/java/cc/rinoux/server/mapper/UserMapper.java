package cc.rinoux.server.mapper;

import java.util.List;

import cc.rinoux.server.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);
    int insert(User record);
    int insertSelective(User record);
    User selectByPrimaryKey(Integer uid);
    int updateByPrimaryKeySelective(User record);
    int updateByPrimaryKey(User record);
    List<User> getUsers();
    
    String getUserNameById(Integer uid);
}