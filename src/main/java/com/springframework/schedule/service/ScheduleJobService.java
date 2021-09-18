package com.springframework.schedule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.springframework.schedule.job.QuartzJobFactory;
import com.springframework.schedule.models.ScheduleJob;

@Service
public class ScheduleJobService {

	@Autowired 
	private Scheduler scheduler;
	
	public List<ScheduleJob> getAllJobList() {  
        List<ScheduleJob> jobList = new ArrayList<>();  
        try {  
        	// 주어진 문자열로 시작하는 작업그룹과 일치하는 GroupMatcher를 만든다.
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();  
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);  
            for (JobKey jobKey : jobKeySet) {  
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);  
                for (Trigger trigger : triggers) {  
                    ScheduleJob scheduleJob = new ScheduleJob();  
                    
                    // ScheduleJob 객체를 셋팅해준다.
                    this.wrapScheduleJob(scheduleJob, scheduler, jobKey, trigger);  
                    
                    // job들을 하나씩 jobList에 add하고 jobList를 화면에 보여준다.
                    jobList.add(scheduleJob);  
                }  
            }  
        } catch (SchedulerException e) {  
        	e.printStackTrace();   
        }  
        return jobList;  
    } 
	
	// scheduleJob 모델을 셋팅해준다.
	private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger) {  
        try {  
            scheduleJob.setJobName(jobKey.getName());  
            scheduleJob.setJobGroup(jobKey.getGroup()); 
  
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);  
            ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");  
            scheduleJob.setDesc(job.getDesc());  
            scheduleJob.setJobId(job.getJobId());
  
            /*
             * trigger.getKey() :
             * job_group_even.job_name_0
             * 
             * scheduler.getTriggerState(trigger.getKey()) : 트리거의 상태가 나온다.
             * ex) NORMAL, ...
             * */
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());  
            scheduleJob.setJobStatus(triggerState.name());  
            
            // CronTrigger 는 trigger의 자손이다.
            if(trigger instanceof CronTrigger) {  
                CronTrigger cronTrigger = (CronTrigger) trigger;  
                String cronExpression = cronTrigger.getCronExpression();  
                scheduleJob.setCronExpression(cronExpression);  
            }  
        } catch (SchedulerException e) {  
            e.printStackTrace(); 
        }  
    }
	
	public void saveOrupdate(ScheduleJob scheduleJob) throws Exception {
		Preconditions.checkNotNull(scheduleJob, "job is null");
		// Create job
		if(StringUtils.isEmpty(scheduleJob.getJobId())) {
			addJob(scheduleJob);
		}
		// Edit job
		else {
			
		}
	}
	
	private void addJob(ScheduleJob scheduleJob) throws Exception {
		checkNotNull(scheduleJob);
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");
		
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if(trigger != null){  
            throw new Exception("job already exists!");  
        }
        
        // simulate job info db persist operation
        scheduleJob.setJobId(String.valueOf(QuartzJobFactory.jobList.size() + 1));
        QuartzJobFactory.jobList.add(scheduleJob);
        
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();  
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);  
  
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());  
        trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();  
  
        scheduler.scheduleJob(jobDetail, trigger);  
	}
	
	private void checkNotNull(ScheduleJob scheduleJob) {
    	Preconditions.checkNotNull(scheduleJob, "job is null");
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobName()), "jobName is null");
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobGroup()), "jobGroup is null");
	}
}
