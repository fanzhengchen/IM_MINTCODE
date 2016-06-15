package com.mintcode.launchr.app.meeting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.MeetingApi;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.meeting.activity.MeetingActivity;
import com.mintcode.launchr.app.meeting.activity.NewMeetingActivity;
import com.mintcode.launchr.app.place.AddPlacesActivity;
import com.mintcode.launchr.pojo.MeetingUnFreePOJO;
import com.mintcode.launchr.pojo.RoomListPOJO;
import com.mintcode.launchr.pojo.SchedulePOJO;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.pojo.entity.MeetingUnFreeRoomEntity;
import com.mintcode.launchr.pojo.entity.RoomEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议时间选择
 * @author KevinQiao
 *
 */
public class MeetingTimerDialog extends Dialog implements OnTimerListener,OnResponseListener{

	/** context */
	private Context mContext;
	@Bind(R.id.timer)
	protected Timer mTimer;
	@Bind(R.id.meeting_conflict)
	protected TextView mTvConflict;
	@Bind(R.id.tv_room)
	protected TextView mTvRoom;
	
	private Calendar mBeginCal  = Calendar.getInstance();
	private Calendar mEndCal  = Calendar.getInstance();
	/** 是否是室内会议地址*/
	private boolean mBoolMeetingRoomAddress = true;
	private List<RoomEntity> mShowRoomList;
	private List<RoomEntity> mRoomList;
	/** 选择的会议室*/
	private RoomEntity mSeletedRoom;
	/** 用户列表*/
	private List<String> mStrListPerson;
	/** 选择的地址*/
	private String mStrAddress;
	private List<EventEntity> mEventList;
	private List<String> mStrListPersonId = new ArrayList<>();
	private List<String> mStrListPersonName = new ArrayList<>();
	private HashMap<String,String> mHashNameKey = new HashMap<>();
	private StringBuilder mStrBuildUserId;

	public static class IntentData implements Serializable {
		private static final long serialVersionUID = -8247010101553887266L;
		public List<EventEntity> list;
	}
	public MeetingTimerDialog(Context context) {
		super(context);
		mContext = context;
		
	}

	public MeetingTimerDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		
	}

	protected MeetingTimerDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflat = LayoutInflater.from(mContext);
		View view = inflat.inflate(R.layout.dialog_meeting_timer, null);
		setContentView(view);
		ButterKnife.bind(this,view);
		String conflictStr = mContext.getResources().getString(R.string.meeting_time_hint, 0);
		mTvConflict.setText(conflictStr);
		mTimer.setConflictViewDisplay(true);
		mTimer.setTimeBeforeNow(true);
		mTimer.setOnTimerListener(this);
		mTimer.setStyle(Timer.FIVE_POSITON);
		mBeginCal.setTimeInMillis(mTimer.getBeginTime());
		mEndCal.setTimeInMillis(mTimer.getEndTime());
	}
	/** 设置冲突事件*/
	public void setConflictText(int conflictCount){
		String conflictStr = mContext.getResources().getString(R.string.meeting_time_hint, conflictCount);
		mTvConflict.setText(conflictStr);
	}

//	public void setRoom(RoomEntity room ,String strPlace){
//		if (room != null && room.getR_NAME() != null) {
//			mTvRoom.setText(room.getR_NAME());
//			if (mRoomType == ROOM_TYPE) {
//				rbtnMeetingRoom.setChecked(true);
//				mTvRoom.setText(room.getR_NAME());
//			}else{
//				rbtnOutRoom.setChecked(true);
//				mTvRoom.setText(strPlace);
//			}
//		}else{
//			if (mRoomType == ROOM_TYPE) {
//				rbtnMeetingRoom.setChecked(true);
//				String text = mContext.getResources().getString(R.string.choose_hint);
//				mTvRoom.setText(text);
//			}else{
//				rbtnOutRoom.setChecked(true);
//				mTvRoom.setText(strPlace);
//			}
//		}
//	}
	
//	public void setListener(OnTimerListener listener){
//		mTimer.setOnTimerListener(listener);
//	}
	
	/** 设置时间和人员*/
	public void setTimeAndPerson(Long beginMill, Long endMill,List<String> persons,
								 RoomEntity room,String address,boolean bool,List<UserDetailEntity> personList){
		mStrListPersonId.clear();
		mStrListPersonName.clear();
		mHashNameKey.clear();
		mStrBuildUserId = new StringBuilder();
		for(UserDetailEntity entity : personList){
			String str = entity.getName();
			String name = entity.getTrueName();
			mStrListPersonId.add(str);
			mStrListPersonName.add(name);
			mHashNameKey.put(str,name);
			mStrBuildUserId.append(str).append("●");
		}
		mStrBuildUserId.substring(0,mStrBuildUserId.length() - 1);
		if(beginMill != null && endMill != null){
			mBeginCal.setTimeInMillis(beginMill);
			mEndCal.setTimeInMillis(endMill);
			mTimer.setShowTime(beginMill, endMill);
			mTimer.setStyle(Timer.FIVE_POSITON);

		}
		if(room != null){
			mTvRoom.setText(room.getR_NAME());
			mSeletedRoom = room;
			mStrAddress = room.getR_NAME();
		}else if(!TextUtils.isEmpty(address)){
			mTvRoom.setText(address);
			mStrAddress = address;
		}
		mBoolMeetingRoomAddress = bool;
//		MeetingApi.getInstance().getNoFreeMeetingRoom(this,beginMill,beginMill);
		MeetingApi.getInstance().getMeetingRoomList(this);
	}

	public void setEventDatas(List<EventEntity> datas){
		mTimer.setEventData(datas);
		int count = mTimer.getConflictCount();
		setConflictText(count);
	}
	
	public void setNowTime(long now){
		mTimer.setNowTime(now);
	}
	
	public int getConflictCount(){
		return mTimer.getConflictCount();
	}
	public void setOutAddress(String address){
		mStrAddress = address;
		mTvRoom.setText(address);
	}
	/** 选择会议地址*/
	@OnClick(R.id.rel_out_of_room)
	protected void selectRoom(){
		if (mBoolMeetingRoomAddress) {
			if(mRoomList==null || mRoomList.isEmpty()){
				Toast.makeText(mContext,mContext.getString(R.string.network_exception),Toast.LENGTH_SHORT).show();
				return;
			}
			MeetingRoomDialog dialog = new MeetingRoomDialog(mContext, R.style.my_dialog);
			dialog.show();
			Window window = dialog.getWindow();
			window.setGravity(Gravity.BOTTOM);
			dialog.setData(this,mRoomList,0);
		}else{
			Intent intent = new Intent(mContext, AddPlacesActivity.class);
			intent.putExtra(Const.ADDRESS, mTvRoom.getText().toString());
			if(mContext instanceof NewMeetingActivity){
				if(((NewMeetingActivity)mContext) != null){
					((NewMeetingActivity)mContext).startActivityForResult(intent, NewMeetingActivity.REQ_ADD_PLACE);
				}
			}
		}
	}
	/** 室内地址*/
	@OnClick(R.id.rbtn_out)
	protected void setGetOutAddress(){
		mBoolMeetingRoomAddress = false;
		mSeletedRoom = null;
	}
	/** 户外地址*/
	@OnClick(R.id.rbtn_room)
	protected void setMeetingRoom(){
		mBoolMeetingRoomAddress = true;
		mStrAddress = "";
	}

	/** 户外地址选择*/
	private void addPlace(){
		Intent intent = new Intent(mContext, AddPlacesActivity.class);
		intent.putExtra(Const.ADDRESS, mTvRoom.getText().toString());
		if(mContext instanceof NewMeetingActivity){
			if(((NewMeetingActivity)mContext) != null){
 				((NewMeetingActivity)mContext).startActivityForResult(intent, NewMeetingActivity.REQ_ADD_PLACE);
			}
		}
	}
	/** 会议室地址选择*/
	private void showRoom(){
		if(mRoomList==null || mRoomList.isEmpty()){
			Toast.makeText(mContext,mContext.getString(R.string.network_exception),Toast.LENGTH_SHORT).show();
			return;
		}
		final MeetingRoomDialog dialog = new MeetingRoomDialog(mContext, R.style.my_dialog);
		dialog.show();
		dialog.setData(this,mRoomList,0);
	}
	/** 冲突事件查看*/
	@OnClick(R.id.btn_another)
	protected void seeConfigPersonEvent(){

		Intent in = new Intent(mContext, MeetingActivity.class);
		IntentData data = new IntentData();
		data.list = mEventList;
		Bundle bData = new Bundle();
		bData.putSerializable(MeetingActivity.USER_HASHMAP, mHashNameKey);
		in.putStringArrayListExtra(MeetingActivity.USER_ID, (ArrayList<String>) mStrListPersonId);
		in.putStringArrayListExtra(MeetingActivity.USER_NAME, (ArrayList<String>) mStrListPersonName);
		in.putExtra(MeetingActivity.MEETING_TIME,mBeginCal.getTimeInMillis());
		in.putExtra(MeetingActivity.EVENT_DATA, data);
		in.putExtras(bData);
		mContext.startActivity(in);
	}
	/** 保存时间选择的内容*/
	@OnClick(R.id.cb_check)
	protected void saveTimeSelect(){
		if (mEndCal.getTimeInMillis() >= mBeginCal.getTimeInMillis()) {
			if (isCheckedTrue()) {
				if(mContext instanceof NewMeetingActivity){
					((NewMeetingActivity)mContext).setTimeAndAddress(mBeginCal.getTimeInMillis(),mEndCal.getTimeInMillis(),mSeletedRoom,mStrAddress);
				}
				this.dismiss();
			}else{
				String toast = mContext.getResources().getString(R.string.choose_hint);
				Toast.makeText(mContext,toast,Toast.LENGTH_SHORT).show();
			}
		}else{
			String toast = mContext.getResources().getString(R.string.end_time_must_not_be_later_than_begin_time);
			Toast.makeText(mContext,toast,Toast.LENGTH_SHORT).show();
		}
	}
	@OnClick(R.id.cb_check_canel)
	protected void  cancelSelect(){
		this.dismiss();
	}
	private boolean isCheckedTrue(){
		if (mBoolMeetingRoomAddress) {
			if (mSeletedRoom != null) {
				return true;
			}else{
				return false;
			}
		}else{
			if (TextUtils.isEmpty(mStrAddress)) {
				return false;
			}else{
				return true;
			}
		}
	}




	@Override
	public void OnTimeChangeListenner(View view, int year, int month, int day,
			int hour, int minute, int type, boolean isEnd) {
		if(isEnd){
			mEndCal.set(year,month - 1,day,hour,minute);
			setNowTime(mEndCal.getTimeInMillis());
		}else{
			mBeginCal.set(year,month - 1,day,hour,minute);
			setNowTime(mBeginCal.getTimeInMillis());
		}
		if(mBeginCal.getTimeInMillis() < mEndCal.getTimeInMillis()){
			ScheduleApi.getInstance().getScheduleList(this,mStrBuildUserId.toString(), mBeginCal.getTimeInMillis(),mEndCal.getTimeInMillis(),null);
		}

		// 必须参加会议的
//		List<String> userName = new ArrayList<>();
//		for (int i = 0; i < mRequireList.size(); i++) {
//			UserDetailEntity entity = mRequireList.get(i);
//			String str = entity.getName();
//			String name = entity.getTrueName();
//			userName.add(str);
//		}
//		Calendar c = Calendar.getInstance();
//		c.clear();
//		c.set(Calendar.YEAR, year);
//		c.set(Calendar.MONTH, month-1);
//		c.set(Calendar.DAY_OF_MONTH, day);
//		long startTime = c.getTimeInMillis();
//		long endTime = startTime + 24 * 3600 * 1000;
//

//		if (isEnd) {
//			mEndCal.set(year, month - 1, day, hour, minute);
//			// 最近
//			Calendar setTime = Calendar.getInstance();
//			setTime.clear();
//			setTime.set(year, month - 1, day, hour, minute);
//			mNow = setTime.getTimeInMillis();
//			mDialog.setNowTime(mNow);
//			MeetingApi.getInstance().getNoFreeMeetingRoom(this, mBeginCal.getTimeInMillis(), mEndCal.getTimeInMillis());

//		}else{
//			mBeginCal.set(year, month - 1, day, hour, minute);
//			Calendar setTime = Calendar.getInstance();
//			setTime.clear();
//			setTime.set(year, month - 1, day, hour, minute);
//			mNow = setTime.getTimeInMillis();
//			mDialog.setNowTime(mNow);
//
//		}
		// 获取非空会议室
//		getUnFreeRoom();
//		mOldYear = year;
//		mOldMonth = month;
//		mOldDay = day;
//		int count = mDialog.getConflictCount();
//		mDialog.setConflictText(count);
		
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		if(response == null){
			return;
		}
		if (taskId.equals(ScheduleApi.URL.GET_LIST)) {
			SchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(
					response.toString(), SchedulePOJO.class);
			if (pojo != null && pojo.isResultSuccess()) {
				mEventList = pojo.getBody().getResponse().getData();
				Iterator<EventEntity> iterable =  mEventList.iterator();
				while(iterable.hasNext()){
					EventEntity entity = iterable.next();
					if(!TextUtils.isEmpty(entity.getType())){
						if(entity.getType().equals("company_festival") || entity.getType().equals("statutory_festival")){
							iterable.remove();
						}
					}
				}
				setEventDatas(mEventList);
			}
		}else if (taskId.equals(MeetingApi.TaskId.TASK_URL_GET_NOFREE_MEETINGROOM)) {
				handleMeetingUnFreeRoomResult(response);
		}else if (taskId.equals(MeetingApi.TaskId.TASK_URL_GET_MEETINGROOM_LIST)) {
				handleGetRoomListResult(response);
		}
	}

	@Override
	public boolean isDisable() {
		return false;
	}
	private void handleGetRoomListResult(Object response){
		RoomListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), RoomListPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 获取会议室列表操作
				mRoomList = pojo.getBody().getResponse().getData();
			}else{
				Toast.makeText(mContext,pojo.getHeader().getReason(),Toast.LENGTH_SHORT).show();
			}
		}else{
//			showNetWorkGetDataException();
		}
		ScheduleApi.getInstance().getScheduleList(this,mStrBuildUserId.toString(),mBeginCal.getTimeInMillis(), mEndCal.getTimeInMillis(),null);

	}
	private void handleMeetingUnFreeRoomResult(Object response){
		MeetingUnFreePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingUnFreePOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<MeetingUnFreeRoomEntity> list = pojo.getBody().getResponse().getData();
				getRoomList(list);
			}
		}
	}
	private void getRoomList(List<MeetingUnFreeRoomEntity> list){
//		mShowRoomList.clear();
//		if (list != null && !list.isEmpty()) {
//			for (int j = 0; j < mRoomList.size(); j++) {
//				RoomEntity room = mRoomList.get(j);
//				boolean b = false;
//				for (int i = 0; i < list.size(); i++) {
//					MeetingUnFreeRoomEntity entity = list.get(i);
//					if (room.getSHOW_ID().equals(entity.getMeetingRoomNo())) {
//						b = true;
//						break;
//					}
//				}
//				if (!b) {
//					mShowRoomList.add(room);
//				}
//			}
//		}else{
//			mShowRoomList.addAll(mRoomList);
//		}
	}

	public void setRoom(RoomEntity room) {
		mSeletedRoom = room;
		mTvRoom.setText(mSeletedRoom.getR_NAME());
	}
}
