package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomEntity implements Serializable{
	
	private String SHOW_ID;
	
	private String R_NAME;
	
	public String getSHOW_ID() {
		return SHOW_ID;
	}

	@JsonProperty("SHOW_ID")
	public void setSHOW_ID(String sHOW_ID) {
		SHOW_ID = sHOW_ID;
	}

	public String getR_NAME() {
		return R_NAME;
	}

	@JsonProperty("R_NAME")
	public void setR_NAME(String r_NAME) {
		R_NAME = r_NAME;
	}

	
}

