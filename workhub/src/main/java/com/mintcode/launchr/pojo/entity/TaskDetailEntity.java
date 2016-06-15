package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

public class TaskDetailEntity implements Serializable{
	private String showId;

	private String pShowId;

	private String tTitle;

	private String tContent;

	private long tStartTime;

	private long tEndTime;

	private String tUser;

	private String tUserName;

	private String tPriority;

	private int tRemindType;

	private long tRemindTime;

	private String tParentShowId;

	private int tLevel;

	private int tIsComment;

	private int tIsAnnex;

	private String sType;

	private long lastUpdateTime;

	private String pName;

	private String createUser;

	private String createUserName;

	private int isStartTimeAllDay;

	private int isEndTimeAllDay;

	private TaskDetail parentTask;

	private List<TaskDetail> childTasks;

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getpShowId() {
		return pShowId;
	}

	public void setpShowId(String pShowId) {
		this.pShowId = pShowId;
	}

	public String gettTitle() {
		return tTitle;
	}

	public void settTitle(String tTitle) {
		this.tTitle = tTitle;
	}

	public String gettContent() {
		return tContent;
	}

	public void settContent(String tContent) {
		this.tContent = tContent;
	}

	public long gettStartTime() {
		return tStartTime;
	}

	public void settStartTime(long tStartTime) {
		this.tStartTime = tStartTime;
	}

	public long gettEndTime() {
		return tEndTime;
	}

	public void settEndTime(long tEndTime) {
		this.tEndTime = tEndTime;
	}

	public String gettUser() {
		return tUser;
	}

	public void settUser(String tUser) {
		this.tUser = tUser;
	}

	public String gettUserName() {
		return tUserName;
	}

	public void settUserName(String tUserName) {
		this.tUserName = tUserName;
	}

	public String gettPriority() {
		return tPriority;
	}

	public void settPriority(String tPriority) {
		this.tPriority = tPriority;
	}

	public int gettRemindType() {
		return tRemindType;
	}

	public void settRemindType(int tRemindType) {
		this.tRemindType = tRemindType;
	}

	public long gettRemindTime() {
		return tRemindTime;
	}

	public void settRemindTime(long tRemindTime) {
		this.tRemindTime = tRemindTime;
	}

	public String gettParentShowId() {
		return tParentShowId;
	}

	public void settParentShowId(String tParentShowId) {
		this.tParentShowId = tParentShowId;
	}

	public int gettLevel() {
		return tLevel;
	}

	public void settLevel(int tLevel) {
		this.tLevel = tLevel;
	}

	public int gettIsComment() {
		return tIsComment;
	}

	public void settIsComment(int tIsComment) {
		this.tIsComment = tIsComment;
	}

	public int gettIsAnnex() {
		return tIsAnnex;
	}

	public void settIsAnnex(int tIsAnnex) {
		this.tIsAnnex = tIsAnnex;
	}

	public String getsType() {
		return sType;
	}

	public void setsType(String sType) {
		this.sType = sType;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public int getIsStartTimeAllDay() {
		return isStartTimeAllDay;
	}

	public void setIsStartTimeAllDay(int isStartTimeAllDay) {
		this.isStartTimeAllDay = isStartTimeAllDay;
	}

	public int getIsEndTimeAllDay() {
		return isEndTimeAllDay;
	}

	public void setIsEndTimeAllDay(int isEndTimeAllDay) {
		this.isEndTimeAllDay = isEndTimeAllDay;
	}

	public TaskDetail getParentTask() {
		return parentTask;
	}

	public void setParentTask(TaskDetail parentTask) {
		this.parentTask = parentTask;
	}

	public List<TaskDetail> getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(List<TaskDetail> childTasks) {
		this.childTasks = childTasks;
	}

	public static class TaskDetail implements Serializable{
		private String showId;
		private String tTitle;
		private long tStartTime;
		private long tEndTime;
		private String tUser;
		private String tUserName;
		private String tPriority;
		private long tRemindTime;
		private int tIsComment;
		private int tIsAnnex;
		private String sType;
		private String createUser;
		private String createUserName;
		private int isStartTimeAllDay;
		private int isEndTimeAllDay;

		public String getShowId() {
			return showId;
		}

		public void setShowId(String showId) {
			this.showId = showId;
		}

		public String gettTitle() {
			return tTitle;
		}

		public void settTitle(String tTitle) {
			this.tTitle = tTitle;
		}

		public long gettStartTime() {
			return tStartTime;
		}

		public void settStartTime(long tStartTime) {
			this.tStartTime = tStartTime;
		}

		public long gettEndTime() {
			return tEndTime;
		}

		public void settEndTime(long tEndTime) {
			this.tEndTime = tEndTime;
		}

		public String gettUser() {
			return tUser;
		}

		public void settUser(String tUser) {
			this.tUser = tUser;
		}

		public String gettUserName() {
			return tUserName;
		}

		public void settUserName(String tUserName) {
			this.tUserName = tUserName;
		}

		public String gettPriority() {
			return tPriority;
		}

		public void settPriority(String tPriority) {
			this.tPriority = tPriority;
		}

		public long gettRemindTime() {
			return tRemindTime;
		}

		public void settRemindTime(long tRemindTime) {
			this.tRemindTime = tRemindTime;
		}

		public int gettIsComment() {
			return tIsComment;
		}

		public void settIsComment(int tIsComment) {
			this.tIsComment = tIsComment;
		}

		public int gettIsAnnex() {
			return tIsAnnex;
		}

		public void settIsAnnex(int tIsAnnex) {
			this.tIsAnnex = tIsAnnex;
		}

		public String getsType() {
			return sType;
		}

		public void setsType(String sType) {
			this.sType = sType;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public String getCreateUserName() {
			return createUserName;
		}

		public void setCreateUserName(String createUserName) {
			this.createUserName = createUserName;
		}

		public int getIsStartTimeAllDay() {
			return isStartTimeAllDay;
		}

		public void setIsStartTimeAllDay(int isStartTimeAllDay) {
			this.isStartTimeAllDay = isStartTimeAllDay;
		}

		public int getIsEndTimeAllDay() {
			return isEndTimeAllDay;
		}

		public void setIsEndTimeAllDay(int isEndTimeAllDay) {
			this.isEndTimeAllDay = isEndTimeAllDay;
		}
	}
}


























