package mintcode.com.workhub_im.im;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.net.URI;
import java.util.ArrayList;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.im.codebutler.WebSocketClient;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.Info;
import mintcode.com.workhub_im.im.pojo.LastMsg;
import mintcode.com.workhub_im.im.pojo.Session;
import mintcode.com.workhub_im.pojo.MergeCard;
import mintcode.com.workhub_im.pojo.MessageEventEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-14.
 */
public class ServiceManager {

    private Context context;
    private WebSocketClient socketClient;
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

    public void connect() {
        IMAPIProvider.getUnreadSession(start, end, new Callback<IMSessionResponse>() {
            @Override
            public void onResponse(Call<IMSessionResponse> call, Response<IMSessionResponse> response) {
                sessions = response.body().getSessions();
                updateSessions(sessions);
            }

            @Override
            public void onFailure(Call<IMSessionResponse> call, Throwable t) {

            }
        });
    }

    private void updateSessions(ArrayList<Session> sessions) {
        SessionItemDaoHelper daoHelper = SessionItemDaoHelper.getInstance();
        for (Session session : sessions) {
            if (session == null || session.getLastMsg() == null) {
                socketConnect();
                continue;
            }
            LastMsg lastMsg = session.getLastMsg();
            SessionItem sessionItem = daoHelper.getSession(session.getSessionName());
            if (sessionItem == null) {
                sessionItem = new SessionItem();
            }
            String sessionName = session.getSessionName();
            Info info = null;
            /*
            1 denotes this is a group session 0 otherwise
            * */
            if (isGroupSession(sessionName)) {
                info = JSON.parseObject(lastMsg.getInfo(), Info.class);
                if (info != null) {
                    sessionItem.setNickName(info.getSessionName());
                }
                sessionItem.setChatRoom(1);
            } else {
                sessionItem.setNickName(session.getNickName());
            }
            sessionItem.setOppositeName(session.getSessionName());
            if (session.getModified() > 0) {
                sessionItem.setModified(session.getModified());
            }
            sessionItem.setTime(lastMsg.getCreateDate());
            sessionItem.setUserName(UserPrefer.getUserName());
            sessionItem.setContent(setSessionContent(lastMsg, sessionItem, info));
            sessionItem.setUnread(0);
            daoHelper.insert(sessionItem);
        }
    }

    public String setSessionContent(LastMsg item, SessionItem sessionItem, Info info) {
        String content = null;
        boolean isSystemMsg = false;
        String type = item.getType();
        if (TextUtils.equals(Command.AUDIO, type)) {
            content = getString(R.string.im_session_audio);
        } else if (TextUtils.equals(Command.IMAGE, type)) {
            content = getString(R.string.im_session_image);
        } else if (TextUtils.equals(Command.VIDEO, type)) {
            content = getString(R.string.im_session_video);
        } else if (TextUtils.equals(Command.EVENT, type)) {
            content = getTaskMessageContent(item);
        } else if (TextUtils.equals(Command.MERGE, type)) {
            content = getMergeMsgTitle(item.getContent());
        } else {
            content = item.getContent();
            if (TextUtils.equals(Command.TEXT, type)) {
                isSystemMsg = true;
            }
        }

        if (isGroupSession(sessionItem.getOppositeName())) {
            if (info != null) {
                content = info.getNickName() + ":" + content;
            }
        }
        return content;
    }

    private static boolean isGroupSession(String sessionName) {
        return (sessionName.contains(AppConsts.CHAT_ROOM) || sessionName.contains(AppConsts.SUPER_GROUP));
    }

    private static String getFileName(MessageItem item) {
        Info info = JSON.parseObject(item.getInfo(), Info.class);
        if (info == null) {
            return "";
        }
        return info.getNickName() + ":";
    }

    private static String getTaskMessageContent(LastMsg item) {
        String result = null;
        MessageEventEntity eventEntity = JSON.parseObject(item.getContent(), MessageEventEntity.class);
        if (eventEntity != null) {
            result = eventEntity.getMsgTitle();
        }
        return result;
    }

    private static String getMergeMsgTitle(String content) {
        MergeCard card = JSON.parseObject(content, MergeCard.class);
        return card.getTitle();
    }

    private String getString(int resId) {
        return context.getResources().getString(resId);
    }


}
