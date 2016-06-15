package com.mintcode.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mintcode.im.service.PushService;
import com.mintcode.im.service.ServiceManager;
import com.mintcode.im.util.IMLog;



public class NetworkConnect extends BroadcastReceiver{

	private boolean mActiveFlag = false;
	private static final String TAG = PushService.class.getSimpleName();
	private ServiceManager mServiceManager;
	
	public NetworkConnect(ServiceManager serviceManager) {
		mServiceManager = serviceManager;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
				.getAction())) {
			if(mActiveFlag){
				if(isNetworkConnected(context)){
					IMLog.i(TAG, "BroadcastReceiver onReceive");
					if (mServiceManager == null) {
						mServiceManager = ServiceManager.getInstance();
					}
					if (mServiceManager != null) {
						IMLog.d(TAG, "onReceive connect: CONNECTIVITY_ACTION");
						mServiceManager.connect();
					}
				}
			}
			mActiveFlag = true;
		}
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
