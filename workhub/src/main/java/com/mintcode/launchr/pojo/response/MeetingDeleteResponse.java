package com.mintcode.launchr.pojo.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.MeetingDeleteEntity;

public class MeetingDeleteResponse extends BaseResponse{

	private MeetingDeleteEntity Data;

	public MeetingDeleteEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(MeetingDeleteEntity data) {
		Data = data;
	}
	
}
