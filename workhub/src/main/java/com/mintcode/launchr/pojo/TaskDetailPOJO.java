package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskDetailBody;

public class TaskDetailPOJO extends BasePOJO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2777909897003645418L;
	
	private TaskDetailBody Body;

	public TaskDetailBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(TaskDetailBody body) {
		Body = body;
	}
}
