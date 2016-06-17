package mintcode.com.workhub_im.daohelper;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.db.SessionItemDao;
import mintcode.com.workhub_im.db.UserDetailEntity;

/**
 * Created by mark on 16-6-15.
 */
public class SessionItemDaoHelper extends BaseDaoHelper {
    private static SessionItemDaoHelper sessionDaoHelper;
    private SessionItemDao sessionItemDao;
    private SessionItemDao.Properties properties;


    public SessionItemDaoHelper() {
        sessionItemDao = daoSession.getSessionItemDao();
    }


    public static SessionItemDaoHelper getInstance() {
        if (sessionDaoHelper == null) {
            sessionDaoHelper = new SessionItemDaoHelper();
        }
        return sessionDaoHelper;
    }

    public SessionItem getSession(String sessionName) {
        List<SessionItem> items = sessionItemDao.queryBuilder().where(
                properties.UserName.eq(UserPrefer.getUserName()),
                properties.OppositeName.eq(sessionName)
        ).list();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public SessionItem getSession(Long sessionId) {
        List<SessionItem> items = sessionItemDao.queryBuilder().where(
                properties.UserName.eq(UserPrefer.getUserName()),
                properties.Id.eq(sessionId)
        ).list();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public List<SessionItem> getListByDesc() {
        return sessionItemDao.queryBuilder()
                .where(properties.UserName.eq(UserPrefer.getUserName()))
                .orderDesc(properties.Time)
                .list();
    }

    public void setReadCount(SessionItem sessionItem) {
//        queryBuilder.where(properties.UserName.eq(sessionItem.getUserName())).
        sessionItemDao.insertOrReplace(sessionItem);
    }

    public void insert(SessionItem item) {
        sessionItemDao.insertOrReplace(item);
    }

    public List<SessionItem> getList() {
        return sessionItemDao.loadAll();
    }

    public void deleteAll() {
        sessionItemDao.deleteAll();
    }
}
