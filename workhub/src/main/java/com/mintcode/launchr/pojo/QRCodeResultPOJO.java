package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.BaseResponse;

public class QRCodeResultPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;

	private QRCodeBody Body;

	public QRCodeBody getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(QRCodeBody body) {
		Body = body;
	}
	
	
	public static class QRCodeBody{
		private QRCodeResponse response;

		public QRCodeResponse getResponse() {
			return response;
		}

		public void setResponse(QRCodeResponse response) {
			this.response = response;
		}
		
	}
	
	
	public static class QRCodeResponse extends BaseResponse{
		/**
		 * 
		 */
		private static final long serialVersionUID = -789704012391601668L;
		private int Code;

		public int getCode() {
			return Code;
		}
		@JsonProperty("Code")
		public void setCode(int code) {
			Code = code;
		}
		
	}
	
}
