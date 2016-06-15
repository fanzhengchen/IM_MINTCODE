package com.mintcode.launchr.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.HeaderEntity;


public class BasePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private HeaderEntity Header;
	
	
	public HeaderEntity getHeader() {
		return Header;
	}
	
	@JsonProperty("Header")
	public void setHeader(HeaderEntity header) {
		Header = header;
	}
	
	public boolean isResultSuccess(){
		boolean success = Header.isIsSuccess();
		return success;
	}
	
	
}
