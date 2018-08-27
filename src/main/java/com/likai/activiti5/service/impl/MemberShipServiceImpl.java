package com.likai.activiti5.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likai.activiti5.dao.IMemberShipMapper;
import com.likai.activiti5.pojo.MemberShip;
import com.likai.activiti5.service.IMemberShipService;

@Service("memberShipService")
public class MemberShipServiceImpl implements IMemberShipService {
	
	@Autowired
	private IMemberShipMapper memberShipMapper ;
	
	@Transactional(readOnly=true)
	public MemberShip login(Map<String,String> map) {
		return memberShipMapper.selectByUserNameAndPWDAndGroupId(map) ;
	}

	public int addMemberShip(String userId, String[] groupIds) {
		boolean flag = false ;
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("userId", userId) ;
		//先将之前的数据删除，再重新添加
		memberShipMapper.deleteById(map) ;
		for (String groupId : groupIds) {
			if(!"".equals(groupId)) {
				map.put("groupId", groupId) ;
				if(memberShipMapper.insertInfo(map) == 1) {
					flag = true ;
				} else {
					flag = false ;
				}
			}
		}
		if(flag) {
			return 1;
		} else {
			return 0 ;
		}
	}

}
