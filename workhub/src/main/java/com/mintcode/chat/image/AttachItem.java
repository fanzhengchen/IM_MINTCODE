package com.mintcode.chat.image;

import com.mintcode.chat.util.JsonUtil;

public class AttachItem {
	private String thumbnail;
	private int fileSize;
	private String fileUrl;
	private String fileName;
	private int thumbnailWidth;
	private int thumbnailHeight;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		return JsonUtil.convertObjToJson(this);
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}
}
