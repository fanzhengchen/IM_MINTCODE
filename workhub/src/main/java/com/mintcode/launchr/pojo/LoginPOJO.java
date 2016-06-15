package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.LoginBody;

public class LoginPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private LoginBody Body;

	
	public LoginBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(LoginBody body) {
		Body = body;
	}

	
	
}
