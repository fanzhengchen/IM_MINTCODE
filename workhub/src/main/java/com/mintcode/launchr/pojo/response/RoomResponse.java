package com.mintcode.launchr.pojo.response;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.RoomEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;

public class RoomResponse extends BaseResponse{

	private List<RoomEntity> Data;

	public List<RoomEntity> getData() {
		return Data;
	}
	
	@JsonProperty("Data")
	public void setData(List<RoomEntity> data) {
		Data = data;
	}
	
	
}
