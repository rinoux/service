package cc.rinoux.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author Rinoux
 * @Orgnization www.5d-tech.com
 * @Description TODO
 * @Datetime 2016年5月11日下午2:48:19
 */
public class SpringBeanManager {
	

	/**
	 * 
	从spring管理的bean中根据bean id获取bean
	@param beanName
	@return
	 */
	public static Object getBean(String beanName) {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
		return ac.getBean(beanName);				
	}

}
