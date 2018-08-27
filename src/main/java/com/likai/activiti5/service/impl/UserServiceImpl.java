package com.likai.activiti5.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.likai.activiti5.dao.IMemberShipMapper;
import com.likai.activiti5.dao.IUserMapper;
import com.likai.activiti5.pojo.User;
import com.likai.activiti5.service.IUserService;
import com.likai.activiti5.utils.CryptographyUtil;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserMapper userMapper ;
	
	@Autowired
	private IMemberShipMapper memberShipMapper ;
	
	@Transactional(readOnly=true)
	public User findById(String id) {
		return userMapper.selectById(id) ;
	}

	@Transactional(readOnly=true)
	public List<User> findList(User po) {
		return userMapper.selectList(po) ;
	}

	@Transactional
	public int removeByIds(String[] ids) {
		boolean flag = false ;
		Map<String,String> map = new HashMap<String,String>() ;
		
		for (String id : ids) {
			map.put("userId", id) ;
			memberShipMapper.deleteById(map) ;
			if(userMapper.deleteById(id) == 1) {
				flag = true ;
			} else {
				flag = false ;
				//编程式事务回滚
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break ;
			}
			
		}
		
		
		if(flag) {
			return 1 ;
		} else {
			return 0 ;
		}
	}

	public int updateInfo(User po) {
		if(po.getPassword() != null) {
			po.setPassword(CryptographyUtil.md5(po.getPassword(), po.getId()));
		}
		return userMapper.updateInfo(po) ;
	}

	public int addInfo(User po) {
		po.setPassword(CryptographyUtil.md5(po.getPassword(), po.getId()));
		return userMapper.insertInfo(po) ;
	}

}
