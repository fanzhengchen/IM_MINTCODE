package com.mintcode.launchr.pojo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse implements Serializable{
	@JsonProperty("IsSuccess")
	private boolean IsSuccess;
	
	
	private String Reason;
	
	private int TotalCount;
	
	
	public boolean isIsSuccess() {
		return IsSuccess;
	}

	@JsonProperty("IsSuccess")
	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}

	public String getReason() {
		return Reason;
	}

	@JsonProperty("Reason")
	public void setReason(String reason) {
		Reason = reason;
	}

	public int getTotalCount() {
		return TotalCount;
	}

	@JsonProperty("TotalCount")
	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	
}
