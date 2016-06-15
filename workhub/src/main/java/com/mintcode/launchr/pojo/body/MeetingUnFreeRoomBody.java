package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.mintcode.launchr.pojo.response.MeetingDeleteResponse;
import com.mintcode.launchr.pojo.response.MeetingUnFreeRoomResponse;

public class MeetingUnFreeRoomBody implements Serializable {
	
	private MeetingUnFreeRoomResponse response;

	public MeetingUnFreeRoomResponse getResponse() {
		return response;
	}
	
	public void setResponse(MeetingUnFreeRoomResponse response) {
		this.response = response;
	}
	
	
}
