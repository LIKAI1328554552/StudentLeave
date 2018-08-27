package com.likai.activiti5.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.likai.activiti5.pojo.Group;
import com.likai.activiti5.pojo.Leave;
import com.likai.activiti5.pojo.User;
import com.likai.activiti5.service.ILeaveService;
import com.likai.activiti5.utils.AnswerJson;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.likai.activiti5.pojo.MemberShip;
import com.likai.activiti5.utils.PageBean;
import com.likai.activiti5.utils.PageList;

@Controller
@RequestMapping(value="/task")
public class TaskController {
	
	@Autowired
	private TaskService taskService ;
	
	@Autowired
	private RepositoryService repositoryService ;
	
	@Autowired
	private RuntimeService runtimeService ;

	@Autowired
	private FormService formService ;

	@Autowired
	private ILeaveService leaveService ;

	@Autowired
	private HistoryService historyService ;

	/**
	 * 待办
	 * @param s_name
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			String s_name,
			Integer page,
			Integer rows,
			HttpServletRequest request
			) {
		
		if(s_name == null) {
			s_name = "%%" ;
		} else {
			s_name = "%" + s_name + "%" ;
		}
		
		PageBean pb = new PageBean(page,rows) ;
		MemberShip ms = (MemberShip) request.getSession().getAttribute("currentMemberShip") ;
		
		List<Task> list = taskService.createTaskQuery()	//创建任务查询
			.taskCandidateUser(ms.getUser().getId())	//根据用户Id
			.taskNameLike(s_name)
			.listPage(pb.getStart(), pb.getPageSize()) ;	//分页
		List<com.likai.activiti5.pojo.Task> taskList = new ArrayList<com.likai.activiti5.pojo.Task>() ;
		for (Task task : list) {
			com.likai.activiti5.pojo.Task t = new com.likai.activiti5.pojo.Task() ;
			t.setId(task.getId());
			t.setName(task.getName());
			t.setCreateTime(task.getCreateTime());
			taskList.add(t) ;
		}
		
		PageInfo pageInfo = new PageInfo(taskList,5) ;
		PageList pageList = new PageList( taskService.createTaskQuery().taskCandidateUser(ms.getUser().getId()).count(),pageInfo.getList()) ;
	
		return new ResponseEntity<PageList>(pageList, HttpStatus.OK) ;
	}

	/**
	 * 已办
	 * @param s_name
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/unFinishedlist",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageList> unFinishedlist(
			String s_name,
			Integer page,
			Integer rows,
			HttpServletRequest request
	) {
		if(s_name == null) {
			s_name = "%%" ;
		} else {
			s_name = "%" + s_name + "%" ;
		}

		PageBean pageBean = new PageBean(page,rows) ;
		//取得用户信息
		MemberShip memberShip =  (MemberShip) request.getSession().getAttribute("currentMemberShip") ;

		List<HistoricTaskInstance> hisList =  historyService.createHistoricTaskInstanceQuery()	//创建历史任务实例查询
			.taskCandidateGroup(memberShip.getGroup().getId())
			.taskNameLike(s_name)
			.list() ;
		List<com.likai.activiti5.pojo.Task> taskList = new ArrayList<com.likai.activiti5.pojo.Task>() ;

		for (HistoricTaskInstance hti : hisList) {
			/**
			 * 确定为已办任务
			 * 1.此流程实例还在运行中
			 * 2.根据当前用户的查询出的List长度为0(即不是此用户的待办任务)
			 */
			Task task = taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult();
			List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(memberShip.getUser().getId()).taskId(hti.getId()).list();
			if (task != null && tasks.size() == 0) {
				com.likai.activiti5.pojo.Task myTask = new com.likai.activiti5.pojo.Task() ;
				myTask.setId(hti.getId());
				myTask.setName(hti.getName());
				myTask.setCreateTime(hti.getCreateTime());
				myTask.setEndTime(hti.getEndTime());
				taskList.add(myTask) ;
			}

		}

		//分页处理
		if(taskList.size()>(pageBean.getStart()+pageBean.getPageSize())) {
			taskList = taskList.subList(pageBean.getStart(),pageBean.getStart()+pageBean.getPageSize()) ;
		}


		PageInfo pageInfo = new PageInfo(taskList,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;

		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}

	/**
	 * 历史任务
	 * @param s_name
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/finishedlist",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageList> finishedlist(
			String s_name,
			Integer page,
			Integer rows,
			HttpServletRequest request
	) {
		if(s_name == null) {
			s_name = "%%" ;
		} else {
			s_name = "%" + s_name + "%" ;
		}

		PageBean pageBean = new PageBean(page,rows) ;
		//取得用户信息
		MemberShip memberShip =  (MemberShip) request.getSession().getAttribute("currentMemberShip") ;

		List<HistoricTaskInstance> hisList =  historyService.createHistoricTaskInstanceQuery()	//创建历史任务实例查询
			.taskCandidateGroup(memberShip.getGroup().getId())
			.taskNameLike(s_name)
			.list() ;
		List<com.likai.activiti5.pojo.Task> taskList = new ArrayList<com.likai.activiti5.pojo.Task>() ;

		for (HistoricTaskInstance hti : hisList) {
			/**
			 * 确定为历史任务
			 * 1.此流程实例已经结束
			 */
			Task task = taskService.createTaskQuery().processInstanceId(hti.getProcessInstanceId()).singleResult();
			if (task == null) {
				com.likai.activiti5.pojo.Task myTask = new com.likai.activiti5.pojo.Task() ;
				myTask.setId(hti.getId());
				myTask.setName(hti.getName());
				myTask.setCreateTime(hti.getCreateTime());
				myTask.setEndTime(hti.getEndTime());
				taskList.add(myTask) ;
			}

		}

		//分页处理
		if(taskList.size()>(pageBean.getStart()+pageBean.getPageSize())) {
			taskList = taskList.subList(pageBean.getStart(),pageBean.getStart()+pageBean.getPageSize()) ;
		}


		PageInfo pageInfo = new PageInfo(taskList,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;

		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}


	/**
	 * 查看流程图
	 * @param taskId
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/showView",method=RequestMethod.GET)
	public ModelAndView showView(
			String taskId,
			HttpServletResponse response
			) {
		
		ModelAndView mv = new ModelAndView() ;
		//根据任务Id查询部署Id与图片资源相关名称
		//	1.根据任务ID查询任务实例
		//	2.根据任务实例中的流程定义ID查询流程定义实体进而获得部署相关的信息
		
		Task task = taskService.createTaskQuery()	//创建任务查询
			.taskId(taskId)	//根据任务ID
			.singleResult() ;
		
		String processDefinitionId = task.getProcessDefinitionId() ;	//获取流程定义ID
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()	//创建流程定义查询
			.processDefinitionId(processDefinitionId)	//根据流程定义Id
			.singleResult() ;
		
		
		//得到活动实体	
		//	1，获取流程定义实体
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId) ;
		//	2.获取流程实例ID
		String processInstanceId = task.getProcessInstanceId() ;
		//	3.获取流程实例
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()	//创建流程实例查询
			.processInstanceId(processInstanceId)	//通过流程实例Id查询
			.singleResult() ;
		//获取活动实例
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId()) ;
		
		mv.addObject("x", activityImpl.getX()) ; 	//x坐标
		mv.addObject("y", activityImpl.getY()) ; 	//y坐标
		mv.addObject("width", activityImpl.getWidth()) ; 	//宽度
		mv.addObject("height", activityImpl.getHeight()) ; 	//高度
		
		
		//部署ID
		mv.addObject("deploymentId", processDefinition.getDeploymentId()) ;
		
		mv.addObject("diagramResourceName", processDefinition.getDiagramResourceName()) ;
		
		mv.setViewName("currentView");
		
		return mv ;
	}

	/**
	 * 查看流程图(已办)
	 * @param taskId
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/showHisView",method=RequestMethod.GET)
	public ModelAndView showHisView(
			String taskId,
			HttpServletResponse response
			) {

		ModelAndView mv = new ModelAndView() ;
		//根据任务Id查询部署Id与图片资源相关名称
		//	1.根据任务ID查询任务实例
		//	2.根据任务实例中的流程定义ID查询流程定义实体进而获得部署相关的信息


		HistoricTaskInstance hit = historyService.createHistoricTaskInstanceQuery()    //创建历史任务查询
				.taskId(taskId)
				.singleResult();

		String processDefinitionId = hit.getProcessDefinitionId() ;	//获取流程定义ID

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()	//创建流程定义查询
			.processDefinitionId(processDefinitionId)	//根据流程定义Id
			.singleResult() ;


		//得到活动实体
		//	1，获取流程定义实体
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId) ;
		//	2.获取流程实例ID
		String processInstanceId = hit.getProcessInstanceId() ;
		//	3.获取流程实例
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()	//创建流程实例查询
			.processInstanceId(processInstanceId)	//通过流程实例Id查询
			.singleResult() ;
		//获取活动实例
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId()) ;

		mv.addObject("x", activityImpl.getX()) ; 	//x坐标
		mv.addObject("y", activityImpl.getY()) ; 	//y坐标
		mv.addObject("width", activityImpl.getWidth()) ; 	//宽度
		mv.addObject("height", activityImpl.getHeight()) ; 	//高度


		//部署ID
		mv.addObject("deploymentId", processDefinition.getDeploymentId()) ;

		mv.addObject("diagramResourceName", processDefinition.getDiagramResourceName()) ;

		mv.setViewName("currentView");

		return mv ;
	}

	/**
	 * 重定向到审核界面(需要取得formKey)
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value="/redirectPage",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<AnswerJson> redirectPage(
			String taskId
		) {
		AnswerJson json = new AnswerJson() ;
		TaskFormData formDate = formService.getTaskFormData(taskId) ;
		String page = formDate.getFormKey() ;
		json.setData(page.split("[.]")[0]);
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}

	/**
	 * 班长办理
	 * @param taskId 任务Id
	 * @param leaveDays 请假天数(决定流程走向)
	 * @param comment 班长批注
	 * @param state 1 同意 2驳回
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/audit_bz",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AnswerJson> audit_bz(
			String taskId,
			Integer leaveDays,
			String comment,
			String state,
			HttpSession session

	) {
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("days",leaveDays) ;
		if("1".equals(state)) {
			param.put("msg","通过") ;
		} else {
			//结束流程
			//	1.根据任务Id查询leaveId
			String leaveId = (String) taskService.getVariable(taskId,"leaveId") ;
			//	2.取得请假信息
			Leave leave = leaveService.findById(leaveId) ;
			leave.setState("7");
			leaveService.update(leave) ;
			param.put("msg","未通过") ;
		}
		AnswerJson json = new AnswerJson() ;
		//根据Id查询流程实例Id
		Task task = taskService.createTaskQuery()	//创建任务查询
			.taskId(taskId)	//根据任务Id
			.singleResult()	;

		//获取当前用户信息
		MemberShip memberShip = (MemberShip)session.getAttribute("currentMemberShip") ;
		User user = memberShip.getUser() ;
		Group group = memberShip.getGroup() ;
		StringBuffer sb = new StringBuffer(100) ;
		sb.append(user.getFirstName()) ;
		sb.append(user.getLastName()) ;
		sb.append("[") ;
		sb.append(group.getName()) ;
 		sb.append("]") ;
		//设置批注得用户信息
		Authentication.setAuthenticatedUserId(sb.toString());

		// 任务Id 流程实例Id 批注
		taskService.addComment(taskId,task.getProcessInstanceId(),comment) ;

		//完成任务
		taskService.complete(taskId,param);

		json.setData("办理成功");

		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	/**
	 * 领导办理
	 * @param taskId 任务Id
	 * @param comment 领导批注
	 * @param state 1 同意 2驳回
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/audit_ld",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AnswerJson> audit_ld(
			String taskId,
			String comment,
			String state,
			HttpSession session

	) {
		AnswerJson json = new AnswerJson() ;
		//根据Id查询流程实例Id
		Task task = taskService.createTaskQuery()	//创建任务查询
			.taskId(taskId)	//根据任务Id
			.singleResult()	;

		String leaveId = (String) taskService.getVariable(taskId,"leaveId") ;
		Leave leave = leaveService.findById(leaveId) ;

		if("1".equals(state)) {
			leave.setState("9");
			leaveService.update(leave) ;
		} else {
			leave.setState("7");
			leaveService.update(leave) ;
		}

		//获取当前用户信息
		MemberShip memberShip = (MemberShip)session.getAttribute("currentMemberShip") ;
		User user = memberShip.getUser() ;
		Group group = memberShip.getGroup() ;
		StringBuffer sb = new StringBuffer(100) ;
		sb.append(user.getFirstName()) ;
		sb.append(user.getLastName()) ;
		sb.append("[") ;
		sb.append(group.getName()) ;
 		sb.append("]") ;
		//设置批注得用户信息
		Authentication.setAuthenticatedUserId(sb.toString());

		// 任务Id 流程实例Id 批注
		taskService.addComment(taskId,task.getProcessInstanceId(),comment) ;

		//完成任务
		taskService.complete(taskId);

		json.setData("办理成功");

		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}

	/**
	 * 查询历史批注
	 * @param taskId 任务id
	 * @return
	 */
	@RequestMapping(value="/listHistoryComment",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageList> listHistoryComment(
			String taskId
	) {
		//查询历史任务实例
		HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery()	//创建历史信息查询
			.taskId(taskId)	//任务Id
			.singleResult() ;
		//根据流程实例Id查询批注信息
		List<Comment> comments = taskService.getProcessInstanceComments(hti.getProcessInstanceId()) ;

		// 集合元素反转
		Collections.reverse(comments);
		PageInfo pageInfo = new PageInfo(comments,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;

		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}


	/**
	 * 查询历史批注
	 * @param processInstanceId 流程实例Id
	 * @return
	 */
	@RequestMapping(value="/listHistoryCommentByProcessInstanceId",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageList> listHistoryCommentByProcessInstanceId(
			String processInstanceId
	) {

		//根据流程实例Id查询批注信息
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId) ;

		// 集合元素反转
		Collections.reverse(comments);
		PageInfo pageInfo = new PageInfo(comments,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;

		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}

	/**
	 * 流程执行过程
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value="/processActinst",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PageList> processActinst(
			String taskId
	) {

		HistoricTaskInstance hit = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String pi = hit.getProcessInstanceId();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()	//创建历史活动实例查询
				.processInstanceId(pi)	//流程实例Id
				.list();
		PageInfo pageInfo = new PageInfo(list,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;
		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}


}
