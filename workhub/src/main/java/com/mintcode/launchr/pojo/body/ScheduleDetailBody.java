package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ScheduleDetailResponse;

public class ScheduleDetailBody implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 503964821914689525L;

	private ScheduleDetailResponse response;
	
	public ScheduleDetailResponse getResponse() {
		return response;
	}
	
	public void setResponse(ScheduleDetailResponse response) {
		this.response = response;
	}
}
