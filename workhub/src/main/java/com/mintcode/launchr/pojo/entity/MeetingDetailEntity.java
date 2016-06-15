package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingDetailEntity implements Serializable{

	private String SHOW_ID;
	
	private String M_TITLE;
	
	private String M_CONTENT;
	
	private long M_START;
	
	private long M_END;
	
	private String R_SHOW_ID;
	
	private String M_EXTERNAL;
	
	private String M_LNGX;
	
	private String M_LATY;
	
	private String C_SHOW_ID;
	
	
	private String R_SHOW_NAME;
	
	private String CREATE_USER;
	
	private String CREATE_USER_NAME;
	
	private List<RequireEntity> REQUIRE_JOIN;
	
	private List<RequireEntity> JOIN;
	
	private List<JoinEntity> REQUIRE_JOIN_NAME;
	
	private List<JoinEntity> JOIN_NAME;
	
	private long CREATE_TIME;
	
	private int  M_RESTART_TYPE;
	
	private int M_REMIND_TYPE;

	private boolean M_SHOW_JOIN_BUTTON;



	private int M_IS_VISIBLE;

	private int M_IS_CANCEL;
	private String M_REASON;

	public int getM_IS_CANCEL() {
		return M_IS_CANCEL;
	}
	@JsonProperty("M_IS_CANCEL")
	public void setM_IS_CANCEL(int m_IS_CANCEL) {
		M_IS_CANCEL = m_IS_CANCEL;
	}

	public String getM_REASON() {
		return M_REASON;
	}
	@JsonProperty("M_REASON")
	public void setM_REASON(String m_REASON) {
		M_REASON = m_REASON;
	}

	public int getM_IS_VISIBLE() {
		return M_IS_VISIBLE;
	}
	@JsonProperty("M_IS_VISIBLE")
	public void setM_IS_VISIBLE(int m_IS_VISIBLE) {
		M_IS_VISIBLE = m_IS_VISIBLE;
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

	public String getC_SHOW_ID() {
		return C_SHOW_ID;
	}

	@JsonProperty("C_SHOW_ID")
	public void setC_SHOW_ID(String c_SHOW_ID) {
		C_SHOW_ID = c_SHOW_ID;
	}

	public String getR_SHOW_NAME() {
		return R_SHOW_NAME;
	}

	@JsonProperty("R_SHOW_NAME")
	public void setR_SHOW_NAME(String r_SHOW_NAME) {
		R_SHOW_NAME = r_SHOW_NAME;
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

	public List<RequireEntity> getREQUIRE_JOIN() {
		return REQUIRE_JOIN;
	}

	@JsonProperty("REQUIRE_JOIN")
	public void setREQUIRE_JOIN(List<RequireEntity> rEQUIRE_JOIN) {
		REQUIRE_JOIN = rEQUIRE_JOIN;
	}

	public List<RequireEntity> getJOIN() {
		return JOIN;
	}

	@JsonProperty("JOIN")
	public void setJOIN(List<RequireEntity> jOIN) {
		JOIN = jOIN;
	}

	public List<JoinEntity> getREQUIRE_JOIN_NAME() {
		return REQUIRE_JOIN_NAME;
	}

	@JsonProperty("REQUIRE_JOIN_NAME")
	public void setREQUIRE_JOIN_NAME(List<JoinEntity> rEQUIRE_JOIN_NAME) {
		REQUIRE_JOIN_NAME = rEQUIRE_JOIN_NAME;
	}

	public List<JoinEntity> getJOIN_NAME() {
		return JOIN_NAME;
	}

	@JsonProperty("JOIN_NAME")
	public void setJOIN_NAME(List<JoinEntity> jOIN_NAME) {
		JOIN_NAME = jOIN_NAME;
	}

	public boolean isM_SHOW_JOIN_BUTTON() {
		return M_SHOW_JOIN_BUTTON;
	}

	@JsonProperty("M_SHOW_JOIN_BUTTON")
	public void setM_SHOW_JOIN_BUTTON(boolean m_SHOW_JOIN_BUTTON) {
		M_SHOW_JOIN_BUTTON = m_SHOW_JOIN_BUTTON;
	}
}
