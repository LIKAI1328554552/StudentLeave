package com.likai.activiti5.service;

import java.util.List;

import com.likai.activiti5.pojo.Leave;

public interface ILeaveService {

	public List<Leave> findList(Leave po) ;
	
	public int update(Leave po) ;
	
	public Leave findById(String id) ;
}
