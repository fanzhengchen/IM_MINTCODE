package com.mintcode.launchr.pojo.body;

import java.io.Serializable;
import java.util.List;

import com.mintcode.launchr.pojo.response.LoginResponse;
import com.mintcode.launchr.pojo.response.MeetingDetailResponse;
import com.mintcode.launchr.pojo.response.MeetingResponse;

public class MeetingDetailBody implements Serializable {
	
	private MeetingDetailResponse response;

	public MeetingDetailResponse getResponse() {
		return response;
	}

	public void setResponse(MeetingDetailResponse response) {
		this.response = response;
	}
	
	
}
