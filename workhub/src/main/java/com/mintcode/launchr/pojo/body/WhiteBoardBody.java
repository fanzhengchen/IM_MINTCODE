package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.ApproveResponse;
import com.mintcode.launchr.pojo.response.WhiteBoardResponse;

public class WhiteBoardBody implements Serializable{


	private WhiteBoardResponse response;

	public WhiteBoardResponse getResponse() {
		return response;
	}

	public void setResponse(WhiteBoardResponse response) {
		this.response = response;
	}

}
