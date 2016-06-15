package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.UserInfoNewResponse;

public class UserInfoNewBody implements Serializable{

	/**
	 * 
	 */
	
	
	private UserInfoNewResponse response;

	public UserInfoNewResponse getResponse() {
		return response;
	}

	public void setResponse(UserInfoNewResponse response) {
		this.response = response;
	}

}
