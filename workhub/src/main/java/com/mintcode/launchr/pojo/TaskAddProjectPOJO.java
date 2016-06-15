package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskAddProjectBody;

/**
 * @author StephenHe 2015/9/15
 *
 */
public class TaskAddProjectPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;
	
	private TaskAddProjectBody Body;

	public TaskAddProjectBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(TaskAddProjectBody body) {
		Body = body;
	}

}
