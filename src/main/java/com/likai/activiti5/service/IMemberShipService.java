package com.likai.activiti5.service;

import java.util.Map;

import com.likai.activiti5.pojo.MemberShip;

public interface IMemberShipService {

	public MemberShip login(Map<String,String> map) ;
	
	public int addMemberShip(String userId,String [] groupIds) ;
}
