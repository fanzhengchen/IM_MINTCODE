package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.AttachementLsitBody;

public class AttachmentListPOJO extends BasePOJO {

	
	

	private AttachementLsitBody Body;
	
	public AttachementLsitBody getBody() {
		return Body;
	}
	@JsonProperty("Body")
	public void setBody(AttachementLsitBody body) {
		Body = body;
	}
}
