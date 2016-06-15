package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveListBody;

public class ApproveListPOJO extends BasePOJO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2039433308404374929L;

	private ApproveListBody Body;

	public ApproveListBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveListBody body) {
		Body = body;
	}
	
	
}
