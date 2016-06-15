package com.mintcode.chat.pojo;


import com.mintcode.chat.entity.HistoryMsgEntity;
import com.mintcode.im.entity.MessageItem;

import java.util.List;

/**
 * Created by KevinQiao on 2016/1/15.
 */
public class SessionHistoryMessagePOJO extends IMBasePOJO {

    /** 消息list */
    private List<HistoryMsgEntity> msg;

    public List<HistoryMsgEntity> getMsg() {
        return msg;
    }

    public void setMsg(List<HistoryMsgEntity> msg) {
        this.msg = msg;
    }

}
