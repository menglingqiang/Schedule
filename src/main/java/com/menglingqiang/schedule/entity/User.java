package com.menglingqiang.schedule.entity;

import java.util.Date;

public class User {

	private long userId;
	private String password;
	private String name;
	private String email;
	private String code;
	private String userPic;
	private String loginType;
	private String tempToken;
	private String threeUserId;
	private boolean alertStatus;

	public boolean getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(boolean alertStatus) {
		this.alertStatus = alertStatus;
	}
	
	public String getThreeUserId() {
		return threeUserId;
	}

	public void setThreeUserId(String threeUserId) {
		this.threeUserId = threeUserId;
	}

	public String getTempToken() {
		return tempToken;
	}

	public void setTempToken(String tempToken) {
		this.tempToken = tempToken;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	private Date createTime;
	private int status;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}


	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", code='" + code + '\'' +
				", userPic='" + userPic + '\'' +
				", loginType='" + loginType + '\'' +
				", tempToken='" + tempToken + '\'' +
				", threeUserId='" + threeUserId + '\'' +
				", alertStatus=" + alertStatus +
				", createTime=" + createTime +
				", status=" + status +
				'}';
	}
	//
}