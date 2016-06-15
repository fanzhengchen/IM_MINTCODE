package com.mintcode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.api.IMAPI.TASKID;

@JsonTypeName(TASKID.GET_OFFLINE_MSG)
public class OfflineMsgPOJO extends BasePOJO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3342877805069785991L;

	private List<MessageItem> msg;
	private int remain;
	private long msgId;
	public List<MessageItem> getMsg() {
		return msg;
	}
	public void setMsg(List<MessageItem> msg) {
		this.msg = msg;
	}
	public int getRemain() {
		return remain;
	}
	public void setRemain(int remain) {
		this.remain = remain;
	}
	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	
	
}
