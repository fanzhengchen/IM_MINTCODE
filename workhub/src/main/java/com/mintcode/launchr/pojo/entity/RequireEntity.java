package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequireEntity implements Serializable{

	private String NAME;
	
	private int ISJOIN;

	public String getNAME() {
		return NAME;
	}

	@JsonProperty("NAME")
	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public int getISJOIN() {
		return ISJOIN;
	}

	@JsonProperty("ISJOIN")
	public void setISJOIN(int iSJOIN) {
		ISJOIN = iSJOIN;
	}


//	


//	
	
	
	
}
