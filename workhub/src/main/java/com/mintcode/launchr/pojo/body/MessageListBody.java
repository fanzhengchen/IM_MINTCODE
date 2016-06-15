package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.MessageListResponse;

public class MessageListBody implements Serializable{
	
	
	

	private MessageListResponse response;
	
	public MessageListResponse getResponse() {
		return response;
	}

	public void setResponse(MessageListResponse response) {
		this.response = response;
	}
}
