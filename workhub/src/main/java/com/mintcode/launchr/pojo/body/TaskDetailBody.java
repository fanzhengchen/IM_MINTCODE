package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.TaskDetailResponse;





public class TaskDetailBody implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6731203548057466891L;
	private TaskDetailResponse response;

	public TaskDetailResponse getResponse() {
		return response;
	}
	@JsonProperty("response")
	public void setResponse(TaskDetailResponse response) {
		this.response = response;
	}
}
