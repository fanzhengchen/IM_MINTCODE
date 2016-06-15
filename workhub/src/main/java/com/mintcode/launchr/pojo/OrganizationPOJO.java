package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MeetingDeleteBody;
import com.mintcode.launchr.pojo.body.OrganizationBody;

public class OrganizationPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private OrganizationBody Body;

	public OrganizationBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(OrganizationBody body) {
		Body = body;
	}
	
	
	
	
	
}
