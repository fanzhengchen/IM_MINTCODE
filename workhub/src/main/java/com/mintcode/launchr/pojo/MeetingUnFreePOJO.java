package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MeetingDeleteBody;
import com.mintcode.launchr.pojo.body.MeetingUnFreeRoomBody;

public class MeetingUnFreePOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private MeetingUnFreeRoomBody Body;

	public MeetingUnFreeRoomBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(MeetingUnFreeRoomBody body) {
		Body = body;
	}
	
	
	
	
	
}
