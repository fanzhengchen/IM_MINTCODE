package mintcode.com.workhub_im.widget;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.pojo.MessageInfoEntity;
import mintcode.com.workhub_im.service.PushService;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.widget.emoji.ParseEmojiMsgUtil;

/**
 * Created by JulyYu on 2016/6/12.
 */
public class IMChatManager {


    private static IMChatManager instance;
    private HashSet<MessageInfoEntity> mSet;

    public static synchronized IMChatManager getInstance() {
        if (instance == null) {
            instance = new IMChatManager();
        }
        return instance;
    }

    private IMChatManager() {
        mSet = new HashSet<MessageInfoEntity>();
    }

    public MessageItem sendTextMsg(Context context, String text, String uid, String myName, String to, String toNickName) {

        String message = ParseEmojiMsgUtil.convertDraToMsg(text, context);
        if (TextUtils.isEmpty(message)) {
            return null;
        }
        MessageItem item = new MessageItem();
        item.setCmd(ChatViewUtil.TYPE_SEND);
        item.setContent(message);
        item.setType(Command.TEXT);
        item.setSent(Command.SEND_FAILED);
        item.setClientMsgId(System.currentTimeMillis());
        item.setCreateDate(System.currentTimeMillis());
        item.setMsgId(item.getCreateDate());
        item.setFrom(uid);
        item.setTo(to);
        item.setMsgId(UserPrefer.getLastMessageId() + 1);
        item.setToNickName(toNickName);
        item.setNickName(myName);
        if (!mSet.isEmpty()) {
            Iterator it = mSet.iterator();
            List<String> list = new ArrayList<String>();
            while (it.hasNext()) {
                MessageInfoEntity entity = (MessageInfoEntity) it.next();
                String nickName = entity.getNickName();
                if (message.contains("@" + nickName + " ")) {
                    String atPerson = entity.getUserName() + "@" + entity.getNickName();
                    if (!list.contains(atPerson)) {
                        list.add(atPerson);
                    }
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("atUsers", list);
//                MTHttpParameters p = new MTHttpParameters();
//                p.setParameter("atUsers", list);
            item.setInfo(JSON.toJSONString(map));
        }
        mSet.clear();
        if (TextUtils.isEmpty(item.getType())) {
            item.setType(Command.TEXT);
        }
        String msgJson = JSON.toJSONString(item);
        return item;
    }


    /**
     * 是否添加新的@人员
     */
    public boolean addAtMessageInfoEntity(MessageInfoEntity entity) {
        if (mSet.contains(entity)) {
            return false;
        } else {
            mSet.add(entity);
            return true;
        }
    }

    /**
     * 清空@人员
     */
    public void clearMessageInfoEntity() {
        if (mSet != null) {
            mSet.clear();
        }
    }

    /**
     * 删除@人员
     */
    public boolean delectAtMessageInfoEntity(Editable editable) {
        if (!mSet.isEmpty()) {
            int len = editable.length();
            Iterator it = mSet.iterator();
            HashSet<MessageInfoEntity> set = new HashSet<MessageInfoEntity>();
            while (it.hasNext()) {
                MessageInfoEntity entity = (MessageInfoEntity) it.next();
                String nickName = entity.getNickName();
                String nick = "@" + nickName + " ";
                if (editable.toString().endsWith(nick)) {
                    set.add(entity);
                    int start = len - nick.length();
                    editable.delete(start, len);
                    break;
                }
            }
            if (set.isEmpty()) {
                return false;
            } else {
                mSet.removeAll(set);
                return true;
            }
        }
        return false;
    }
    /** 设置会话已读*/
//    public void setReadState(List<Map> unreadMsgId ,String strTo,String strUid){
//        List<MessageItem> unreadMsg = MessageDBService.getInstance().findUnReadRevMsgId(strTo, strUid);
//        for (int i = 0; i < unreadMsg.size(); i++) {
//            MessageItem item = unreadMsg.get(i);
//            // 收到的信息才做已读处理
//            if (item.getCmd() == MessageItem.TYPE_RECV) {
//                // 设置已读参数
//                MTHttpParameters p = new MTHttpParameters();
//                p.setParameter("msgId", item.getMsgId());
//                p.setParameter("from", item.getFromLoginName());
//                Map map = p.getMap();
//                unreadMsgId.add(map);
//            }
//        }
//    }
}
