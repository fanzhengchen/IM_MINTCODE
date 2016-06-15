package mintcode.com.workhub_im.daohelper;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.db.SessionItemDao;

/**
 * Created by mark on 16-6-15.
 */
public class SessionItemDaoHelper extends BaseDaoHelper {
    private static SessionItemDaoHelper sessionDaoHelper;
    private SessionItemDao sessionItemDao;
    private QueryBuilder queryBuilder;
    private SessionItemDao.Properties properties;


    public SessionItemDaoHelper(String dbName) {
        super(dbName);
        sessionItemDao = daoSession.getSessionItemDao();
        queryBuilder = sessionItemDao.queryBuilder();
    }


    public static SessionItemDaoHelper getInstance(String dbName) {
        if (sessionDaoHelper == null) {
            sessionDaoHelper = new SessionItemDaoHelper(dbName);
        }
        return sessionDaoHelper;
    }

    public SessionItem getSession(String sessionName) {
//        sessionItemDao.queryBuilder().
        List<SessionItem> items = queryBuilder.where(
                properties.UserName.eq(sessionName),
                properties.OppositeName.eq(sessionName)
        ).list();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }
}
