package com.likai.activiti5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.likai.activiti5.service.IMemberShipService;
import com.likai.activiti5.utils.AnswerJson;

@Controller
@RequestMapping(value="/membership")
public class MemberShipController {

	@Autowired
	@Qualifier("memberShipService")
	private IMemberShipService memberShipService ;
	
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> update(
			String userId,
			String groupIds
			) {
		AnswerJson json = new AnswerJson() ;
		String [] groups = groupIds.split(",") ;
		int result = memberShipService.addMemberShip(userId, groups) ;
		if(result == 1) {
			json.setData("添加成功");
		} else {
			json.setData("添加失败");
		}
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
}
