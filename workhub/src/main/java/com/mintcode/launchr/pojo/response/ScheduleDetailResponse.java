package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;

public class ScheduleDetailResponse extends BaseResponse {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1125804889126923401L;
	public ScheduleEntity getDate() {
		return Date;
	}
	@JsonProperty("Data")
	public void setDate(ScheduleEntity date) {
		Date = date;
	}

	private ScheduleEntity Date;
	
	
}
