package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ScheduleDetailBody;

public class ScheduleDetailPOJO extends BasePOJO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5992989152597658332L;

	public ScheduleDetailBody getBody() {
		return Body;
	}
	@JsonProperty("Body")
	public void setBody(ScheduleDetailBody Body) {
		this.Body = Body;
	}

	private ScheduleDetailBody Body;
	
}
