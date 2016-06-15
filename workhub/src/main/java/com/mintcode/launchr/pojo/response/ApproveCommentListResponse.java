package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;

/**
 * @author StephenHe 2015/9/18
 *
 */
public class ApproveCommentListResponse extends BaseResponse {
	private static final long serialVersionUID = 1L;

	private List<ApproveCommentEntity> Data;

	public List<ApproveCommentEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<ApproveCommentEntity> data) {
		Data = data;
	}

}
