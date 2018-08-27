package com.likai.activiti5.dao;

import java.util.Map;

import com.likai.activiti5.pojo.MemberShip;

public interface IMemberShipMapper {

	public MemberShip selectByUserNameAndPWDAndGroupId(Map<String,String> map) ;
	
	/**
	 * 根据userId/groupId删除关联信息
	 * @param po
	 * @return
	 */
	public int deleteById(Map<String,String> map) ;
	
	public int insertInfo(Map<String,String> map) ;
 }
