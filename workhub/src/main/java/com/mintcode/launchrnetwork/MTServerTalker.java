package com.mintcode.launchrnetwork;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class MTServerTalker {
	private static String TAG = "ServerTalker";
	private static final int THREAD_COUNT = 10;

	private static MTServerTalker mInstance;
	private ExecutorService mService;

	private CallbackHandler mCallbackHandler;

	private MTServerTalker() {
		mService = Executors.newFixedThreadPool(THREAD_COUNT);
		mCallbackHandler = new CallbackHandler();
	}

	/**
	 * create a instance of the MTServerTalker
	 * 
	 * @return
	 */
	public static synchronized MTServerTalker getInstance() {
		if (mInstance == null) {
			mInstance = new MTServerTalker();
		}
		return mInstance;
	}

	/**
	 * accept the data of the runnable and response to the main thread
	 * 
	 * @author MiyaJiang
	 * 
	 */
	private static class CallbackHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			HttpRunnable getRunnable = (HttpRunnable) msg.obj;
			if (getRunnable != null) {
				getRunnable.updateResponse();
			}
		}
	}

	abstract class HttpRunnable implements Runnable {
		protected String mTaskId;

		@Override
		public abstract void run();

		public abstract void abortRunnable();

		public abstract void updateResponse();
	}

	private class HttpJSONRunnable extends HttpRunnable {
		private String mTaskId;
		private String mUrl;
		private String mHttpMethod;
		private MTHttpParameters mParams;
		private WeakReference<OnResponseListener> mListener;
		private Object mData;
		private boolean mRawData;

		public HttpJSONRunnable(OnResponseListener listener, String taskId,
				String url, String httpMethod, HttpParams params,
				boolean rawData) {
			this.mUrl = url;
			this.mParams = (MTHttpParameters) params;
			this.mTaskId = taskId;
			this.mListener = new WeakReference<OnResponseListener>(listener);
			this.mRawData = rawData;
			this.mHttpMethod = httpMethod;
			
		}

		@Override
		public void run() {
			long intime = System.currentTimeMillis();
			// if( mRawData ) {
			// mHttpMethod = MTHttpManager.HTTP_GET;
			// }
			if (NetDebuger.DEBUG) {
				// 如果开启调试模式，则从NetDebuger类中寻找假数据。
				String debugJson = NetDebuger.getResponse(mTaskId);
				if (debugJson == null) {
					// 没有在测试数据json表中，还是从网络读取。
					mData = getDataFromNetwork();
				} else {
					mData = MTBeanFactory.getBean(debugJson);
				}
				 
			} else {
				// 正常模式，非调试模式！
				mData = getDataFromNetwork();
			}
			long outtime = System.currentTimeMillis();
			Log.d("", ""+(outtime-intime));
			Message msg = Message.obtain();
			msg.obj = this;
			mCallbackHandler.sendMessage(msg);
		}

		/**
		 * 从网络获取数据
		 * 
		 * @return
		 */
		private Object getDataFromNetwork() {
			Object resultData = null;
			try {
				Object res = MTHttpManager.openUrl(mUrl, mHttpMethod, mParams,
						this.mRawData);
				Log.d("http", "Post==JsonString===>" + "\n" + mParams.toJson());
				if (!this.mRawData) {
					String responseStr = (String) res;
					Log.i("http", "Response==JsonString===>" + "\n" + responseStr);
//					resultData = MTBeanFactory.getBean(responseStr);
					resultData = res;
				} else {
					resultData = res;
				}

			} catch (MTException e) {
				// network error
				e.printStackTrace();
				return resultData;
			}
			return resultData;
		}

		@Override
		public void abortRunnable() {

		}

		@Override
		public void updateResponse() {
			OnResponseListener l = mListener.get();
			if (null != l) {
				boolean isAvaliable = !l.isDisable();
				if (isAvaliable) {
					l.onResponse(mData, mTaskId, mRawData);
				}
			}
		}
	}

	private void enqueeHttpRequest(OnResponseListener listener,
			final String taskId, final String url, final String httpMethod,
			final HttpParams params, final boolean getRawData) {
		HttpJSONRunnable runnable = new HttpJSONRunnable(listener, taskId, url,
				httpMethod, params, getRawData);
		mService.execute(runnable);
	}

	public void executeHttpMethod(OnResponseListener listener,
			final String taskId, final String targetUrl,
			final String httpMethod, final HttpParams httpParams,
			final boolean getRawData) {
		enqueeHttpRequest(listener, taskId, targetUrl, httpMethod, httpParams,
				getRawData);
	}

}
