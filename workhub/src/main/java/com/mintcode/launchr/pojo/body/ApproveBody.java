package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveResponse;

public class ApproveBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3027226226159259726L;

	private ApproveResponse response;

	public ApproveResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveResponse response) {
		this.response = response;
	}

}
