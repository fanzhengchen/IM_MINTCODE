package com.mintcode.im.util;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class MessageNotifyManager {

	static ActivityManager activityManager;

	private static NotificationManager mNotificationManager;

	private static Notification notification;

	private static int notificationId;


	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device
		if (activityManager == null) {
			activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(context.getApplicationContext()
					.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void showMessageNotify(Context context, boolean isCurrent, String title,
										 String text, int id, int iconId, Class<?> targetClass) {
		if (isAppOnForeground(context) && isCurrent) {
			return;
		}

		notificationId = id;

		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(Intent.ACTION_MAIN); // new
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		// notificationIntent.setClass(context, MainActivity_pt.class);
		notificationIntent.setClass(context, targetClass);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification = new Notification(iconId, text, System.currentTimeMillis());
		notification = new Notification.Builder(context)
				.setContentTitle(title)
				.setSmallIcon(iconId)
				.setContentText(text)
				.setContentIntent(pendingIntent)
				.build();
		notification.defaults = notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;


//		notification.setLatestEventInfo(context, title, text, pendingIntent);
		mNotificationManager.notify(id, notification);

	}

	public static void cancelNotification(){
		if(mNotificationManager!=null){
			mNotificationManager.cancel(notificationId);
		}
	}


}
