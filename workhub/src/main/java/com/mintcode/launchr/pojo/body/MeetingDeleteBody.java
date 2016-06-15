package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.MeetingDeleteResponse;

public class MeetingDeleteBody implements Serializable {
	
	private MeetingDeleteResponse response;

	public MeetingDeleteResponse getResponse() {
		return response;
	}

	public void setResponse(MeetingDeleteResponse response) {
		this.response = response;
	}
	
	
}
