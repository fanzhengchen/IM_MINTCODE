package com.mintcode.launchr.app.newApproval.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;


public class AgreePopWindow extends PopupWindow implements OnClickListener {

		private View mContentView;
		private Context mContext;
		private TextView mTvAgreen;
		private TextView mTvTurnOver;
		private TextView mTvCancel;
		private ImageView mIvTransparency;

		private String mShowId;
		private ItemSelectListener mListener;
		
		private int mPosition;		
		
		private boolean mWay;

		public AgreePopWindow(Context context, String showId,int position) {
			super(context);
			mContext = context;
			mShowId = showId;
			mPosition = position;
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = inflater.inflate(R.layout.popwindow_agree, null);
			setWindow();
			initView();
		}
		public AgreePopWindow(Context context) {
			super(context);
			mContext = context;
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = inflater.inflate(R.layout.popwindow_agree, null);
			setWindow();
			initView();
		}
		public AgreePopWindow(Context context, String showId,int position,ItemSelectListener listener) {
			super(context);
			mShowId = showId;
			mPosition = position;
			this.mListener = listener;
			LayoutInflater inflater = LayoutInflater.from(context);
			mContentView = inflater.inflate(R.layout.popwindow_agree, null);
			setWindow();
			initView();
		}

		private void initView() {
			mTvAgreen = (TextView) mContentView.findViewById(R.id.tv_agree);
			mTvTurnOver = (TextView) mContentView.findViewById(R.id.tv_turn_over);
			mTvCancel = (TextView) mContentView.findViewById(R.id.tv_cancel);
			mIvTransparency = (ImageView) mContentView.findViewById(R.id.iv_popwindow_tran);
			mTvAgreen.setOnClickListener(this);
			mTvTurnOver.setOnClickListener(this);
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

		public void show(View parent) {
			showAtLocation(parent, Gravity.CENTER, 0, 0);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_agree:
				mListener.agree(mShowId,mPosition);
				dismiss();
				break;
			case R.id.tv_turn_over:
				mListener.turnOver(mShowId,mPosition);				
				dismiss();
				break;
			case R.id.tv_cancel:
			case R.id.iv_popwindow_tran:
				dismiss();
				break;

			default:
				break;
			}
		}
		public void setItemSelectListener(ItemSelectListener listener){
			this.mListener = listener;
		}
		public interface ItemSelectListener {
			void agree(String showId, int position);

			void turnOver(String showId, int position);
		}
	}


