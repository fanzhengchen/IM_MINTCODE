package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;

/**
 * 
 * @author StephenHe 2015/9/14
 *
 */
public class TaskProjectListResponse extends BaseResponse {
	private static final long serialVersionUID = 1L;
	
	private List<TaskProjectListEntity> Data;

	public List<TaskProjectListEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<TaskProjectListEntity> data) {
		Data = data;
	}

}
