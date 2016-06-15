package com.mintcode.chat.entity;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626424159674281200L;

	private String nickName;

	private String uid;

	private String avatar;
	
	public User(String nickname,String uid) {
		this.nickName = nickname;
		this.uid = uid;
	}

	public User() {
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
