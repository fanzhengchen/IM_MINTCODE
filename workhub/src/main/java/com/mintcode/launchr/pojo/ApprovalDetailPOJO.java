package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveDetailBody;

public class ApprovalDetailPOJO extends BasePOJO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -773769929656128619L;
	private ApproveDetailBody Body;

	public ApproveDetailBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveDetailBody body) {
		Body = body;
	}
}
