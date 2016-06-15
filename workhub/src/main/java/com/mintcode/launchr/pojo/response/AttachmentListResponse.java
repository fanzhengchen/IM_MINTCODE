package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;

public class AttachmentListResponse extends BaseResponse {

	

	private List<AttachmentListEntity> Date;
	
	public List<AttachmentListEntity> getDate() {
		return Date;
	}
	@JsonProperty("Data")
	public void setDate(List<AttachmentListEntity> date) {
		Date = date;
	}
}
