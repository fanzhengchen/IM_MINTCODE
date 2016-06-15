package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskDetailEntity;

public class TaskDetailResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2779297039223681844L;
	
	private TaskDetailEntity Data;

	public TaskDetailEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(TaskDetailEntity data) {
		Data = data;
	}

}
