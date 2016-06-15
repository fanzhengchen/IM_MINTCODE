package com.mintcode.chat.entity;

import java.io.Serializable;

public class Info implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5725445924509626146L;

	private String nickName;
	
	private String avatar;
	
	private String userName;

	private String sessionName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
