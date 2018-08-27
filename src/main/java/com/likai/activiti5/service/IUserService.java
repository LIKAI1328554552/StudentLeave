package com.likai.activiti5.service;

import java.util.List;

import com.likai.activiti5.pojo.User;

public interface IUserService {

	/**
	 * 根据id查询用户信息
	 * @param id
	 * @return
	 */
	public User findById(String id) ;
	
	public List<User> findList(User po) ;
	
	public int removeByIds(String [] ids) ;
	
	public int updateInfo(User po) ;
	
	public int addInfo(User po) ;
	
}
