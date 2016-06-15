package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveBody;

public class ApprovePOJO extends BasePOJO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7352354429966153019L;

	private ApproveBody Body;

	public ApproveBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveBody body) {
		Body = body;
	}
	
	
}
