package cc.rinoux.server.mapper;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<K,V> {

	  public V selectById(@Param("id") K id);
	  public int insert(V obj);
	  public int update(V obj);
	  public int delete(V obj);

}
