package com.likai.activiti5.dao;

import java.util.List;

import com.likai.activiti5.pojo.Group;

public interface IGroupMapper {

	public List<Group> selectList(Group po) ;
	
	public int insertInfo(Group po) ;
	
	public int updateInfo(Group po) ;
	
	public int deleteById(String id) ;
	
	public Group selectById(String id) ;
	
	public List<Group> selectByUserId(String id) ;
}
