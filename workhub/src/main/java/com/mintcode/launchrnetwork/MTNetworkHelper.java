package com.mintcode.launchrnetwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MTNetworkHelper {
	/**
	 * Indicates whether network connectivity is possible.
	 * @param context
	 * @return true if the network is available, false otherwise
	 */
	public static boolean isNetworkConnected(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if(network != null){
			return network.isAvailable();
		}
		return false;
	}
}
