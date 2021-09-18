package com.springframework.schedule.controller.api;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springframework.schedule.models.ScheduleJob;
import com.springframework.schedule.service.ScheduleJobService;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	private static Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired 
	private ScheduleJobService scheduleJobService;
	
	@RequestMapping("/getAllJobs")
	public Object getAllJobs() throws SchedulerException{
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList(); 
		return jobList;
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
	public Object saveOrupdate(ScheduleJob job) {
		boolean flag = true;
		logger.info("params, job = {}", job);
		try {
			scheduleJobService.saveOrupdate(job);
		} catch(Exception e) {
			flag = false;
			logger.error("updateCron ex:", e);
		}
		return flag;
	}
}
