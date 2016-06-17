package mintcode.com.workhub_im.im;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.net.URI;
import java.util.ArrayList;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.codebutler.WebSocketClient;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.Session;
import mintcode.com.workhub_im.util.AESUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-14.
 */
public class ServiceManager {

    private Context context;
    private WebSocketClient webSocketClient;
    private WebSocketClient.Listener listener;
    private String IP;
    private static ServiceManager serviceManager;
    private static int start = 0;
    private static int end = 200;
    private ArrayList<Session> sessions;

    private ServiceManager(Context context) {
        this.context = context;
    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager(App.getGlobalContext());
        }
        return serviceManager;
    }

    public void socketConnect() {
        if (TextUtils.isEmpty(IP)) {
            IP = UserPrefer.getIpAddress();
        }

        if (TextUtils.isEmpty(IP)) {
            return;
        }

        URI uri = URI.create(IP);
        if (webSocketClient != null) {
            synchronized (webSocketClient) {
                webSocketClient.disconnect();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webSocketClient.setListener(null);
                webSocketClient = null;
                webSocketClient = new WebSocketClient(context, uri, listener, null);
                webSocketClient.setListener(listener);
                webSocketClient.connect();
            }
        } else {
            webSocketClient = new WebSocketClient(context, uri, listener, null);
        }
        webSocketClient.setListener(listener);
        webSocketClient.connect();
    }

    public void connect() {
        IMAPIProvider.getUnreadSession(start, end, new Callback<IMSessionResponse>() {
            @Override
            public void onResponse(Call<IMSessionResponse> call, Response<IMSessionResponse> response) {
                sessions = response.body().getSessions();
                if (sessions == null) {
                    socketConnect();
                }
                SessionItemDaoHelper.getInstance().updateSessions(sessions);
            }

            @Override
            public void onFailure(Call<IMSessionResponse> call, Throwable t) {

            }
        });
    }

    public void keepBeet() {

    }


    public void sendMsg(MessageItem messageItem) {
        if (webSocketClient == null) {
            return;
        }
        String jsonStr = JSON.toJSONString(messageItem);
        String aesKey = UserPrefer.getAesKey();
        if (aesKey != null) {
            jsonStr = AESUtil.EncryptIM(jsonStr, aesKey);
        }
        send(jsonStr);
    }

    public void send(String msg) {
        webSocketClient.send(msg);
    }


}
