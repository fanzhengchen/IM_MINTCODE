package com.mintcode.launchr.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * 短信铃声或震动提醒
 * 
 * @author KevinQiao
 * 
 */
public class AudioTipHelper {

	public static final String KEY_SOUND = "key_sound";

	public static final String KEY_VIBRATION = "key_vibration";


	public static SoundPool soundPool = null;

	public static Map<Integer,Integer> map = null;


	// 打开声音
	public static void PlaySound(final Context context) {



//		NotificationManager mgr = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification nt = new Notification();
//		nt.defaults = Notification.DEFAULT_SOUND;
//		int soundId = new Random(System.currentTimeMillis())
//				.nextInt(Integer.MAX_VALUE);
//		mgr.notify(soundId, nt);


//		AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
//		float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		float volumnCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//		float volumnRatio = volumnCurrent / audioMaxVolumn;
//		SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
//		sp.l
//		sp.play(soundId, volumnRatio, volumnRatio, 0, soundId, 1f);
//
//		if(soundPool == null){
//			soundPool == new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
//		}

//        soundPool.play();

		if(soundPool == null){
			soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
			map = new HashMap<>();
			map.put(1,soundPool.load(context, R.raw.fadein,1));
		}

		AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volumnCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//		float volumnRatio = volumnCurrent / audioMaxVolumn;

		soundPool.play(map.get(1),volumnCurrent,volumnCurrent,1,0,1);

//		return soundId;
	}

	// 开启振动
	public static void startVibration(final Context context) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1);

	}

	// 开启消息提示音
	public static void saveSoundAction(Context context, boolean flag) {
		AppUtil util = AppUtil.getInstance(context);
		util.saveBooleanValue(KEY_SOUND,flag);
	}
	
	public static boolean containAction(Context context){
		SharedPreferences sp = context.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE);
		return sp.contains("sound")||sp.contains("vibration");
	}

	// 关闭消息提示音
	public static boolean getSoundAction(Context context) {
		AppUtil util = AppUtil.getInstance(context);
		boolean b = util.getBooleanValue(KEY_SOUND);
		return b;
	}

	// 设置是否打开振动
	public static void saveVibrationAction(Context context, boolean flag) {
		AppUtil util = AppUtil.getInstance(context);
		util.saveBooleanValue(KEY_VIBRATION, flag);
	}

	// 获得振动状态
	public static boolean getVibrationAction(Context context) {
		AppUtil util = AppUtil.getInstance(context);
		boolean b = util.getBooleanValue(KEY_VIBRATION);
		return b;
	}
	//消息接收  开启提示
	
	public static void openRemind(Context context){

			boolean isSound =  getSoundAction(context);// 是否开启铃声
			boolean isVibration = getVibrationAction(context);// 是否开启振动
			if(isSound) {
				PlaySound(context);
			}
			if(isVibration){
				startVibration(context);
			}
	}
}
