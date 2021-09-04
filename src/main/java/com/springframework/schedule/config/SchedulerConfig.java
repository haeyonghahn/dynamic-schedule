package com.springframework.schedule.config;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.springframework.schedule.job.QuartzJobFactory;
import com.springframework.schedule.models.ScheduleJob;
import com.springframework.schedule.spring.AutowiringSpringBeanJobFactory;

@Configuration
public class SchedulerConfig {

	// JobFactory는 Job 클래스의 인스턴스 생성을 담당한다.
	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	/*
	 * schedulerFactoryBean :  
	 * SchedulerFactoryBean을 사용하여 Quartz 스케줄러(org.quartz.Scheduler)에 직접 접근할 수 있다. 
	 * 이를 통해서 새 작업 및 트리거를 생성하고 전체 스케줄러를 제어 및 모니터링할 수 있다.
	 * dataSource :
	 * 현재 사용하고 있는 db정보
	 * 
	 */
	@Bean
	public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		// 이를 통해 구성 파일의 설정을 업데이트할 때 DB의 트리거를 업데이트할 수 있다.
		factory.setOverwriteExistingJobs(true);				// Job이 존재할 경우 덮어쓰기
		factory.setDataSource(dataSource);					// DB 셋팅
		factory.setJobFactory(jobFactory);					// Job 인스턴스 생성
		factory.setQuartzProperties(quartzProperties());	// quartz 속성 셋팅
		/*
		 * 모든 빈 속성이 설정되었을 때 빈 인스턴스가 전체 구성의 유효성 검사 및 최종 초기화를 수행할 수 있다.
		 * 설정이 잘못된 경우(필수 속성 설정 실패 등) 또는 기타 이유로 초기화에 실패한 경우 Exception 이 발생한다.
		 */ 
		factory.afterPropertiesSet();						

		// scheduler는 jobDetail과 trigger에 담긴 정보를 이용해서 실제 Job의 실행 스케줄링을 담당한다.
		Scheduler scheduler = factory.getScheduler();
		scheduler.setJobFactory(jobFactory);				// scheduler에 job 등록

		List<ScheduleJob> jobs = QuartzJobFactory.getInitAllJobs();									// Jobs 모두 가져오기
		for (ScheduleJob job : jobs) {
			/*
			 * triggerKey의 정보 :
			 * group과 name이 있다.
			 * ex) group : job_group_even
			 *     name : job_name_0
			 * job_group_even.job_name_0
			 */ 
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());		// 트리거키 가져오기
			
			// trigger에는 Job을 언제, 어떤 주기로, 언제부터 언제까지 실행할지에 대한 정보가 담겨 있다.
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);					// 트리거 가져오기
			
			// 트리거 등록
			if(trigger == null) {
				/* 
				 * JobDetail에는 Job의 실제 구현 내용과 Job 실행에 필요한 제반 상세 정보가 담겨 있다.
				 * description : i am job number 0
				 * durability,
				 * group,
				 * jobClass : class com.springframework.schedule.job.QuartzJobFactory
				 * jobDataMap,
				 * key : job_group_even.job_name_0
				 * name : job_name_0
				 */
				JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
						.withIdentity(job.getJobName(), job.getJobGroup())
						.withDescription(job.getDesc()).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);
				/*
				 * jobDetail.getJobDataMap().get("scheduleJob")[0, 1, 2] : 
				 * ScheduleJob [jobId=0, jobName=job_name_0, jobGroup=job_group_even, 
				 * jobStatus=1, cronExpression=0/5 * * * * ?, desc=i am job number 0
				 * , interfaceName=interface0]
				 * */	
				
				/* 
				 * scheduleBuilder는 해당 job의 cronExpression을 셋팅한다.
				 * cronExpression : 0/5 * * * * ?
				 * */
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
						.getCronExpression());

				/*
				 * trigger :
				 * Trigger 'job_group_odd.job_name_1':  
				 * triggerClass: 'org.quartz.impl.triggers.CronTriggerImpl calendar: 'null' 
				 * misfireInstruction: 0 nextFireTime: null
				 * */
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
				
				scheduler.scheduleJob(jobDetail, trigger);
			}
			/*
			 * 만약, job을 동적으로 create 하게되면, 기존의 있던 job 들은 trigger가 존재하고
			 * 새롭게 등록된 job들은 trigger가 없다. 그렇기 때문에 
			 * if-else 조건을 통하여 scheduleJob을 rescheduleJob하거나 scheduleJob으로 나눈다.
			 * */
			else {
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
						.getCronExpression());
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(scheduleBuilder).build();
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}

		scheduler.start();
		return scheduler;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
}
