package com.mintcode.launchr.pojo.response;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserDetailTempEntity;

public class UserDetailResponse extends BaseResponse{

	private UserDetailTempEntity Data;

	public UserDetailTempEntity getData() {
		return Data;
	}
	
	@JsonProperty("Data")
	public void setData(UserDetailTempEntity data) {
		Data = data;
	}
	
	
}
