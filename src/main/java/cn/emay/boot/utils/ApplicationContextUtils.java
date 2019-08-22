package cn.emay.boot.utils;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * Spring Bean获取工具
 * 
 * @author Frank
 *
 */
public class ApplicationContextUtils {

	/**
	 * 上下文对象实例
	 */
	private static ApplicationContext APPLICATION_CONTEXT;

	/**
	 * 传入上下文对象
	 * 
	 * @param applicationContext
	 *            上下文对象
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtils.APPLICATION_CONTEXT = applicationContext;
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return APPLICATION_CONTEXT;
	}

	/**
	 * 通过name获取 Bean.
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 * 
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * 
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	/**
	 * 获取Bean管理工厂
	 * 
	 * @return
	 */
	public static DefaultListableBeanFactory getListableBeanFactory() {
		return (DefaultListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
	}

	/**
	 * 注册bean
	 * 
	 * @param beanClass
	 * @param beanName
	 */
	public static void registBean(Class<?> beanClass, String... beanName) {
		String name = beanClass.getName();
		if (beanName != null && beanName.length > 0) {
			name = beanName[0];
		}
		BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
		getListableBeanFactory().registerBeanDefinition(name, beanDefinition.getBeanDefinition());
	}

	/**
	 * 注册单例bean
	 * 
	 * @param bean
	 */
	public static void registSingletonBean(Object bean) {
		getListableBeanFactory().registerSingleton(bean.getClass().getName(), bean);
	}

}
