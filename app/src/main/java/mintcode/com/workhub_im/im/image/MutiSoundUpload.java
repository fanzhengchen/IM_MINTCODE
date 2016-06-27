package mintcode.com.workhub_im.im.image;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.pojo.AttachDetail;
import mintcode.com.workhub_im.widget.upload.DownLoadAttachTask;
import mintcode.com.workhub_im.widget.upload.UploadFileTask;

public class MutiSoundUpload {
    private static MutiSoundUpload sInstance;
    /**
     * 上传队列
     */
    private LinkedList<Runnable> mTaskQueue;
    private LinkedList<Runnable> mDownloadQueue;

    /** 消息队列*/
    private LinkedList<MessageItem> mMsgQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    // 初始线程数
    private int mThreadCount = 5;

    /** 是否有任务在执行*/
    private boolean isTaskUpload = false;

    public static MutiSoundUpload getInstance() {
        if (sInstance == null) {
            synchronized (MutiSoundUpload.class) {
                if (sInstance == null) {
                    sInstance = new MutiSoundUpload(1);
                }
            }
        }
        return sInstance;
    }

    private MutiSoundUpload(int mThreadCount) {
        initBackThread();
    }

    /**
     * 初始化后台轮询线程
     */
    public void initBackThread() {
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        if(msg.what == 0x110){
                            Log.i("------task-load", "execute");
//                            mThreadPool.execute(getTask());
                            Thread t = new Thread(getTask());
                            t.start();
                        }else if(msg.what == 0x111){
                            Thread t = new Thread(getDownloadTask());
                            t.start();
//                            mThreadPool.execute(getDownloadTask());
                        }
                    }
                };
                Looper.loop();
            }
        };
        mPoolThread.start();
        mThreadPool = Executors.newFixedThreadPool(mThreadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mDownloadQueue = new LinkedList<Runnable>();
        mMsgQueue = new LinkedList<MessageItem>();
        isTaskUpload = false;
    }

    /** 添加语音到上传队列 */
    public void sendSound(final AttachDetail detail, final File file,
                          final Context mContext, final Handler mUIHandler,
                          final MessageItem item) {
        mMsgQueue.add(item);
        addTask(new Runnable() {
            @Override
            public void run() {
                UploadFileTask task = new UploadFileTask(mContext, detail, file, mUIHandler, item);
                task.execute();
            }
        });

//        if(mPoolThreadHandler != null){
//            mPoolThreadHandler.sendEmptyMessage(0x110);
//        }

        if(mTaskQueue!=null){// && mTaskQueue.size()==1
            isTaskUpload = false;
            startFirstUpload(false);
        }
    }

    /** 开始一个任务*/
    public void startFirstUpload(boolean isFailed){
        if(mPoolThreadHandler != null && (!isTaskUpload || isFailed)){
            Log.i("------startFirstUpload", "startFirstUpload");
            mPoolThreadHandler.sendEmptyMessage(0x110);
            isTaskUpload = true;
        }
    }

    /** 当第一个任务执行成功后结束*/
    public void stopFirstUpload(MessageItem item){
        if(mTaskQueue!=null && mTaskQueue.size()>0 && item.getClientMsgId() == mMsgQueue.getFirst().getClientMsgId()){
            isTaskUpload = false;
            mTaskQueue.removeFirst();
            mMsgQueue.removeFirst();
            if(mTaskQueue != null && mTaskQueue.size()>0){
                startFirstUpload(false);
            }
        }
    }

    /** 添加一个任务 */
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
//        if(mPoolThreadHandler == null || mTaskQueue==null){
//            sInstance = new MutiSoundUpload(1);
//        }
//        if(mTaskQueue.size() == 1){
//            mPoolThreadHandler.sendEmptyMessage(0x110);
//        }
    }

    /**
     * 从任务队列取出一个方法,默认是FIFO
     */
    private Runnable getTask() {
        if (mTaskQueue.isEmpty()){
            return new Runnable() {
                @Override
                public void run() {
                    isTaskUpload = false;
                    Log.e("infos", "taskQueue is empty");
                }
            };
        }else {
            return mTaskQueue.getFirst();
        }
    }

    /** 语音下载队列 */
    public void sendSoundToDownload(final MessageItem item, final Context context, final Handler handler) {
        mDownloadQueue.add(new Runnable() {
            @Override
            public void run() {
                DownLoadAttachTask task;
                task = new DownLoadAttachTask(context, item, handler, false);
                task.execute();
            }
        });
        if(mPoolThreadHandler != null){
            mPoolThreadHandler.sendEmptyMessage(0x111);
        }
    }

    /** 从下载任务队列取出一个执行*/
    private Runnable getDownloadTask(){
        if (mDownloadQueue.isEmpty()){
            return new Runnable() {
                @Override
                public void run() {
                    Log.e("infos","mDownloadQueue is empty");
                }
            };
        }else {
            return mDownloadQueue.removeFirst();
        }
    }
}
