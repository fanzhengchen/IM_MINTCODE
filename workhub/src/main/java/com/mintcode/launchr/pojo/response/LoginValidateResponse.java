package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.LoginValidateEntity;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;

public class LoginValidateResponse extends BaseResponse{

	private LoginValidateEntity Data;

	public LoginValidateEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(LoginValidateEntity data) {
		Data = data;
	}
	
	
	
}
