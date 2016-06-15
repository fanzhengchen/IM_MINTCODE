package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.EventEntity;

public class ScheduleResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4257694828998086085L;
	@JsonProperty("Data")
	private List<EventEntity> Data;

	public List<EventEntity> getData() {
		return Data;
	}

	public void setData(List<EventEntity> data) {
		Data = data;
	}
	
}
