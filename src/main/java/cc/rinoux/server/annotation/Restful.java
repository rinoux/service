package cc.rinoux.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//��ע�ӿ�

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restful {
	//��ʹ��ʱ���������ϲ�ע���У�������@Path("/user/")�������ڵĲ�����������������
	//����д��ʾʹ�������Ĭ��ֵ
	/**
	 * ע�����ͺͽӿڵĶ������ƣ�ע���е�ÿһ�����������������ע�����͵�һ��Ԫ�أ�
	 * ע���еķ���������һ�����ܰ���������Ҳ�����׳��쳣���ҷ����ķ������ͱ���Ϊ������
	 * ����String��Class��enums�����������и�ȱʡֵ��
	 * 
	 * �������е�default ��xyz��
	 */
	//String value() default "xyz"
		
}
