package com.likai.activiti5.controller;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/page")
public class pageController {


	@RequestMapping(value="/main",method=RequestMethod.GET)
	public String main() {
		return "main" ;
	}
	
	@RequestMapping(value="/userManage",method=RequestMethod.GET)
	public String userManage() {
		return "userManage" ;
	}
	
	@RequestMapping(value="/groupManage",method=RequestMethod.GET)
	public String groupManage() {
		return "groupManage" ;
	}
	
	@RequestMapping(value="/authManage",method=RequestMethod.GET)
	public String authManage() {
		return "authManage" ;
	}
	
	@RequestMapping(value="/deployManage",method=RequestMethod.GET)
	public String deployManage() {
		return "deployManage" ;
	}
	
	@RequestMapping(value="/processDefinitionManage",method=RequestMethod.GET)
	public String processDefinitionManage() {
		return "processDefinitionManage" ;
	}
	
	@RequestMapping(value="/leaveManage",method=RequestMethod.GET)
	public String leaveManage() {
		return "leaveManage" ;
	}
	
	
	@RequestMapping(value="/daibanManage",method=RequestMethod.GET)
	public String daibanManage() {
		return "daibanManage" ;
	}

	@RequestMapping(value="/yibanManage",method=RequestMethod.GET)
	public String yibanManage() {
		return "yibanManage" ;
	}

	@RequestMapping(value="/lishiManage",method=RequestMethod.GET)
	public String lisiManage() {
		return "lishiManage" ;
	}

	@RequestMapping(value="/audit_bz",method=RequestMethod.GET)
	public ModelAndView audit_bz(
			String taskId ,
			ModelAndView mv
	) {
		mv.setViewName("audit_bz");
		mv.addObject("taskId",taskId) ;
		return mv ;
	}
	@RequestMapping(value="/audit_ld",method=RequestMethod.GET)
	public ModelAndView audit_ld(
			String taskId ,
			ModelAndView mv
	) {
		mv.setViewName("audit_ld");
		mv.addObject("taskId",taskId) ;
		return mv ;
	}

}
