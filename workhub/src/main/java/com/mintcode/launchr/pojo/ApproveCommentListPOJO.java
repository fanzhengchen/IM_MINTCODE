package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveCommentListBody;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveCommentListPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private ApproveCommentListBody Body;

	public ApproveCommentListBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveCommentListBody body) {
		Body = body;
	}
	
}
