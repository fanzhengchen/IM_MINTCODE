package com.mintcode.launchr.pojo.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserEntity;

public class LoginResponse extends BaseResponse{

	private UserEntity Data;

	public UserEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(UserEntity data) {
		Data = data;
	}
	
}
