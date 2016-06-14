package mintcode.com.workhub_im.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import mintcode.com.workhub_im.service.PushService;

/**
 * Created by mark on 16-6-14.
 */
public class IMManager {


    private Context context;
    private static IMManager instance;

    public IMManager() {
        startService(PushService.class);
    }

    public static IMManager getInstance() {
        if (instance == null) {
            instance = new IMManager();
        }
        return instance;
    }

    public void startService(Class<? extends Service> service) {
        Intent intent = new Intent(context, service);
        context.startService(intent);
    }
}
