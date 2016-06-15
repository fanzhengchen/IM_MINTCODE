package com.mintcode.launchr.pojo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.app.newApproval.Entity.MessageFormData;

import java.util.List;

public class MessageEventApproveEntity extends MessageEventEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4086622913508199151L;
	private String msgInfo;
	
	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public static class ApproveCard{
		private String title;
		private String id;
		private String approvalTypeShowID;
		private String approvalType;
		private String approvalStatus;
		private List<MessageFormData> approvalForm;
		private Object approvalFormData;

//		private long start;
//		private long end;
//		private long deadline;
//		private int fee;
//		private int isDeadlineAllday;
//		private int isTimeslotAllday;
//		private String backup;
//		private String approvalShowID;


		public String getApprovalTypeShowID() {
			return approvalTypeShowID;
		}
		@JsonProperty("approvalTypeShowID")
		public void setApprovalTypeShowID(String approvalTypeShowID) {
			this.approvalTypeShowID = approvalTypeShowID;
		}

		public List<MessageFormData> getApprovalForm() {
			return approvalForm;
		}
		@JsonProperty("approvalForm")
		public void setApprovalForm(List<MessageFormData> approvalForm) {
			this.approvalForm = approvalForm;
		}

		public Object getApprovalFormData() {
			return approvalFormData;
		}
		@JsonProperty("approvalFormData")
		public void setApprovalFormData(Object approvalFormData) {
			this.approvalFormData = approvalFormData;
		}
		public String getTitle() {
			return title;
		}
		@JsonProperty("title")
		public void setTitle(String title) {
			this.title = title;
		}
		public String getId() {
			return id;
		}
		@JsonProperty("id")
		public void setId(String id) {
			this.id = id;
		}
		public String getApprovalType() {
			return approvalType;
		}
		@JsonProperty("approvalType")
		public void setApprovalType(String approvalType) {
			this.approvalType = approvalType;
		}
		public String getApprovalStatus() {
			return approvalStatus;
		}
		@JsonProperty("approvalStatus")
		public void setApprovalStatus(String approvalStatus) {
			this.approvalStatus = approvalStatus;
		}

//		public long getStart() {
//			return start;
//		}
//		public void setStart(long start) {
//			this.start = start;
//		}
//		public long getEnd() {
//			return end;
//		}
//		public void setEnd(long end) {
//			this.end = end;
//		}
//		public long getDeadline() {
//			return deadline;
//		}
//		public void setDeadline(long deadline) {
//			this.deadline = deadline;
//		}
//		public int getFee() {
//			return fee;
//		}
//		public void setFee(int fee) {
//			this.fee = fee;
//		}
//		public int getIsDeadlineAllday() {
//			return isDeadlineAllday;
//		}
//		public void setIsDeadlineAllday(int isDeadlineAllday) {
//			this.isDeadlineAllday = isDeadlineAllday;
//		}
//		public int getIsTimeslotAllday() {
//			return isTimeslotAllday;
//		}
//		public void setIsTimeslotAllday(int isTimeslotAllday) {
//			this.isTimeslotAllday = isTimeslotAllday;
//		}
//		public String getBackup() {
//			return backup;
//		}
//		public void setBackup(String backup) {
//			this.backup = backup;
//		}
//
//		public String getApprovalShowID() {
//			return approvalShowID;
//		}
//		public void setApprovalShowID(String approvalShowID) {
//			this.approvalShowID = approvalShowID;
//		}


	}
//	@JsonProperty("createUserName")
//	public void setCreateUserName(String createUserName) {
//		this.createUserName = createUserName;
//	}
}

















