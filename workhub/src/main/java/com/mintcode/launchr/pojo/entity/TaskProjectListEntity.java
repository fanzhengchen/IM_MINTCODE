package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * 
 * @author StephenHe 2015/9/14
 *
 */
public class TaskProjectListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String showId;
	
	private String name;
	
	private int teamNumber;
	
	private int unFinishedTask;
	
	private int allTask;

	private String createUser;
	
	public String getShowId() {
		return showId;
	}
	public void setShowId(String showId) {
		this.showId = showId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	public int getUnFinishedTask() {
		return unFinishedTask;
	}
	public void setUnFinishedTask(int unFinishedTask) {
		this.unFinishedTask = unFinishedTask;
	}
	public int getAllTask() {
		return allTask;
	}
	public void setAllTask(int allTask) {
		this.allTask = allTask;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
