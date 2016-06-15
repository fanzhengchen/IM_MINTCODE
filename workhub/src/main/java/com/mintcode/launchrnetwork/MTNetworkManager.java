package com.mintcode.launchrnetwork;

import java.util.HashSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

/**网络管理类，目前只负责网络切换的监听和回调
 * @author RobinLin
 *
 */
public class MTNetworkManager {
	private static MTNetworkManager sManager;

	private Context mContext;
	private HashSet<MTINetworkListner> mListners;
	private NetworkReceiver mReceiver;
	
	public static MTNetworkManager getInstance(Context context) {
		if (sManager == null) {
			sManager = new MTNetworkManager(context);
		}else{
			sManager.updateContext(context);
		}
		return sManager;
	}

	public MTNetworkManager(Context context) {
		mContext = context;
		init();
	}

	/**每次切换Activity时，更新Context，防止Toast等事件无法通知
	 * @param context
	 */
	public void updateContext(Context context){
		mContext = context;
	}
	
	private void init(){
		mListners = new HashSet<MTINetworkListner>();
//		mReceiver = new NetworkReceiver();
//		registerDateTransReceiver();
	}
	
	private void registerDateTransReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(NetConst.Action.CONNECTIVITY_CHANGE_ACTION);
		filter.setPriority(1000);
	}
	

	/**注册接口
	 * @param listner
	 */
	public void register(MTINetworkListner listner){
		mListners.add(listner);
		
//		update(listner);
	}
	
	public void unregister(MTINetworkListner listner){
		mListners.remove(listner);
	}
	private class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (TextUtils.equals(action, NetConst.Action.CONNECTIVITY_CHANGE_ACTION)) {
				boolean status = MTNetworkHelper.isNetworkConnected(mContext);
				if(mListners != null){
					for (MTINetworkListner listner : mListners) {
						listner.onNetworkChanged(status);
					}
				}
				return;
			}
		}

	}
	public void update() {
		boolean status = MTNetworkHelper.isNetworkConnected(mContext);
		if(mListners != null){
			for (MTINetworkListner listner : mListners) {
				listner.onNetworkChanged(status);
			}
		}
	}
	public void update(MTINetworkListner listner) {
		boolean status = MTNetworkHelper.isNetworkConnected(mContext);
		listner.onNetworkChanged(status);
	}

}
