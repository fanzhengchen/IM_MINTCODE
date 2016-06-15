package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.RoomResponse;
import com.mintcode.launchr.pojo.response.UserListResponse;

public class RoomListBody implements Serializable{

	private RoomResponse response;

	public RoomResponse getResponse() {
		return response;
	}

	public void setResponse(RoomResponse response) {
		this.response = response;
	}
	
}
