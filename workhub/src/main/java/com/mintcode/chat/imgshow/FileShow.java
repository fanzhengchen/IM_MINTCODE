package com.mintcode.chat.imgshow;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.mintcode.RM;
import com.mintcode.launchr.R;

/**
 * @author dlion
 * 
 */
public class FileShow extends Activity implements Callback,
		OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener {
	private static final String TAG = "FileShow";
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_RENAME = Menu.FIRST + 1;
	private GridView fileGrid;
	private BaseAdapter adapter = null;
	private String path;
	private EditText etRename;
	private File file;

	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private Context context;
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		context=FileShow.this;
		setContentView(R.layout.map_file_show);
		path = getIntent().getStringExtra("camera_path");
//		if (path.indexOf("http://") >= 0) {
//			String movieurl = path;// 在线视频地址
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			String type = "video/mpeg";// 视频类型
//			Uri uri = Uri.parse(movieurl);
//			intent.setDataAndType(uri, type);
//			startActivity(intent);
//
//		} else {
//			file = new File(path);
//			Intent intent = new Intent();
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setAction(android.content.Intent.ACTION_VIEW);
//			String type = thisFileType(file.getName());
//			intent.setDataAndType(Uri.fromFile(file), type);
//			startActivity(intent);
//		}

		mPreview = (SurfaceView) findViewById(R.id.surfaceView1);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public static Bitmap getBitmap(String videoPath){
		return ThumbnailUtils.createVideoThumbnail(
				videoPath, Video.Thumbnails.MINI_KIND);
	}

	/*
	 * 获取文件类型
	 */
	public static String thisFileType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("jpg")) {
			type = "image";
		} else if (end.equals("mp4")) {
			type = "video";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/*
	 * 消息提示
	 */
	private Toast toast;

	public void showMsg(String arg) {
		if (toast == null) {
			toast = Toast.makeText(this, arg, Toast.LENGTH_SHORT);
		} else {
			toast.cancel();
			toast.setText(arg);
		}
		toast.show();
	}

	private void playVideo() {
		clean();
		try {

			// 创建一个MediaPlayer对象
			mMediaPlayer = new MediaPlayer();
			// 设置播放的视频数据源
			mMediaPlayer.setDataSource(path);
			// 将视频输出到SurfaceView
			mMediaPlayer.setDisplay(holder);
			// 播放准备，使用异步方式，配合OnPreparedListener
			mMediaPlayer.prepareAsync();
			// 设置相关的监听器
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			// 设置音频流类型
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setLooping(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion");
	}

	// 获得视频的宽和高
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	// 实现OnPreparedListener中的方法，当视频准备完毕会调用这个回调方法，开始播放
	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged");
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		playVideo();
	}

	@Override
	protected void onPause() {
		releaseMediaPlayer();
		clean();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		clean();
	}

	// 释放MediaPlayer
	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	// 资源清理工作
	private void clean() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	// 播放视频的方法
	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}
}
