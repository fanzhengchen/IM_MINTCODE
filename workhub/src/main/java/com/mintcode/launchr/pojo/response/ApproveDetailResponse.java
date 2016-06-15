package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveDetailEntity;

public class ApproveDetailResponse extends BaseResponse {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2535957149256995969L;
		
	private ApproveDetailEntity Data;
	
	public ApproveDetailEntity getData() {
		return Data;
	}
	@JsonProperty("Data")
	public void setData(ApproveDetailEntity data) {
		Data = data;
	}
}
