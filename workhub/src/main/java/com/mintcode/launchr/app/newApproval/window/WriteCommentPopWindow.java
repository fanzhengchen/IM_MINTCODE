package com.mintcode.launchr.app.newApproval.window;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.MeetingApi;
import com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity;
import com.mintcode.launchr.app.newApproval.activity.ApprovalMainActivity;
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.CustomToast;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

public class WriteCommentPopWindow extends PopupWindow implements OnClickListener,OnResponseListener{

	private View mContentView;
	private Context mContext;
	private ImageButton mBtnCanel;
	private ImageButton mBtnOk;
	private EditText mEdtRemark;
	private EditText mEdtBottom;
	private TextView mTvSelectNext;
	private UserDetailEntity mUser;
	private RelativeLayout mRelUser;
	private RelativeLayout mRelButton;
	private RelativeLayout mRlNull;
	private TextView mTvName;
	
	private int mType;
	public static final int TYPE_RECALL = 0;
	public static final int TYPE_RUFUSE = 1;
	public static final int TYPE_AGREE = 2;
	public static final int TYPE_TURN_OVER = 3;
	public static final int TYPE_MEETING_REFUSE = 4;
	public static final int TYPE_MEETING_CANCEL = 5;
	private String mStrType;

	private String mShowId;
	
//	private DeptFragment mDeptFragment;
	
	/**
	 * true 列表删除 / false 详情中删除
	 */
	
	/** 调用WriteCommentPopWindow是哪一个类，用于执行调用类中的方法，如果不用调用则为-1*/
	private int mWay;
//	private DeptWindow deptWindow;
	
	private int mPosition;
	
	private int mIndex;
	
	private startActivityLinsenter mstartActivityLinsenter;

	/** 下一审批人头像*/
	private ImageView mIvHeader;
	
	public WriteCommentPopWindow(Context context,int type, String showId,int position,int way){
		this.mContext = context;
		this.mType = type;
		this.mShowId = showId;
		this.mPosition = position;
		this.mWay = way;
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.dialog_remark, null);
		setWindow();
		initsView();
	}
	
	public WriteCommentPopWindow(Context context){
		this.mContext = context;		
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.dialog_remark, null);
		setWindow();
		initsView();
	}
	
	private void initsView() {
		mBtnCanel = (ImageButton) mContentView.findViewById(R.id.btn_cancel);
		mBtnOk = (ImageButton) mContentView.findViewById(R.id.btn_ok);
		mEdtRemark = (EditText) mContentView.findViewById(R.id.edt_remark);
		mEdtBottom = (EditText) mContentView.findViewById(R.id.edt_bottom);
		mTvSelectNext = (TextView) mContentView.findViewById(R.id.tv_select_next);
		mRelButton = (RelativeLayout) mContentView.findViewById(R.id.rl_select_user_bottom);
		mRelUser = (RelativeLayout) mContentView.findViewById(R.id.rel_user);
		mTvName = (TextView) mContentView.findViewById(R.id.tv_name);
		mRlNull = (RelativeLayout)mContentView.findViewById(R.id.rl_remark_null);
		mIvHeader = (ImageView) mContentView.findViewById(R.id.iv_next_friend);
//		mDeptFragment = new DeptFragment();
		
		
//		mDeptFragment.setOnSelectUserListener(this);
		mRlNull.setOnClickListener(this);
		mBtnCanel.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
		mTvSelectNext.setOnClickListener(this);
		mRelUser.setOnClickListener(this);
		
		
		switch (mType) {
		case TYPE_RECALL:
		case TYPE_RUFUSE:
		case TYPE_AGREE:
		case TYPE_MEETING_REFUSE:
		case TYPE_MEETING_CANCEL:
			mRelButton.setVisibility(View.GONE);
			break;
		case TYPE_TURN_OVER:
			mRelButton.setVisibility(View.VISIBLE);
			break;

		default:
			break;
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
		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// 设置PopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popupAnimation);
		// 实例化一个ColorDrawable颜色为透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
	}
	public void setData(int type, String showId,int position,int way) {
		mType = type;
		mShowId = showId;
		mPosition = position;
		this.mWay = way;
	}
	public void show(View parent,int type,String showId, int position,int way) {
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		this.mType = type;
		this.mShowId = showId;
		this.mPosition = position;
		this.mWay = way;
	}
	public void show(View parent) {
		
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		getFocus(mEdtRemark);
		
	}
	/**
	 * 隐藏软键盘
	 */
	private void hideSoftInputWindow(View v,Context context){
		 InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public void setCompleteSelected(UserDetailEntity users,int index) {
		mUser = users;
		mTvSelectNext.setVisibility(View.GONE);
		mRelUser.setVisibility(View.VISIBLE);
		mTvName.setText(mUser.getTrueName());
		mIndex = index;

		HeadImageUtil.getInstance(mContext).setAvatarResourceAppendUrl(mIvHeader, users.getShowId(), 2, 60, 60);
	}
	
	@Override
	public void onClick(View v) {
		//取消
		if (v == mBtnCanel) {
			    this.dismiss();
				hideSoftInputWindow(v,mContext);
		} else if (v == mBtnOk) {
			
			String reason = mEdtRemark.getText().toString().trim();
			if(reason.equals("")){
				Toast.makeText(mContext,mContext.getString(R.string.apply_message_send_toast), Toast.LENGTH_SHORT).show();
				return;
			}
			switch (mType) {

			case TYPE_RECALL:
//				ApproveAPI.getInstance().dealApprove( this, mShowId,"CALL_BACK", reason);
				ApproveAPI.getInstance().processApprove(this, mShowId, "CALL_BACK", reason);
				this.dismiss();
				hideSoftInputWindow(v, mContext);
				mStrType = "CALL_BACK";
				break;
			case TYPE_RUFUSE:
//				ApproveAPI.getInstance().dealApprove(this, mShowId, "DENY",reason);
				ApproveAPI.getInstance().processApprove(this, mShowId, "DENY", reason);
				this.dismiss();
				hideSoftInputWindow(v, mContext);
				mStrType = "DENY";
				break;
			case TYPE_AGREE:
//				ApproveAPI.getInstance().dealApprove(this, mShowId, "APPROVE",reason);
				ApproveAPI.getInstance().processApprove(this, mShowId, "APPROVE",reason);
				this.dismiss();
				hideSoftInputWindow(v, mContext);
				mStrType = "APPROVE";
				break;
			case TYPE_TURN_OVER:
				if (mUser == null) {
					Toast.makeText(mContext, mContext.getString(R.string.approval_select_next),Toast.LENGTH_SHORT).show();
				} else {
//					ApproveAPI.getInstance().TransmitApprove(this, mShowId,mUser.getName(), mUser.getTrueName(), reason);
					ApproveAPI.getInstance().transmitApproveV2(this, mShowId, mUser.getName(), mUser.getTrueName(), reason);
					this.dismiss();
					hideSoftInputWindow(v,mContext);
				}
				mStrType = "APPROVE";
				break;
			case TYPE_MEETING_REFUSE:
				MeetingApi.getInstance().confirmMeeting( this, mShowId, 0, reason);
				this.dismiss();
				hideSoftInputWindow(v,mContext);
				break;
			case TYPE_MEETING_CANCEL:
				MeetingApi.getInstance().cancelMeeting(this,mShowId,reason);
				this.dismiss();
				hideSoftInputWindow(v,mContext);
				break;
			default:
				break;
			}
		}else if(v == mRlNull){
			this.dismiss();
			hideSoftInputWindow(v,mContext);
		} else if( v == mTvSelectNext){
				mstartActivityLinsenter.startDeptActiviy();
			
				hideSoftInputWindow(v,mContext);
		}else if(v == mRelUser){
			mstartActivityLinsenter.startDeptActiviy();
			
			hideSoftInputWindow(v,mContext);
		}
		hideSoftInputWindow(v,mContext);
		
	}

	
	private void getFocus(final EditText editText) {
		editText.postDelayed(new Runnable() {

			@Override
			public void run() {
				editText.setFocusable(true);
				editText.setFocusableInTouchMode(true);
				editText.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}, 300);
	}
	
	//response 返回值判断
		private void NormalHandlePOJO(NormalPOJO pojo){
			if(pojo == null){
				return;
			}
			if(pojo.getBody().getResponse().isIsSuccess() == false){
				CustomToast.showToast(mContext, pojo.getBody().getResponse().getReason(), 1000);
				return;
			}
			
		}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		if(response == null){
			return;
		}
		NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
		NormalHandlePOJO(pojo);

			if(mWay == 1){
				if(mContext instanceof ApprovalMainActivity){
					ApprovalMainActivity activity = (ApprovalMainActivity)mContext;
					activity.updateApproveList();
				}

			}else if(mWay == 2){
				 if(mContext instanceof ApproveDetailActivity){
					ApproveDetailActivity activity = (ApproveDetailActivity)mContext;
					activity.approvalDeal();
				}
			}else if(mWay == -1){
				if(mContext instanceof MeetingDetailActivity){
					MeetingDetailActivity activity = (MeetingDetailActivity)mContext;
					activity.finish();
				}
			}
	}

	@Override
	public boolean isDisable() {
		return false;
	}

	public void setstartActivityLinsenter(startActivityLinsenter listener){
		this.mstartActivityLinsenter = listener;
	}
	
	public interface startActivityLinsenter{
		void startDeptActiviy();
	}
	
}



