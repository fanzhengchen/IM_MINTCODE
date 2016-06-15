package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UpdatePassWordBody;

public class UpdatePassWordPOJO extends BasePOJO{
	
	
	private UpdatePassWordBody Body;

	public UpdatePassWordBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(UpdatePassWordBody body) {
		Body = body;
	}
	
	
}



