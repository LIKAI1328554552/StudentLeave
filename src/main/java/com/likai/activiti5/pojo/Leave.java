package com.likai.activiti5.pojo;

import java.util.Date;

public class Leave {

	private String id ;
	private String userId ;
	private Date leaveDate ;
	private Integer leaveDays ;
	private String leaveReason ;
	private String state ;
	private String processInstanceId ;
	private String userName ;	//用户姓名
	private String taskId ; //任务Id
	
	public Leave() {
		super() ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Integer getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(Integer leaveDays) {
		this.leaveDays = leaveDays;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
