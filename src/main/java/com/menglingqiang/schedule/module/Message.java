package com.menglingqiang.schedule.module;

import org.assertj.core.internal.cglib.proxy.Enhancer;

public class Message {

	private String projectName;
	private String detailProjectName;
	private String email;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDetailProjectName() {
		return detailProjectName;
	}
	public void setDetailProjectName(String detailProjectName) {
		this.detailProjectName = detailProjectName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Message [projectName=" + projectName + ", detailProjectName="
				+ detailProjectName + ", email=" + email + "]";
	}
	
}
