package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.DepartmentEntity;

public class DepartmentResponse extends BaseResponse{

	private List<DepartmentEntity> Data;

	public List<DepartmentEntity> getData() {
		return Data;
	}

	
	@JsonProperty("Data")
	public void setData(List<DepartmentEntity> data) {
		Data = data;
	}
	
	
	
}
