package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.DepartmentBody;

public class DepartmentPOJO extends BasePOJO{

	private DepartmentBody Body;

	public DepartmentBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(DepartmentBody body) {
		Body = body;
	}
	
}
