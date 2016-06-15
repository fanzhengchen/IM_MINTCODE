package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;

public class DelectCommentResponse extends BaseResponse {

	
	

	private ApproveCommentEntity Data;
	
	public ApproveCommentEntity getData() {
		return Data;
	}
	@JsonProperty("Data")
	public void setData(ApproveCommentEntity data) {
		Data = data;
	}
}
