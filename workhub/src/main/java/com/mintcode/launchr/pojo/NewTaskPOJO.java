package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.NewTaskBody;

public class NewTaskPOJO extends BasePOJO{

	private NewTaskBody Body;

	public NewTaskBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(NewTaskBody body) {
		Body = body;
	}
	
}
