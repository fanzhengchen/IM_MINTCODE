package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskProjectListBody;

/**
 * 
 * @author StephenHe 2015/9/14
 *
 */
public class TaskProjectListPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;
	
	private TaskProjectListBody Body;

	public TaskProjectListBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(TaskProjectListBody body) {
		this.Body = body;
	}
}
