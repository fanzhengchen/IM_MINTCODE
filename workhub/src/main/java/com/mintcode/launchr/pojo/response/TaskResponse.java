package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskEntity;

/**
 * @author StephenHe
 *
 */
public class TaskResponse extends BaseResponse {
	private static final long serialVersionUID = 1L;

	private TaskEntity Data;

	public TaskEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(TaskEntity data) {
		Data = data;
	}
	
}
