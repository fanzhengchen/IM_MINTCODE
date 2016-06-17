package mintcode.com.workhub_im.daohelper;

import mintcode.com.workhub_im.db.FriendEntityDao;

/**
 * Created by mark on 16-6-17.
 */
public class FriendEntityDaoHelper extends BaseDaoHelper {
    private static FriendEntityDaoHelper daoHelper = null;

    private FriendEntityDao friendEntityDao;
    private FriendEntityDao.Properties properties;

    private FriendEntityDaoHelper() {
        friendEntityDao = daoSession.getFriendEntityDao();
    }

    public static FriendEntityDaoHelper getInstance() {
        if (daoHelper == null) {
            daoHelper = new FriendEntityDaoHelper();
        }
        return daoHelper;
    }
}
