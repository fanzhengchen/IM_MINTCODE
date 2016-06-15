package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.BaseResponse;

public class NormalPOJO extends BasePOJO {

	private Body Body;

	public Body getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(Body body) {
		Body = body;
	}

	public static class Body {

		private BaseResponse response;

		public BaseResponse getResponse() {
			return response;
		}

		public void setResponse(BaseResponse response) {
			this.response = response;
		}

	}
}
