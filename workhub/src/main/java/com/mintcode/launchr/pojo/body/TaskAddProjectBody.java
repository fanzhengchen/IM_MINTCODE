package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.TaskAddProjectResponse;

/**
 * @author StephenHe 2015/9/15
 *
 */
public class TaskAddProjectBody implements Serializable {
	private static final long serialVersionUID = 1L;

	private TaskAddProjectResponse response;

	public TaskAddProjectResponse getResponse() {
		return response;
	}

	public void setResponse(TaskAddProjectResponse response) {
		this.response = response;
	}

}
