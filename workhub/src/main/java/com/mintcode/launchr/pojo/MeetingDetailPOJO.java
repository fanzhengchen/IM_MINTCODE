package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MeetingDetailBody;

public class MeetingDetailPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private MeetingDetailBody Body;

	public MeetingDetailBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(MeetingDetailBody body) {
		Body = body;
	}
	
	
	
	
	
}
