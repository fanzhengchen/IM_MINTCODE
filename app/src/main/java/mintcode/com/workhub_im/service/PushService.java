package mintcode.com.workhub_im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.handler.BeetTimer;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.im.ServiceManager;
import mintcode.com.workhub_im.codebutler.WebSocketClient;
import mintcode.com.workhub_im.util.AESUtil;

/**
 * Created by mark on 16-6-14.
 */
public class PushService extends Service {

    /** SerViceManager连接 动作*/
    public static final String ACTION_CONNECT = "action_connect";
    /** 消息发送 动作*/
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
            BeetTimer.getInstance().startBeet();
            return Service.START_STICKY;
        }
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_CONNECT)) {
            serviceManager.connect();
        } else if (TextUtils.equals(action, ACTION_SEND)) {
            serviceManager.send(intent.getStringExtra(ACTION_SEND));
        }
//        return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    WebSocketClient.Listener msgListener = new WebSocketClient.Listener() {

        @Override
        public void onMessage(byte[] data) {
//            Log.i(TAG, "ThreadName:" + Thread.currentThread().getName() + " "
//                    + "onMessage(byte[]) " + new String(data));
            Log.i("infos", "Listener byte[] : " + new String(data));
        }

        @Override
        public void onMessage(String message) {
            Log.i("msg", "onMessage :---" + message);
            String AES_KEY = UserPrefer.getAesKey();
            if (AES_KEY != null) {
                message = AESUtil.DecryptIM(message, AES_KEY);
            }
//            IMLog.d("msg", "onMessage Decode:---" + message);
            MessageItem item = new MessageItem();
            item.setContent(message);
            String type = item.getType();

            // 记得放开
//            mServiceManager.waitForResponse(true);
//            IMLog.d("msg-fsfdsfdsfd", "type:---" + type);
            if (type.equals(Command.LOGIN_SUCCESS)) {
                // 登录成功！
//                Intent intent = new Intent(MainActivity.KEY_CONNECT_SUCCESS_ACTION);
//                sendBroadcast(intent);

//                notifyStatusChanged(IMMessageListenerConst.SUCCESS, null);
                // 开启心跳包监听，每隔一段时间发送一条请求
                BeetTimer.getInstance().startBeet();
            } else if (type.equals(Command.LOGIN_OUT)) {
//                LauchrConst.PUSH_SERVICE_IS_LOGIN = false;
//                 登录失败！或者已经被登出
//                notifyStatusChanged(IMMessageListenerConst.FAILURE, item.getContent());
                // 关闭心跳
                BeetTimer.getInstance().stopBeet();
                try {
//                    mAIDLGuardServiceInterface.stopService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stopSelf();
            } else if (type.equals(Command.REV)) {
                // 消息收到回执
                item.setSent(Command.SEND_SUCCESS);
//                item.setCmd(MessageItem.TYPE_SEND);
//                mServiceManager.update(item);
//                nofityMessageReceived(item);
//                mServiceManager.removeSendMsgTask(item);
            } else if (type.equals(Command.CMD)) {
//                mServiceManager.cmd(item);
            } else if (isNormalMessage(type)) {
                // 接收普通消息类型
//                dealMessage(item);
//                AppUtil util = AppUtil.getInstance(getApplicationContext());
//                util.saveValue(LauchrConst.KEY_MAX_MSGID, item.getMsgId() + "");
            }
        }

        private boolean isNormalMessage(String type) {
            return type.equals(Command.TEXT) || type.equals(Command.AUDIO)
                    || type.equals(Command.IMAGE) || type.equals(Command.ALERT)
                    || type.equals(Command.VIDEO) || type.equals(Command.READ_SESSION)
                    || type.equals(Command.EVENT) || type.equals(Command.FILE)
                    || type.equals(Command.RESEND) || type.equals(Command.MERGE);
        }

        @Override
        public void onError(Exception error) {
            error.printStackTrace();
        }

        @Override
        public void onDisconnect(int code, String reason) {
            BeetTimer.getInstance().stopBeet();
        }

        @Override
        public void onConnect() {
            serviceManager.login();
        }
    };



}
