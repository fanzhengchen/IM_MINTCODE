package com.mintcode.launchr.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.OrganizationEntity;

public class OrganizationResponse extends BaseResponse{

	
	private List<OrganizationEntity> Data;

	public List<OrganizationEntity> getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(List<OrganizationEntity> data) {
		Data = data;
	}
}
