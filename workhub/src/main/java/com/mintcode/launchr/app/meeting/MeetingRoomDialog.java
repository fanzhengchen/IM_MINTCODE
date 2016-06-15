package com.mintcode.launchr.app.meeting;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.entity.RoomEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议时间选择
 * @author KevinQiao
 *
 */
public class MeetingRoomDialog extends Dialog implements OnValueChangeListener{

	/** context */
	private Context mContext;
	
	/** 会议场所选择控件 */
	@Bind(R.id.np_room)
	protected NumberPicker mNpRoom;
//	OnValueChangeListener onValueChangeListener;

	private List<RoomEntity> mShowRoomList;
	private int mIndex = 0;
	private MeetingTimerDialog mDialog;
	
//	public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
//		this.onValueChangeListener = onValueChangeListener;
//		if (onValueChangeListener != null) {
//			mNpRoom.setOnValueChangedListener(onValueChangeListener);
//		}
//	}

	public MeetingRoomDialog(Context context) {
		super(context);
		mContext = context;
		
	}

	public MeetingRoomDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		
	}

	protected MeetingRoomDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflat = LayoutInflater.from(mContext);
		View view = inflat.inflate(R.layout.dialog_num_room, null);
		setContentView(view);
		ButterKnife.bind(this,view);
		mNpRoom.setOnValueChangedListener(this);
	}
	

	public void setData(MeetingTimerDialog dialog,List<RoomEntity> list,int position){
		mDialog = dialog;
		mShowRoomList = list;
		if (list != null) {
			if (!list.isEmpty()) {
				mNpRoom.setMaxValue(list.size() - 1);
				mNpRoom.setMinValue(0);
				String[] strArray = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					RoomEntity entity = list.get(i);
					strArray[i] = entity.getR_NAME();
				}
				mNpRoom.setDisplayedValues(strArray);
				mNpRoom.setValue(position);
			}else{
				mNpRoom.setMaxValue(0);
				mNpRoom.setDisplayedValues(new String[]{""});
				mNpRoom.setValue(position);
			}
		}else{
			mNpRoom.setMaxValue(0);
			mNpRoom.setDisplayedValues(new String[]{""});
		}
		mNpRoom.postInvalidate();
		
	}
	@OnClick(R.id.cb_check_c)
	protected void selectMeetingRoom(){
		if ((mShowRoomList != null) && !mShowRoomList.isEmpty()) {
//			index = dialog.getIndex();
			RoomEntity mRoomEntity = mShowRoomList.get(mIndex);
			if(mDialog != null){
				mDialog.setRoom(mRoomEntity);
			}
			this.dismiss();
		}
	}
	@OnClick(R.id.btn_cancel)
	protected void cancelSelect(){
		this.dismiss();
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		mIndex = newVal;
	}
	
	public int getIndex(){
		return mIndex;
	}
	

}
