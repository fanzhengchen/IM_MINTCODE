package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.DepartmentResponse;

public class DepartmentBody implements Serializable{

	private DepartmentResponse response;
	

	public DepartmentResponse getResponse() {
		return response;
	}

	public void setResponse(DepartmentResponse response) {
		this.response = response;
	}
	
}
