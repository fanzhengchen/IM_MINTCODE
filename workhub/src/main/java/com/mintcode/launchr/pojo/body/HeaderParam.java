package com.mintcode.launchr.pojo.body;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * head实体
 * @author KevinQiao
 *
 */
public class HeaderParam implements Serializable{

	@JsonProperty("LoginName")
	private String LoginName;
	
	@JsonProperty("UserName")
	private String UserName;
	
	@JsonProperty("CompanyShowID")
	private String CompanyShowID;
	
	@JsonProperty("CompanyCode")
	private String CompanyCode;
	
	@JsonProperty("AuthToken")
	private String AuthToken;
	
	@JsonProperty("ResourceUri")
	private String ResourceUri;
	
	@JsonProperty("Async")
	private boolean Async;
	
	@JsonProperty("Type")
	private String Type;
	
	@JsonProperty("Language")
	private String Language;

	public String getLoginName() {
		return LoginName;
	}

	@JsonProperty("LoginName")
	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	public String getUserName() {
		return UserName;
	}

	@JsonProperty("UserName")
	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getCompanyShowID() {
		return CompanyShowID;
	}

	@JsonProperty("CompanyShowID")
	public void setCompanyShowID(String companyShowID) {
		CompanyShowID = companyShowID;
	}

	public String getCompanyCode() {
		return CompanyCode;
	}

	@JsonProperty("CompanyCode")
	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}

	public String getAuthToken() {
		return AuthToken;
	}

	@JsonProperty("AuthToken")
	public void setAuthToken(String authToken) {
		AuthToken = authToken;
	}

	public String getResourceUri() {
		return ResourceUri;
	}

	@JsonProperty("ResourceUri")
	public void setResourceUri(String resourceUri) {
		ResourceUri = resourceUri;
	}

	public boolean isAsync() {
		return Async;
	}

	@JsonProperty("Async")
	public void setAsync(boolean async) {
		Async = async;
	}

	public String getType() {
		return Type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		Type = type;
	}

	public String getLanguage() {
		return Language;
	}

	@JsonProperty("Language")
	public void setLanguage(String language) {
		Language = language;
	}

	
}
