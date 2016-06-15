package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.litepal.crud.DataSupport;


public class DepartmentEntity extends DataSupport implements Serializable {
	private int id;
	private String showId;
	private String cShowId;
	private String dName;
	private String dLevel;
	private String dParentName;
	private String dParentShowId;
	private String cName;
	private String createUserName;
	private int dSort;
	private int dAvailableCount;
	private int dUnAvailableCount;
	private int childDeptCount;
	private String extendField;  // 扩展字段
	private String otherField;   //扩展字段

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShowId() {
		return showId;
	}

	@JsonProperty("SHOW_ID")
	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getcShowId() {
		return cShowId;
	}

	@JsonProperty("C_SHOW_ID")
	public void setcShowId(String cShowId) {
		this.cShowId = cShowId;
	}

	public String getdName() {
		return dName;
	}

	@JsonProperty("D_NAME")
	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getdLevel() {
		return dLevel;
	}

	@JsonProperty("D_LEVEL")
	public void setdLevel(String dLevel) {
		this.dLevel = dLevel;
	}

	public String getdParentName() {
		return dParentName;
	}

	@JsonProperty("D_PARENT_NAME")
	public void setdParentName(String dParentName) {
		this.dParentName = dParentName;
	}

	public String getdParentShowId() {
		return dParentShowId;
	}

	@JsonProperty("D_PARENTID_SHOW_ID")
	public void setdParentShowId(String dParentShowId) {
		this.dParentShowId = dParentShowId;
	}

	public String getcName() {
		return cName;
	}

	@JsonProperty("C_NAME")
	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	@JsonProperty("CREATE_USER_NAME")
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public int getdSort() {
		return dSort;
	}

	@JsonProperty("D_SORT")
	public void setdSort(int dSort) {
		this.dSort = dSort;
	}

	public int getdAvailableCount() {
		return dAvailableCount;
	}

	@JsonProperty("D_AVAILABLE_COUNT")
	public void setdAvailableCount(int dAvailableCount) {
		this.dAvailableCount = dAvailableCount;
	}

	public int getdUnAvailableCount() {
		return dUnAvailableCount;
	}

	@JsonProperty("D_UNAVAILABLE_COUNT")
	public void setdUnAvailableCount(int dUnAvailableCount) {
		this.dUnAvailableCount = dUnAvailableCount;
	}

	public int getChildDeptCount() {
		return childDeptCount;
	}

	public void setChildDeptCount(int childDeptCount) {
		this.childDeptCount = childDeptCount;
	}

	public String getExtendField() {
		return extendField;
	}

	public void setExtendField(String extendField) {
		this.extendField = extendField;
	}

	public String getOtherField() {
		return otherField;
	}

	public void setOtherField(String otherField) {
		this.otherField = otherField;
	}
}
