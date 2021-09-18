package com.springframework.schedule.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.Lists;
import com.springframework.schedule.models.ScheduleJob;

public class QuartzJobFactory implements Job {

	public static List<ScheduleJob> jobList = Lists.newArrayList();

	public static List<ScheduleJob> getInitAllJobs() {
		return jobList;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
		String jobName = scheduleJob.getJobName();

		System.err.println(jobName);
	}
}
