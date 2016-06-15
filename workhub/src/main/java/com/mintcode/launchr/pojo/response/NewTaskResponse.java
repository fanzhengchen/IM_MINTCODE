package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.NewTaskEntity;

public class NewTaskResponse extends BaseResponse{

	private NewTaskEntity Data;

	public NewTaskEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(NewTaskEntity data) {
		Data = data;
	}
	
	
	
}
