package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.MessageListBody;

public class MessageListPOJO extends BasePOJO{
 
	

	private MessageListBody Body;
	
	public MessageListBody getBody() {
		return Body;
	}
	@JsonProperty("Body")
	public void setBody(MessageListBody body) {
		Body = body;
	}
}
