package mintcode.com.workhub_im.widget.auido;


import com.alibaba.fastjson.JSON;

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
		return JSON.toJSONString(this);
	}

}
