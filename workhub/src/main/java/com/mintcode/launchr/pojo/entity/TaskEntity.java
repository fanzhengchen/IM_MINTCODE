package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author StephenHe 2015/9/17
 * 适用于解析：删除子任务，更新任务转化为父任务，
 */
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String SHOW_ID;
	private String P_SHOW_ID;
	private String T_TITLE;
	private String T_PRIORITY;
	private List<String> T_USERS;
	private List<String> T_USERS_NAME;
	private long T_END_TIME;
	private String T_PARENT_SHOW_ID;
	private int T_LEVEL;
	private int T_IS_ANNEX;
	private int T_IS_REPEAT;
	private int T_RESTART_TYPE;
	private long T_REMIND_TIME;
	private String T_BACKUP;
	private String S_SHOW_ID;
	private long LAST_UPDATE_TIME;
	private String C_SHOW_ID;
	private String CREATE_USER;
	private String CREATE_USER_NAME;
	private long CREATE_TIME;
	
	private List<String> MAIN_TASK_USERS;
	private List<String> MAIN_TASK_USERNAMES;
	private String MAIN_TASK_CREATE_USER;
	private String MAIN_TASK_CREATE_USER_NAME;
	
	public String getSHOW_ID() {
		return SHOW_ID;
	}
	
	@JsonProperty("SHOW_ID")
	public void setSHOW_ID(String sHOW_ID) {
		SHOW_ID = sHOW_ID;
	}
	public String getP_SHOW_ID() {
		return P_SHOW_ID;
	}
	
	@JsonProperty("P_SHOW_ID")
	public void setP_SHOW_ID(String p_SHOW_ID) {
		P_SHOW_ID = p_SHOW_ID;
	}
	public String getT_TITLE() {
		return T_TITLE;
	}
	
	@JsonProperty("T_TITLE")
	public void setT_TITLE(String t_TITLE) {
		T_TITLE = t_TITLE;
	}
	public String getT_PRIORITY() {
		return T_PRIORITY;
	}
	
	@JsonProperty("T_PRIORITY")
	public void setT_PRIORITY(String t_PRIORITY) {
		T_PRIORITY = t_PRIORITY;
	}
	public List<String> getT_USERS() {
		return T_USERS;
	}
	
	@JsonProperty("")
	public void setT_USERS(List<String> t_USERS) {
		T_USERS = t_USERS;
	}
	public List<String> getT_USERS_NAME() {
		return T_USERS_NAME;
	}
	
	@JsonProperty("T_USERS_NAME")
	public void setT_USERS_NAME(List<String> t_USERS_NAME) {
		T_USERS_NAME = t_USERS_NAME;
	}
	public long getT_END_TIME() {
		return T_END_TIME;
	}
	
	@JsonProperty("T_END_TIME")
	public void setT_END_TIME(long t_END_TIME) {
		T_END_TIME = t_END_TIME;
	}
	public String getT_PARENT_SHOW_ID() {
		return T_PARENT_SHOW_ID;
	}
	
	@JsonProperty("T_PARENT_SHOW_ID")
	public void setT_PARENT_SHOW_ID(String t_PARENT_SHOW_ID) {
		T_PARENT_SHOW_ID = t_PARENT_SHOW_ID;
	}
	public int getT_LEVEL() {
		return T_LEVEL;
	}
	
	@JsonProperty("T_LEVEL")
	public void setT_LEVEL(int t_LEVEL) {
		T_LEVEL = t_LEVEL;
	}
	public int getT_IS_ANNEX() {
		return T_IS_ANNEX;
	}
	
	@JsonProperty("T_IS_ANNEX")
	public void setT_IS_ANNEX(int t_IS_ANNEX) {
		T_IS_ANNEX = t_IS_ANNEX;
	}
	public int getT_IS_REPEAT() {
		return T_IS_REPEAT;
	}
	
	@JsonProperty("T_IS_REPEAT")
	public void setT_IS_REPEAT(int t_IS_REPEAT) {
		T_IS_REPEAT = t_IS_REPEAT;
	}
	public int getT_RESTART_TYPE() {
		return T_RESTART_TYPE;
	}
	
	@JsonProperty("T_RESTART_TYPE")
	public void setT_RESTART_TYPE(int t_RESTART_TYPE) {
		T_RESTART_TYPE = t_RESTART_TYPE;
	}
	public long getT_REMIND_TIME() {
		return T_REMIND_TIME;
	}
	
	@JsonProperty("T_REMIND_TIME")
	public void setT_REMIND_TIME(long t_REMIND_TIME) {
		T_REMIND_TIME = t_REMIND_TIME;
	}
	public String getT_BACKUP() {
		return T_BACKUP;
	}
	
	@JsonProperty("T_BACKUP")
	public void setT_BACKUP(String t_BACKUP) {
		T_BACKUP = t_BACKUP;
	}
	public String getS_SHOW_ID() {
		return S_SHOW_ID;
	}
	
	@JsonProperty("S_SHOW_ID")
	public void setS_SHOW_ID(String s_SHOW_ID) {
		S_SHOW_ID = s_SHOW_ID;
	}
	public long getLAST_UPDATE_TIME() {
		return LAST_UPDATE_TIME;
	}
	
	@JsonProperty("LAST_UPDATE_TIME")
	public void setLAST_UPDATE_TIME(long lAST_UPDATE_TIME) {
		LAST_UPDATE_TIME = lAST_UPDATE_TIME;
	}
	public String getC_SHOW_ID() {
		return C_SHOW_ID;
	}
	
	@JsonProperty("C_SHOW_ID")
	public void setC_SHOW_ID(String c_SHOW_ID) {
		C_SHOW_ID = c_SHOW_ID;
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
	public long getCREATE_TIME() {
		return CREATE_TIME;
	}
	
	@JsonProperty("CREATE_TIME")
	public void setCREATE_TIME(long cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public List<String> getMAIN_TASK_USERS() {
		return MAIN_TASK_USERS;
	}
	
	@JsonProperty("MAIN_TASK_USERS")
	public void setMAIN_TASK_USERS(List<String> mAIN_TASK_USERS) {
		MAIN_TASK_USERS = mAIN_TASK_USERS;
	}
	public List<String> getMAIN_TASK_USERNAMES() {
		return MAIN_TASK_USERNAMES;
	}
	
	@JsonProperty("MAIN_TASK_USERNAMES")
	public void setMAIN_TASK_USERNAMES(List<String> mAIN_TASK_USERNAMES) {
		MAIN_TASK_USERNAMES = mAIN_TASK_USERNAMES;
	}
	public String getMAIN_TASK_CREATE_USER() {
		return MAIN_TASK_CREATE_USER;
	}
	
	@JsonProperty("MAIN_TASK_CREATE_USER")
	public void setMAIN_TASK_CREATE_USER(String mAIN_TASK_CREATE_USER) {
		MAIN_TASK_CREATE_USER = mAIN_TASK_CREATE_USER;
	}
	public String getMAIN_TASK_CREATE_USER_NAME() {
		return MAIN_TASK_CREATE_USER_NAME;
	}
	
	@JsonProperty("MAIN_TASK_CREATE_USER_NAME")
	public void setMAIN_TASK_CREATE_USER_NAME(String mAIN_TASK_CREATE_USER_NAME) {
		MAIN_TASK_CREATE_USER_NAME = mAIN_TASK_CREATE_USER_NAME;
	}
	
}
