package com.mintcode.chat.image;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mintcode.chat.activity.ChatActivity.HandleMsgType;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.chat.util.DownLoadAttachTask;
import com.mintcode.chat.util.UploadFileTask;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * 用于处理多图片上传,下载
 * 
 * @author ChristLu
 * 
 */
public class MutiTaskUpLoad {

	private static MutiTaskUpLoad sInstance;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;

	/** 任务下载队列*/
	private LinkedList<Runnable> mDownloadQueue;

	/** 消息队列*/
	private LinkedList<MessageItem> mMsgQueue;

	/**
	 * 后台轮询线程
	 */
	private Thread mPoolThread;
	private static Handler mPoolThreadHandler;

	/**
	 * UI线程中的Handler
	 */
	private Handler mUIHandler;

	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	// 初始线程数
	private int mThreadCount = 1;

	/**
	 * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
	 */
//	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
//	private Semaphore mSemaphoreThreadPool;

	public static MutiTaskUpLoad getInstance() {
		if (sInstance == null || mPoolThreadHandler==null) {
			synchronized (MutiTaskUpLoad.class) {
				if (sInstance == null || mPoolThreadHandler==null) {
					sInstance = new MutiTaskUpLoad(4);
				}
			}
		}
		return sInstance;
	}

	private MutiTaskUpLoad(int mThreadCount) {
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
							mThreadPool.execute(getTask());
						}else if(msg.what == 0x111){
							mThreadPool.execute(getDownloadTask());
						}
//						try {
//							mSemaphoreThreadPool.acquire();
//						} catch (InterruptedException e) {
//						}
					}
				};
				// 释放一个信号量
//				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			}
		};
		mPoolThread.start();
		mThreadPool = Executors.newFixedThreadPool(mThreadCount);
//		mSemaphoreThreadPool = new Semaphore(mThreadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mDownloadQueue = new LinkedList<Runnable>();
		mMsgQueue = new LinkedList<MessageItem>();
	}

	// 图片上传队列
	public void sendMsg(final AttachDetail detail, final String filePath,
			final Context mContext, final Handler mUIHandler,
			final MessageItem item) {
		mMsgQueue.add(item);
		addTask(new Runnable() {
			@Override
			public void run() {
//				MImageCache cache = MImageCache.getInstance(mContext);
//				File file = new File(filePath);
				Bitmap bmp = BitmapFactory.decodeFile(filePath);
				String p = Environment.getExternalStorageDirectory() + "/launchr/send/" + LauchrConst.header.getLoginName() + "/"
						+ System.currentTimeMillis() + ".jpg";
				boolean b = BitmapUtil.saveBitmap(bmp, mContext, p, 20);

				File file = null;
				if (b) {
					file = new File(p);
				} else {
					file = new File(filePath);
				}

				// 发送图片需要带上尺寸信息
				if (bmp != null) {
					AttachItem attch = new AttachItem();
					attch.setFileName(item.getFileName());
					attch.setThumbnailWidth(bmp.getWidth());
					attch.setThumbnailHeight(bmp.getHeight());
					item.setContent(attch.toString());
				}


//				Bitmap bitmap = cache.getBitmapAlbum(filePath);
//				Bitmap localBitmap = BitmapFactory.decodeFile(filePath); 
//				if (bitmap == null) {
//					try {
//						bitmap = localBitmap;
//					} catch (Exception e) {
//						e.printStackTrace();
//						return;// TODO: fix oom
//					}
//				}
//				cache.putBitmapToExtStorage(filePath, bitmap);
//				cache.putBitmapToExtStorage("o"+filePath, localBitmap);

				new UploadFileTask(mContext, detail, file, mUIHandler,
						item).execute();
				/** 更新到主界面 */
				Message msg = Message.obtain();
				msg.what = HandleMsgType.TYPE_MSG_1;
				msg.obj = file.getAbsolutePath();
				mUIHandler.sendMessage(msg);

//				mSemaphoreThreadPool.release();
			}
		});
	}

	// 图片下载队列
	public void sendMsgToDownload(final MessageItem item,
			final Context context, final Handler handler) {
		mDownloadQueue.add(new Runnable() {
			@Override
			public void run() {
				DownLoadAttachTask task;
				task = new DownLoadAttachTask(context, item, handler, false);
				task.execute();
//				mSemaphoreThreadPool.release();
			}
		});
		if(mPoolThreadHandler != null){
			mPoolThreadHandler.sendEmptyMessage(0x111);
		}
	}

	/** 开始一个任务，只发送一张图片为true*/
	public void startNextUpload(boolean single){
		if(single && mTaskQueue.size()==1 && mPoolThreadHandler!=null){
			mPoolThreadHandler.sendEmptyMessage(0x110);
		}else if(!single && mPoolThreadHandler != null){
			mPoolThreadHandler.sendEmptyMessage(0x110);
		}else if(mPoolThreadHandler == null && mTaskQueue!=null && mTaskQueue.size()>0){
			mTaskQueue.removeFirst();
			mMsgQueue.removeFirst();
			getInstance();
		}
	}

	/** 当第一个任务执行成功后结束*/
	public void stopFirstUpload(MessageItem item, boolean state){
		if(mMsgQueue!=null && mMsgQueue.size()>0 && item.getClientMsgId() == mMsgQueue.getFirst().getClientMsgId()){
			if(mTaskQueue!=null && mTaskQueue.size()>0){
				mTaskQueue.removeFirst();
				mMsgQueue.removeFirst();
			}
		}

		startNextUpload(state);
	}

	// 添加一个任务
	private synchronized void addTask(Runnable runnable) {
		mTaskQueue.add(runnable);
//		// if(mPoolThreadHandler==null)wait();
//		try {
//			if (mPoolThreadHandler == null)
//				mSemaphorePoolThreadHandler.acquire();
//		} catch (InterruptedException e) {
//		}
//		mPoolThreadHandler.sendEmptyMessage(0x110);
	}

	/**
	 * 从任务队列取出一个方法,默认是FIFO
	 * 
	 * @return
	 */
	private Runnable getTask() {
		if (mTaskQueue.isEmpty()){
			return new Runnable() {
				@Override
				public void run() {
					Log.e("infos","taskQueue is empty");
				}
			};
		}else {
			return mTaskQueue.getFirst();
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
