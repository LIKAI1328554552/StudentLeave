package com.likai.activiti5.dao;

import java.util.List;

import com.likai.activiti5.pojo.Leave;

public interface ILeaveMapper {

	public List<Leave> selectList(Leave po) ;
	
	public int insertInfo(Leave po) ;
	
	public Leave selectById(String id) ;
	
	public int updateInfo(Leave po) ;
}
