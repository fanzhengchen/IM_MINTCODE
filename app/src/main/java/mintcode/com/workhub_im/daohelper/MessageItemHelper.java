package mintcode.com.workhub_im.daohelper;

import de.greenrobot.dao.query.QueryBuilder;
import mintcode.com.workhub_im.db.MessageItemDao;

/**
 * Created by mark on 16-6-15.
 */
public class MessageItemHelper extends BaseDaoHelper {

    private static MessageItemHelper messageItemHelper = null;
    private MessageItemDao messageItemDao;
    private QueryBuilder queryBuilder;
    private MessageItemDao.Properties properties;

    public MessageItemHelper() {
        messageItemDao = daoSession.getMessageItemDao();

    }

    public static MessageItemHelper getInstance() {
        if (messageItemHelper == null) {
            messageItemHelper = new MessageItemHelper();
        }
        return messageItemHelper;
    }
}
