package mintcode.com.workhub_im.im.pojo;

import android.content.Context;
import android.text.TextUtils;

import java.net.URI;

import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.im.codebutler.WebSocketClient;

/**
 * Created by mark on 16-6-14.
 */
public class ServiceManager {

    private Context context;
    private WebSocketClient socketClient;
    private WebSocketClient.Listener listener;
    private String IP;

    public void connect() {
        if (TextUtils.isEmpty(IP)) {
            IP = UserPrefer.getIpAddress();
        }

        if (TextUtils.isEmpty(IP)) {
            return;
        }

        URI uri = URI.create(IP);
        if (socketClient != null) {
            synchronized (socketClient) {
                socketClient.disconnect();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socketClient.setListener(null);
                socketClient = null;
                socketClient = new WebSocketClient(context, uri, listener, null);
                socketClient.setListener(listener);
                socketClient.connect();
            }
        } else {
            socketClient = new WebSocketClient(context, uri, listener, null);
        }
        socketClient.setListener(listener);
        socketClient.connect();
    }
}
