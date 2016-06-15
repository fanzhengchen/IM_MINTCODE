package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEntity implements Serializable{

	private String U_SHOW_ID;
	
	private String LAST_LOGIN_TOKEN;
	
	private String U_TRUE_NAME;
	
	private String U_NAME;
	
	private String C_SHOW_ID;
	
	private String C_CODE;

	public String getU_SHOW_ID() {
		return U_SHOW_ID;
	}

	@JsonProperty("U_SHOW_ID")
	public void setU_SHOW_ID(String u_SHOW_ID) {
		U_SHOW_ID = u_SHOW_ID;
	}

	public String getLAST_LOGIN_TOKEN() {
		return LAST_LOGIN_TOKEN;
	}

	@JsonProperty("LAST_LOGIN_TOKEN")
	public void setLAST_LOGIN_TOKEN(String lAST_LOGIN_TOKEN) {
		LAST_LOGIN_TOKEN = lAST_LOGIN_TOKEN;
	}

	public String getU_TRUE_NAME() {
		return U_TRUE_NAME;
	}

	@JsonProperty("U_TRUE_NAME")
	public void setU_TRUE_NAME(String u_TRUE_NAME) {
		U_TRUE_NAME = u_TRUE_NAME;
	}

	public String getU_NAME() {
		return U_NAME;
	}

	@JsonProperty("U_NAME")
	public void setU_NAME(String u_NAME) {
		U_NAME = u_NAME;
	}

	public String getC_SHOW_ID() {
		return C_SHOW_ID;
	}

	@JsonProperty("C_SHOW_ID")
	public void setC_SHOW_ID(String c_SHOW_ID) {
		C_SHOW_ID = c_SHOW_ID;
	}

	public String getC_CODE() {
		return C_CODE;
	}

	@JsonProperty("C_CODE")
	public void setC_CODE(String c_CODE) {
		C_CODE = c_CODE;
	}
	
	
}
