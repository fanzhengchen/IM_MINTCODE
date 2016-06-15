package com.mintcode.launchr.pojo.entity;


import java.io.Serializable;
import java.util.List;

/**
 * @author Kevinqiao
 */
public class MessageInfoEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List atUsers;

	private String sessionName;

	private String userName;

	private String nickName;


	public List getAtUsers() {
		return atUsers;
	}

	public void setAtUsers(List atUsers) {
		this.atUsers = atUsers;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
