package mintcode.com.workhub_im.im;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;

import java.net.URI;
import java.util.ArrayList;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.handler.BeetTimer;
import mintcode.com.workhub_im.im.codebutler.WebSocketClient;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.Info;
import mintcode.com.workhub_im.im.pojo.Session;
import mintcode.com.workhub_im.pojo.HeartBeat;
import mintcode.com.workhub_im.pojo.MergeCard;
import mintcode.com.workhub_im.util.AESUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-14.
 */
public class ServiceManager {

    private static final String TAG = "ServiceManager";
    private Context context;
    private WebSocketClient webSocketClient;
    private WebSocketClient.Listener listener = new WebSocketClient.Listener() {
        @Override
        public void onConnect() {
            Logger.e(TAG + " connect");
            login();
        }

        @Override
        public void onMessage(String message) {
            Logger.i(TAG, "message recevied " + message);
            handleMessage(message);
        }

        @Override
        public void onMessage(byte[] data) {
            Logger.e(TAG + " " + data.toString());
        }

        @Override
        public void onDisconnect(int code, String reason) {
            Logger.e(TAG + " " + "code " + code + " reason " + reason);
        }

        @Override
        public void onError(Exception error) {
            Logger.e(TAG + "  error " + error);
        }
    };

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
                    return;
                }
                SessionItemDaoHelper.getInstance().updateSessions(sessions);
            }

            @Override
            public void onFailure(Call<IMSessionResponse> call, Throwable t) {
                Logger.e(TAG, call.request());
            }
        });
        socketConnect();
    }


    public void login() {
        long currentTime = System.currentTimeMillis();
        long msgId = UserPrefer.getLastMessageId() + 1;
        MessageItem item = new MessageItem();
        item.setAppName(AppConsts.appName);
        item.setUserName(UserPrefer.getImUsername());
        item.setContent(UserPrefer.getImToken());
        item.setMsgId(msgId);
        item.setClientMsgId(currentTime);
        item.setType(Command.LOGIN);
        item.setInfo(UserPrefer.getInfo());
        item.setModified(UserPrefer.getLastMessageId());
        sendMsg(item);
        Logger.i(" im socket login");
    }

    public void keepBeet() {
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setMsgId(UserPrefer.getLastMessageId() + 1);
        heartBeat.setType(Command.LOGIN_KEEP);
        send(JSON.toJSONString(heartBeat), 1);
    }

    public void stopBeet() {
        BeetTimer.getInstance().stopBeet();
        if (webSocketClient != null) {
            webSocketClient.setListener(null);
            webSocketClient.disconnect();
            webSocketClient = null;
        }
    }


    public void setMsg(MessageItem item, int ping) {
        send(MsgToString(item), ping);
    }

    public void sendMsg(MessageItem messageItem) {
        send(MsgToString(messageItem));
    }

    public void send(String msg, int ping) {
        webSocketClient.send(msg, ping);
    }

    public void send(String msg) {
        webSocketClient.send(msg);
    }

    private String MsgToString(MessageItem item) {
        if (webSocketClient == null) {
            return null;
        }
        String jsonStr = JSON.toJSONString(item);
        String aesKey = UserPrefer.getAesKey();
        if (!TextUtils.isEmpty(aesKey)) {
            jsonStr = AESUtil.EncryptIM(jsonStr, aesKey);
        }
        return jsonStr;
    }

    private void handleMessage(String message) {
        String aesKey = UserPrefer.getAesKey();
        if (!TextUtils.isEmpty(aesKey)) {
            message = AESUtil.EncryptIM(message, aesKey);
        }
        MessageItem item = JSON.parseObject(message, MessageItem.class);
        String type = item.getType();
        if (TextUtils.equals(Command.LOGIN_SUCCESS, type)) {
            BeetTimer.getInstance().startBeet();
        } else if (TextUtils.equals(Command.LOGIN_OUT, type)) {
            BeetTimer.getInstance().stopBeet();
        }
    }

}
