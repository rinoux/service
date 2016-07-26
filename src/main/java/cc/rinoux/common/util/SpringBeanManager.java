package cc.rinoux.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author Rinoux
 * @Orgnization www.5d-tech.com
 * @Description TODO
 * @Datetime 2016��5��11������2:48:19
 */
public class SpringBeanManager {
	

	/**
	 * 
	��spring�����bean�и���bean id��ȡbean
	@param beanName
	@return
	 */
	public static Object getBean(String beanName) {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
		return ac.getBean(beanName);				
	}

}
