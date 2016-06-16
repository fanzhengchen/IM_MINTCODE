package mintcode.com.workhub_im.pojo;

import java.util.ArrayList;

/**
 * Created by mark on 16-6-16.
 */
public class MergeCard {
    private ArrayList<MergeEntity> msg;
    private String title;

    public ArrayList<MergeEntity> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<MergeEntity> msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
