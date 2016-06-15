package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveEntity;

public class ApproveListResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 186577626783020309L;
	private List<ApproveEntity> Data;

	public List<ApproveEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<ApproveEntity> data) {
		Data = data;
	}
	
	
}
