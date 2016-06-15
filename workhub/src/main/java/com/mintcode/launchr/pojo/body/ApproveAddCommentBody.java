package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveAddCommentResponse;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveAddCommentBody implements Serializable {
	private static final long serialVersionUID = 1L;

	private ApproveAddCommentResponse response;

	public ApproveAddCommentResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveAddCommentResponse response) {
		this.response = response;
	}
	
}
