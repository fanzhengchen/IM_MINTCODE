package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGen {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "mintcode.com.workhub_im.db");
        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, "./app/src/main/java");
    }


    private Entity addSessionItem(Schema schema) {
        Entity sessionItem = schema.addEntity("SessionItem");
        sessionItem.addIdProperty();
        sessionItem.addStringProperty("userName");
        sessionItem.addStringProperty("nickName");
        sessionItem.addStringProperty("oppositeName");
        sessionItem.addStringProperty("content");
        sessionItem.addLongProperty("time");
        sessionItem.addStringProperty("info");
        sessionItem.addIntProperty("unread");
        sessionItem.addIntProperty("chatRoom");
        sessionItem.addIntProperty("sort");
        sessionItem.addIntProperty("recieve");
        sessionItem.addIntProperty("drafts");
        sessionItem.addLongProperty("clientMsgId");
        sessionItem.addLongProperty("sessionTime");
        return sessionItem;
    }

    private Entity addMessageItem(Schema schema) {
        Entity messageItem = schema.addEntity("MessageItem");
        messageItem.addIdProperty();
        messageItem.addStringProperty("appName");
        messageItem.addStringProperty("userName");
        messageItem.addLongProperty("modified");
        messageItem.addStringProperty("nickName");
        messageItem.addLongProperty("msgId");
        messageItem.addStringProperty("fromLoginName");
        messageItem.addStringProperty("toLoginName");
        messageItem.addStringProperty("info");
        messageItem.addStringProperty("content");
        messageItem.addLongProperty("clientMsgId");
        messageItem.addLongProperty("createDate");
        messageItem.addStringProperty("type");
        messageItem.addIntProperty("cmd");
        messageItem.addStringProperty("timeText");
        messageItem.addStringProperty("fileName");
        messageItem.addStringProperty("fileSize");
        messageItem.addStringProperty("isRead");
        messageItem.addIntProperty("actionSend");
        messageItem.addIntProperty("isMarkPoint");
        messageItem.addIntProperty("isResp");
        messageItem.addLongProperty("send_time");
        messageItem.addStringProperty("toNickName");
        messageItem.addStringProperty("percent");
        return messageItem;
    }
}
