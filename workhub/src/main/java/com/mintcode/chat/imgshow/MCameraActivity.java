package com.mintcode.chat.imgshow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.mintcode.chat.imgshow.SidProgressBar.ButtonListener;
import com.mintcode.launchr.R;

public class MCameraActivity extends Activity implements ButtonListener {

	private int parentId;
	protected Camera camera;
	protected boolean isPreview;
	private File mRecVedioPath;
	private File mRecAudioFile;
	private SurfaceView mSurfaceview;
	private SurfaceHolder mSurfaceHolder;
	private SidProgressBar mProgressBar;
	private MediaRecorder mMediaRecorder;
	private String mVedioPath;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=MCameraActivity.this;
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_video);
		parentId = getIntent().getIntExtra("parentId", 0);
		mProgressBar = (SidProgressBar) findViewById(R.id.progress_bar);
		mSurfaceview = (SurfaceView) findViewById(R.id.arc_hf_video_view);
		mProgressBar.setButtonListener(this);

		// 设置缓存路径
		mRecVedioPath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/video/temp/");
		if (!mRecVedioPath.exists()) {
			mRecVedioPath.mkdirs();
		}

		// 绑定预览视图
		SurfaceHolder holder = mSurfaceview.getHolder();
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (camera != null) {
					if (isPreview) {
						camera.stopPreview();
						isPreview = false;
					}
					camera.release();
					camera = null; // 记得释放
				}
				mSurfaceview = null;
				mSurfaceHolder = null;
				mMediaRecorder = null;
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					camera = Camera.open();
					camera.setDisplayOrientation(90);
					Parameters params = getParmas();

					camera.setParameters(params);

					camera.setPreviewDisplay(holder);
					camera.startPreview();
					isPreview = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mSurfaceHolder = holder;
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				mSurfaceHolder = holder;
			}
		});
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void onStartRecode() {
		if (mMediaRecorder == null)
			mMediaRecorder = new MediaRecorder();
		else
			mMediaRecorder.reset();
		camera.unlock();
		mMediaRecorder.setCamera(camera);
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mMediaRecorder.setOrientationHint(90);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mMediaRecorder.setVideoSize(320, 240);
		mMediaRecorder.setVideoEncodingBitRate(30 * 1024 * 1024);
		try {
			mRecAudioFile = File.createTempFile("Vedio", ".mp4", mRecVedioPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEndRecord() {
		try {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			camera.stopPreview();
			camera.lock();
			camera.release();
			mMediaRecorder = null;
			videoRename();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			camera = Camera.open();
			Parameters params = getParmas();
			camera.setParameters(params);
			camera.setPreviewDisplay(mSurfaceHolder);
			camera.startPreview();
			isPreview = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("camera_path", mVedioPath);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

	private Parameters getParmas() {
		Parameters params = camera.getParameters();

		// 设置相机参数

		if (params.getMaxNumMeteringAreas() > 0) { // 检查是否支持测光区域
			List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

			Rect areaRect1 = new Rect(-100, -100, 100, 100); // 在图像的中心指定一个区域
			meteringAreas.add(new Camera.Area(areaRect1, 600)); // 设置宽度到60%
			Rect areaRect2 = new Rect(800, -1000, 1000, -800); // 在图像的右上角指定一个区域
			meteringAreas.add(new Camera.Area(areaRect2, 400)); // 收置宽度为40%
			params.setMeteringAreas(meteringAreas);
			Log.i("infos", "------------");

		}
		params.setFocusMode(Parameters.FOCUS_MODE_AUTO);

		return params;

	}

	/*
	 * 生成video文件名字
	 */
	protected void videoRename() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/hfdatabase/video/"
				+ String.valueOf(parentId) + "/";
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + ".mp4";
		File out = new File(path);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(path, fileName);
		if (mRecAudioFile.exists())
			mRecAudioFile.renameTo(out);
		mVedioPath = out.getAbsolutePath();
	}

	@Override
	protected void onPause() {
		super.onPause();
		onBackPressed();
	}

}
