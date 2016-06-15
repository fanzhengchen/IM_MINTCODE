package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveSearchResponse;

public class ApproveSearchBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6304738485330864440L;

	private ApproveSearchResponse response;

	public ApproveSearchResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveSearchResponse response) {
		this.response = response;
	}
	
}
