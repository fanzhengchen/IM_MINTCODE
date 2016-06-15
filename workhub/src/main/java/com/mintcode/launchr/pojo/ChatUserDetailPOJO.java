package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ChatUserDetailBody;

public class ChatUserDetailPOJO extends BasePOJO{
	private ChatUserDetailBody Body;

	public ChatUserDetailBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(ChatUserDetailBody body) {
		Body = body;
	}
	
}
