package com.mintcode.chat.user;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

public class GroupInfo extends DataSupport implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1684738086339465825L;
	private String userName;
	private String nickName;
	private String type;
	private String avatar;
	private long modified;
	/**
	 * 1为免打扰状态  2为消息接受状态 
	 * litepal貌似有bug ，不能将int 更新为0
	 */
	private int isDND;
	private List<GroupInfo> memberList;
	private String memberJson;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getModified() {
		return modified;
	}
	public void setModified(long modified) {
		this.modified = modified;
	}
	public List<GroupInfo> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<GroupInfo> memberList) {
		this.memberList = memberList;
	}
	public int getIsDND() {
		return isDND;
	}
	public void setIsDND(int isDND) {
		this.isDND = isDND;
	}
	public String getMemberJson() {
		return memberJson;
	}
	public void setMemberJson(String memberJson) {
		this.memberJson = memberJson;
	}
	
}
