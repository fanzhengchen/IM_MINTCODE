package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserDetailNewEntity;

public class UserInfoNewResponse extends BaseResponse{

	
	
	private UserDetailNewEntity Data;

	
	public UserDetailNewEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(UserDetailNewEntity data) {
		Data = data;
	}
	
	
}
