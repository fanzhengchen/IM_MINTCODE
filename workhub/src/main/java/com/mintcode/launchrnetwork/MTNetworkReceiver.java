package com.mintcode.launchrnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class MTNetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.equals(action, NetConst.Action.CONNECTIVITY_CHANGE_ACTION)) {
			return;
		}
	}

}
