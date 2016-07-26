package cc.rinoux.server.application;

import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.reflections.Reflections;

import cc.rinoux.server.annotation.Restful;
import cc.rinoux.server.filter.CORSResponseFilter;

/**
 * ResourceConfig的实现类
 * 注册必要的类
 * 否者无法实现某项服务
 * @author Administrator
 *
 */
public class Application extends ResourceConfig{
	
	public Application(){
		//restful服务所在包名,必须规定，否则restful api无效
		packages("cc.rinoux.server.service");
		//注册multipartform特性，否则无法使用
		register(MultiPartFeature.class);
		//加载请求上下文过滤器
		register(RequestContextFilter.class);
		//解决跨域问题
		register(CORSResponseFilter.class);
		//加载json的特征
		register(JacksonFeature.class);	

		Reflections reflections = new Reflections(getClass().getPackage().getName());		
		//把所有以@Restful标识的类注册
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Restful.class);
		for (Class<?> cls : annotated) {
			register(cls);
		}
		
	}

}
