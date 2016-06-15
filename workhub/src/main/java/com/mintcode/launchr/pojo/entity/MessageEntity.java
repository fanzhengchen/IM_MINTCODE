package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageEntity implements Serializable{
	
	

	private String showID;
	
	private String from;
	
	private String from_Name;
	
	private String to;
	
	private String to_Name;
	
	private String title;
	
	private String content;
	
	private int type;
	
	private String appMessageType;
	
	private String remark;
	
	private Long statusTime;
	
	private String appShowID;
	
	private String rmShowID;
	
	private int readStatus;
	
	private int handleStatus;
	
	private Long createTime;
	
	private String createUser;
	
	private String createUserName;
	
	
	public String getShowID() {
		return showID;
	}
	@JsonProperty("showID")
	public void setShowID(String showID) {
		this.showID = showID;
	}

	public String getFrom() {
		return from;
	}
	@JsonProperty("from")
	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom_Name() {
		return from_Name;
	}
	@JsonProperty("from_Name")
	public void setFrom_Name(String from_Name) {
		this.from_Name = from_Name;
	}

	public String getTo() {
		return to;
	}
	@JsonProperty("to")
	public void setTo(String to) {
		this.to = to;
	}

	public String getTo_Name() {
		return to_Name;
	}
	@JsonProperty("to_Name")
	public void setTo_Name(String to_Name) {
		this.to_Name = to_Name;
	}

	public String getTitle() {
		return title;
	}
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}
	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(int type) {
		this.type = type;
	}

	public String getAppMessageType() {
		return appMessageType;
	}
	@JsonProperty("appMessageType")
	public void setAppMessageType(String appMessageType) {
		this.appMessageType = appMessageType;
	}

	public String getRemark() {
		return remark;
	}
	@JsonProperty("remark")
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getStatusTime() {
		return statusTime;
	}
	@JsonProperty("statusTime")
	public void setStatusTime(Long statusTime) {
		this.statusTime = statusTime;
	}

	public String getAppShowID() {
		return appShowID;
	}
	@JsonProperty("appShowID")
	public void setAppShowID(String appShowID) {
		this.appShowID = appShowID;
	}

	public String getRmShowID() {
		return rmShowID;
	}
	@JsonProperty("rmShowID")
	public void setRmShowID(String rmShowID) {
		this.rmShowID = rmShowID;
	}

	public int getReadStatus() {
		return readStatus;
	}
	@JsonProperty("readStatus")
	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public int getHandleStatus() {
		return handleStatus;
	}
	@JsonProperty("handleStatus")
	public void setHandleStatus(int handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Long getCreateTime() {
		return createTime;
	}
	@JsonProperty("createTime")
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}
	@JsonProperty("createUser")
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return createUserName;
	}
	@JsonProperty("createUserName")
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
}

















