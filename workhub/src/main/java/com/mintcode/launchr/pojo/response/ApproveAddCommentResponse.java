package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveAddCommentResponse extends BaseResponse {
	private static final long serialVersionUID = 1L;

	private ApproveCommentEntity Data;

	public ApproveCommentEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(ApproveCommentEntity data) {
		Data = data;
	}
	
}
