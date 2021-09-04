package com.springframework.schedule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    this.wrapScheduleJob(scheduleJob, scheduler, jobKey, trigger);  
                    jobList.add(scheduleJob);  
                }  
            }  
        } catch (SchedulerException e) {  
        	e.printStackTrace();   
        }  
        return jobList;  
    } 
	
	private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger) {  
        try {  
            scheduleJob.setJobName(jobKey.getName());  
            scheduleJob.setJobGroup(jobKey.getGroup()); 
  
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);  
            ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");  
            scheduleJob.setDesc(job.getDesc());  
            scheduleJob.setJobId(job.getJobId());
  
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());  
            scheduleJob.setJobStatus(triggerState.name());  
            if(trigger instanceof CronTrigger) {  
                CronTrigger cronTrigger = (CronTrigger) trigger;  
                String cronExpression = cronTrigger.getCronExpression();  
                scheduleJob.setCronExpression(cronExpression);  
            }  
        } catch (SchedulerException e) {  
            e.printStackTrace(); 
        }  
    }
}
