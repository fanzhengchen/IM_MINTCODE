package com.mintcode.im.entity;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.im.util.JsonUtil;

public class MessageItem extends DataSupport implements Parcelable{

	/**
	 * 
	 */
	public static final long serialVersionUID = 4467546758425809004L;
	/**
	 * 发送、接收
	 */
	public static final int TYPE_RECV = 1;
	public static final int TYPE_SEND = 0;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 应用名称
	 */
	private String appName;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 最后修改时间
	 */
	private long modified;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 服务端消息Id
	 */
	private long msgId;
	/**
	 * 客户端用户名
	 */
	@JsonProperty(value="from")
	private String fromLoginName;
	/**
	 * 客户端用户名
	 */
	@JsonProperty(value="to")
	private String toLoginName;
	/**
	 * 客户端用户信息
	 */
	private String info;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 客户端消息Id
	 */
	private long clientMsgId;
	/**
	 * 消息创建时间
	 */
	private long createDate;
	/**
	 * 消息类型
	 */
	private String type;
	/**
	 * Can be {@link MessageItem.TYPE_RECV} or {@link MessageItem.TYPE_SEND}
	 */
	private int cmd;
	/**
	 * 发送状态 成功 0 失败 1 这个标记用于显示 loading框
	 */
	private int sent;

	private String timeText;

	private String fileName;

	private String fileSize;
	/**
	 * 发送成功与否 1 成功 0 失败 这个标记用于判断文件是否上传成功
	 */
	private int actionSend;
	/**
	 * 是否已读
	 */
	private int isRead;
	/**
	 * 是否标记重点
	 */
	private int isMarkPoint;
	/**
	 * 是否收到服务器回执 0 收到 1没收到
	 */
	private int isResp;
	/**
	 * 用来记录消息发送的时间
	 */
	private long send_time;
	
	private String toNickName;

	/** 存储发送图片显示进度百分比 */
	private String percent;

	public int getIsMarkPoint() {
		return isMarkPoint;
	}

	public void setIsMarkPoint(int isMarkPoint) {
		this.isMarkPoint = isMarkPoint;
	}

	public MessageItem() {
		super();
	}


	public String getToNickName() {
		return toNickName;
	}

	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	/**
	 * create from a json string.
	 * 
	 * @param json
	 * @return
	 */
	public static MessageItem createMessageItem(String json) {
		return JsonUtil.convertJsonToCommonObj(json, MessageItem.class);
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	
	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getFromLoginName() {
		return fromLoginName;
	}

	public void setFromLoginName(String fromLoginName) {
		this.fromLoginName = fromLoginName;
	}

	public String getToLoginName() {
		return toLoginName;
	}

	public void setToLoginName(String toLoginName) {
		this.toLoginName = toLoginName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getClientMsgId() {
		return clientMsgId;
	}

	public void setClientMsgId(long clientMsgId) {
		this.clientMsgId = clientMsgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSent() {
		return sent;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTimeText() {
		return timeText;
	}

	public void setTimeText(String timeText) {
		this.timeText = timeText;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public int getActionSend() {
		return actionSend;
	}

	public void setActionSend(int actionSend) {
		this.actionSend = actionSend;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsResp() {
		return isResp;
	}

	public void setIsResp(int isResp) {
		this.isResp = isResp;
	}

	public long getSend_time() {
		return send_time;
	}

	public void setSend_time(long send_time) {
		this.send_time = send_time;
	}

	/**
	 * Convert this object to json string.
	 */
	@Override
	public String toString() {
		return JsonUtil.convertObjToJson(this);
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

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(msgId);
		dest.writeString(fromLoginName);
		dest.writeString(toLoginName);
		dest.writeString(info);
		dest.writeString(content);
		dest.writeLong(clientMsgId);
		dest.writeLong(createDate);
		dest.writeString(type);
		dest.writeInt(cmd);
		dest.writeString(fileName);
		dest.writeString(fileSize);
		dest.writeString(timeText);
		dest.writeInt(actionSend);
		dest.writeInt(isRead);
		dest.writeInt(sent);
		dest.writeInt(isResp);
		dest.writeLong(send_time);
		dest.writeString(toNickName);
		dest.writeInt(isMarkPoint);
		dest.writeString(percent);

	}
	
	private MessageItem(Parcel source){
		this.msgId = source.readLong();
		this.fromLoginName = source.readString();
		this.toLoginName = source.readString();
		this.info = source.readString();
		this.content = source.readString();
		this.clientMsgId = source.readLong();
		this.createDate = source.readLong();
		this.type = source.readString();
		this.cmd = source.readInt();
		this.fileName = source.readString();
		this.fileSize = source.readString();
		this.timeText = source.readString();
		this.actionSend = source.readInt();
		this.isRead = source.readInt();
		this.sent = source.readInt();
		this.isResp = source.readInt();
		this.send_time = source.readLong();
		this.toNickName = source.readString();
		this.isMarkPoint = source.readInt();
		this.percent = source.readString();
	}
	
	public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
		
		@Override
		public MessageItem[] newArray(int size) {
			return new MessageItem[size];
		}
		
		@Override
		public MessageItem createFromParcel(Parcel source) {
			return new MessageItem(source);
		}
	};
}
