package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserListBody;

public class UserListPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private UserListBody Body;

	public UserListBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(UserListBody body) {
		Body = body;
	}
	
	
	
	
	
}
