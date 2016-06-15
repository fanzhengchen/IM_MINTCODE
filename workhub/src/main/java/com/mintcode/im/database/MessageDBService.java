package com.mintcode.im.database;

import java.util.Collections;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.ResendEntity;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.Command;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.pojo.entity.MessageEventEntity;
import com.mintcode.launchr.util.TTJSONUtil;

public class MessageDBService {

    private static MessageDBService sInstance;

    public synchronized static MessageDBService getInstance() {
        Log.i("MessageDBService", "====getInstance=====");
        if (sInstance == null) {
            Log.i("MessageDBService", "====newInstance=====");
            sInstance = new MessageDBService();
        }
        return sInstance;
    }

    /**
     * 当切换账号的时候，必须结束这个单例，不然mUid仍然是原来的
     */
    public void destoryInstance() {
        sInstance = null;
    }

    private String mUid = null;

    /**
     * 插入消息
     *
     * @param item
     */
    public void insert(MessageItem item) {
        Log.i("MessageDBService", "====insert==item==" + item.getContent());
        Log.d("clientMsgId", item.getClientMsgId() + "  " + item.getSent()
                + "db");
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        if (mUid == null) {
            mUid = KeyValueDBService.getInstance().find(Keys.UID);
        }
        if (handleEventAlert(item)) {
            return;
        }
        // 消息撤回
        if (Command.RESEND.equals(item.getType())) {
            ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
            item.setMsgId(entity.getMsgId());
            item.setContent(entity.getContent());
            item.setClientMsgId(entity.getClientMsgId());
        }
        // 如果是CMD消息则跳过
        if (!Command.CMD.equals(item.getType())) {
            if (mUid.equals(item.getFromLoginName())) {
                item.setCmd(MessageItem.TYPE_SEND);
            } else {
                item.setCmd(MessageItem.TYPE_RECV);
            }
            if (!exist(item)) {
                Log.i("MessageDBService", "======" + item.getContent());
                item.save();
            } else {
                List<MessageItem> messageItems = DataSupport.where("clientMsgId = ?",
                        item.getClientMsgId() + "").find(MessageItem.class);
                MessageItem msgItem = messageItems.get(0);


                // 判断是否是应用消息
                if (item.getFromLoginName().contains("@APP")) {
                    // 数据库原有的msgId越小，说明数据越旧，需要更新，更新的时候MsgId不变，跟排序有关
                    if (msgItem.getMsgId() < item.getMsgId()) {
                        long id = msgItem.getId();
                        item.setMsgId(msgItem.getMsgId());
                        item.update(id);
                    }
                } else {
                    long id = msgItem.getId();
                    if (Command.RESEND.equals(item.getType())) {
                        Log.i("infos", "----");
                    } else {

                    }
                    item.setMsgId(msgItem.getMsgId());
                    item.update(id);
                }

            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 批量插入消息
     *
     * @param items
     */
    public void insert(List<MessageItem> items) {
        Log.i("MessageDBService", "====insert===List====item==" + items.size());
        if (items == null) {
            return;
        }
//		SQLiteDatabase db = Connector.getDatabase();
//		db.beginTransaction();
        for (MessageItem item : items) {
            Log.i("MessageItem", "====item====");
            item.setIsResp(0);
            item.setSent(Command.SEND_SUCCESS);

            if (mUid == null) {
                mUid = KeyValueDBService.getInstance().find(Keys.UID);
            }
            if (handleEventAlert(item)) {
                continue;
            }
            if (mUid.equals(item.getFromLoginName())) {
                item.setCmd(MessageItem.TYPE_SEND);
            } else {
                item.setCmd(MessageItem.TYPE_RECV);
            }
            // 如果是CMD消息则跳过
            if (Command.CMD.equals(item.getType())) {
                continue;
            }
            // 消息撤回
            if (Command.RESEND.equals(item.getType())) {
                ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
                item.setMsgId(entity.getMsgId());
                item.setContent(entity.getContent());
                item.setClientMsgId(entity.getClientMsgId());
            }
            if (!exist(item)) {
                item.save();

            } else {
                List<MessageItem> messageItems = DataSupport.where("clientMsgId = ?",
                        item.getClientMsgId() + "").find(MessageItem.class);
                MessageItem msgItem = messageItems.get(0);

                // 判断是否是应用消息
                if (item.getFromLoginName().contains("@APP")) {
                    // 数据库原有的msgId越小，说明数据越旧，需要更新，更新的时候MsgId不变，跟排序有关
                    if (msgItem.getMsgId() < item.getMsgId()) {
                        long id = msgItem.getId();
//						item.setMsgId(msgItem.getMsgId());
                        item.update(id);
                    }
                } else {
                    long id = msgItem.getId();
                    item.setMsgId(msgItem.getMsgId());
                    item.update(id);
                }


            }
        }
//		db.setTransactionSuccessful();
//		db.endTransaction();
        Log.i("MessageDBService", "====endTransaction===");
    }

    /**
     * 如果Event中用户操作引起的推送，则过滤
     */
    private boolean handleEventAlert(MessageItem item) {
        if (item.getType().equals(Command.EVENT)) {
            MessageEventEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventEntity>() {
            });
            if (eventEntity != null && eventEntity.getMsgType() == 0) {
                String userName = KeyValueDBService.getInstance().find(Keys.INFO);
                Info info = JsonUtil.convertJsonToCommonObj(userName, Info.class);
                if (item.getInfo() != null && info != null && eventEntity.getMsgTitle().contains(info.getNickName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断item是否存在
     *
     * @param item
     * @return
     */
    public boolean exist(MessageItem item) {
//		Log.i("MessageDBService", "====exist=====item==");
        Log.d("exist", "clientMsgId = " + item.getClientMsgId());
        List<MessageItem> messageItem = DataSupport.where("clientMsgId = ?",
                item.getClientMsgId() + "").find(MessageItem.class);
        Log.d("exist", "exist " + messageItem.size());
        return messageItem.size() == 0 ? false : true;
    }


    public boolean existMessageId(MessageItem item) {
        // 消息撤回
        boolean b = false;
        if (Command.RESEND.equals(item.getType())) {
            ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
            if (entity != null) {
//				String clientId = entity.getClientMsgId();
//				List<MessageItem> messageItem = DataSupport.where("msgId = ?",
//						entity.getMsgId() + "").find(MessageItem.class);
                List<MessageItem> messageItem = DataSupport.where("msgId = ?",
                        entity.getMsgId() + "").find(MessageItem.class);
                b = messageItem.size() == 0 ? false : true;
            }
        }
        return b;
    }


    /**
     * 根据clientMsgId来更新对应消息
     *
     * @param item
     */
    public void update(MessageItem item) {
        Log.i("MessageDBService", "====update====item==");
        Log.d("clientMsgId", item.getClientMsgId() + "  " + item.getSent() + "");
        int i = item.updateAll("clientMsgId = ?", item.getClientMsgId() + "");
        Log.d("clientMsgId", i + "");
    }

    /**
     * 获取历史消息，排序根据clientMsgId
     *
     * @param fromId
     * @param toId
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<MessageItem> getMessageByIndexForDesc(String fromId,
                                                      String toId, int startIndex, int endIndex) {
        Log.i("MessageDBService", "====getMessageByIndexForDesc====item==");
        String whereSql = "(fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId + "')";
        List<MessageItem> list = DataSupport.where(whereSql).order("msgId desc")//("msgId desc ,clientMsgId desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);


        Collections.reverse(list);

//		String whereSql = "(fromLoginName = '" + fromId
//				+ "' and toLoginName = '" + toId + "') or (toLoginName = '"
//				+ fromId + "' and fromLoginName = '" + toId + "') order by msgId desc";
//
//		List<MessageItem> list = DataSupport.where(whereSql).find(MessageItem.class);

        return list;
    }


    /**
     * 获取某个Id前10条的聊天记录
     */
    public List<MessageItem> getLastTenMsgList(String fromId,
                                               String toId, long id, int offset) {
        String whereSql = "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId + "')) and msgId < " + id;
        List<MessageItem> list = DataSupport.where(whereSql).order("msgId desc")
                .limit(10).offset(offset)
                .find(MessageItem.class);
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取某个Id后10条的聊天记录
     */
    public List<MessageItem> getNextTenMsgList(String fromId,
                                               String toId, long id, int offset) {
        String whereSql = "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId + "')) and msgId >= " + id;
        List<MessageItem> list = DataSupport.where(whereSql).order("msgId asc")
                .limit(10).offset(offset)
                .find(MessageItem.class);
        return list;
    }

    /**
     * 查询历史消息
     */
    public List<MessageItem> getHistoryMsgList(String fromId,
                                               String toId, long id, int offset) {
        String whereSql = "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId + "')) and msgId >= " + id;
        List<MessageItem> list = DataSupport.where(whereSql).order("msgId asc")
                .limit(10).offset(offset)
                .find(MessageItem.class);
        return list;
    }

    /**
     * 根据clientMsgId删除消息
     *
     * @param clientMsgId
     */
    public void deletMsg(long clientMsgId) {
        DataSupport.deleteAll(MessageItem.class, "clientMsgId = ?", clientMsgId + "");
    }

    /**
     * 通过关键字搜索与某人的聊天记录
     */
    public List<MessageItem> searchMsg(String fromId, String toId,
                                       int startIndex, int endIndex, String type, String searchWord) {
        String whereSql = "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId
                + "')) and (content like '%" + searchWord + "%') and (type='" + type + "')";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);
        Collections.reverse(list);
        return list;
    }

    /**
     * 搜索全部聊天记录
     */
    public List<MessageItem> searchMsg(int startIndex, int endIndex,
                                       String type, String searchWord) {
        String whereSql = "(content like '%" + searchWord + "%') and ( type='" + type + "')";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);
        Collections.reverse(list);
        return list;
    }

    /**
     * 搜索任务、日程、申请
     */
    public List<MessageItem> searchEvent(String type, String searchWord) {
        String whereSql = "(content like '%" + searchWord + "%') and ( type='" + type + "')";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .find(MessageItem.class);
        return list;
    }

    /**
     * 根据type搜索全部带mark的记录
     */
    public List<MessageItem> searchMarkMsg(int startIndex, int endIndex, String type) {
        String whereSql = "(isMarkPoint=1) and ( type='" + type + "')";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);
        return list;
    }

    /**
     * 根据type和id搜索带mark的记录
     */
    public List<MessageItem> searchMarkMsg(String fromId, String toId, int startIndex, int endIndex,
                                           String type) {
        String whereSql = ""
                + "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId
                + "')) and (isMarkPoint=1) and type <>  '" + Command.RESEND + "'";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);
        Collections.reverse(list);
        return list;
    }

    /**
     * 根据type和id搜索带mark的记录
     */
    public List<MessageItem> searchMarkMsg(String fromId, String toId, int startIndex, int endIndex,
                                           String type, int markPoint) {
        String whereSql = ""
                + "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId
                + "')) and (isMarkPoint = " + markPoint + ")";
//		Log.i("infos","whereSql==" + whereSql);

//		String whereSql = "fromLoginName = '" + fromId + "' and isMarkPoint = '" + markPoint + "'";
        Log.i("infos", "whereSql==" + whereSql);

        List<MessageItem> list = DataSupport.where(whereSql).order("id desc")
                .limit(endIndex - startIndex).offset(startIndex)
                .find(MessageItem.class);
        Collections.reverse(list);
        return list;
    }


    /**
     * 删除与某人的聊天记录
     */
    public void delHistoryMsg(String fromId, String toId) {
        String whereSql = "(fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '"
                + fromId + "' and fromLoginName = '" + toId + "')";
        DataSupport.deleteAll(MessageItem.class, whereSql);
    }


    /**
     * 获得某人发送未读非语言的msgId
     */
    public List<MessageItem> findUnReadRevMsgId(String fromId, String toId) {
        String whereSql = "(fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') and (type <> 'Audio') and (isRead = 0)";
        List<MessageItem> list = DataSupport.where(whereSql).order("id desc").find(MessageItem.class);
        return list;
    }

    /**
     * 更新与某人的已读记录(非语音)
     */
    public void updateReadSession(String fromId, String toId, long msgId) {
        String whereSql = "(fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "' and type <> 'Audio' and  msgId <= " + msgId + ")";
        ContentValues values = new ContentValues();
        values.put("isRead", 1);
        DataSupport.updateAll(MessageItem.class, values, whereSql);
    }

    /**
     * 更新与某人的已读记录(语音)
     */
    public void updateAudioReadSession(String fromId, String toId, long msgId) {
        String whereSql = "((fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "') or (toLoginName = '" +
                fromId + "' and fromLoginName = '" + toId +
                "')) and (type = 'Audio' and  msgId = " + msgId + ")";
        ContentValues values = new ContentValues();
        values.put("isRead", 1);
        DataSupport.updateAll(MessageItem.class, values, whereSql);
    }

    /**
     * 更新与某人的已读记录(语音)
     */
    public void updateCommonReadSession(String fromId, String toId, long msgId) {
        String whereSql = "(fromLoginName = '" + fromId
                + "' and toLoginName = '" + toId + "' and  msgId = " + msgId + ")";
        ContentValues values = new ContentValues();
        values.put("isRead", 1);
        DataSupport.updateAll(MessageItem.class, values, whereSql);
    }

    /**
     * 标记重点
     */
    public void setMark(String fromId, String toId, long msgId, int mark) {
        String whereSql = "((fromLoginName = '" + fromId + "' and toLoginName = '" + toId + "') or (toLoginName='"
                + fromId + "' and fromLoginName = '" + toId +
                "')) and  (msgId = " + msgId + ")";
        ContentValues values = new ContentValues();
        values.put("isMarkPoint", mark);
        DataSupport.updateAll(MessageItem.class, values, whereSql);
    }

}
