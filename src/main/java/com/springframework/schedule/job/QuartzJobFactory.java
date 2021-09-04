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
		for (int i = 0; i < 5; i++) {
			ScheduleJob job = new ScheduleJob();
			job.setJobId(String.valueOf(i));
			job.setJobName("job_name_" + i);
			if (i % 2 == 0) {
				job.setJobGroup("job_group_even");
			}
			else {
				job.setJobGroup("job_group_odd");
			}
			job.setJobStatus("1");
			job.setCronExpression(String.format("0/%s * * * * ?", (i + 1) * 5));
			job.setDesc("i am job number " + i);
			job.setInterfaceName("interface" + i);
			jobList.add(job);
		}
		return jobList;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
		String jobName = scheduleJob.getJobName();

		System.err.println(jobName);

		if (jobName.equals("job_name_4") || jobName.equals("addjob")) {
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
