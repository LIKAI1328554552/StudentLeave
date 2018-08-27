package com.likai.activiti5.pojo;

public class Deployment {

	private String id ;
	private String name ;
	private String deploymentTime ;
	
	public Deployment() {
		super() ;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeploymentTime() {
		return deploymentTime;
	}
	public void setDeploymentTime(String deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	
}
