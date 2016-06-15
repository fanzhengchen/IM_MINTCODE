package com.mintcode.im.listener;

import java.util.List;

import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;

public interface OnIMMessageListener{
	
	void onStatusChanged(int result, String msg);
	
	void onMessage(List<MessageItem> messages, int msgCount);
	
	void onSession(List<SessionItem> sections);
}
