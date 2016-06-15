package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskSearchAndGetTaskListEntity implements Serializable{

	public String projectId;
	public String projectName;
	public String statusId;
	public String statustype;
	public String statusName;
	
	public String taskId;
	public String title;
	public String priority;
	public int level;
	public String parentTaskId;
	
	public List<String> userNames;
	
	public List<String> userTrueNames;
	
	public String image;
	public long endTime;
	public String isAnnex;
	public String finishedTask;
	public String allTask;


	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}

	public List<String> getUserTrueNames() {
		return userTrueNames;
	}

	public void setUserTrueNames(List<String> userTrueNames) {
		this.userTrueNames = userTrueNames;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatustype() {
		return statustype;
	}

	public void setStatustype(String statustype) {
		this.statustype = statustype;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getIsAnnex() {
		return isAnnex;
	}

	public void setIsAnnex(String isAnnex) {
		this.isAnnex = isAnnex;
	}

	public String getFinishedTask() {
		return finishedTask;
	}

	public void setFinishedTask(String finishedTask) {
		this.finishedTask = finishedTask;
	}

	public String getAllTask() {
		return allTask;
	}

	public void setAllTask(String allTask) {
		this.allTask = allTask;
	}
	
}
