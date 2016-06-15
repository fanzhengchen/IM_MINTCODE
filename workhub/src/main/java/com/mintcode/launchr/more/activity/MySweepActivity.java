package com.mintcode.launchr.more.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mintcode.im.crypto.AESUtil;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.QRCodeResultPOJO;
import com.mintcode.launchr.util.TTJSONUtil;
import com.zxing.android.MessageIDs;
import com.zxing.android.camera.CameraManager;
import com.zxing.android.decoding.CaptureActivityHandler;
import com.zxing.android.decoding.InactivityTimer;
import com.zxing.android.view.ViewfinderView;

public class MySweepActivity extends BaseActivity implements Callback, View.OnClickListener{
	public static final String QR_RESULT = "RESULT";

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private SurfaceView surfaceView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;
	CameraManager cameraManager;

	View mPart2;
	View mLoginView;
	View mCancelView;
	View mBack;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		setTitle("扫描二维码");
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);
		mPart2 = findViewById(R.id.layout_part2);
		mLoginView = findViewById(R.id.login);
		mCancelView = findViewById(R.id.cancel);
		mBack = findViewById(R.id.iv_back);
		
		mBack.setOnClickListener(this);
		mLoginView.setOnClickListener(this);
		mCancelView.setOnClickListener(this);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		cameraManager = new CameraManager(getApplication());

		viewfinderView.setCameraManager(cameraManager);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		cameraManager.closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			cameraManager.openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		showResult(obj, barcode);
	}

	private void gotoCAS(String URL) {
		Uri uri = Uri.parse(URL);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();
	}

	String mId;
	String mUrl;
	String mAction = "";
	@SuppressWarnings("deprecation")
	protected void showResult(final Result rawResult, Bitmap barcode) {
		Map<String,String> qrCode = checkResult(rawResult);
		if(qrCode != null){
			String id = qrCode.get("id");
			String url = qrCode.get("url");
			mId = AESUtil.DecryptServerAuthToken(id);
			mUrl = AESUtil.DecryptServerAuthToken(url);
			inactivityTimer.shutdown();
			mPart2.setVisibility(View.VISIBLE);
			showLoading();
			mAction = "scan";
			UserApi.getInstance().loginQRCode(this, mId, mUrl, mAction);
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		Drawable drawable = new BitmapDrawable(barcode);
		builder.setIcon(drawable);

		builder.setTitle(rawResult.getText());
		String confirm = getResources().getString(R.string.confirm);
		builder.setPositiveButton(confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				gotoCAS(rawResult.getText());
				finish();
			}
		});
		String reScan = getResources().getString(R.string.confirm);
		builder.setNegativeButton(reScan, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				restartPreviewAfterDelay(0L);
			}
		});
		builder.setCancelable(false);
		builder.show();

	}

	private Map<String, String> checkResult(Result rawResult) {
		String code = rawResult.getText();
		
		HashMap<String, String> infoMap = TTJSONUtil.convertJsonToObj(
				code, new TypeReference<HashMap<String, String>>() {});
		if(infoMap != null && infoMap.size() == 2){
			if(infoMap.get("id") != null && infoMap.get("url") != null){
				return infoMap;
			}
		}
		return null;
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			try {
				AssetFileDescriptor fileDescriptor = getAssets().openFd(
						"qrbeep.ogg");
				this.mediaPlayer.setDataSource(
						fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				this.mediaPlayer.setVolume(0.1F, 0.1F);
				this.mediaPlayer.prepare();
			} catch (IOException e) {
				this.mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v == mLoginView){
			mAction = "login";
			UserApi.getInstance().loginQRCode(this, mId, mUrl, mAction);
			showLoading();
		}else if(v == mCancelView){
			mAction = "cancel";
			UserApi.getInstance().loginQRCode(this, mId, mUrl, mAction);
			showLoading();
			finish();
		}else if(v == mBack){
			finish();
		}
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if (response == null) {
			showNoNetWork();
			return;
		}
		dismissLoading();
		if (taskId.equals(TaskId.TASK_URL_QRCODE_CONNECTION)) {
			QRCodeResultPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), QRCodeResultPOJO.class);
			if(pojo.getBody().getResponse().isIsSuccess()){
				if(mAction.equals("login") || mAction.equals("cancel"))
				finish();
			}
		}	
	}

	
}