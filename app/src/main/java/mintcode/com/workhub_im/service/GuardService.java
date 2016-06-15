package mintcode.com.workhub_im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import mintcode.com.workhub_im.AIDLGuardServiceInterace;

/**
 * Created by mark on 16-6-15.
 */
public class GuardService extends Service {


//    public AIDLGuardServiceInterface


    public AIDLGuardServiceInterace.Stub aidl = new AIDLGuardServiceInterace.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
