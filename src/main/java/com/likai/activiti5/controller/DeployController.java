package com.likai.activiti5.controller;

import com.github.pagehelper.PageInfo;
import com.likai.activiti5.utils.AnswerJson;
import com.likai.activiti5.utils.PageBean;
import com.likai.activiti5.utils.PageList;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping(value="/deploy")
public class DeployController {
	@Autowired
	private RepositoryService repositoryService ;
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			String s_name,
			Integer page,
			Integer rows
			) {
		if(s_name == null) {
			s_name = "%%" ;
		} else {
			s_name = "%" + s_name + "%" ;
		}
		PageBean pb = new PageBean(page, rows) ;
		List<Deployment> list = repositoryService.createDeploymentQuery()	//创建流程部署查询
			.deploymentNameLike(s_name)
			.orderByDeploymenTime().desc()	//根据部署时间降序
			.listPage(pb.getStart(), pb.getPageSize()) ; 	//返回带分页的数据集合
		
		//转换
		List<com.likai.activiti5.pojo.Deployment> deploymentList = new ArrayList<com.likai.activiti5.pojo.Deployment>() ;
		for (Deployment deployment : list) {
			com.likai.activiti5.pojo.Deployment deploymentEntity = new com.likai.activiti5.pojo.Deployment() ;
			deploymentEntity.setId(deployment.getId());
			deploymentEntity.setName(deployment.getName());
			deploymentEntity.setDeploymentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(deployment.getDeploymentTime()));
			deploymentList.add(deploymentEntity) ;
		}
		PageInfo pageInfo = new PageInfo(deploymentList,5) ;
		PageList pageList = new PageList(repositoryService.createDeploymentQuery().deploymentNameLike(s_name).count(),pageInfo.getList()) ;
		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}
	
	
	@RequestMapping(value="/deploy",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> deploy(
			MultipartFile deployFile
			) {
		AnswerJson json = new AnswerJson() ;
		Deployment deployment = null ;
		try {
			deployment = repositoryService.createDeployment()	//创建部署
					.name(deployFile.getOriginalFilename()) 	//流程名称
					.addZipInputStream(new ZipInputStream(deployFile.getInputStream())) 	//添加zip输入流
					.deploy() ;	//部署
			json.setData("部署成功");
		} catch (IOException e) {
			json.setData("部署失败");
			json.setError(e.toString());
			e.printStackTrace();
		}	
		
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	public ResponseEntity<AnswerJson> remove(
			@PathVariable(value="ids") String[] ids
			) {
		AnswerJson json = new AnswerJson() ;
		
		for (String id : ids) {
			repositoryService.deleteDeployment(id, true);	//删除部署
		}
		
		json.setData("删除成功");
		
		return new ResponseEntity<AnswerJson>(json, HttpStatus.OK) ;
	}
}
