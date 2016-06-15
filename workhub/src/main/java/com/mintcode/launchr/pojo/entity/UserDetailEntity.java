package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.litepal.crud.DataSupport;

public class UserDetailEntity extends DataSupport implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5781849704789187609L;

	private int id;

	private String showId;

	private String name;

	private String trueName;

	private String trueNameC;

	private String mail;

	private String status;

	private String mobile;

	private String job;

	private String number;

	private String sort;

	private String lastLoginTime;

	private String lastLoginToken;

	private String isAdmin;

	private String cShowId;

	private String createUser;

	private String createTime;

	private String createUserName;

	private String dName;

	private String deptId;

	private String parentShowId;

	private String pathName;

	private String hira;

	private String telephone;

	private String otherField;

	private long launchrId;

	public long getLaunchrId() {
		return launchrId;
	}

	@JsonProperty("LAUNCHR_ID")
	public void setLaunchrId(long launchrId) {
		this.launchrId = launchrId;
	}

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

	public String getName() {
		return name;
	}

	@JsonProperty("U_NAME")
	public void setName(String name) {
		this.name = name;
	}

	public String getTrueName() {
		return trueName;
	}

	@JsonProperty("U_TRUE_NAME")
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getTrueNameC() {
		return trueNameC;
	}

	@JsonProperty("U_TRUE_NAME_C")
	public void setTrueNameC(String trueNameC) {
		this.trueNameC = trueNameC;
	}

	public String getMail() {
		return mail;
	}

	@JsonProperty("U_MAIL")
	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("U_STATUS")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	@JsonProperty("U_MOBILE")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getJob() {
		return job;
	}

	@JsonProperty("U_JOB")
	public void setJob(String job) {
		this.job = job;
	}

	public String getNumber() {
		return number;
	}

	@JsonProperty("U_NUMBER")
	public void setNumber(String number) {
		this.number = number;
	}

	public String getSort() {
		return sort;
	}

	@JsonProperty("U_SORT")
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	@JsonProperty("LAST_LOGIN_TIME")
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginToken() {
		return lastLoginToken;
	}

	@JsonProperty("LAST_LOGIN_TOKEN")
	public void setLastLoginToken(String lastLoginToken) {
		this.lastLoginToken = lastLoginToken;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	@JsonProperty("IS_ADMIN")
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getcShowId() {
		return cShowId;
	}

	@JsonProperty("C_SHOW_ID")
	public void setcShowId(String cShowId) {
		this.cShowId = cShowId;
	}

	public String getCreateUser() {
		return createUser;
	}

	@JsonProperty("CREATE_USER")
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	@JsonProperty("CREATE_TIME")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	@JsonProperty("CREATE_USER_NAME")
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getdName() {
		return dName;
	}

	@JsonProperty("D_NAME")
	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getDeptId() {
		return deptId;
	}

	@JsonProperty("U_DEPT_ID")
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getParentShowId() {
		return parentShowId;
	}

	@JsonProperty("D_PARENTID_SHOW_ID")
	public void setParentShowId(String parentShowId) {
		this.parentShowId = parentShowId;
	}

	public String getPathName() {
		return pathName;
	}

	@JsonProperty("D_PATH_NAME")
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getHira() {
		return hira;
	}

	@JsonProperty("U_HIRA")
	public void setHira(String hira) {
		this.hira = hira;
	}

	public String getTelephone() {
		return telephone;
	}

	@JsonProperty("U_TELEPHONE")
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOtherField() {
		return otherField;
	}

	@JsonProperty("otherField")
	public void setOtherField(String otherField) {
		this.otherField = otherField;
	}
}

