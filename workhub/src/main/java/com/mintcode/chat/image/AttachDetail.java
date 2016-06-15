package com.mintcode.chat.image;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.BasePOJO;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.MTHttpParameters;

/**
 * 附件详情
 * 
 * @author ChristLu
 * 
 */
@JsonTypeName("upload")
public class AttachDetail extends BasePOJO {
	private static final long serialVersionUID = 1L;
	private String appName;
	private String userName;
	private String userToken;
	private String fileName;
	private int srcOffset;
	private String fileUrl;
	private int fileStatus;
	private String thumbnail;
	private int fileSize;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSrcOffset() {
		return srcOffset;
	}

	public void setSrcOffset(int srcOffset) {
		this.srcOffset = srcOffset;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public int getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(int fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Override
	public String toString() {
		return TTJSONUtil.convertObjToJson(this);
	}

	public String toJson(){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", getAppName());
		params.setParameter("userToken", getUserToken());
		params.setParameter("userName", getUserName());
		params.setParameter("fileName", getFileName());
		params.setParameter("srcOffset", getSrcOffset());
		params.setParameter("fileUrl", getFileUrl());
		params.setParameter("fileStatus", getFileStatus());


//		MTHttpParameters p = new MTHttpParameters();
//		MTHttpParameters body = new MTHttpParameters();
//		body.setParameter("param", params.getMap());
//		p.setParameter("Body", body.getMap());
//
//		HeaderParam entity = LauchrConst.header;
//
//		entity.setType("POST");
//
//		entity.setResourceUri("/Base-Module/Annex/Mobile");
//		p.setParameter("Header", entity);
		String str = TTJSONUtil.convertObjToJson(params.getMap());
		return str;
	}

}
