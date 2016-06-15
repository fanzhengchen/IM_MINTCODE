package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.AddScheduleResponse;

public class AddScheduleBody implements Serializable{

	private AddScheduleResponse response;

	public AddScheduleResponse getResponse() {
		return response;
	}

	public void setResponse(AddScheduleResponse response) {
		this.response = response;
	}
	
}
