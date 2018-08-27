package com.likai.activiti5.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.likai.activiti5.pojo.MemberShip;

public class LoginInterceptor implements HandlerInterceptor{
	
	private static final String [] IGNORE_URL = {"/login.jsp","/login","/combobox"} ;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean flag = false ;
		//请求路径
		String requestPath = request.getServletPath() ;
		for (String path : IGNORE_URL) {
			if(requestPath.contains(path)) {
				flag = true ;
				break ;
			}
		}
		if(!flag) {
			MemberShip memberShip = (MemberShip) request.getSession().getAttribute("currentMemberShip") ;
			if(memberShip == null) {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				return flag ;
			} else {
				flag = true ;
			}
		}
		return flag;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
