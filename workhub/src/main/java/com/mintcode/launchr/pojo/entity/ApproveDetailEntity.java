package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApproveDetailEntity implements Serializable{
	
	

	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5976819594146757523L;
	
	private String SHOW_ID;
	private String A_TITLE;
	private String T_SHOW_ID;
	private List<Person> A_APPROVE;
	private List<Person> A_CC;
	private List<Person> A_APPROVE_PATH;
	private int A_IS_URGENT;
	private String A_BACKUP;
	private String A_STATUS;
	private long LAST_UPDATE_TIME;
	private long A_START_TIME;
	private long A_END_TIME;
	private long A_DEADLINE;
	private double A_FEE;
	private String CREATE_USER;
	private String CREATE_USER_NAME;
	private long CREATE_TIME;
	private int IS_PROCESS;
	private int IS_TIMESLOT_ALL_DAY;
	private int IS_DEADLINE_ALL_DAY;

	private int HAS_FILE;
	private int HAS_COMMENT;

	private int IS_CAN_APPROVE;
	private int IS_CAN_MODIFY;
	private int IS_CAN_DELETE;

	private String T_WORKFLOW_ID;
	private String A_MESSAGE_FORM;
	private List<String> A_APPROVE_TRIGGERS;
	private String A_FORM_DATA;
	private String A_FORM_INSTANCE_ID;
	
//	private List<String> sA_APPROVE;
//	private List<String> sA_CC;


	public String getT_WORKFLOW_ID() {
		return T_WORKFLOW_ID;
	}
	@JsonProperty("T_WORKFLOW_ID")
	public void setT_WORKFLOW_ID(String t_WORKFLOW_ID) {
		T_WORKFLOW_ID = t_WORKFLOW_ID;
	}

	public String getA_MESSAGE_FORM() {
		return A_MESSAGE_FORM;
	}
	@JsonProperty("A_MESSAGE_FORM")
	public void setA_MESSAGE_FORM(String a_MESSAGE_FORM) {
		A_MESSAGE_FORM = a_MESSAGE_FORM;
	}

	public List<String> getA_APPROVE_TRIGGERS() {
		return A_APPROVE_TRIGGERS;
	}
	@JsonProperty("A_APPROVE_TRIGGERS")
	public void setA_APPROVE_TRIGGERS(List<String> a_APPROVE_TRIGGERS) {
		A_APPROVE_TRIGGERS = a_APPROVE_TRIGGERS;
	}

	public String getA_FORM_DATA() {
		return A_FORM_DATA;
	}
	@JsonProperty("A_FORM_DATA")
	public void setA_FORM_DATA(String a_FORM_DATA) {
		A_FORM_DATA = a_FORM_DATA;
	}

	public String getA_FORM_INSTANCE_ID() {
		return A_FORM_INSTANCE_ID;
	}
	@JsonProperty("A_FORM_INSTANCE_ID")
	public void setA_FORM_INSTANCE_ID(String a_FORM_INSTANCE_ID) {
		A_FORM_INSTANCE_ID = a_FORM_INSTANCE_ID;
	}

	public int getIS_DEADLINE_ALL_DAY() {
		return IS_DEADLINE_ALL_DAY;
	}
	@JsonProperty("IS_DEADLINE_ALL_DAY")
	public void setIS_DEADLINE_ALL_DAY(int iS_DEADLINE_ALL_DAY) {
		IS_DEADLINE_ALL_DAY = iS_DEADLINE_ALL_DAY;
	}
	public int getIS_CAN_MODIFY() {
		return IS_CAN_MODIFY;
	}
	@JsonProperty("IS_CAN_MODIFY")
	public void setIS_CAN_MODIFY(int iS_CAN_MODIFY) {
		IS_CAN_MODIFY = iS_CAN_MODIFY;
	}
	public int getIS_CAN_DELETE() {
		return IS_CAN_DELETE;
	}
	@JsonProperty("IS_CAN_DELETE")
	public void setIS_CAN_DELETE(int iS_CAN_DELETE) {
		IS_CAN_DELETE = iS_CAN_DELETE;
	}
	public int getHAS_FILE() {
		return HAS_FILE;
	}
	@JsonProperty("HAS_FILE")
	public void setHAS_FILE(int hAS_FILE) {
		HAS_FILE = hAS_FILE;
	}

	public int getHAS_COMMENT() {
		return HAS_COMMENT;
	}
	@JsonProperty("HAS_COMMENT")
	public void setHAS_COMMENT(int hAS_COMMENT) {
		HAS_COMMENT = hAS_COMMENT;
	}
	public int getIS_CAN_APPROVE() {
		return IS_CAN_APPROVE;
	}
	@JsonProperty("IS_CAN_APPROVE")
	public void setIS_CAN_APPROVE(int iS_CAN_APPROVE) {
		IS_CAN_APPROVE = iS_CAN_APPROVE;
	}
	
	public String getSHOW_ID() {
		return SHOW_ID;
	}
	
	@JsonProperty("SHOW_ID")
	public void setSHOW_ID(String sHOW_ID) {
		SHOW_ID = sHOW_ID;
	}
	public String getA_TITLE() {
		return A_TITLE;
	}
	@JsonProperty("A_TITLE")
	public void setA_TITLE(String a_TITLE) {
		A_TITLE = a_TITLE;
	}
	public String getT_SHOW_ID() {
		return T_SHOW_ID;
	}
	@JsonProperty("T_SHOW_ID")
	public void setT_SHOW_ID(String t_SHOW_ID) {
		T_SHOW_ID = t_SHOW_ID;
	}
	public List<Person> getA_APPROVE() {
		return A_APPROVE;
	}
	@JsonProperty("A_APPROVE")
	public void setA_APPROVE(List<Person> a_APPROVE) {
		A_APPROVE = a_APPROVE;
	}
	
	public List<Person> getA_CC() {
		return A_CC;
	}
	@JsonProperty("A_CC")
	public void setA_CC(List<Person> a_CC) {
		A_CC = a_CC;
	}
	
	public List<Person> getA_APPROVE_PATH() {
		return A_APPROVE_PATH;
	}
	@JsonProperty("A_APPROVE_PATH")
	public void setA_APPROVE_PATH(List<Person> a_APPROVE_PATH) {
		A_APPROVE_PATH = a_APPROVE_PATH;
	}
	
	public int getA_IS_URGENT() {
		return A_IS_URGENT;
	}
	@JsonProperty("A_IS_URGENT")
	public void setA_IS_URGENT(int a_IS_URGENT) {
		A_IS_URGENT = a_IS_URGENT;
	}
	public String getA_BACKUP() {
		return A_BACKUP;
	}
	@JsonProperty("A_BACKUP")
	public void setA_BACKUP(String a_BACKUP) {
		A_BACKUP = a_BACKUP;
	}
	public String getA_STATUS() {
		return A_STATUS;
	}
	@JsonProperty("A_STATUS")
	public void setA_STATUS(String a_STATUS) {
		A_STATUS = a_STATUS;
	}
	public long getLAST_UPDATE_TIME() {
		return LAST_UPDATE_TIME;
	}
	@JsonProperty("LAST_UPDATE_TIME")
	public void setLAST_UPDATE_TIME(long lAST_UPDATE_TIME) {
		LAST_UPDATE_TIME = lAST_UPDATE_TIME;
	}
	public long getA_START_TIME() {
		return A_START_TIME;
	}
	@JsonProperty("A_START_TIME")
	public void setA_START_TIME(long a_START_TIME) {
		A_START_TIME = a_START_TIME;
	}
	public long getA_END_TIME() {
		return A_END_TIME;
	}
	@JsonProperty("A_END_TIME")
	public void setA_END_TIME(long a_END_TIME) {
		A_END_TIME = a_END_TIME;
	}
	public long getA_DEADLINE() {
		return A_DEADLINE;
	}
	@JsonProperty("A_DEADLINE")
	public void setA_DEADLINE(long a_DEADLINE) {
		A_DEADLINE = a_DEADLINE;
	}
	public double getA_FEE() {
		return A_FEE;
	}
	@JsonProperty("A_FEE")
	public void setA_FEE(double a_FEE) {
		A_FEE = a_FEE;
	}
	public String getCREATE_USER() {
		return CREATE_USER;
	}
	@JsonProperty("CREATE_USER")
	public void setCREATE_USER(String cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}
	public String getCREATE_USER_NAME() {
		return CREATE_USER_NAME;
	}
	@JsonProperty("CREATE_USER_NAME")
	public void setCREATE_USER_NAME(String cREATE_USER_NAME) {
		CREATE_USER_NAME = cREATE_USER_NAME;
	}
	public long getCREAT_TIME() {

		return CREATE_TIME;
	}
	@JsonProperty("CREATE_TIME")
	public void setCREAT_TIME(long cREAT_TIME) {
		CREATE_TIME = cREAT_TIME;
	}
	public int getIS_PROCESS() {
		return IS_PROCESS;
	}
	@JsonProperty("IS_PROCESS")
	public void setIS_PROCESS(int iS_PROCESS) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
		IS_PROCESS = iS_PROCESS;
	}
	public int getIS_TIMESLOT_ALL_DAY() {
		return IS_TIMESLOT_ALL_DAY;
	}
	@JsonProperty("IS_TIMESLOT_ALL_DAY")
	public void setIS_TIMESLOT_ALL_DAY(int iS_TIMESLOT_ALL_DAY) {
		IS_TIMESLOT_ALL_DAY = iS_TIMESLOT_ALL_DAY;
	}
	
	public static class Person implements Serializable{

		

		/**
		 * 
		 */
		private static final long serialVersionUID = 5047062662337965166L;
		
		private String USER;
		
		private String USER_NAME;
		
		
		public String getUSER() {
			return USER;
		}
		@JsonProperty("USER")
		public void setUSER(String uSER) {
			USER = uSER;
		}

		public String getUSER_NAME() {
			return USER_NAME;
		}
		@JsonProperty("USER_NAME")
		public void setUSER_NAME(String uSER_NAME) {
			USER_NAME = uSER_NAME;
		}
		
	}
}
