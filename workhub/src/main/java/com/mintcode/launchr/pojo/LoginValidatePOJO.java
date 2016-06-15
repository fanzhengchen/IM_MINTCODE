package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.LoginValidateBody;

public class LoginValidatePOJO extends BasePOJO{

	private LoginValidateBody Body;

	public LoginValidateBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(LoginValidateBody body) {
		Body = body;
	}
	
}
