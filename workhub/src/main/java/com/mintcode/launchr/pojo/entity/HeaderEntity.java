package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeaderEntity implements Serializable{

	/** 是否成功 */
	@JsonProperty("IsSuccess")
	private boolean IsSuccess;
	
	public int ResCode;

	public boolean isIsSuccess() {
		return IsSuccess;
	}

	public String Reason;
	
	@JsonProperty("IsSuccess")
	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}

	public int getResCode() {
		return ResCode;
	}
	
	
	public void setResCode(int resCode) {
		ResCode = resCode;
	}

	public String getReason() {
		return Reason;
	}

	@JsonProperty("Reason")
	public void setReason(String reason) {
		Reason = reason;
	}
	
	
}
