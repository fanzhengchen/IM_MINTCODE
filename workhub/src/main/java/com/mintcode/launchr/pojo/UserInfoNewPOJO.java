package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserInfoNewBody;

public class UserInfoNewPOJO extends BasePOJO{
	
	/**
	 * 
	 */
	
	private UserInfoNewBody Body;
	

	public UserInfoNewBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(UserInfoNewBody body) {
		Body = body;
	}
	
	
	
	
}
