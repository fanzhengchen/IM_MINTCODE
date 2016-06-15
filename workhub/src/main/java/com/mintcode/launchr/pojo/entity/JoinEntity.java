package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinEntity implements Serializable{

	private String Name;

	public String getName() {
		return Name;
	}

	@JsonProperty("NAME")
	public void setName(String name) {
		Name = name;
	}
	
}
