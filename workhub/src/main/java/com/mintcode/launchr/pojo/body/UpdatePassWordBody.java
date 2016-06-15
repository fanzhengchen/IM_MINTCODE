package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.UpdatePassWordResponse;

public class UpdatePassWordBody implements Serializable{
	
	public UpdatePassWordResponse getResponse() {
		return response;
	}

	public void setResponse(UpdatePassWordResponse response) {
		this.response = response;
	}

	private UpdatePassWordResponse response;
	
	
}
