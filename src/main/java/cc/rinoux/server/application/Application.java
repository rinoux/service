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
 * ResourceConfig��ʵ����
 * ע���Ҫ����
 * �����޷�ʵ��ĳ�����
 * @author Administrator
 *
 */
public class Application extends ResourceConfig{
	
	public Application(){
		//restful�������ڰ���,����涨������restful api��Ч
		packages("cc.rinoux.server.service");
		//ע��multipartform���ԣ������޷�ʹ��
		register(MultiPartFeature.class);
		//�������������Ĺ�����
		register(RequestContextFilter.class);
		//�����������
		register(CORSResponseFilter.class);
		//����json������
		register(JacksonFeature.class);	

		Reflections reflections = new Reflections(getClass().getPackage().getName());		
		//��������@Restful��ʶ����ע��
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Restful.class);
		for (Class<?> cls : annotated) {
			register(cls);
		}
		
	}

}
