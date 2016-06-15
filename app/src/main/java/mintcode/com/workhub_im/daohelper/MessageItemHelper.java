package mintcode.com.workhub_im.daohelper;

/**
 * Created by mark on 16-6-15.
 */
public class MessageItemHelper extends BaseDaoHelper {

    public static MessageItemHelper messageItemHelper = null;

    public MessageItemHelper(String name) {
        super(name);
    }

    public static MessageItemHelper getInstance(String dbName) {
        if (messageItemHelper == null) {
            messageItemHelper = new MessageItemHelper(dbName);
        }
        return messageItemHelper;
    }
}
