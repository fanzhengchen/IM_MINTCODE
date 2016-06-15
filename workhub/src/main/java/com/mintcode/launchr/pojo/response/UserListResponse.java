package com.mintcode.launchr.pojo.response;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;

public class UserListResponse extends BaseResponse{

	private List<UserDetailEntity> Data;

	public List<UserDetailEntity> getData() {
		return Data;
	}
	
	@JsonProperty("Data")
	public void setData(List<UserDetailEntity> data) {
		Data = data;
	}
	
	
}
