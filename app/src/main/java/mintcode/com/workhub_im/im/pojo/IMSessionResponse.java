package mintcode.com.workhub_im.im.pojo;

import java.util.ArrayList;

/**
 * Created by mark on 16-6-15.
 */
public class IMSessionResponse extends IMBaseResponse {
    private ArrayList<Session> sessions;

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
}
