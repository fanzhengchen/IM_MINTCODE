package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.TaskResponse;

/**
 * @author StephenHe
 *
 */
public class TaskBody implements Serializable {
	private static final long serialVersionUID = 1L;

	private TaskResponse response;

	public TaskResponse getResponse() {
		return response;
	}

	public void setResponse(TaskResponse response) {
		this.response = response;
	}
	
}
