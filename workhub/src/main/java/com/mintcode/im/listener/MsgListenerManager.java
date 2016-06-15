package com.mintcode.im.listener;

import java.util.ArrayList;
import java.util.List;

import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;


public class MsgListenerManager {

    private List<OnIMMessageListener> mListeners = new ArrayList<OnIMMessageListener>();

    private static MsgListenerManager sInstance;

    public static MsgListenerManager getInstance() {
        if (sInstance == null) {
            sInstance = new MsgListenerManager();
        }
        return sInstance;
    }

    public void onStatusChanged(int result, String msg) {
        for (OnIMMessageListener listener : mListeners) {
            listener.onStatusChanged(result, msg);
        }
    }

    public void onMessage(List<MessageItem> items) {
        for (OnIMMessageListener listener : mListeners) {
            listener.onMessage(items, 1);
        }
    }

    public void onSession(List<SessionItem> items) {
        for (OnIMMessageListener listener : mListeners) {
            listener.onSession(items);
        }
    }

    public void setMsgListener(OnIMMessageListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeListener(OnIMMessageListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

}
