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
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;

public class MCamera extends Activity {
	private Button mVideoStartBtn;
	private SurfaceView mSurfaceview;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder mSurfaceHolder;
	private File mRecVedioPath;
	private File mRecAudioFile;
	private TextView timer;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private boolean bool;
	private int parentId;
	protected Camera camera;
	protected boolean isPreview;
	private Drawable iconStart;
	private Drawable iconStop;
	private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=MCamera.this;
		/*
		 * 全屏显示
		 * 
		 */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.map_video);
		iconStart = getResources().getDrawable(R.drawable.arc_hf_btn_video_start);
		iconStop = getResources().getDrawable(R.drawable.arc_hf_btn_video_stop);

		parentId = getIntent().getIntExtra("parentId", 0);
		timer = (TextView) findViewById(R.id.arc_hf_video_timer);
		mVideoStartBtn = (Button) findViewById(R.id.arc_hf_video_start);
		mSurfaceview = (SurfaceView) this.findViewById(R.id.arc_hf_video_view);

		// 设置计时器不可见
		timer.setVisibility(View.GONE);

		// 设置缓存路径
		mRecVedioPath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/kevin/video/temp/");
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

		mVideoStartBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRecording) {
					/*
					 * 点击开始录像
					 */
//					if (isPreview) {
//						camera.stopPreview();
//						camera.release();
//						camera = null;
//					}
					second = 0;
					minute = 0;
					hour = 0;
					bool = true;
					if (mMediaRecorder == null)
						mMediaRecorder = new MediaRecorder();
					else
						mMediaRecorder.reset();
					camera.unlock();
					mMediaRecorder.setCamera(camera);
					mMediaRecorder.setPreviewDisplay(mSurfaceHolder
							.getSurface());
					mMediaRecorder
							.setVideoSource(MediaRecorder.VideoSource.CAMERA);
					mMediaRecorder
							.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
					mMediaRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					mMediaRecorder
							.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
					mMediaRecorder
							.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
					mMediaRecorder.setVideoSize(320, 240);
//					mMediaRecorder.setVideoFrameRate(15);
					mMediaRecorder.setVideoEncodingBitRate(30*1024*1024);
					try {
						mRecAudioFile = File.createTempFile("Vedio", ".mp4",
								mRecVedioPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					mMediaRecorder.setOutputFile(mRecAudioFile
							.getAbsolutePath());
					try {
						mMediaRecorder.prepare();
						timer.setVisibility(View.VISIBLE);
						handler.postDelayed(task, 1000);
						mMediaRecorder.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					showMsg("开始录制");
					mVideoStartBtn.setText("结束");
					isRecording = !isRecording;
				} else {
					/*
					 * 点击停止
					 */
					try {
						bool = false;
						mMediaRecorder.stop();
						timer.setText(format(hour) + ":" + format(minute) + ":"
								+ format(second));
						mMediaRecorder.release();
						camera.stopPreview();
						camera.lock();
						camera.release();
						mMediaRecorder = null;
						videoRename();
					} catch (Exception e) {
						e.printStackTrace();
					}
					isRecording = !isRecording;
					mVideoStartBtn.setText("开始");
					showMsg("录制完成，已保存");
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
			}
		});
		
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
	String mVedioPath;
	/*
	 * 定时器设置，实现计时
	 */
	private Handler handler = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			if (bool) {
				handler.postDelayed(this, 1000);
				second++;
				if (second >= 60) {
					minute++;
					second = second % 60;
				}
				if (minute >= 60) {
					hour++;
					minute = minute % 60;
				}
				timer.setText(format(hour) + ":" + format(minute) + ":"
						+ format(second));
			}
		}
	};

	/*
	 * 格式化时间
	 */
	public String format(int i) {
		String s = i + "";
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

//	/*
//	 * 覆写返回键监听
//	 */
//	@Override
//	public void onBackPressed() {
//		try {
//			if (mMediaRecorder != null) {
//				mMediaRecorder.stop();
//				mMediaRecorder.release();
//				mMediaRecorder = null;
//				videoRename();
//			}
//		} catch (Exception e) {
//		}
//		finish();
//	}

	@Override
	protected void onPause() {
		super.onPause();
		onBackPressed();
	}
	
	
	private Parameters getParmas(){
		Parameters params = camera.getParameters();
        
	     // 设置相机参数们

		     if (params.getMaxNumMeteringAreas() > 0){ // 检查是否支持测光区域
		         List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
		
		         Rect areaRect1 = new Rect(-100, -100, 100, 100);    // 在图像的中心指定一个区域
		         meteringAreas.add(new Camera.Area(areaRect1, 600)); // 设置宽度到60%
		         Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // 在图像的右上角指定一个区域
		         meteringAreas.add(new Camera.Area(areaRect2, 400)); // 收置宽度为40%
		         params.setMeteringAreas(meteringAreas);
		         Log.i("infos", "------------");
		         
		     }
		     params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
		     
		     
		 return params;
		     
	}
	
}