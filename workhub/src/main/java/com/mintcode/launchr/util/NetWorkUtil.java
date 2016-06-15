package com.mintcode.launchr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络检测类
 * Created by KevinQiao on 2016/1/15.
 */
public class NetWorkUtil {


    /**
     * 检测网络是否可用
     * @param context
     * @return
     */
    public static boolean checkNetWorkIsAvalable(Context context){
        boolean wifiState = isWifiAvailable(context);
        boolean mobileState = isMobilAvailable(context);

        if(wifiState || mobileState){
            return true;
        }
        return false;
    }




    /**
     * 获取ConnectivityManager
     * @param context
     * @return
     */
    private  static ConnectivityManager getConnectivityManager(Context context){
        ConnectivityManager manager = null;
        if(context != null){
            manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return manager;
    }


    /**
     * 判断wifi是否可用
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context){
        ConnectivityManager manager = getConnectivityManager(context);
        boolean isWifiAvilable = false;
        if(manager != null){
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            isWifiAvilable = networkInfo.isConnectedOrConnecting();
        }
        return isWifiAvilable;
    }

    /**
     * 判断移动网络是否可用
     * @param context
     * @return
     */
    public static boolean isMobilAvailable(Context context){
        ConnectivityManager manager = getConnectivityManager(context);
        boolean isWifiAvilable = false;
        if(manager != null){
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            isWifiAvilable = networkInfo.isConnectedOrConnecting();
        }
        return isWifiAvilable;
    }

}
