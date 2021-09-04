package com.springframework.schedule.controller.api;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springframework.schedule.models.ScheduleJob;
import com.springframework.schedule.service.ScheduleJobService;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired 
	private ScheduleJobService scheduleJobService;
	
	@RequestMapping("/getAllJobs")
	public Object getAllJobs() throws SchedulerException{
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList(); 
		return jobList;
	}
}
