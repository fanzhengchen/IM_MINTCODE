package mintcode.com.workhub_im.daohelper;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.db.SessionItemDao;
import mintcode.com.workhub_im.db.UserDetailEntity;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.im.pojo.Info;
import mintcode.com.workhub_im.im.pojo.LastMsg;
import mintcode.com.workhub_im.im.pojo.Session;
import mintcode.com.workhub_im.pojo.MergeCard;
import mintcode.com.workhub_im.pojo.MessageEventEntity;

/**
 * Created by mark on 16-6-15.
 */
public class SessionItemDaoHelper extends BaseDaoHelper {
    private static SessionItemDaoHelper sessionDaoHelper;
    private SessionItemDao sessionItemDao;
    private SessionItemDao.Properties properties;


    public SessionItemDaoHelper() {
        sessionItemDao = daoSession.getSessionItemDao();
    }


    public static SessionItemDaoHelper getInstance() {
        if (sessionDaoHelper == null) {
            sessionDaoHelper = new SessionItemDaoHelper();
        }
        return sessionDaoHelper;
    }

    public SessionItem getSession(String sessionName) {
        List<SessionItem> items = sessionItemDao.queryBuilder().where(
                properties.UserName.eq(UserPrefer.getUserName()),
                properties.OppositeName.eq(sessionName)
        ).list();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public SessionItem getSession(Long sessionId) {
        List<SessionItem> items = sessionItemDao.queryBuilder().where(
                properties.UserName.eq(UserPrefer.getUserName()),
                properties.Id.eq(sessionId)
        ).list();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public List<SessionItem> getListByDesc() {
        return sessionItemDao.queryBuilder()
                .where(properties.UserName.eq(UserPrefer.getUserName()))
                .orderDesc(properties.Time)
                .list();
    }

    public void setReadCount(SessionItem sessionItem) {
//        queryBuilder.where(properties.UserName.eq(sessionItem.getUserName())).
        sessionItemDao.insertOrReplace(sessionItem);
    }

    public void insert(SessionItem item) {
        sessionItemDao.insertOrReplace(item);
    }

    public List<SessionItem> getList() {
        return sessionItemDao.loadAll();
    }

    public void deleteAll() {
        sessionItemDao.deleteAll();
    }




    public void updateSessions(ArrayList<Session> sessions) {
        SessionItemDaoHelper daoHelper = SessionItemDaoHelper.getInstance();
        for (Session session : sessions) {
            if (session == null || session.getLastMsg() == null) {
//                socketConnect();
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
        Context context = App.getGlobalContext();
        return context.getResources().getString(resId);
    }
}
