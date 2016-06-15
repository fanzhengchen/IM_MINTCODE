package com.mintcode.launchr.pojo.entity;


public class MessageEventScheduleEntity extends MessageEventEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571793535115161240L;
	private String msgInfo;
	
	public static class ScheduleCard{
		private String title;
		private long start;
		private long end;
		private String roomName;
		private String id;
		private String content;
		private String external;
		private int isAgree;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public long getStart() {
			return start;
		}
		public void setStart(long start) {
			this.start = start;
		}
		public long getEnd() {
			return end;
		}
		public void setEnd(long end) {
			this.end = end;
		}
		public String getRoomName() {
			return roomName;
		}
		public void setRoomName(String roomName) {
			this.roomName = roomName;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getExternal() {
			return external;
		}
		public void setExternal(String external) {
			this.external = external;
		}

		public int getIsAgree() {
			return isAgree;
		}

		public void setIsAgree(int isAgree) {
			this.isAgree = isAgree;
		}
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}
	
	
//	@JsonProperty("createUserName")
//	public void setCreateUserName(String createUserName) {
//		this.createUserName = createUserName;
//	}
}

















