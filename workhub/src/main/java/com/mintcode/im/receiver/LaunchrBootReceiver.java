package com.mintcode.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mintcode.im.service.PushService;

/**
 * Created by RobinLin on 2015/11/27.
 */
public class LaunchrBootReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent bootIntent = new Intent(context, PushService.class);
            context.startService(bootIntent);
        }
    }
}
