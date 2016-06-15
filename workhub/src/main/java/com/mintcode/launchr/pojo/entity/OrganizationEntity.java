package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 组织架构实体
 * @author KevinQiao
 *
 */
public class OrganizationEntity implements Serializable{

	private String SHOW_ID;
	
	private String D_NAME;
	
	private String D_PARENT_NAME;
	
	private String D_PARENTID_SHOW_ID;
	
	private String C_NAME;
	
	private String CREATE_USER_NAME;
	
	private String D_SORT;

	public String getSHOW_ID() {
		return SHOW_ID;
	}

	@JsonProperty("SHOW_ID")
	public void setSHOW_ID(String sHOW_ID) {
		SHOW_ID = sHOW_ID;
	}

	public String getD_NAME() {
		return D_NAME;
	}

	@JsonProperty("D_NAME")
	public void setD_NAME(String d_NAME) {
		D_NAME = d_NAME;
	}

	public String getD_PARENT_NAME() {
		return D_PARENT_NAME;
	}

	@JsonProperty("D_PARENT_NAME")
	public void setD_PARENT_NAME(String d_PARENT_NAME) {
		D_PARENT_NAME = d_PARENT_NAME;
	}

	public String getD_PARENTID_SHOW_ID() {
		return D_PARENTID_SHOW_ID;
	}

	@JsonProperty("D_PARENTID_SHOW_ID")
	public void setD_PARENTID_SHOW_ID(String d_PARENTID_SHOW_ID) {
		D_PARENTID_SHOW_ID = d_PARENTID_SHOW_ID;
	}

	public String getC_NAME() {
		return C_NAME;
	}

	@JsonProperty("C_NAME")
	public void setC_NAME(String c_NAME) {
		C_NAME = c_NAME;
	}

	public String getCREATE_USER_NAME() {
		return CREATE_USER_NAME;
	}

	@JsonProperty("CREATE_USER_NAME")
	public void setCREATE_USER_NAME(String cREATE_USER_NAME) {
		CREATE_USER_NAME = cREATE_USER_NAME;
	}

	public String getD_SORT() {
		return D_SORT;
	}

	@JsonProperty("D_SORT")
	public void setD_SORT(String d_SORT) {
		D_SORT = d_SORT;
	}
	
	
}
