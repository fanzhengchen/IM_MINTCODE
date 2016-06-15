package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 迁移自拓拓
 * @author KevinQiao
 *
 */
public class UserSeetingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String settingLoginName;
	private String remark;
	@JsonProperty("isRemind")
	private boolean isRemind;

	public String getSettingLoginName() {
		return settingLoginName;
	}

	public void setSettingLoginName(String settingLoginName) {
		this.settingLoginName = settingLoginName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isRemind() {
		return isRemind;
	}

	public void setRemind(boolean isRemind) {
		this.isRemind = isRemind;
	}

}