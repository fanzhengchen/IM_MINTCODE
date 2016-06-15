package com.mintcode.launchr.pojo.response;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.MeetingDeleteEntity;
import com.mintcode.launchr.pojo.entity.MeetingUnFreeRoomEntity;

public class MeetingUnFreeRoomResponse extends BaseResponse{

	private List<MeetingUnFreeRoomEntity> Data;

	public List<MeetingUnFreeRoomEntity> getData() {
		return Data;
	}
	
	@JsonProperty("Data")
	public void setData(List<MeetingUnFreeRoomEntity> data) {
		Data = data;
	}
	
}
