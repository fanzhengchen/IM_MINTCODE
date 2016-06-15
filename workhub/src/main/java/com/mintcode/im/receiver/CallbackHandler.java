package com.mintcode.im.receiver;

import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.MsgListenerManager;

public class CallbackHandler extends Handler {

	public final static int CALLBACK_STATUS = 0;
	public final static int CALLBACK_MESSAGE = 1;
	public final static int CALLBACK_SECTION = 2;
	private MsgListenerManager mMsgListenerManager;
	
	public CallbackHandler() {
		mMsgListenerManager = MsgListenerManager.getInstance();
	}

	public CallbackHandler(Looper looper){
		super(looper);
		mMsgListenerManager = MsgListenerManager.getInstance();
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case CALLBACK_STATUS:
			Log.d("handleMessage", "CALLBACK_STATUS");
			int statusResult = msg.arg1;
			String msgStr = (String) msg.obj;
			mMsgListenerManager.onStatusChanged(statusResult, msgStr);
			break;
		case CALLBACK_MESSAGE:
			List<MessageItem> msgs = (List<MessageItem>) msg.obj;
			mMsgListenerManager.onMessage(msgs);
			break;
		case CALLBACK_SECTION:
			List<SessionItem> sections = (List<SessionItem>) msg.obj;
			mMsgListenerManager.onSession(sections);
			break;

		default:
			break;
		}
	}
}
