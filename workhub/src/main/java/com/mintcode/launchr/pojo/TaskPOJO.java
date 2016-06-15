package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskBody;

/**
 * @author StephenHe
 * 适用于解析：删除子任务，更新任务转化为父任务，
 */
public class TaskPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private TaskBody Body;

	public TaskBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(TaskBody body) {
		Body = body;
	}
	
}
