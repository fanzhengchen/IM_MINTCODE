package com.mintcode.launchr.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.mintcode.launchr.R;


/**
 * loadig显示类
 * @author KevinQiao
 *
 */
public class DialogProgressLoading extends ProgressDialog  {
	private Context context = null;
	private static DialogProgressLoading customProgressDialog = null;

	/** loading 提示 */
	private TextView mTVLoading;
	public DialogProgressLoading(Context context, int theme) {
		super(context, theme);
	}


	public DialogProgressLoading(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * 获取loading实例
	 * @param context
	 * @return
	 */
	public static DialogProgressLoading creatDialog(Context context){
		customProgressDialog = new DialogProgressLoading(context,R.style.loading);//R.style.progress_loading
		return customProgressDialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading_wait);
//		setProgressStyle();
		this.setCanceledOnTouchOutside(false);  
		initView();
		
	}
	
	/**
	 * 实例化view
	 */
	private void initView(){
		mTVLoading = (TextView) findViewById(R.id.tv_loading);
	}

	/**
	 * 设置loading字体 tt
	 * @param text
	 */
	public void setLoadingText(String text){
		mTVLoading.setText(text);
		if(mTVLoading != null){
		}

	}
}