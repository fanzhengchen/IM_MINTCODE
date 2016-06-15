package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserInfoBody;

public class UserInfoPOJO extends BasePOJO{

	private UserInfoBody Body;

	public UserInfoBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(UserInfoBody body) {
		Body = body;
	}
	
	
}
