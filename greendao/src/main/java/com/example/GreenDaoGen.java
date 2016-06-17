package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGen {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "mintcode.com.workhub_im.db");
        DaoGenerator daoGenerator = new DaoGenerator();
        addSessionItem(schema);
        addMessageItem(schema);
        addDepartmentEntity(schema);
        addGroupInfo(schema);
        addContactUser(schema);
        addKeyValue(schema);
        addUserDetailEntity(schema);
        addMessageId(schema);
        addFriendEntity(schema);
        addChatGroupEntity(schema);
        daoGenerator.generateAll(schema, "./app/src/main/java");
    }


    private static void addSessionItem(Schema schema) {
        Entity sessionItem = schema.addEntity("SessionItem");
        sessionItem.addIdProperty();
        sessionItem.addStringProperty("userName");
        sessionItem.addStringProperty("nickName");
        sessionItem.addStringProperty("oppositeName");
        sessionItem.addStringProperty("content");
        sessionItem.addLongProperty("time");
        sessionItem.addStringProperty("info");
        sessionItem.addIntProperty("unread");
        sessionItem.addLongProperty("modified");
        sessionItem.addIntProperty("chatRoom");
        sessionItem.addIntProperty("sort");
        sessionItem.addIntProperty("recieve");
        sessionItem.addIntProperty("drafts");
        sessionItem.addLongProperty("clientMsgId");
        sessionItem.addLongProperty("sessionTime");
    }

    private static void addMessageItem(Schema schema) {
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
        messageItem.addIntProperty("sent");
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
    }

    private static void addMessageId(Schema schema) {
        Entity messageId = schema.addEntity("MessageId");
        messageId.addIdProperty();
        messageId.addLongProperty("msgId");
        messageId.addStringProperty("uid");
    }

    private static void addKeyValue(Schema schema) {
        Entity keyValue = schema.addEntity("KeyValue");
        keyValue.addStringProperty("key");
        keyValue.addStringProperty("value");
    }

    private static void addGroupInfo(Schema schema) {
        Entity groupInfo = schema.addEntity("GroupInfo");
        groupInfo.addIdProperty();
        groupInfo.addStringProperty("userName");
        groupInfo.addStringProperty("nickName");
        groupInfo.addStringProperty("type");
        groupInfo.addStringProperty("avatar");
        groupInfo.addLongProperty("modified");
    }

    private static void addContactUser(Schema schema) {
        Entity contactUser = schema.addEntity("ContactUser");
        contactUser.addIdProperty();
        contactUser.addStringProperty("userId");
        contactUser.addStringProperty("updateTime");
        contactUser.addStringProperty("userName");
        contactUser.addStringProperty("departId");
    }

    private static void addDepartmentEntity(Schema schema) {
        Entity department = schema.addEntity("DepartmentEntity");
        department.addIdProperty();
        department.addStringProperty("showId");
        department.addStringProperty("cShowId");
        department.addStringProperty("dName");
        department.addStringProperty("dLevel");
        department.addStringProperty("dParentName");
        department.addStringProperty("dParentShowId");
        department.addStringProperty("cName");
        department.addStringProperty("createUserName");
        department.addIntProperty("dSort");
        department.addIntProperty("dAvailableCount");
        department.addIntProperty("dUnAvailableCount");
        department.addIntProperty("childDeptCount");
        department.addStringProperty("extendField");
        department.addStringProperty("otherField");
    }

    private static void addUserDetailEntity(Schema schema) {
        Entity userDetail = schema.addEntity("UserDetailEntity");
        userDetail.addIdProperty();
        userDetail.addStringProperty("showId");
        userDetail.addStringProperty("name");
        userDetail.addStringProperty("trueName");
        userDetail.addStringProperty("trueNameC");
        userDetail.addStringProperty("mail");
        userDetail.addStringProperty("status");
        userDetail.addStringProperty("mobile");
        userDetail.addStringProperty("job");
        userDetail.addStringProperty("number");
        userDetail.addStringProperty("sort");
        userDetail.addStringProperty("lastLoginTime");
        userDetail.addStringProperty("lastLoginToken");
        userDetail.addStringProperty("isAdmin");
        userDetail.addStringProperty("cShowId");
        userDetail.addStringProperty("createUser");
        userDetail.addStringProperty("createTime");
        userDetail.addStringProperty("dName");
        userDetail.addStringProperty("deptId");
        userDetail.addStringProperty("parentShowId");
        userDetail.addStringProperty("pathName");
        userDetail.addStringProperty("hira");
        userDetail.addStringProperty("telephone");
        userDetail.addStringProperty("otherField");
        userDetail.addLongProperty("launchrId");
    }

    private static void addFriendEntity(Schema schema) {
        Entity friendEntity = schema.addEntity("FriendEntity");
        friendEntity.addIdProperty();
        friendEntity.addStringProperty("appName");
        friendEntity.addStringProperty("userName");
        friendEntity.addStringProperty("relationName");
        friendEntity.addStringProperty("remark");
        friendEntity.addStringProperty("tag");
        friendEntity.addIntProperty("relationGroupId");
        friendEntity.addLongProperty("createDate");
        friendEntity.addStringProperty("isPass");
        friendEntity.addStringProperty("nickName");
        friendEntity.addStringProperty("relationAvatar");
        friendEntity.addLongProperty("relationModified");
        friendEntity.addStringProperty("mobile");
        friendEntity.addStringProperty("imNumber");
    }

    private static void addChatGroupEntity(Schema schema) {
        Entity groupEntity = schema.addEntity("ChatGroupEntity");
        groupEntity.addIdProperty();
        groupEntity.addStringProperty("userName");
        groupEntity.addStringProperty("nickName");
        groupEntity.addStringProperty("type");
        groupEntity.addIntProperty("receiveMode");
        groupEntity.addStringProperty("avatar");
        groupEntity.addLongProperty("modified");
        groupEntity.addStringProperty("tag");
    }
}
