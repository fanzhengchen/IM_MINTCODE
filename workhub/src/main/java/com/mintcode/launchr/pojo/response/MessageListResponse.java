package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.MessageEntity;

public class MessageListResponse extends BaseResponse {
	
	
	private List<MessageEntity> Data;
	
	
	public List<MessageEntity> getData() {
		return Data;
	}
	@JsonProperty("Data")
	public void setData(List<MessageEntity> data) {
		Data = data;
	}

}
