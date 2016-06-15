package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ScheduleBody;


public class SchedulePOJO extends BasePOJO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1127130657488584343L;
	
	
	private ScheduleBody Body;

	
	public ScheduleBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ScheduleBody body) {
		Body = body;
	}
	
}
