package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserDetailBody;
import com.mintcode.launchr.pojo.body.UserListBody;

public class UserDetailPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private UserDetailBody Body;

	public UserDetailBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(UserDetailBody body) {
		Body = body;
	}
	
	
	
	
	
}
