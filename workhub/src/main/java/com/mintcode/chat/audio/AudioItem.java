package com.mintcode.chat.audio;

import com.mintcode.chat.util.JsonUtil;


public class AudioItem {
	private String fileUrl;
	private int audioLength;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public int getAudioLength() {
		return audioLength;
	}

	public void setAudioLength(int audioLength) {
		this.audioLength = audioLength;
	}

	@Override
	public String toString() {
		return JsonUtil.convertObjToJson(this);
	}

}
