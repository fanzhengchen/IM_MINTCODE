package com.mintcode.im.entity;

import java.util.Comparator;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

public class SessionItem extends DataSupport implements
		Comparator<SessionItem>, Parcelable {

	/**
	 * 
	 */
	public static final long serialVersionUID = -2930971922642344734L;
	/**
	 * 数据库id
	 */
	private int id;
	/**
	 * 自己的uid
	 */
	private String userName;
	/**
	 * 对方昵称
	 */
	private String nickName;
	/**
	 * 对方的uid
	 */
	private String oppositeName;
	/**
	 * 最后一条聊天内容
	 */
	private String content;

	/**
	 * 最后一条消息的时间
	 */
	private long time;

	/**
	 * 额外字段的json
	 */
	private String info;

	/**
	 * 未读条目
	 */
	private int unread;

	/**
	 * 用户资料更新时间戳
	 */
	private long modified;
	
	/**
	 * 是否是群聊 ，0不是 1是
	 */
	private int chatRoom; 
	
	/**
	 * 会话列表排序，0为最小，越大越置顶。
	 */
	private int sort;

	/** 类型   1普通，2草稿，3为@ */
	private int type;

	/** 是否接收显示角标 1显示，2不显示*/
	private int recieve;

	/** 草稿 */
	private String drafts;

	private long clientMsgId;

	private long msgId;

	private long sessiontime;

	public long getSessiontime() {
		return sessiontime;
	}

	public void setSessiontime(long sessiontime) {
		this.sessiontime = sessiontime;
	}

	public long getClientMsgId() {
		return clientMsgId;
	}

	public void setClientMsgId(long clientMsgId) {
		this.clientMsgId = clientMsgId;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getDrafts() {
		return drafts;
	}

	public void setDrafts(String drafts) {
		this.drafts = drafts;
	}

	public int getRecieve() {
		return recieve;
	}

	public void setRecieve(int recieve) {
		this.recieve = recieve;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(int chatRoom) {
		this.chatRoom = chatRoom;
	}

	public SessionItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOppositeName() {
		return oppositeName;
	}

	public void setOppositeName(String oppositeName) {
		this.oppositeName = oppositeName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	@Override
	public int compare(SessionItem lhs, SessionItem rhs) {
		long lt = lhs.getTime();
		long rt = rhs.getTime();
		if (lt > rt) {
			return 1;
		} else if (lt < rt) {
			return -1;
		}
		return 0;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(nickName);
		dest.writeString(oppositeName);
		dest.writeString(content);
		dest.writeString(info);
		dest.writeLong(time);
		dest.writeLong(modified);
		dest.writeInt(unread);
		dest.writeInt(chatRoom);

		dest.writeInt(recieve);
		dest.writeInt(type);
		dest.writeString(drafts);
		dest.writeLong(sessiontime);

	}

	private SessionItem(Parcel source) {
		userName = source.readString();
		nickName = source.readString();
		oppositeName = source.readString();
		content = source.readString();
		info = source.readString();
		time = source.readLong();
		modified = source.readLong();
		unread = source.readInt();
		chatRoom = source.readInt();

		recieve = source.readInt();
		type = source.readInt();
		drafts = source.readString();
		sessiontime = source.readLong();
	}

	public static final Creator<SessionItem> CREATOR = new Creator<SessionItem>() {

		@Override
		public SessionItem[] newArray(int size) {
			return new SessionItem[size];
		}

		@Override
		public SessionItem createFromParcel(Parcel source) {
			return new SessionItem(source);
		}
	};
}
