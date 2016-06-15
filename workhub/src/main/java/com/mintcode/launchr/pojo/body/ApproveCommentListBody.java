package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveCommentListResponse;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveCommentListBody implements Serializable{
	private static final long serialVersionUID = 1L;

	private ApproveCommentListResponse response;

	public ApproveCommentListResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveCommentListResponse response) {
		this.response = response;
	}
	
}
