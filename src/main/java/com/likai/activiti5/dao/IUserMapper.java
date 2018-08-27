package com.likai.activiti5.dao;

import java.util.List;

import com.likai.activiti5.pojo.User;

public interface IUserMapper {

	/**
	 * 查询用户列表
	 * @return
	 */
	public List<User> selectList(User po) ;
	
	/**
	 * 根据Id查询用户信息
	 * @param id
	 * @return
	 */
	public User selectById(String id) ;
	
	/**
	 * 添加用户信息
	 * @param po
	 * @return
	 */
	public int insertInfo(User po) ;
	
	/**
	 * 更新用户信息
	 * @param po
	 * @return
	 */
	public int updateInfo(User po) ;
	
	/**
	 * 根据ID删除用户信息
	 * @param id
	 * @return
	 */
	public int deleteById(String id) ;
	
}
