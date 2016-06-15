package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskSearchAndGetTaskListBody;

public class TaskSearchAndGetTaskListPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private TaskSearchAndGetTaskListBody Body;

	public TaskSearchAndGetTaskListBody getBody() {
		return Body;
	}
	
	@JsonProperty("Body")
	public void setBody(TaskSearchAndGetTaskListBody body) {
		Body = body;
	}
	
	
}
