package com.mintcode.im.util;

import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by KevinQiao on 2016/2/23.
 */
public class ServiceUtil {


    /**
     * 判断service是否存活
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean getServiceIsRunning(Context context, String serviceName){
        boolean isRunning = false;

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> lists = am.getRunningServices(100);
        for (RunningServiceInfo info : lists) {// 获取运行服务再启动
            String pn = info.process;
            String sn = info.service.getClassName();
//            Log.i("GuardThead",pn + "---" + sn);
            if (info.service.getClassName().equals(serviceName)) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }


    /**
     * 判断进程是否在前台
     * @param context
     * @param proessName
     * @return
     */
    public static boolean getProessIsRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


}
