package com.mintcode.launchr.app.meeting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;

/**
 * @author JulyYu
 *
 * d2015-8-18
 */
public class MeetingDelectDialog extends Dialog{
	
	
	private Context mContext;
	/**
	 * 删除会议
	 */
	private RelativeLayout mRlDelect;
	/**
	 * 取消删除
	 */
	private RelativeLayout mRlCanel;
	/**
	 * 标题
	 */
	private TextView mTitle;
	/**
	 * 内容
	 */
	private TextView mContent;
	/**
	 * 重复操作仅此条
	 */
	private TextView mRepeactOnly;
	/**
	 * 重复操作此及将来
	 */
	private TextView mRepeactAll;
	
	private RelativeLayout mReapactOnly ;
	
	private RelativeLayout mReapactAll;
	
	public MeetingDelectDialog(Context context) {
		super(context);
		this.mContext = context;
		
	}
	public MeetingDelectDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public MeetingDelectDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
	}


	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mView = inflater.inflate(R.layout.dialog_delect, null);
		mTitle = (TextView)mView.findViewById(R.id.tv_dialog_title);
		mContent = (TextView)mView.findViewById(R.id.tv_dialog_context);
		mRlDelect = (RelativeLayout)mView.findViewById(R.id.rl_dialog_meeting_delect);
		mRlCanel = (RelativeLayout)mView.findViewById(R.id.rl_dialog_meeting_canel);
		mReapactOnly = (RelativeLayout) mView.findViewById(R.id.rl_dialog_reapect_only);
		mReapactAll = (RelativeLayout) mView.findViewById(R.id.rl_dialog_reapect_all);				
		
		mRepeactOnly = (TextView)mView.findViewById(R.id.tv_repeact_only);
		mRepeactAll = (TextView)mView.findViewById(R.id.tv_repeact_all);
		mRlCanel.setOnClickListener((View.OnClickListener) mContext);
		mRlDelect.setOnClickListener((View.OnClickListener) mContext);
		mReapactOnly.setOnClickListener((View.OnClickListener) mContext);
		mReapactAll.setOnClickListener((View.OnClickListener) mContext);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(mView);
	}
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		mTitle.setText(title.trim());
	}
	/**
	 * 设置内容
	 * @param content
	 */
	public void setContent(String content){
		mContent.setText(content.trim());
	}
	

}
