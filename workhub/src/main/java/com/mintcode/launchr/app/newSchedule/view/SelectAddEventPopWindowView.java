package com.mintcode.launchr.app.newSchedule.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;

public class SelectAddEventPopWindowView extends PopupWindow implements OnClickListener{


	private Context mContext;
	private View mContentView;
	
	private TextView mTvSecondSelect;
	
	private TextView mTvFristSelect;
	//透明空白处
	private ImageView mIvTransparency;
	//透明空白处
	private TextView mIvNull;
	
	private LinearLayout mRlNull;

	private boolean mBoolDefalut = true;
	
	private PopWindowListener mPopWindowListener;
	
	public SelectAddEventPopWindowView(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.popwindow_add_evnet, null);		
		setWindow();
		initView();
	}
	public SelectAddEventPopWindowView(Context context ,boolean friendEdit) {
		super(context);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.popwindow_add_evnet, null);
		mBoolDefalut = friendEdit;
		setWindow();
		initView();
	}
	public void initView(){
		mTvSecondSelect = (TextView)mContentView.findViewById(R.id.tv_popwindow_add_meeting);
		mTvFristSelect = (TextView)mContentView.findViewById(R.id.tv_popwindow_add_event);
		mIvTransparency =(ImageView)mContentView.findViewById(R.id.iv_popwindow_tran);
		mIvNull = (TextView)mContentView.findViewById(R.id.iv_popwindow_null);
		mRlNull = (LinearLayout)mContentView.findViewById(R.id.ll_popwindow_null);
		mTvFristSelect.setOnClickListener(this);
		mTvSecondSelect.setOnClickListener(this);
		mIvTransparency.setOnClickListener(this);
		mIvNull.setOnClickListener(this);
		if(!mBoolDefalut){
			mTvSecondSelect.setTextColor(mContext.getResources().getColor(R.color.gray_launchr));
			mTvFristSelect.setTextColor(mContext.getResources().getColor(R.color.red_launchr));
			mIvNull.setTextColor(mContext.getResources().getColor(R.color.blue_launchr));
		}
	}


	private void setWindow() {
		// 设置PopupWindow的View
		this.setContentView(mContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置PopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popupAnimation);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	public void setBackgroundBlack() {
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	public void setFirstTextView(int string){
		mTvSecondSelect.setText(string);
		
	}
	public void setSeconedTextView(int string){
		mTvFristSelect.setText(string);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//跳转到增加会议界面
		case R.id.tv_popwindow_add_meeting:
			mPopWindowListener.getSecondOption();
			this.dismiss();
			break;
		//跳转到增加事件界面
		case R.id.tv_popwindow_add_event:
			mPopWindowListener.selectFristOption();
			this.dismiss();
			break;
		//销毁popwindow
		case R.id.iv_popwindow_tran:
			dismiss();
			break;
	    //销毁popwindow
		case R.id.iv_popwindow_null:
			dismiss();
		default:
			break;
		}
	}
	
	public void setsetFirstTextViewVisibility(){
		mTvSecondSelect.setVisibility(View.GONE);
		ImageView line = (ImageView)mContentView.findViewById(R.id.iv_popwindow_iv);
		line.setVisibility(View.GONE);
	}
	public void setPopWindowListener(PopWindowListener listener){
		this.mPopWindowListener = listener;
	}
	public interface  PopWindowListener{
		void getSecondOption();
		void selectFristOption();
	}
}
