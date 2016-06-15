package com.mintcode.launchr.upload_download;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UplaodResponsePOJO {

	private Header Header;
	private Body Body;

	public Header getHeader() {
		return Header;
	}

	@JsonProperty("Header")
	public void setHeader(Header header) {
		Header = header;
	}

	public Body getBody() {
		return Body;
	}

	@JsonProperty("Body")
	public void setBody(Body body) {
		Body = body;
	}

	public static class Header {
		private boolean IsSuccess;
		private int ResCode;
		private String Reason;

		public boolean isIsSuccess() {
			return IsSuccess;
		}

		@JsonProperty("IsSuccess")
		public void setIsSuccess(boolean isSuccess) {
			IsSuccess = isSuccess;
		}

		public int getResCode() {
			return ResCode;
		}

		@JsonProperty("ResCode")
		public void setResCode(int resCode) {
			ResCode = resCode;
		}

		public String getReason() {
			return Reason;
		}

		@JsonProperty("Reason")
		public void setReason(String reason) {
			Reason = reason;
		}
	}

	public static class Body {

		private Response response;

		public Response getResponse() {
			return response;
		}

		@JsonProperty("response")
		public void setResponse(Response response) {
			this.response = response;
		}

		public static class Response {
			private boolean IsSuccess;
			private String Reason;
			private int TotalCount;
			private Data Data;

			public boolean isIsSuccess() {
				return IsSuccess;
			}

			@JsonProperty("IsSuccess")
			public void setIsSuccess(boolean isSuccess) {
				IsSuccess = isSuccess;
			}

			public String getReason() {
				return Reason;
			}

			@JsonProperty("Reason")
			public void setReason(String reason) {
				Reason = reason;
			}

			public int getTotalCount() {
				return TotalCount;
			}

			@JsonProperty("TotalCount")
			public void setTotalCount(int totalCount) {
				TotalCount = totalCount;
			}

			public Data getData() {
				return Data;
			}

			@JsonProperty("Data")
			public void setData(Data data) {
				Data = data;
			}

			public static class Data {
				private String ShowID;
				private String title;
				private String path;
				private int size;

				public String getShowID() {
					return ShowID;
				}

				@JsonProperty("ShowID")
				public void setShowID(String showID) {
					ShowID = showID;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getPath() {
					return path;
				}

				public void setPath(String path) {
					this.path = path;
				}

				public int getSize() {
					return size;
				}

				public void setSize(int size) {
					this.size = size;
				}
			}
		}
	}
}
