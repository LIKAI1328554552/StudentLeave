package com.likai.activiti5.controller;

import com.github.pagehelper.PageInfo;
import com.likai.activiti5.utils.PageBean;
import com.likai.activiti5.utils.PageList;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/processDefinition")
public class ProcessDefinitionController {
	
	@Autowired
	private RepositoryService repositoryService ;

	@Autowired
	private HistoryService historyService ;
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			String s_name,
			Integer page,
			Integer rows
			) {
		PageBean pb = new PageBean(page, rows) ;
		if(s_name == null) {
			s_name = "%%" ;
		} else {
			s_name = "%" + s_name + "%" ;
		}
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()	//创建流程定义查询
			.orderByProcessDefinitionId().desc()	
			.processDefinitionNameLike(s_name)		//模糊
			.listPage(pb.getStart(), pb.getPageSize()) ;	//分页
		List<com.likai.activiti5.pojo.ProcessDefinition> pdList = new ArrayList<com.likai.activiti5.pojo.ProcessDefinition>() ;
		for (ProcessDefinition pd : list) {
			com.likai.activiti5.pojo.ProcessDefinition processDefinition = new com.likai.activiti5.pojo.ProcessDefinition() ;
			processDefinition.setId(pd.getId());
			processDefinition.setName(pd.getName());
			processDefinition.setKey(pd.getKey());
			processDefinition.setVersion(pd.getVersion());
			processDefinition.setResourceName(pd.getResourceName());
			processDefinition.setDiagramResourceName(pd.getDiagramResourceName());
			processDefinition.setDeploymentId(pd.getDeploymentId());
			pdList.add(processDefinition) ;
		}
		
		PageInfo pageInfo = new PageInfo(pdList,5);
		PageList pageList = new PageList(repositoryService.createProcessDefinitionQuery().processDefinitionNameLike(s_name).count(),pageInfo.getList()) ;
		
		return new ResponseEntity<PageList>(pageList, HttpStatus.OK) ;
	}
	
	/**
	 * 查看流程图
	 * @param deploymentId
	 * @param diagramResourceName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/showView",method=RequestMethod.GET)
	public String showView(
			String deploymentId,
			String diagramResourceName,
			HttpServletResponse response
			) throws Exception {
		InputStream in = repositoryService.getResourceAsStream(deploymentId, diagramResourceName) ;
		OutputStream out = response.getOutputStream() ;
		for(int b = -1;(b=in.read()) != -1 ;) {
			out.write(b);
		}
		out.close();
		in.close();
		return null ;
	}


	/**
	 * 查询流程图
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/showViewByTaskId",method=RequestMethod.GET)
	public String showViewByTaskId(
			String taskId,
			HttpServletResponse response
			) throws Exception {
		//根据任务Id查询历史任务
		HistoricTaskInstance hit = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

		//获取流程定义Id
		String processDefinitionId = hit.getProcessDefinitionId();

		//取得流程定义对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

		InputStream in = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName()) ;


		OutputStream out = response.getOutputStream() ;
		for(int b = -1;(b=in.read()) != -1 ;) {
			out.write(b);
		}
		out.close();
		in.close();
		return null ;
	}

}
