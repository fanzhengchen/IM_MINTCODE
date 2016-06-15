package com.mintcode.im.database;

import com.mintcode.im.util.Keys;

public class MsgIdDBService {

	private long mMsgId = -1;

	private static MsgIdDBService sInstance;
	
	private static KeyValueDBService keyValueDBService;


	public synchronized static MsgIdDBService getInstance() {
		sInstance = new MsgIdDBService();
		keyValueDBService = KeyValueDBService.getInstance();
		return sInstance;
	}

	public long getMsgId() {
		if (mMsgId > 0) {
			return mMsgId;
		}
		Long msgId = keyValueDBService.findLong(Keys.MSG_ID);
		if (msgId == null) {
			return -1;
		}
		return msgId;
	}
	
	public void put(long msgId){
		if (mMsgId < 0) {
			mMsgId = getMsgId();
		}
		if (mMsgId >= msgId) {
			return;
		}
		keyValueDBService.put(Keys.MSG_ID, msgId);
		mMsgId = msgId;
	}

}
