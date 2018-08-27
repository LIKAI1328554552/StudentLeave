package com.likai.activiti5.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.likai.activiti5.pojo.Group;
import com.likai.activiti5.pojo.MemberShip;
import com.likai.activiti5.pojo.User;
import com.likai.activiti5.service.IGroupService;
import com.likai.activiti5.service.IMemberShipService;
import com.likai.activiti5.service.IUserService;
import com.likai.activiti5.utils.AnswerJson;
import com.likai.activiti5.utils.CryptographyUtil;
import com.likai.activiti5.utils.PageList;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService ;
	
	@Autowired
	@Qualifier("memberShipService")
	private IMemberShipService memberShipService ;
	
	@Autowired
	@Qualifier("groupService")
	private IGroupService groupService ;
	
	@ResponseBody
	@RequestMapping(value="login",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> login(
			String userName,
			String password,
			String groupId,
			HttpServletRequest request
			) {
		AnswerJson json = new AnswerJson() ;
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("userName", userName) ;
		map.put("password", CryptographyUtil.md5(password, userName)) ;
		map.put("groupId", groupId) ;
		MemberShip memberShip = memberShipService.login(map) ;
		
		if(memberShip != null) {
			json.setData("登录成功!");
			request.getSession().setAttribute("currentMemberShip", memberShip);
		} else {
			json.setData("用户名密码错误啦!");
			json.setError("0");
		}
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<PageList> list(
			Integer page,
			Integer rows,
			User po
			) {
		PageHelper.startPage(page, rows) ;
		List<User> userList = userService.findList(po) ;
		
		PageInfo pageInfo = new PageInfo(userList,5) ;
		
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;
		
		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/uagList",method=RequestMethod.GET)
	public ResponseEntity<PageList> uagList(
			Integer page,
			Integer rows,
			User po
			) {
		PageHelper.startPage(page, rows) ;
		List<User> userList = userService.findList(po) ;
		StringBuffer sb = new StringBuffer() ;
		for (User user : userList) {
			List<Group> groups = groupService.findByUserId(user.getId()) ;
			for (Group group : groups) {
				sb.append(group.getName()) ;
				sb.append(" ") ;
			}
			user.setGroups(sb.toString());
			sb.setLength(0);
		}
		
		PageInfo pageInfo = new PageInfo(userList,5) ;
		
		PageList pageList = new PageList(pageInfo.getTotal(),pageInfo.getList()) ;
		
		return new ResponseEntity<PageList>(pageList,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	public ResponseEntity<AnswerJson> remove(
			@PathVariable("ids") String [] ids
			) {
		AnswerJson json = new AnswerJson(); 
		
		int result = userService.removeByIds(ids) ;
		
		if(result == 1) {
			json.setData("删除成功");
		} else {
			json.setData("删除失败");
		}
		
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	/**
	 * 
	 * @param po
	 * @param type 1为添加 2为更新
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> update(
			User po,
			String type
			) {
		AnswerJson json = new AnswerJson() ;
		int result = 0 ;
		if("1".equals(type)) {
			result = userService.addInfo(po) ;
			if(result == 1) {
				json.setData("添加成功");
			} else {
				json.setData("添加失败");
			}
		} else {
			result = userService.updateInfo(po) ;
			if(result == 1) {
				json.setData("修改成功");
			} else {
				json.setData("修改失败");
			}
		}
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@ResponseBody
	@RequestMapping(value="/vaild/{id}",method=RequestMethod.GET)
	public ResponseEntity<AnswerJson> vaild(
			@PathVariable(value="id") String id
			) {
		AnswerJson json = new AnswerJson() ;
		
		User user = userService.findById(id) ;
		
		if(user != null) {
			json.setData("false");
		} else {
			json.setData("true");
		}
		
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(
			HttpServletRequest request
			) {
		request.getSession().removeAttribute("currentMemberShip");
		
		return "redirect:/login.jsp" ;
	}


	@RequestMapping(value="/updatePassWord",method=RequestMethod.POST)
	public ResponseEntity<AnswerJson> updatePassWord(
			String oldPassWord,
			String newPassWord,
			HttpServletRequest request
	) {
		MemberShip memberShip = (MemberShip)request.getSession().getAttribute("currentMemberShip");
		User user = memberShip.getUser() ;
		AnswerJson json = new AnswerJson();
		String oldPassWord_MD5 = CryptographyUtil.md5(oldPassWord,user.getId()) ;
		if (user.getPassword().equals(oldPassWord_MD5)) {
			user.setPassword(newPassWord);
			int result = userService.updateInfo(user) ;
			if (result == 1) {
				json.setData("更改成功");
			} else {
				json.setData("更改失败");
			}
		} else {
			json.setData("原始密码错误");
		}
		return new ResponseEntity<AnswerJson>(json,HttpStatus.OK) ;
	}
	
}
