package com.mintcode.launchr.pojo.body;

import java.io.Serializable;
import java.util.List;

import com.mintcode.launchr.pojo.response.LoginResponse;
import com.mintcode.launchr.pojo.response.MeetingResponse;
import com.mintcode.launchr.pojo.response.OrganizationResponse;

public class OrganizationBody implements Serializable {
	
	private OrganizationResponse response;

	public OrganizationResponse getResponse() {
		return response;
	}

	public void setResponse(OrganizationResponse response) {
		this.response = response;
	}
	
	
}
