package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.NewTaskResponse;

public class NewTaskBody implements Serializable{

	private NewTaskResponse response;

	public NewTaskResponse getResponse() {
		return response;
	}

	public void setResponse(NewTaskResponse response) {
		this.response = response;
	}
	
}
