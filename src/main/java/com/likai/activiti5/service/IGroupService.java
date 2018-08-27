package com.likai.activiti5.service;

import java.util.List;

import com.likai.activiti5.pojo.Group;

public interface IGroupService {

	public List<Group> findList(Group po) ;
	
	public int addInfo(Group po) ;
	
	public int updateInfo(Group po) ;
	
	public int removeByIds(String [] ids) ;
	
	public Group findById(String id) ;
	
	public List<Group> findByUserId(String id) ;
}
