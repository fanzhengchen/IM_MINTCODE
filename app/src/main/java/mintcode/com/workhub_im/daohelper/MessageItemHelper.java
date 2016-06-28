package mintcode.com.workhub_im.daohelper;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.db.MessageItemDao;
import mintcode.com.workhub_im.db.SessionItemDao;

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

    public long getMsgId() {
        return 0;
    }

    public void insert(List<MessageItem> items) {
        if (items == null) {
            return;
        }
    }

    public void insert(MessageItem item) {
        List<MessageItem> items = messageItemDao.queryBuilder().where(
                properties.ClientMsgId.eq(item.getClientMsgId())
        ).list();
        if (items == null || items.isEmpty()) {
            messageItemDao.insert(item);
        }
    }
}
