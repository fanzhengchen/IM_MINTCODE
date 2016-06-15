package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author StephenHe 2015/9/18
 *         审批-新增评论，获取评论列表
 */
public class ApproveCommentEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String showID;
	private String content;
	private String filePath;
	private int isComment;
	private int isDeleted;
	private String appShowID;
	private String rm_ShowID;
	private String c_ShowID;
	private String creatUser;
	private String creatUserName;
	private long createTime;
	private String transType;
	private String rmData;

	public String getRmData(){
		return this.rmData;
	}
	@JsonProperty("rmData")
	public void setRmData(String rmData){
		this.rmData = rmData;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	@JsonProperty("isDeleted")
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getShowID() {
		return showID;
	}
	public void setShowID(String showID) {
		this.showID = showID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getIsComment() {
		return isComment;
	}
	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}
	public String getAppShowID() {
		return appShowID;
	}
	public void setAppShowID(String appShowID) {
		this.appShowID = appShowID;
	}
	public String getRm_ShowID() {
		return rm_ShowID;
	}
	public void setRm_ShowID(String rm_ShowID) {
		this.rm_ShowID = rm_ShowID;
	}
	public String getC_ShowID() {
		return c_ShowID;
	}
	public void setC_ShowID(String c_ShowID) {
		this.c_ShowID = c_ShowID;
	}
	public String getCreatUser() {
		return creatUser;
	}
	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
	}
	public String getCreatUserName() {
		return creatUserName;
	}
	public void setCreatUserName(String creatUserName) {
		this.creatUserName = creatUserName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
}
