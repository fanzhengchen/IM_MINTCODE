package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.UserDetailResponse;
import com.mintcode.launchr.pojo.response.UserListResponse;

public class UserDetailBody implements Serializable{

	private UserDetailResponse response;

	public UserDetailResponse getResponse() {
		return response;
	}

	public void setResponse(UserDetailResponse response) {
		this.response = response;
	}
	
}
