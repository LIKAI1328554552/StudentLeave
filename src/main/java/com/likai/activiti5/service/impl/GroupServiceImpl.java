package com.likai.activiti5.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.likai.activiti5.dao.IGroupMapper;
import com.likai.activiti5.dao.IMemberShipMapper;
import com.likai.activiti5.pojo.Group;
import com.likai.activiti5.service.IGroupService;

@Service("groupService") 
public class GroupServiceImpl implements IGroupService {
	
	@Autowired
	private IGroupMapper groupMapper ;
	
	@Autowired
	private IMemberShipMapper memberShipMapper ;

	@Transactional(readOnly=true)
	public List<Group> findList(Group po) {
		return groupMapper.selectList(po) ;
	}

	@Transactional
	public int addInfo(Group po) {
		return groupMapper.insertInfo(po) ;
	}
	
	
	@Transactional
	public int updateInfo(Group po) {
		return groupMapper.updateInfo(po) ;
	}

	@Transactional(readOnly=true)
	public Group findById(String id) {
		return groupMapper.selectById(id) ;
	}
	
	@Transactional
	public int removeByIds(String[] ids) {
		boolean flag = false ;
		Map<String,String> map = new HashMap<String,String>() ;
		for (String id : ids) {
			map.put("groupId", id) ;
			memberShipMapper.deleteById(map) ;
			if(groupMapper.deleteById(id) == 1) {
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
	
	@Transactional(readOnly=true)
	public List<Group> findByUserId(String id) {
		return groupMapper.selectByUserId(id) ;
	}

	
}
