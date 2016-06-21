package mintcode.com.workhub_im.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.orhanobut.logger.Logger;

import mintcode.com.workhub_im.im.ServiceManager;

public class BeetTimer extends Handler {

	public static final int BEET = 12;
	public static final String TAG = "Beet Timer";
	private static BeetTimer sInstance;
	private boolean beetSwitch = false;

	private BeetTimer() {
//		Looper.prepare();
//		Log.d("handleMessage", "=BeetTimer==" + Thread.currentThread().getName());
	}

	public synchronized static BeetTimer getInstance() {
		if (sInstance == null) {
			sInstance = new BeetTimer();
		}
		return sInstance;
	}

	public void startBeet() {
		beetSwitch = true;
		sendEmptyMessage(BEET);
	}

	public void stopBeet() {
		Logger.d("BeetTimer", "StopBeet!!!!!!!!!!!!!!!!!!!!!");
		beetSwitch = false;
		removeMessages(BEET);
	}


	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
//		Logger.i(TAG + " handle Message " + msg.what );
		if (msg.what == BEET) {
			Logger.d("BeetTimerssssss", "beet!!!!!!");
			ServiceManager.getInstance().keepBeet();
			if (beetSwitch) {
				sendEmptyMessageDelayed(BEET, 60000);
			}
		}
	}

}
