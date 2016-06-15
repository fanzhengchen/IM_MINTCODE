package com.mintcode.im.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BeetTimer extends Handler {

	public static final int BEET = 12;
	private static BeetTimer sInstance;
	private boolean beetSwitch = false;

	private BeetTimer() {
//		Log.d("handleMessage", "=BeetTimer==" + Thread.currentThread().getName());
	}

	public synchronized static BeetTimer getInstance() {
		if (sInstance == null) {
			sInstance = new BeetTimer();
		}
		return sInstance;
	}

	public void startBeet() {
		beetSwitch = true;
		sendEmptyMessage(BEET);
	}

	public void stopBeet() {
		Log.d("BeetTimer", "StopBeet!!!!!!!!!!!!!!!!!!!!!");
		beetSwitch = false;
		removeMessages(BEET);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.d("handleMessage", "handleMessage===" + Thread.currentThread().getName());
		if (msg.what == BEET) {
			Log.d("BeetTimerssssss", "beet!!!!!!");
			ServiceManager.getInstance().keepBeat();
			if (beetSwitch) {
				sendEmptyMessageDelayed(BEET, 60000);
			}
		}
	}

}
