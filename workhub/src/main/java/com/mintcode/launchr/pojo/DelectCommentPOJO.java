package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.DelectCommentBody;

public class DelectCommentPOJO extends BasePOJO {
	
  

	private DelectCommentBody Body;
	
	public DelectCommentBody getBody() {
		return Body;
	}
	@JsonProperty("Body")
	public void setBody(DelectCommentBody body) {
		this.Body = body;
	}
   
}
