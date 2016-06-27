package mintcode.com.workhub_im.beans;

import android.os.Handler;


/**
 * Created by mark on 16-6-27.
 */
public class GlobalBeans {

    private Handler handler;
    private static GlobalBeans globalBeans = null;

    private GlobalBeans() {

    }

    public static GlobalBeans getInstance() {
        if (globalBeans == null) {
            globalBeans = new GlobalBeans();
            globalBeans.init();
        }
        return globalBeans;
    }

    public void init() {
        handler = new Handler();
    }


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
