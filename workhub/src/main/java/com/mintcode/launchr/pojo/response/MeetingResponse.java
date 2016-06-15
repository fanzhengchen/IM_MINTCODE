package com.mintcode.launchr.pojo.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.MeetingEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;

public class MeetingResponse extends BaseResponse{

	@JsonProperty("Data")
	private MeetingEntity Data;

	public MeetingEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(MeetingEntity data) {
		Data = data;
	}
	
	
	
}
