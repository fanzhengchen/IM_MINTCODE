package com.mintcode.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态的检测类
 * 
 * @author ChristLu
 * 
 */
public class NetState extends BroadcastReceiver {
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		ConnectivityManager connMannager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// gprs
		NetworkInfo gprs = connMannager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// wifi
		NetworkInfo wifi = connMannager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (((gprs != null) && !gprs.isConnected()) && (wifi != null)
				&& !wifi.isConnected()) {
			//TODO:断网回调
			
		} else {
			//TODO：网络又重新连接上了

		}
	}
}
