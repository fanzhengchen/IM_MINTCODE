package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApproveEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7235026109232053205L;

	private String SHOW_ID;
	private String A_TITLE;
	private String T_SHOW_ID;
	private String T_NAME;
	private String A_APPROVE;
	private String A_APPROVE_NAME;
	private String A_CC;
	private String A_CC_NAME;
	private String A_APPROVE_PATH;
	private String A_APPROVE_PATH_NAME;
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
	private List<String> fileShowIds;
	private int HAS_FILE;
	private int HAS_COMMENT;
	private int IS_DEADLINE_ALL_DAY;
	private String T_GET_TYPE;
	private int isCanApprove;
	private int isCanModify;
	private int isCanDelete;
	
//	private List<String> sA_APPROVE;
//	private List<String> sA_CC;
    public int getIsCanApprove(){return isCanApprove;}
	@JsonProperty("IS_CAN_APPROVE")
	public void setIsCanApprove(int isCanApprove){
		this.isCanModify = isCanApprove;
	}
	public int getIsCanModify(){return isCanModify;}
	@JsonProperty("IS_CAN_MODIFY")
	public void setIsCanModify(int isCanModify){
		this.isCanModify = isCanModify;
	}
	public int getIsCanDelete(){return isCanDelete;}
	@JsonProperty("IS_CAN_DELETE")
	public void setIsCanDelete(int isCanDelete){
		this.isCanDelete = isCanDelete;
	}

	public int getIS_DEADLINE_ALL_DAY() {
		return IS_DEADLINE_ALL_DAY;
	}
	@JsonProperty("IS_DEADLINE_ALL_DAY")
	public void setIS_DEADLINE_ALL_DAY(int iS_DEADLINE_ALL_DAY) {
		IS_DEADLINE_ALL_DAY = iS_DEADLINE_ALL_DAY;
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
	public List<String> getFileShowIds() {
		return fileShowIds;
	}
	@JsonProperty("fileShowIds")
	public void setFileShowIds(List<String> fileShowIds) {
		this.fileShowIds = fileShowIds;
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
	@JsonProperty("T_NAME")
	public void setT_NAME(String t_NAME) {
		T_NAME = t_NAME;
	}
	public String getT_NAME() {
		return T_NAME;
	}
	public void setT_GET_TYPE(String t_GET_TYPE) {
		T_GET_TYPE = t_GET_TYPE;
	}
	public String getT_GET_TYPE() {
		return T_GET_TYPE;
	}
	@JsonProperty("T_SHOW_ID")
	public void setT_SHOW_ID(String t_SHOW_ID) {
		T_SHOW_ID = t_SHOW_ID;
	}
	public String getA_APPROVE() {
		return A_APPROVE;
	}
	@JsonProperty("A_APPROVE")
	public void setA_APPROVE(String a_APPROVE) {
		A_APPROVE = a_APPROVE;
	}
	public String getA_APPROVE_NAME() {
		return A_APPROVE_NAME;
	}
	@JsonProperty("A_APPROVE_NAME")
	public void setA_APPROVE_NAME(String a_APPROVE_NAME) {
		A_APPROVE_NAME = a_APPROVE_NAME;
	}
	public String getA_CC() {
		return A_CC;
	}
	@JsonProperty("A_CC")
	public void setA_CC(String a_CC) {
		A_CC = a_CC;
	}
	public String getA_CC_NAME() {
		return A_CC_NAME;
	}
	@JsonProperty("A_CC_NAME")
	public void setA_CC_NAME(String a_CC_NAME) {
		A_CC_NAME = a_CC_NAME;
	}
	public String getA_APPROVE_PATH() {
		return A_APPROVE_PATH;
	}
	@JsonProperty("A_APPROVE_PATH")
	public void setA_APPROVE_PATH(String a_APPROVE_PATH) {
		A_APPROVE_PATH = a_APPROVE_PATH;
	}
	public String getA_APPROVE_PATH_NAME() {
		return A_APPROVE_PATH_NAME;
	}
	@JsonProperty("A_APPROVE_PATH_NAME")
	public void setA_APPROVE_PATH_NAME(String a_APPROVE_PATH_NAME) {
		A_APPROVE_PATH_NAME = a_APPROVE_PATH_NAME;
	}
	@JsonProperty("A_IS_URGENT")
	public int getA_IS_URGENT(){
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
	
	
//	public List<String> getsA_APPROVE() {
//		return sA_APPROVE;
//	}
//	@JsonProperty("A_APPROVE")
//	public void setsA_APPROVE(List<String> A_APPROVE) {
//		this.sA_APPROVE = A_APPROVE;
//	}
//
//	public List<String> getsA_CC() {
//		return sA_CC;
//	}
//	@JsonProperty("A_CC")
//	public void setsA_CC(List<String> A_CC) {
//		this.sA_CC = A_CC;
//	}
}
