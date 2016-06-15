package com.mintcode.launchr.pojo.body;

import java.io.Serializable;
import java.util.List;

import com.mintcode.launchr.pojo.response.LoginResponse;

public class LoginBody implements Serializable {
	
	private LoginResponse response;

	public LoginResponse getResponse() {
		return response;
	}

	public void setResponse(LoginResponse response) {
		this.response = response;
	}
	
	
}
