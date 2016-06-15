package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.RoomListBody;
import com.mintcode.launchr.pojo.body.UserListBody;

public class RoomListPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private RoomListBody Body;

	public RoomListBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(RoomListBody body) {
		Body = body;
	}
	
	
	
	
	
}
