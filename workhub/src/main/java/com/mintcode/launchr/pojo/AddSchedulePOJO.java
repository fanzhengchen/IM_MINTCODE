package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.AddScheduleBody;

public class AddSchedulePOJO extends BasePOJO{

	private AddScheduleBody Body;

	public AddScheduleBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(AddScheduleBody body) {
		Body = body;
	}
	
}
