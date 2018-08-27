package com.likai.activiti5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.likai.activiti5.pojo.Group;
import com.likai.activiti5.pojo.User;
import com.likai.activiti5.service.IGroupService;
import com.likai.activiti5.utils.AnswerJson;
import com.likai.activiti5.utils.PageList;

@Controller
@RequestMapping(value="/group")
public class GroupController {

	@Autowired
	@Qualifier("groupService")
	private IGroupService groupService ;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			Group po,
			Integer page,
			Integer rows
			) {
		List<Group> list = null ;
		if(page != null) {
			PageHelper.startPage(page, rows) ;
			list = groupService.findList(po) ;
		} else {
			list = groupService.findList(po) ;
		}
		PageInfo pageInfo = new PageInfo(list,5) ;
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;
		
		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}
	
	@RequestMapping(value="/combobox",method=RequestMethod.GET)
	public ResponseEntity<List<Group>> combobox() {
		List<Group> list = groupService.findList(null) ;
		return new ResponseEntity<List<Group>>(list,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	public ResponseEntity<AnswerJson> remove(
			@PathVariable("ids") String [] ids
			) {
		AnswerJson json = new AnswerJson(); 
		
		int result = groupService.removeByIds(ids) ;
		
		if(result == 1) {
			json.setData("删除成功");
		} else {
			json.setData("删除失败");
		}
		
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	/**
	 * 
	 * @param po
	 * @param type 1为添加 2为更新
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> update(
			Group po,
			String type
			) {
		AnswerJson json = new AnswerJson() ;
		int result = 0 ;
		if("1".equals(type)) {
			result = groupService.addInfo(po) ;
			if(result == 1) {
				json.setData("添加成功");
			} else {
				json.setData("添加失败");
			}
		} else {
			result = groupService.updateInfo(po) ;
			if(result == 1) {
				json.setData("修改成功");
			} else {
				json.setData("修改失败");
			}
		}
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/vaild/{id}",method=RequestMethod.GET)
	public ResponseEntity<AnswerJson> vaild(
			@PathVariable(value="id") String id
			) {
		AnswerJson json = new AnswerJson() ;
		
		Group group = groupService.findById(id) ;
		
		if(group != null) {
			json.setData("false");
		} else {
			json.setData("true");
		}
		
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/findByUserId",method=RequestMethod.GET)
	public ResponseEntity<List<Group>> findById(
			String id
			) {
		List<Group> groups = groupService.findByUserId(id) ;
		
		return new ResponseEntity<List<Group>>(groups,HttpStatus.OK) ;
	}
}
