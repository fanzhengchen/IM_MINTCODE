package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;

public class AddScheduleResponse extends BaseResponse{

	private ScheduleEntity Data;

	public ScheduleEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(ScheduleEntity data) {
		Data = data;
	}
	
	
	
}
