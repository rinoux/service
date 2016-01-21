package cc.rinoux.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//批注接口

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restful {
	//在使用时添加在类的上部注释中，类似于@Path("/user/")，括号内的参数由下面语句决定，
	//不填写表示使用下面的默认值
	/**
	 * 注释类型和接口的定义类似，注释中的每一个方法都定义了这个注释类型的一个元素，
	 * 注释中的方法的声明一定不能包含参数，也不能抛出异常，且方法的返回类型必须为简单类型
	 * 例如String、Class、enums，方法可以有个缺省值；
	 * 
	 * 如下例中的default ”xyz“
	 */
	//String value() default "xyz"
		
}
