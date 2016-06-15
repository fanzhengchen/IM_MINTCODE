package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;

/**
 * @author StephenHe 2015/9/15
 *
 */
public class TaskAddProjectResponse extends BaseResponse {
	private static final long serialVersionUID = 1L;

	private TaskAddProjectEntity Data;

	public TaskAddProjectEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(TaskAddProjectEntity data) {
		Data = data;
	}

}
