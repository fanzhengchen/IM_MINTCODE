package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveDetailResponse;
import com.mintcode.launchr.pojo.response.ApproveResponse;

public class ApproveDetailBody implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6597125305693804367L;
	private ApproveDetailResponse response;

	public ApproveDetailResponse getResponse() {
		return response;
	}

	public void setResponse(ApproveDetailResponse response) {
		this.response = response;
	}
}
