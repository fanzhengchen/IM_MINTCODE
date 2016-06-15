package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;
import java.util.List;


public class LoginValidateEntity implements Serializable {
	
	private String validatorToken;
	
	private List<CompanyEntity> companyList;

	public String getValidatorToken() {
		return validatorToken;
	}

	public void setValidatorToken(String validatorToken) {
		this.validatorToken = validatorToken;
	}

	public List<CompanyEntity> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<CompanyEntity> companyList) {
		this.companyList = companyList;
	}
	

}
