package mintcode.com.workhub_im.im.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

import mintcode.com.workhub_im.db.MessageItem;

/**
 * Created by mark on 16-6-20.
 */
public class IMMessageResponse extends IMBaseResponse {

    private ArrayList<MessageItem> msg;

    public ArrayList<MessageItem> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<MessageItem> msg) {
        this.msg = msg;
    }
}
