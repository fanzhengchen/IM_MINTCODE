package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingEntity implements Serializable{

	/** */
	private static final long serialVersionUID = 1L;

	public int M_ID;
	
	public String SHOW_ID;
	
	private String M_TITLE;
	
	private String M_CONTENT;
	
	private long M_START;
	
	private long M_END;
	
	private String R_SHOW_ID;
	
	private String R_SHOW_NAME;
	
	private String M_EXTERNAL;
	
	private String M_LNGX;
	
	private String M_LATY;
	
	private String M_REQUIRE_JOIN;
	
	private String M_REQUIRE_JOIN_NAME;
	
	private String M_JOIN;
	
	private String M_JOIN_NAME;
	
	private String C_SHOW_ID;
	
	private String CREATE_USER;
	
	private String CREATE_USER_NAME;
	
	private long CREATE_TIME;
	
	private int M_RESTART_TYPE;
	
	private int M_REMIND_TYPE;

	private int O_IS_AGREE;
	
	public int getM_ID() {
		return M_ID;
	}
	
	@JsonProperty("M_ID")
	public void setM_ID(int m_ID) {
		M_ID = m_ID;
	}

	public String getSHOW_ID() {
		return SHOW_ID;
	}

	@JsonProperty("SHOW_ID")
	public void setSHOW_ID(String sHOW_ID) {
		SHOW_ID = sHOW_ID;
	}

	public String getM_TITLE() {
		return M_TITLE;
	}

	@JsonProperty("M_TITLE")
	public void setM_TITLE(String m_TITLE) {
		M_TITLE = m_TITLE;
	}

	public String getM_CONTENT() {
		return M_CONTENT;
	}

	@JsonProperty("M_CONTENT")
	public void setM_CONTENT(String m_CONTENT) {
		M_CONTENT = m_CONTENT;
	}

	public long getM_START() {
		return M_START;
	}

	@JsonProperty("M_START")
	public void setM_START(long m_START) {
		M_START = m_START;
	}

	public long getM_END() {
		return M_END;
	}

	@JsonProperty("M_END")
	public void setM_END(long m_END) {
		M_END = m_END;
	}

	public String getR_SHOW_ID() {
		return R_SHOW_ID;
	}

	@JsonProperty("R_SHOW_ID")
	public void setR_SHOW_ID(String r_SHOW_ID) {
		R_SHOW_ID = r_SHOW_ID;
	}

	public String getR_SHOW_NAME() {
		return R_SHOW_NAME;
	}

	@JsonProperty("R_SHOW_NAME")
	public void setR_SHOW_NAME(String r_SHOW_NAME) {
		R_SHOW_NAME = r_SHOW_NAME;
	}

	public String getM_EXTERNAL() {
		return M_EXTERNAL;
	}

	@JsonProperty("M_EXTERNAL")
	public void setM_EXTERNAL(String m_EXTERNAL) {
		M_EXTERNAL = m_EXTERNAL;
	}

	public String getM_LNGX() {
		return M_LNGX;
	}

	@JsonProperty("M_LNGX")
	public void setM_LNGX(String m_LNGX) {
		M_LNGX = m_LNGX;
	}

	public String getM_LATY() {
		return M_LATY;
	}

	@JsonProperty("M_LATY")
	public void setM_LATY(String m_LATY) {
		M_LATY = m_LATY;
	}

	public String getM_REQUIRE_JOIN() {
		return M_REQUIRE_JOIN;
	}

	@JsonProperty("M_REQUIRE_JOIN")
	public void setM_REQUIRE_JOIN(String m_REQUIRE_JOIN) {
		M_REQUIRE_JOIN = m_REQUIRE_JOIN;
	}

	public String getM_REQUIRE_JOIN_NAME() {
		return M_REQUIRE_JOIN_NAME;
	}

	@JsonProperty("M_REQUIRE_JOIN_NAME")
	public void setM_REQUIRE_JOIN_NAME(String m_REQUIRE_JOIN_NAME) {
		M_REQUIRE_JOIN_NAME = m_REQUIRE_JOIN_NAME;
	}

	public String getM_JOIN() {
		return M_JOIN;
	}

	@JsonProperty("M_JOIN")
	public void setM_JOIN(String m_JOIN) {
		M_JOIN = m_JOIN;
	}

	public String getM_JOIN_NAME() {
		return M_JOIN_NAME;
	}
	
	@JsonProperty("M_JOIN_NAME")
	public void setM_JOIN_NAME(String m_JOIN_NAME) {
		M_JOIN_NAME = m_JOIN_NAME;
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

	public int getM_RESTART_TYPE() {
		return M_RESTART_TYPE;
	}

	@JsonProperty("M_RESTART_TYPE")
	public void setM_RESTART_TYPE(int m_RESTART_TYPE) {
		M_RESTART_TYPE = m_RESTART_TYPE;
	}

	public int getM_REMIND_TYPE() {
		return M_REMIND_TYPE;
	}

	@JsonProperty("M_REMIND_TYPE")
	public void setM_REMIND_TYPE(int m_REMIND_TYPE) {
		M_REMIND_TYPE = m_REMIND_TYPE;
	}
	
	
	
	
	
	
	
}
