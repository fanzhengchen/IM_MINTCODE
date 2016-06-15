package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.TaskSearchAndGetTaskListResponse;

public class TaskSearchAndGetTaskListBody implements Serializable{

	private TaskSearchAndGetTaskListResponse response;

	public TaskSearchAndGetTaskListResponse getResponse() {
		return response;
	}

	public void setResponse(TaskSearchAndGetTaskListResponse response) {
		this.response = response;
	}
	
}
