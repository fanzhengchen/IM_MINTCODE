package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.LoginBody;
import com.mintcode.launchr.pojo.body.WhiteBoardBody;

public class WhiteBoardPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private WhiteBoardBody Body;
	
	
	public WhiteBoardBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(WhiteBoardBody body) {
		Body = body;
	}

	
	
}
