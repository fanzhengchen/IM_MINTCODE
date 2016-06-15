package com.mintcode.launchr.app.newTask.view;

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

public class EditTaskPopWindow extends PopupWindow implements OnClickListener{
	
	private View mContentView;
	
	private TextView mTvEditTask;
	
	private TextView mTvDelectTask;
	
	private TextView mTvChangeTask;
	//透明空白处
	private ImageView mIvTransparency;
	//透明空白处
	private TextView mIvNull;
	
	private LinearLayout mRlNull;
	
	private TaskPopWindowListener mPopWindowListener;
	
	public EditTaskPopWindow(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.popwindow_edit_subtask, null);		
		setWindow();
		initView();
	}
	public void initView(){
		
		mTvEditTask = (TextView)mContentView.findViewById(R.id.tv_popwindow_task_frist);
		mTvDelectTask = (TextView)mContentView.findViewById(R.id.tv_popwindow_task_seconed);	
		mTvChangeTask = (TextView)mContentView.findViewById(R.id.tv_popwindow_task_third);		
		mIvTransparency =(ImageView)mContentView.findViewById(R.id.iv_popwindow_tran);
		mIvNull = (TextView)mContentView.findViewById(R.id.iv_popwindow_null);
		mRlNull = (LinearLayout)mContentView.findViewById(R.id.ll_popwindow_null);		
		mTvEditTask.setOnClickListener(this);
		mTvDelectTask.setOnClickListener(this);
		mTvChangeTask.setOnClickListener(this);
		mIvTransparency.setOnClickListener(this);
		mIvNull.setOnClickListener(this);
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
//	public void setFirstTextView(int string){
//		mTvAddMeeting.setText(string);
//		
//	}
//	public void setSeconedTextView(int string){		
//		mTvAddEvent.setText(string);
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//编辑任务
		case R.id.tv_popwindow_task_frist:
			mPopWindowListener.editTask();
			this.dismiss();
			break;
		//删除任务
		case R.id.tv_popwindow_task_seconed:
			mPopWindowListener.delectTask();
			this.dismiss();
			break;
		//任务转成父任务
		case R.id.tv_popwindow_task_third:
			mPopWindowListener.ChnageTask();
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
	
	public void setTaskPopWindowListener(TaskPopWindowListener listener){
		this.mPopWindowListener = listener;
	}
	public interface  TaskPopWindowListener{
		void editTask();
		void delectTask();
		void ChnageTask();
	}
}
