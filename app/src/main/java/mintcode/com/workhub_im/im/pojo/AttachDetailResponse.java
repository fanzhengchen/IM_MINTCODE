package mintcode.com.workhub_im.im.pojo;

import com.alibaba.fastjson.JSON;

/**
 * 附件详情
 * 
 * @author ChristLu
 * 
 */
public class AttachDetailResponse extends IMBaseResponse {

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
		return JSON.toJSONString(this);
	}


}
