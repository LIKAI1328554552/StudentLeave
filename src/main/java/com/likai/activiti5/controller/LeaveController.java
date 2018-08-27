package com.likai.activiti5.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.likai.activiti5.pojo.User;
import com.likai.activiti5.service.IUserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.likai.activiti5.pojo.Leave;
import com.likai.activiti5.pojo.MemberShip;
import com.likai.activiti5.service.ILeaveService;
import com.likai.activiti5.utils.AnswerJson;
import com.likai.activiti5.utils.PageList;

@Controller
@RequestMapping(value="/leave")
public class LeaveController {

	
	@Autowired
	@Qualifier("leaveService")
	private ILeaveService leaveService ;
	
	@Autowired
	private RuntimeService runtimeService ;
	
	@Autowired
	private TaskService taskService ;

	@Autowired
	private IUserService userService ;
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			Leave po,
			Integer page,
			Integer rows,
			HttpServletRequest request
			) {
		MemberShip ms = (MemberShip) request.getSession().getAttribute("currentMemberShip") ;
		po.setUserId(ms.getUser().getId());
		PageHelper.startPage(page, rows) ;
		List<Leave> list = leaveService.findList(po) ;
		
		PageInfo pageInfo = new PageInfo(list,5) ;
		
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;
		
		return new ResponseEntity<PageList>(pageList, HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> update(
			Leave po,
			HttpServletRequest request
			) {
		AnswerJson json = new AnswerJson() ;
		MemberShip ms = (MemberShip) request.getSession().getAttribute("currentMemberShip") ;
		if(ms != null) {
			po.setUserId(ms.getUser().getId());
		}
		po.setLeaveDate(new Date());
		
		int result = leaveService.update(po) ;
		if(result == 1) {
			json.setData("添加成功");
		} else {
			json.setData("添加失败");
		}
		
		return new ResponseEntity<AnswerJson>(json, HttpStatus.OK) ;
	}
	
	/**
	 * 发起请假申请
	 * @param leaveId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/startApply",method=RequestMethod.GET)
	public ResponseEntity<AnswerJson> startApply(
			String leaveId
			) {
		AnswerJson json = new AnswerJson() ;
		//设置流程变量
		Map<String,Object> variables = new HashMap<String,Object>() ;
		variables.put("leaveId", leaveId) ;
		//启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("studentLeaveProcess",variables) ;
		//根据流程实例Id查询任务
		Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult() ;
		//完成学生填写请假单任务
		taskService.complete(task.getId());
 		//更新请假表
		//	根据Id查询请假信息
		Leave leave = leaveService.findById(leaveId) ;
		//	根据Id更新请假信息
		leave.setProcessInstanceId(pi.getProcessInstanceId());
		leave.setState("1");
		int result = leaveService.update(leave) ;
		if(result == 1) {
			json.setData("提交成功");
		} else {
			json.setData("提交失败");
		}
		return new ResponseEntity<AnswerJson>(json, HttpStatus.OK) ;
	}

	/**
	 * 根据任务Id获取请假信息(leaveId在流程变量中)
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value="/getLeaveByTaskId",method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<AnswerJson> getLeaveByTaskId(
			String taskId
	) {
		AnswerJson json = new AnswerJson() ;
		String leaveId = (String) taskService.getVariable(taskId,"leaveId") ;

		Leave leave = leaveService.findById(leaveId) ;
		User user = userService.findById(leave.getUserId()) ;
		leave.setUserName(user.getFirstName() + user.getLastName());
		leave.setTaskId(taskId);
		json.setData(leave);

		return new ResponseEntity<AnswerJson>(json, HttpStatus.OK) ;
	}
}
