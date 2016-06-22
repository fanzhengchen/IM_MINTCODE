package mintcode.com.workhub_im.callback;

import mintcode.com.workhub_im.db.MessageItem;

/**
 * Created by mark on 16-6-22.
 */
public interface ChatMessageListener {

    public void receiveMessage(final MessageItem item);
}
