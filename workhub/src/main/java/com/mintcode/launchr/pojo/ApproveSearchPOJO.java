package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveSearchBody;

public class ApproveSearchPOJO extends BasePOJO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9169546858981683954L;

	private ApproveSearchBody Body;

	public ApproveSearchBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveSearchBody body) {
		Body = body;
	}
	
}
