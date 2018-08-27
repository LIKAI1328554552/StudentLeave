package com.likai.activiti5.utils;

/**
 * 添加、删除、更新 返回的json对象
 * @author likai
 * @since 2018-03-19
 */
public class AnswerJson {

	private Object data ;
	
	private String error ;
	
	private String warn ;
	
	public AnswerJson() {
		super() ;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}
	
}
