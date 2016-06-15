package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by sid'pc on 2015/8/27.
 */
public class EventEntity implements Serializable{

    

	/**
	 * 
	 */
	private static final long serialVersionUID = 2465734378706869988L;
	private String showId;
    private String title;
    private String place;
    private String lngx;
    private String laty;
    private long createTime;    
    private long startTime;
    private long endTime;
    private String type;
    private int isImportant;
    private int  isAllDay;
    private int isCancel;
    private int isVisible;
    private String relateId;
    private String createUser;
    private String creatUserName;
    private boolean isAllowSearch;

    public int getIsVisible() {
        return isVisible;
    }
    @JsonProperty("isVisible")
    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getIsAllDay() {
		return isAllDay;
	}

	public void setIsAllDay(int isAllDay) {
		this.isAllDay = isAllDay;
	}
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public int getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(int isImportant) {
        this.isImportant = isImportant;
    }

    public int getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(int isCancel) {
        this.isCancel = isCancel;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreatUserName() {
        return creatUserName;
    }

    public void setCreatUserName(String creatUserName) {
        this.creatUserName = creatUserName;
    }

    public boolean isAllowSearch() {
        return isAllowSearch;
    }

    public void setIsAllowSearch(boolean isAllowSearch) {
        this.isAllowSearch = isAllowSearch;
    }
}
