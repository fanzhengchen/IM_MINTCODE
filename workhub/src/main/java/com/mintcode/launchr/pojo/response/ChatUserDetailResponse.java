package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ChatUserDetailEntity;

public class ChatUserDetailResponse extends BaseResponse{
	private ChatUserDetailEntity Data;

	public ChatUserDetailEntity getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(ChatUserDetailEntity data) {
		Data = data;
	}
	
	
}
