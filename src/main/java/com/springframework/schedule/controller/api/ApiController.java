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
	
	@RequestMapping(value = "/deleteJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object deleteJob(ScheduleJob job){
		boolean flag = true;
		logger.info("params, job = {}", job);
		try {
			scheduleJobService.deleteJob(job);
		} catch (Exception e) {
			flag = false;
			logger.error("deleteJob ex:", e);
		}
		return flag;
	}
	
	@RequestMapping(value = "/resumeJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object resumeJob(ScheduleJob job){
		boolean flag = true;
		logger.info("params, job = {}", job);
		try {
			scheduleJobService.resumeJob(job);
		} catch (Exception e) {
			flag = false;
			logger.error("resumeJob ex:", e);
		}
		return flag;
	}
	
	@RequestMapping(value = "/pauseJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object pauseJob(ScheduleJob job){
		boolean flag = true;
		logger.info("params, job = {}", job);
		try {
			scheduleJobService.pauseJob(job);
		} catch (Exception e) {
			flag = false;
			logger.error("pauseJob ex:", e);
		}
		return flag;
	}
	
	@RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object runJob(ScheduleJob job){
		boolean flag = true;
		logger.info("params, job = {}", job);
		try {
			scheduleJobService.runJobOnce(job);
		} catch (Exception e) {
			flag = false;
			logger.error("runJob ex:", e);
		}
		return flag;
	}
}
