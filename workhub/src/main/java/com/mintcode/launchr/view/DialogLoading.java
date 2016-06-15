package com.mintcode.launchr.view;



import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * loadig显示类
 * @author KevinQiao
 *
 */
public class DialogLoading extends ProgressDialog  {
	private Context context = null;
	private static DialogLoading customProgressDialog = null;

	/** 飞机 */
	private ImageView mIvPlane;

	/** 西湖 */
	private ProgressBar mPbLoading;

	public DialogLoading(Context context, int theme) {
		super(context, theme);
	}

	
	public DialogLoading(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * 获取loading实例
	 * @param context
	 * @return
	 */
	public static DialogLoading creatDialog(Context context){
		customProgressDialog = new DialogLoading(context,R.style.loading);//R.style.progress_loading 
		return customProgressDialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading_plane);
//		setProgressStyle();
		this.setCanceledOnTouchOutside(false);  
//		initView();
		mIvPlane = (ImageView) findViewById(R.id.iv_loading_plane);
		mPbLoading = (ProgressBar) findViewById(R.id.pb_loading_xihu);
		mIvPlane.setVisibility(ImageView.VISIBLE);
		mPbLoading.setVisibility(View.GONE);
	}

	public void start(){
		AnimationDrawable drawable = (AnimationDrawable) mIvPlane
				.getBackground();
		drawable.start();
	}

	Handler handler = new Handler();
	@Override
	public void dismiss() {
		super.dismiss();
	}
}