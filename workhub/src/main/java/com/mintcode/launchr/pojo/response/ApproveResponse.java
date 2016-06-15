package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveEntity;

public class ApproveResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6977639695968684092L;

	private ApproveEntity Data;
	    
	public ApproveEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(ApproveEntity data) {
		Data = data;
	}
	
}
