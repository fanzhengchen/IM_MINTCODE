package mintcode.com.workhub_im.widget.auido;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import com.mintcode.imkit.crypto.Base64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.util.Base64Decoder;

public class AudioRecordPlayUtil implements OnInfoListener,
		OnCompletionListener {

	public interface OnInfoListener {
		/** 0-10000 */
		void onVolume(int volume);

		void onMaxDuration();

		void onMaxFileSize();

		void onPlayCompletion();
	}

	public static class SimpleInfoListener implements OnInfoListener {

		@Override
		public void onVolume(int volume) {
		}

		@Override
		public void onMaxDuration() {
		}

		@Override
		public void onMaxFileSize() {
		}

		@Override
		public void onPlayCompletion() {
		}

	}


	private OnInfoListener mOnInfoListener;

	private Context mContext;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	private String mFileName;

	private int mMaxDurationMs = 1000 * (60 + 1); // 60 seconds

	private long mStartTime, mRecordDuration;

	private Handler mHandler = new Handler(Looper.getMainLooper());

	private UpdateMicStatusRunnable mUpdateMicStatusRunnable = new UpdateMicStatusRunnable();

	private View v;

	public AudioRecordPlayUtil(OnInfoListener l, Context context, View v) {
		mOnInfoListener = l;
		mContext = context;
		if (v != null) {
			this.v = v;
		}

	}

	public void setFileName(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException(
					"fileName cannot be null or empty.");
		}

		mFileName = AppConsts.DOWNLOAD_AUDIO_PATH_SDCARD + fileName;
		if (fileName.contains(File.separator)) {
			mFileName = fileName;
		} else {
			mFileName = AppConsts.DOWNLOAD_AUDIO_PATH_SDCARD + fileName;
		}
//		if (fileName.contains(File.separator)) {
//			mFileName = fileName;
//		} else {
//			if (mContext.getExternalCacheDir() != null) {
//				mFileName = mContext.getExternalCacheDir().getAbsolutePath()
//						+ File.separator + fileName;
//			} else {
//				mFileName = mContext.getCacheDir().getAbsolutePath()
//						+ File.separator + fileName;
//			}
//		}
	}

	private void checkFileName() {
		if (TextUtils.isEmpty(mFileName)) {
			throw new IllegalArgumentException("mast call setFileName first.");
		}
	}

	public void setMaxDurationMs(int ms) {
		mMaxDurationMs = ms;
	}

	public void startRecording() {
		checkFileName();
		if (mRecorder != null) {
			stopRecording();
		}

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOnInfoListener(this);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setMaxDuration(mMaxDurationMs);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mRecorder.start();
		mStartTime = System.currentTimeMillis();
		mHandler.post(mUpdateMicStatusRunnable);
	}

	public void stopRecording() {
		try {
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				mRecordDuration = System.currentTimeMillis() - mStartTime;
			}
		} catch (Exception e) {
		}
	}

	public String getOutputFileName() {
		return mFileName;
	}

	public String getBase64Output() {
		FileInputStream in;
		try {
			in = new FileInputStream(mFileName);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			return Base64Encoder.encode(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordMilliseconds() {
		return getDurationMilliseconds();
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		switch (what) {
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
			if (mOnInfoListener != null) {
				mOnInfoListener.onMaxDuration();
			}
			break;

		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
			if (mOnInfoListener != null) {
				mOnInfoListener.onMaxFileSize();
			}
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		stopPlaying();
		if(mPlayer != null){
			mPlayer.release();
		}
		if (mOnInfoListener != null) {
			mOnInfoListener.onPlayCompletion();
		}
	}

	private static final int BASE = 600;
	private static final int UPDATE_DURATION = 100;

	private class UpdateMicStatusRunnable implements Runnable {

		@Override
		public void run() {
			if (mRecorder != null) {
				// double ratio = (double) mRecorder.getMaxAmplitude() / BASE;
				// int db = 0;
				// if (ratio > 1) {
				// db = (int) (10000 * Math.log10(ratio));
				// }
				// db = (int) (ratio * 1000);
				int max = 32767 / BASE;
				int amplitude = mRecorder.getMaxAmplitude() / BASE;
				int db = 0;
				if (amplitude > 1) {
					db = (int) (10000 * Math.log10(amplitude) / Math.log10(max));
				}
				int t = getDurationMilliseconds();
				Log.i("UpdateMicStatusRunnable",
						"UpdateMicStatusRunnable time:" + t);
				if (t >= (mMaxDurationMs - 1000)) {
					// CustomToast.showToast(mContext,
					// mContext.getString(R.string.speak_to_long), 2000);
					v.setVisibility(View.GONE);
					stopRecording();
				}
				if (mOnInfoListener != null) {
					mOnInfoListener.onVolume(db);
				}
				mHandler.postDelayed(this, UPDATE_DURATION);
			}
		}

	}

	public void saveBase64Input(String fileName, String base64Data) {
		setFileName(fileName);
		FileOutputStream out;
		try {
			byte[] data = Base64Decoder.decodeToBytes(base64Data);
			out = new FileOutputStream(mFileName);
			out.write(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getDurationMilliseconds() {
		int duration = 0;
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(mFileName);
			player.prepare();
			duration = player.getDuration();
			player.release();
//			player = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duration;
	}

	public void startPlaying() {
		checkFileName();
		if (mPlayer != null) {
			stopPlaying();
		}

		if(mPlayer != null){
			try {
				mPlayer.setDataSource(mFileName);
				mPlayer.setOnCompletionListener(this);
				mPlayer.prepare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mPlayer.start();
		}else{
			mPlayer = MediaPlayer.create(mContext, Uri.parse(mFileName));
			if(mPlayer!=null){
				mPlayer.setOnCompletionListener(this);
				mPlayer.start();
			}
		}

	}

	public void stopPlaying() {
		if (mPlayer != null) {
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}

	public boolean isPlaying() {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
			return false;
		}
		return mPlayer.isPlaying();
	}

	public boolean isPlayingNotNull() {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
			return false;
		}else{
			return mPlayer.isPlaying();
		}
	}

	public void setAudioModeEarpiece() {
		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMode(AudioManager.MODE_IN_CALL);
		audioManager.setSpeakerphoneOn(false);
		((Activity)mContext).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
	}

	public void setAudioModeNormal() {
		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMode(AudioManager.MODE_NORMAL);
	}

}
