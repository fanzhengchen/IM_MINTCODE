package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.UserListResponse;

public class UserListBody implements Serializable{

	private UserListResponse response;

	public UserListResponse getResponse() {
		return response;
	}

	public void setResponse(UserListResponse response) {
		this.response = response;
	}
	
}
