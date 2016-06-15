package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveListResponse;

public class ApproveListBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4573916139561469174L;

	private ApproveListResponse response;

	public ApproveListResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveListResponse response) {
		this.response = response;
	}

	
	
}
