package com.springframework.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springframework.schedule.models.ScheduleJob;
import com.springframework.schedule.service.ScheduleJobService;

@Controller
public class IndexController {

	@Autowired 
	private ScheduleJobService scheduleJobService;
	
	@RequestMapping("/index")
	public String index(Model model) {
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList();
		model.addAttribute("jobs", jobList);
		return "index";
	}
	
}
