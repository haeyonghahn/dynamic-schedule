package com.springframework.schedule.spring;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/*
 * SpringBeanJobFactory: 
 * Quartz Job Bean을 동적으로 autowired 추가하기 위한 클래스이다.
 * 즉, 스프링부트가 제공하는 SpringBeanJobFactory를 통해 애플리케이션 구동 완료 후에 
 * 동적으로 추가하는 Job bean에도 의존 관계를 주입할 수 있다.
 * 
 * ApplicationContextAware :
 * Bean 들의 ID 값을 이용하여 ApplicationContext 객체로부터 
 * 동적으로 객체를 얻고 싶을 때 사용한다. 즉, 이것을 사용하여
 * Application 에서 Component로 가지고 있는 Bean에 접근이 가능하다.
 */
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	/* AutowireCapableBeanFactory :
	 * AutowireCapableBeanFactory의 autowireBean을 이용하여 
	 * 인스턴스화 된 객체에 종속성을 주입할 수 있다.
	 * 
	 * transient :
	 * transient는 Serialize하는 과정에 제외하고 싶은 경우 선언하는 키워드이다.
	 * 패스워드와 같은 보안정보가 직렬화(Serialize) 과정에서 제외하고 싶은 경우에 적용된다.
	 * 다양한 이유로 데이터를 전송을 하고 싶지 않을 때 선언할 수 있다.
	 */
	private transient AutowireCapableBeanFactory beanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		/* applicationContext : 
		 * 해당 어플리케이션의 bean 정보를 확인할 수 있다. 
		 */
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}	
}
