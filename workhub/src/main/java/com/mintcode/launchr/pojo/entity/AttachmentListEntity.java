package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttachmentListEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6360272811537898283L;



	private String ShowID;
	
	private String title;
	
	private String path;
	
	private Long size;
	
	public String getShowID() {
		return ShowID;
	}
	@JsonProperty("ShowID")
	public void setShowID(String showID) {
		ShowID = showID;
	}

	public String getTitle() {
		return title;
	}
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}
	@JsonProperty("path")
	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}
	@JsonProperty("size")
	public void setSize(Long size) {
		this.size = size;
	}
	
}
