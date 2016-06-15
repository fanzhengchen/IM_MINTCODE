package com.mintcode.chat.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mintcode.RM;
import com.mintcode.launchr.R;


public class MyProgressDialog extends ProgressDialog {
	private Context context = null;
	private static MyProgressDialog customProgressDialog = null;
	private Boolean mIsCancel = false;
	private Boolean mIsBackJudge = false;
	private String mAlert = "";

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public MyProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public MyProgressDialog(Context context, Boolean isBackJudge,
			String alert) {
		super(context);
		this.context = context;
		mIsBackJudge = isBackJudge;
		mAlert = alert;
	}
	
	public static MyProgressDialog creatDialog(Context context) {
		customProgressDialog = new MyProgressDialog(context,
				Color.TRANSPARENT);
		return customProgressDialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.widget_custom_dialog);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIsBackJudge && !mIsCancel) {
				mIsCancel = true;
				Toast.makeText(context, mAlert, Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mIsCancel = false;
					}
				}, 2000);

				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}

		}
		return super.onKeyDown(keyCode, event);
	}
}