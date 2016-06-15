package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MeetingDeleteBody;

public class MeetingDeletePOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private MeetingDeleteBody Body;

	public MeetingDeleteBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(MeetingDeleteBody body) {
		Body = body;
	}
	
	
	
	
	
}
