package com.mintcode.launchr.upload_download;

import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchrnetwork.MTHttpParameters;


public class FileDetail {

	private String fileName;
	private String filePath;
	private long srcOffset;
	private int fileStatus;
	private String appShowID;
	
	public String getAppShowID() {
		return appShowID;
	}

	public void setAppShowID(String appShowID) {
		this.appShowID = appShowID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getSrcOffset() {
		return srcOffset;
	}

	public void setSrcOffset(long srcOffset) {
		this.srcOffset = srcOffset;
	}

	public int getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(int fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String toJson() {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("fileName", getFileName());
		params.setParameter("srcOffset", getSrcOffset());
		params.setParameter("filePath", getFilePath());
		params.setParameter("fileStatus", getFileStatus());
		params.setParameter("appShowID", getAppShowID());
		
		MTHttpParameters p = new MTHttpParameters();
		MTHttpParameters body = new MTHttpParameters();
		body.setParameter("param", params.getMap());
		p.setParameter("Body", body.getMap());

		HeaderParam entity = LauchrConst.header;

		entity.setType("POST");

		entity.setResourceUri("/Base-Module/Annex/Mobile");
		p.setParameter("Header", entity);
		return p.toJson();
	}




}
