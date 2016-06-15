package com.mintcode.launchr.app.my;

import com.mintcode.launchr.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MyQrDialog extends Dialog{

	private Context mContext;
	
	private ImageView mIvUserHead;
	
	private TextView mTvUserName;
	
	private TextView mTvUserId;
	
	private ImageView mIvQR;
	
	public MyQrDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_myqr, null);
		mIvUserHead = (ImageView)view.findViewById(R.id.iv_user_head);
		mTvUserName = (TextView)view.findViewById(R.id.tv_user_name);
		mTvUserId = (TextView)view.findViewById(R.id.tv_user_id);
		mIvQR = (ImageView)view.findViewById(R.id.iv_user_qr);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
	}
}
