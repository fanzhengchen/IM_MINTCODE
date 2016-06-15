package com.mintcode.im.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import com.mintcode.im.BaseManager;

/**
 * Created by KevinQiao on 2016/3/4.
 */
public class IMHeartBeetManager extends BaseManager{


    private static IMHeartBeetManager instance = new IMHeartBeetManager();

    public static IMHeartBeetManager instance(){
        return instance;
    }

    public static final String ACITON_SENDING_HEARTBEAT = "aciton_sending_heartbeat";


    private PendingIntent mPendingIntent = null;

    /**
     *
     */
    public void startServiceHeart(long millseconds){

        if(mPendingIntent == null){
            Intent intent = new Intent(ACITON_SENDING_HEARTBEAT);

            mPendingIntent = PendingIntent.getBroadcast(context,0,intent,0);

            if(mPendingIntent == null){
                Log.i("mPendingIntent","mPendingIntent is null");
                return;
            }

        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + millseconds,millseconds,mPendingIntent);

    }

    public void cancelHeartBeat(){
        if (mPendingIntent == null) {
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(mPendingIntent);
    }


    private BroadcastReceiver imReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action != null && action.equals(ACITON_SENDING_HEARTBEAT)){
                sendHeartBeatPacket();
            }

        }
    };



    @Override
    public void doOnStart() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACITON_SENDING_HEARTBEAT);
        context.registerReceiver(imReceiver, intentFilter);
        //获取AlarmManager系统服务
        startServiceHeart(10000);
    }

    @Override
    public void reset() {
        cancelHeartBeat();
    }

    public void sendHeartBeatPacket(){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE,"IMHeartBeetManager");
        wl.acquire(1000);
        Log.i("BroadcastReceiver", "=====还活着======");
        Intent intent = new Intent(context,GuardService.class);
        context.startService(intent);
        wl.release();
    }


}
