package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MeetingBody;
import com.mintcode.launchr.pojo.body.UserListBody;

public class MeetingPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private MeetingBody Body;

	public MeetingBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(MeetingBody body) {
		Body = body;
	}
	
	
	
	
	
}
