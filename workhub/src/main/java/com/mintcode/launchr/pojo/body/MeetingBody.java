package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.MeetingResponse;

public class MeetingBody implements Serializable {
	
	private MeetingResponse response;

	public MeetingResponse getResponse() {
		return response;
	}

	public void setResponse(MeetingResponse response) {
		this.response = response;
	}
	
	
}
