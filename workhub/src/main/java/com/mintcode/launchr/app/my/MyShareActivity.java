package com.mintcode.launchr.app.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/**
 * @author StephenHe 2015/9/17 
 *         分享
 */
public class MyShareActivity extends BaseActivity {
	/** 返回 */
	private ImageView mIvBack;

	/** Line */
	private ImageView mIvShareLine;

	/** Facebook */
	private ImageView mIvShareFacebook;

	/** Twitter */
	private ImageView mIvShareTwitter;

	/** */
	private ImageView mIvSharePower;

	/** 邮箱 */
	private ImageView mIvShareMail;

	/** 复制 */
	private ImageView mIvShareCopy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_share);

		initView();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_title_back);

		mIvShareLine = (ImageView) findViewById(R.id.iv_share_line);
		mIvShareFacebook = (ImageView) findViewById(R.id.iv_share_facebook);
		mIvShareTwitter = (ImageView) findViewById(R.id.iv_share_twitter);
		mIvSharePower = (ImageView) findViewById(R.id.iv_share_power);
		mIvShareMail = (ImageView) findViewById(R.id.iv_share_mail);
		mIvShareCopy = (ImageView) findViewById(R.id.iv_share_copy);

		mIvBack.setOnClickListener(this);

		mIvShareLine.setOnClickListener(this);
		mIvShareFacebook.setOnClickListener(this);
		mIvShareTwitter.setOnClickListener(this);
		mIvSharePower.setOnClickListener(this);
		mIvShareMail.setOnClickListener(this);
		mIvShareCopy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			// 返回操作
			finish();
		} else if (v == mIvShareLine) {
			shareToLine();
		} else if (v == mIvShareFacebook) {
			shareToFacebook();
		} else if (v == mIvShareTwitter) {
			shareToTwitter();
		} else if (v == mIvSharePower) {
			shareToPower();
		} else if (v == mIvShareMail) {
			shareToMail();
		} else if (v == mIvShareCopy) {
			shareToCopy();
		}
	}

	/** 分享到Line */
	private void shareToLine() {

	}

	/** 分享到Facebook */
	private void shareToFacebook() {

	}

	/** 分享到Twitter */
	private void shareToTwitter() {

	}

	/** 分享到 */
	private void shareToPower() {

	}

	/** 分享到Mail */
	private void shareToMail() {

	}

	/** 复制 */
	private void shareToCopy() {

	}
}
