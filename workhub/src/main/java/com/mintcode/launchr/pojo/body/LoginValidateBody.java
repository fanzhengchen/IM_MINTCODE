package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.LoginValidateResponse;

public class LoginValidateBody implements Serializable{

	private LoginValidateResponse response;

	public LoginValidateResponse getResponse() {
		return response;
	}

	public void setResponse(LoginValidateResponse response) {
		this.response = response;
	}
	
}
