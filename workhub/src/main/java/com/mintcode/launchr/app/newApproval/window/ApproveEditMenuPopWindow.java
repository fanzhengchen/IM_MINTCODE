package com.mintcode.launchr.app.newApproval.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;

public class ApproveEditMenuPopWindow extends PopupWindow implements OnClickListener{
	
	private Context mContext;
	
	private View mContentView;
	
	private RelativeLayout mRlReCall;
	
	private RelativeLayout mRlRefuse;
	
	private RelativeLayout mRlPass;
	
	private ApprovePopWindowListener mApprovePopWindowListener;
	
	private String mShowId;
	
	private int mPosition;
	
	public ApproveEditMenuPopWindow(Context context,String showid,int position){
		this.mContext = context;
		mShowId = showid;
		mPosition = position;
		initViews();
		setWindow();
	}


	private void initViews() {
		
		LayoutInflater inflater  = LayoutInflater.from(mContext);
		mContentView = inflater.inflate(R.layout.item_approve_menu, null);
		
		mRlReCall = (RelativeLayout)mContentView.findViewById(R.id.rl_approval_recall);
		mRlRefuse = (RelativeLayout)mContentView.findViewById(R.id.rl_approval_refuse);
		mRlPass = (RelativeLayout)mContentView.findViewById(R.id.rl_approval_pass);
		
		mRlReCall.setOnClickListener(this);
		mRlRefuse.setOnClickListener(this);
		mRlPass.setOnClickListener(this);
		
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
		// 设置PopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popupAnimation);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
//		 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}


	@Override
	public void onClick(View v) {
		
		if(v == mRlReCall){
			mApprovePopWindowListener.reCall(mShowId,mPosition);
			this.dismiss();
		}else if(v == mRlRefuse){
			mApprovePopWindowListener.refuse(mShowId,mPosition);
			this.dismiss();
		}else if(v == mRlPass){
			mApprovePopWindowListener.pass(mShowId,mPosition);
			this.dismiss();
		}
		
	}

	public void setApprovePopWindowListener(ApprovePopWindowListener listener){
		this.mApprovePopWindowListener = listener;
	}
	
    public interface ApprovePopWindowListener{
    	void reCall(String showid, int position);
    	void refuse(String showid, int position);
    	void pass(String showid, int position);
    }
	
	
	
	
	
	
	
	
}
