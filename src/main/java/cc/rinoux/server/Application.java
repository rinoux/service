package cc.rinoux.server;

import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.reflections.Reflections;

import cc.rinoux.server.annotation.Restful;

public class Application extends ResourceConfig{
	
	public Application(){
		register(RequestContextFilter.class);
		register(JacksonFeature.class);	
		Reflections reflections = new Reflections(getClass().getPackage().getName());
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Restful.class);
		for (Class<?> cls : annotated) {
			register(cls);
		}
		
	}

}
