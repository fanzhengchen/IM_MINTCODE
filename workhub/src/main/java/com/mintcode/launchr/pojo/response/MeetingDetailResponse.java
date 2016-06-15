package com.mintcode.launchr.pojo.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.MeetingDetailEntity;
import com.mintcode.launchr.pojo.entity.MeetingEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;

public class MeetingDetailResponse extends BaseResponse{

	@JsonProperty("Data")
	private MeetingDetailEntity Data;

	public MeetingDetailEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(MeetingDetailEntity data) {
		Data = data;
	}
	
	
	
}
