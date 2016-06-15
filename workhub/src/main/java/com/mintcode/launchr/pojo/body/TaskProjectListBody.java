package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.TaskProjectListResponse;

/**
 * 
 * @author StephenHe 2015/9/14
 *
 */
public class TaskProjectListBody implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private TaskProjectListResponse response;

	public TaskProjectListResponse getResponse() {
		return response;
	}

	public void setResponse(TaskProjectListResponse response) {
		this.response = response;
	}
	
}
