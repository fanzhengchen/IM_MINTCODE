package com.mintcode.im.entity;

import org.litepal.crud.DataSupport;

public class MessageId extends DataSupport {

	private long msgId;

	private String uid;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
