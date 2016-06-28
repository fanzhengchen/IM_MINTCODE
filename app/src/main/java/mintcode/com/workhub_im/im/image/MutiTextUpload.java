package mintcode.com.workhub_im.im.image;



import java.util.LinkedList;

import mintcode.com.workhub_im.db.MessageItem;

public class MutiTextUpload {
    private static MutiTextUpload sInstance;
    /**
     * 上传队列
     */
    private LinkedList<MessageItem> mTaskQueue;

    public static MutiTextUpload getInstance() {
        if (sInstance == null) {
            synchronized (MultiTaskUpLoad.class) {
                if (sInstance == null) {
                    sInstance = new MutiTextUpload();
                }
            }
        }
        return sInstance;
    }

    private MutiTextUpload() {
        mTaskQueue = new LinkedList<MessageItem>();
    }

    /** 添加消息到上传队列 */
    public void sendText(final MessageItem item) {
        mTaskQueue.add(item);

        if(mTaskQueue.size() ==1){
            startFirstUpload();
        }
    }

    /** 移除已经发送成功的，并开始下一条发送*/
    public void removeFirstUpload(MessageItem item){
        if(mTaskQueue!=null && mTaskQueue.size()>0 && mTaskQueue.getFirst().getClientMsgId()==item.getClientMsgId()){
            mTaskQueue.removeFirst();
            startFirstUpload();
        }
    }

    /** 开始消息发送*/
    public void startFirstUpload(){
        if(mTaskQueue!=null && mTaskQueue.size()>0){
//            IMManager.getInstance().sendMessageItem(mTaskQueue.getFirst());
        }
    }

}
