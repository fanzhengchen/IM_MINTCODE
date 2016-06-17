package mintcode.com.workhub_im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import mintcode.com.workhub_im.im.ServiceManager;

/**
 * Created by mark on 16-6-14.
 */
public class PushService extends Service {

    public static final String ACTION_CONNECT = "action_connect";
    public static final String ACTION_SEND = "action_send";
    public static final String KEY_MSG = "KEY_MSG";


    private ServiceManager serviceManager;
    private String imUserName;
    private String companyCode;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        serviceManager = ServiceManager.getInstance();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        }
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_CONNECT)) {
            serviceManager.connect();
        } else if (TextUtils.equals(action, ACTION_SEND)) {
        }
//        return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }




}
