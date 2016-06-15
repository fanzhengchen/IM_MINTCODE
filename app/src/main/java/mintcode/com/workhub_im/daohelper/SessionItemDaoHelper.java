package mintcode.com.workhub_im.daohelper;

import mintcode.com.workhub_im.db.SessionItemDao;

/**
 * Created by mark on 16-6-15.
 */
public class SessionItemDaoHelper extends BaseDaoHelper {
    private static SessionItemDaoHelper sessionDaoHelper;
    private SessionItemDao sessionItemDao;


    public SessionItemDaoHelper(String dbName) {
        super(dbName);
        sessionItemDao = daoSession.getSessionItemDao();
    }


    public static SessionItemDaoHelper getInstance(String dbName) {
        if (sessionDaoHelper == null) {
            sessionDaoHelper = new SessionItemDaoHelper(dbName);
        }
        return sessionDaoHelper;
    }
}
