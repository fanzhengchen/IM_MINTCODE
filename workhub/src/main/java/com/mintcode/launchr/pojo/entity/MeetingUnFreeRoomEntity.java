package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingUnFreeRoomEntity implements Serializable{

	private String MeetingRoomNo;

	public String getMeetingRoomNo() {
		return MeetingRoomNo;
	}

	@JsonProperty("MeetingRoomNo")
	public void setMeetingRoomNo(String meetingRoomNo) {
		MeetingRoomNo = meetingRoomNo;
	}

	
	
	
}
