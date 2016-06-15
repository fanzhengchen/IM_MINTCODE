package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskSearchAndGetTaskListEntity;

public class TaskSearchAndGetTaskListResponse extends BaseResponse{

	private List<TaskSearchAndGetTaskListEntity> Data;

	public List<TaskSearchAndGetTaskListEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<TaskSearchAndGetTaskListEntity> data) {
		Data = data;
	}
	
	
	
}
