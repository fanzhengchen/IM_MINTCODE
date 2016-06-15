package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;

public class UserInfoResponse extends BaseResponse{

	private UserDetailEntity Data;

	public UserDetailEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(UserDetailEntity data) {
		Data = data;
	}
	
	
}
