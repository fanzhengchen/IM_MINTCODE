package mintcode.com.workhub_im.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.service.PushService;

/**
 * Created by mark on 16-6-14.
 */
public class IMManager {


    private Context context;
    private static IMManager instance;

    public IMManager(Context context) {
        this.context = context;
//        startService(PushService.class);
    }

    public static IMManager getInstance() {
        if (instance == null) {
            instance = new IMManager(App.getGlobalContext());
        }
        return instance;
    }

    public void startIM(){
        startService(PushService.class, PushService.ACTION_CONNECT);

    }

    public void startService(Class<? extends Service> service, String action) {
        Intent intent = new Intent(context, service);
        intent.setAction(action);
        context.startService(intent);
    }

    /**发送消息item */
    public void sendTextMessage(String msgJson) {
        Intent startServiceIntent = new Intent(context, PushService.class);
        startServiceIntent.setAction(PushService.ACTION_SEND);
        startServiceIntent.putExtra(PushService.KEY_MSG, msgJson);
        context.startService(startServiceIntent);
    }

}
