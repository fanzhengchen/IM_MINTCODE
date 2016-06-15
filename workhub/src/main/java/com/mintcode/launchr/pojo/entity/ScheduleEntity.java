package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6714695341260144792L;
	
	private String title;
	private List<Long> start;
	private List<Long> end;
	private String type;
	private String place;
	private String lngx;
	private String laty;
	private int isImportant;
	private int isAllDay;
	private String content;
	private int repeatType;
	private int remindType;
	private String showId;
	private String createUser;
	private String createUserName;
	private long createTime;
	private List<Times> times;
	private int isVisible;
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public List<Long> getStart() {
		return start;
	}


	public void setStart(List<Long> start) {
		this.start = start;
	}


	public List<Long> getEnd() {
		return end;
	}


	public void setEnd(List<Long> end) {
		this.end = end;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public String getLngx() {
		return lngx;
	}


	public void setLngx(String lngx) {
		this.lngx = lngx;
	}


	public String getLaty() {
		return laty;
	}


	public void setLaty(String laty) {
		this.laty = laty;
	}


	public int getIsImportant() {
		return isImportant;
	}


	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}


	public int getIsAllDay() {
		return isAllDay;
	}


	public void setIsAllDay(int isAllDay) {
		this.isAllDay = isAllDay;
	}

	@JsonProperty("content")
	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public int getRepeatType() {
		return repeatType;
	}


	public void setRepeatType(int repeatType) {
		this.repeatType = repeatType;
	}


	public int getRemindType() {
		return remindType;
	}


	public void setRemindType(int remindType) {
		this.remindType = remindType;
	}


	public String getShowId() {
		return showId;
	}


	public void setShowId(String showId) {
		this.showId = showId;
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

	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public List<Times> getTimes() {
		return times;
	}


	public void setTimes(List<Times> times) {
		this.times = times;
	}

	public int getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}

	public static class Times implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3325011026929955263L;
		private long start;
		private long end;
		private String showId;
		public long getStart() {
			return start;
		}
		public void setStart(long start) {
			this.start = start;
		}
		public long getEnd() {
			return end;
		}
		public void setEnd(long end) {
			this.end = end;
		}
		public String getShowId() {
			return showId;
		}
		public void setShowId(String showId) {
			this.showId = showId;
		}
		
	}
}
