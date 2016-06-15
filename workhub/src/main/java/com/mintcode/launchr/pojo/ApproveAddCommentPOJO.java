package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveAddCommentBody;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveAddCommentPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private ApproveAddCommentBody Body;

	public ApproveAddCommentBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ApproveAddCommentBody body) {
		Body = body;
	}
	
}
