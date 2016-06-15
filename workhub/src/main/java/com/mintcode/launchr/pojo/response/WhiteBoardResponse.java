package com.mintcode.launchr.pojo.response;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.WhiteBoardEntity;

public class WhiteBoardResponse extends BaseResponse{

	private List<WhiteBoardEntity> Data;

	public List<WhiteBoardEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<WhiteBoardEntity> data) {
		Data = data;
	}
	
}
