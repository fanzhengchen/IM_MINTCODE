package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ScheduleResponse;

public class ScheduleBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3589688144302463581L;

	private ScheduleResponse response;

	public ScheduleResponse getResponse() {
		return response;
	}

	public void setResponse(ScheduleResponse response) {
		this.response = response;
	}
	
	
}
