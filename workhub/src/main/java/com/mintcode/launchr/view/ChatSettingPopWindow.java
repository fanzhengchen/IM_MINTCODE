package com.mintcode.launchr.view;

import com.mintcode.launchr.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ChatSettingPopWindow extends PopupWindow implements OnClickListener{
	
	private View mContentView;
	private TextView mTvMsgHistory;
	private TextView mTvChatSetting;
	private ChatSettingListener listener;
	
	public ChatSettingPopWindow(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.window_chat_more,null);
		setWindow();
		initView();
	}
	
	private void initView() {
		mTvMsgHistory = (TextView) mContentView.findViewById(R.id.tv_msg_history);
		mTvChatSetting = (TextView) mContentView.findViewById(R.id.tv_chat_setting);
		mTvMsgHistory.setOnClickListener(this);
		mTvChatSetting.setOnClickListener(this);
	}

	private void setWindow() {
		// 设置PopupWindow的View
		this.setContentView(mContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_msg_history:
			if (listener != null) {
				listener.OnMsgHistoryClick();
			}
			break;
		case R.id.tv_chat_setting:
			if (listener != null) {
				listener.OnChatSettingClick();
			}
			break;

		default:
			break;
		}
		dismiss();
		
	}
	
	public void setChatSettingListener(ChatSettingListener listener){
		this.listener = listener;
	}
	
	public interface ChatSettingListener{
		void OnMsgHistoryClick();
		void OnChatSettingClick();
	}

}
