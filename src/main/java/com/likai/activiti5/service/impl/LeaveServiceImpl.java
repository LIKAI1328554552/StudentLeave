package com.likai.activiti5.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likai.activiti5.dao.ILeaveMapper;
import com.likai.activiti5.pojo.Leave;
import com.likai.activiti5.service.ILeaveService;
import com.likai.activiti5.utils.IdUtil;

@Service("leaveService")
public class LeaveServiceImpl implements ILeaveService {
	
	@Autowired
	private ILeaveMapper leaveMapper ;

	@Transactional(readOnly=true)
	public List<Leave> findList(Leave po) {
		return leaveMapper.selectList(po) ;
	}

	@Transactional
	public int update(Leave po) {
		String id = po.getId() ;
		if("".equals(id) || id == null) {
			po.setId(IdUtil.uuid());
			po.setState("0");
			return leaveMapper.insertInfo(po) ;
		} else {
			return leaveMapper.updateInfo(po) ;
		}
	}

	@Transactional(readOnly=true)
	public Leave findById(String id) {
		return leaveMapper.selectById(id) ;
	}

}
