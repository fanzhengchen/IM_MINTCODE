package com.mintcode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.api.IMAPI.TASKID;

@JsonTypeName(TASKID.HISTORY_MESSAGE)
public class HistoryMessagePOJO extends BasePOJO {
	private List<MessageItem> msg;

	public List<MessageItem> getMsg() {
		return msg;
	}

	public void setMsg(List<MessageItem> msg) {
		this.msg = msg;
	}

}
