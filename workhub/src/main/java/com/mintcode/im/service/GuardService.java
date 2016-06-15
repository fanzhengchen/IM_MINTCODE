package com.mintcode.im.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.mintcode.im.aidl.AIDLGuardServiceInterface;
import com.mintcode.im.util.ServiceUtil;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * Created by KevinQiao on 2016/2/23.
 */
public class GuardService extends Service {

    public static final String PUSHSERVICE_NAME = "com.mintcode.im.service.PushService";

    public static final String ACTION_STOP = "action_stop";
    IMHeartBeetManager heartBeetManager = IMHeartBeetManager.instance();
    public AIDLGuardServiceInterface.Stub mAIDLGuardServiceInterface = new AIDLGuardServiceInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void startService() throws RemoteException {
            Intent intent = new Intent(GuardService.this, PushService.class);
            intent.setAction(PushService.ACTION_CONNECT);
            GuardService.this.startService(intent);
        }

        @Override
        public void keepBeet() throws RemoteException {
            Intent intent = new Intent(GuardService.this, PushService.class);
            intent.setAction("testserverice");
            GuardService.this.startService(intent);
        }

        @Override
        public void stopService() throws RemoteException {

        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mAIDLGuardServiceInterface;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 启动线程，监听pushservice
        GuardThead guardThead = new GuardThead();
        Thread thread = new Thread(guardThead);
        thread.start();

//        heartBeetManager.onStartManger(getApplicationContext());
    }



    class GuardThead implements Runnable{

        @Override
        public void run() {
            int i = 0;
            while (true){
                // 判断用户是否登出
                if(LauchrConst.PUSH_SERVICE_IS_LOGIN){
                    // 获取pushservice是否存活
                    boolean isRunning = ServiceUtil.getServiceIsRunning(getApplicationContext(),PUSHSERVICE_NAME);
                    Log.i("GuardThead",i + "==" + isRunning);
                    try {
                        if(!isRunning){
                            mAIDLGuardServiceInterface.startService();
                        }else {
                            if(i > 10){
                                mAIDLGuardServiceInterface.keepBeet();
                                i = 0;
                            }
                        }
                        Thread.sleep(10000);

                        i ++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        if (intent != null){
            action = intent.getAction();
        }
        if(action != null && ACTION_STOP.equals(action)){
            LauchrConst.PUSH_SERVICE_IS_LOGIN = false;
            stopSelf();
            Log.i("BroadcastReceiver", "===死了=====");
            return super.onStartCommand(intent, flags, startId);
        }else {
            LauchrConst.PUSH_SERVICE_IS_LOGIN = true;

        }
        Log.i("BroadcastReceiver","========");
        return Service.START_STICKY;
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("onTrimMemory","========" + level);
        super.onTrimMemory(level);
    }
}
