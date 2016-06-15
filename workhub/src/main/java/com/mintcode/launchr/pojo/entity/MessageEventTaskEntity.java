package com.mintcode.launchr.pojo.entity;


public class MessageEventTaskEntity extends MessageEventEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6502134579842932897L;
	private String msgInfo;

	public static class MessageEventTaskBackupEntity extends MessageEventEntity{
		private EventTaskCard msgInfo;

		public EventTaskCard getMsgInfo() {
			return msgInfo;
		}

		public void setMsgInfo(EventTaskCard msgInfo) {
			this.msgInfo = msgInfo;
		}

	}

	public static class EventTaskCard{
		private String title;
		private long end;
		private String projectName;
		private String content;
		private String priority;
		private int level;
		private String id;
		private int allTask;
		private int finishTask;
		private String stateName;
		private String stateType;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public long getEnd() {
			return end;
		}
		public void setEnd(long end) {
			this.end = end;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public int getAllTask() {
			return allTask;
		}
		public void setAllTask(int allTask) {
			this.allTask = allTask;
		}
		public int getFinishTask() {
			return finishTask;
		}
		public void setFinishTask(int finishTask) {
			this.finishTask = finishTask;
		}
		public String getStateName() {
			return stateName;
		}
		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getStateType() {
			return stateType;
		}

		public void setStateType(String stateType) {
			this.stateType = stateType;
		}
	}

//	public EventTaskCard getMsgInfo() {
//		return msgInfo;
//	}

//	public void setMsgInfo(EventTaskCard msgInfo) {
//		this.msgInfo = msgInfo;
//	}

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

















