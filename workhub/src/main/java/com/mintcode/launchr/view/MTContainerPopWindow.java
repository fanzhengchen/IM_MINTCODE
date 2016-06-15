package com.mintcode.launchr.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;


public class MTContainerPopWindow extends PopupWindow implements OnClickListener {

		private View mContentView;
		private Context mContext;
		private TextView mTvCancel;
		private ImageView mIvTransparency;

		private LinearLayout mContainerViewGroup;

		public MTContainerPopWindow(Context context) {
			super(context);
			mContext = context;
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = inflater.inflate(R.layout.popwindow_container_view, null);
			setWindow();
			initView();
		}

		private void initView() {
			mContainerViewGroup = (LinearLayout) mContentView.findViewById(R.id.main_container);
			mTvCancel = (TextView) mContentView.findViewById(R.id.iv_popwindow_null);
			mIvTransparency = (ImageView) mContentView.findViewById(R.id.iv_popwindow_tran);
			mTvCancel.setOnClickListener(this);
			mIvTransparency.setOnClickListener(this);
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

		public void addView(View view){
			mContainerViewGroup.addView(view);
			//ImageView lineView = new ImageView(mContext);
			//android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);
			//lineView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bar_launchr));
			//lineView.setLayoutParams(lp);
			//mContainerViewGroup.addView(lineView);
		}
		
		public void addTextView(String name, OnClickListener listener){
//			<TextView 
//	        android:id="@+id/tv_popwindow_add_meeting"
//	        android:layout_width="match_parent"
//	        android:layout_height="47dp"
//	        android:gravity="center"
//	        android:text="添加会议"
//	        android:textSize="15sp"
//	        android:textColor="@color/black"
//	        />
			
			TextView textView = new TextView(mContext);
			textView.setText(name);
			textView.setTextSize(15);
			textView.setTextColor(mContext.getResources().getColor(R.color.black));
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setOnClickListener(listener);
			addView(textView);
		}
		
		public void addTextView(int rId, OnClickListener listener){
//			<TextView 
//	        android:id="@+id/tv_popwindow_add_meeting"
//	        android:layout_width="match_parent"
//	        android:layout_height="47dp"
//	        android:gravity="center"
//	        android:text="添加会议"
//	        android:textSize="15sp"
//	        android:textColor="@color/black"
//	        />
			
			TextView textView = new TextView(mContext);
			textView.setText(rId);
			textView.setTextSize(15);
			textView.setTextColor(mContext.getResources().getColor(R.color.black));
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TTDensityUtil.dip2px(mContext, 47)));
			textView.setOnClickListener(listener);
			addView(textView);
		}
		
		/** 添加复制选项*/
		public void addCopyTextView(String name, OnClickListener listener){
			TextView textView = new TextView(mContext);
			textView.setText(name);
			textView.setTextSize(15);
			textView.setTextColor(mContext.getResources().getColor(R.color.black));
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TTDensityUtil.dip2px(mContext, 47)));
			textView.setOnClickListener(listener);
			addView(textView);
			
			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bar_launchr));
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TTDensityUtil.dip2px(mContext, 1)));
			addView(imageView);
		}
		
		public void show(View parent) {
			showAtLocation(parent, Gravity.CENTER, 0, 0);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_popwindow_null:
				dismiss();
			case R.id.iv_popwindow_tran:
				dismiss();
				break;

			default:
				break;
			}
		}
	}


