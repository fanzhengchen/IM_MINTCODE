package com.mintcode.launchr.app.meeting.activity;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.map.UiSettings;
//import com.baidu.mapapi.model.LatLng;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.MeetingApi;
import com.mintcode.launchr.api.MeetingApi.TaskId;
import com.mintcode.launchr.api.ScheduleApi.URL;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.MapLoadView;
import com.mintcode.launchr.app.meeting.MeetingRoomDialog;
import com.mintcode.launchr.app.meeting.MeetingTimerDialog;
import com.mintcode.launchr.app.newSchedule.fragment.DayFragment;
import com.mintcode.launchr.app.place.AddPlacesActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.pojo.MeetingDeletePOJO;
import com.mintcode.launchr.pojo.MeetingPOJO;
import com.mintcode.launchr.pojo.MeetingUnFreePOJO;
import com.mintcode.launchr.pojo.RoomListPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.JoinEntity;
import com.mintcode.launchr.pojo.entity.MeetingDetailEntity;
import com.mintcode.launchr.pojo.SchedulePOJO;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.pojo.entity.MeetingDeleteEntity;
import com.mintcode.launchr.pojo.entity.MeetingEntity;
import com.mintcode.launchr.pojo.entity.MeetingUnFreeRoomEntity;
import com.mintcode.launchr.pojo.entity.RequireEntity;
import com.mintcode.launchr.pojo.entity.RoomEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.widget.CollapsibleTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新建会议流程
 * @author KevinQiao
 *
 */
public class NewMeetingActivity extends BaseActivity implements OnSelectContactListner{

	/** 编辑实体key */
	public static final String KEY_EDITOR_ENTITY = "key_editor_entity";
	
	/** 会议详情返回码  */
	public static final int REQ_MEETING_DETAIL = 0x11;
	
	/** 添加外部场所返回码 */
	public static final int REQ_ADD_PLACE = 0x10;
	
	/** 必须参加会议的类型 */
	public static final int REQUIRE_TYPE = 1;
	
	/** 非必须参加会议类型 */
	public static final int JOIN_TYPE = 2;
	
	/** 会议场所类型 */
//	public static final int ROOM_TYPE = 3;
	
	/** 外部会议场所类型 */
//	public static final int OUT_SIDE_TYPE = 4;
	
	/** 提醒方式状态 */
	private int[] mStatus = {100, 101, 102, 103, 104, 105, 106, 107, 108,200, 201, 202, 203 };
	
	/** 提醒数组 */
	private String[] mRepeatArray;
	
	/** 提醒数组 */
	private String[] mRemintArray; 
	
	/** 标题输入 */
	@Bind(R.id.et_title)
	protected EditText mEtTitle;
	/** 必须参加人名称 */
	@Bind(R.id.tv_have_to_name)
	protected CollapsibleTextView mTvHaveToName;
	/** 选择参加人名称 */
	@Bind(R.id.tv_choose)
	protected CollapsibleTextView mTvChooseName;
	/** 重复方式 */
	@Bind(R.id.tv_repeat_name)
	protected TextView mTvRepeatName;
	/* 重复返回码*/
	public static final int RESTART_CODE=1;
	
	/* 提醒返回码*/
	public static final int REMIND_CODE=2;
	/** 提醒方式 */
	@Bind(R.id.tv_notice_name)
	protected TextView mTvNoticeName;
	/** 是否可见*/
	@Bind(R.id.cb_only_me_see)
	protected CheckBox mCbSee;
	/** 备注 */
	@Bind(R.id.et_remark)
	protected EditText mEtRemark;
	@Bind(R.id.ll_map)
	protected MapLoadView mLlMap;
	/** */
	@Bind(R.id.tv_choose_time)
	protected TextView mTvChooseTime;
	/** */
	@Bind(R.id.tv_choose_room)
	protected TextView mTvChooseRoom;
	
//	private TextView mTvRoom;
	private RoomEntity mRoomEntity;

	/** FragmentManager */
	private FragmentManager mFragManger;
	/** 联系人 */
	private ContactFragment mDeptFragment;
	/** 添加的必须参加的联系人 */
	private List<UserDetailEntity> mRequireList;
	/** 非必须参加的 */
	private List<UserDetailEntity> mJoinList;
	

	public int mType = 0;
	
	/** 提醒方式 */
	private int mReminType = 0;
	
	/** 重复类型 */
	private int mRestartType = 0;
	
	/** 选择会议场所类型 */
//	private int mRoomType = ROOM_TYPE;
	/** 是否是室内会议*/
	private boolean mBoolMeetingAddress = true;
	/** 年 */
	private int mYear;
	
	/** 月 */
	private int mMonth;
	
	/** 日 */
	private int mDay;
	
	/** 时 */
	private int mHour;
	
	/** 分 */
	private int mMinte;
	

	private Calendar mBeginCal;
	private Long mLongStart;
	private Calendar mEndCal;
	private Long mLongEnd;
	
	private List<RoomEntity> mRoomList;
	
	private List<RoomEntity> mShowRoomList;
	
	private MeetingDetailEntity mEntity;
	/** 必须参加的人*/
	private List<String> mRequireStrList;
	/** 选择参加的人*/
	private List<String> mJoinStrList;

	/** 必须参加人的Id*/
	private List<String> mRequireIdList;
	/** 选择参加人的Id*/
	private List<String> mJoinIdList;
	
	
	private long endTime = -1;
	
	private String strPlace = "";
	/** 经度、维度*/
	private String mLatitude;
	private String mLongitude;



	private long mTodayTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_meeting);
		ButterKnife.bind(this);
		initView();
		initData();
		
	}
	
	/**
	 * 实例化view
	 */
	private void initView(){

		initTime();
		mDeptFragment = new ContactFragment();
		mDeptFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE, null);
		mDeptFragment.setOnSelectContactListner(this);
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction().add(R.id.ll_contact, mDeptFragment).hide(mDeptFragment).commit();
	
		MeetingApi.getInstance().getMeetingRoomList(this);
	}
	
	
	private void initData(){

		mRequireList = new ArrayList<UserDetailEntity>();
		mJoinList = new ArrayList<UserDetailEntity>();

		mRoomList = new ArrayList<RoomEntity>();
		mShowRoomList = new  ArrayList<RoomEntity>();

		mRequireStrList = new ArrayList<String>();
		mJoinStrList = new ArrayList<String>();

		mRequireIdList = new ArrayList<>();
		mJoinIdList = new ArrayList<>();

		mTodayTime = getIntent().getLongExtra(DayFragment.TODAY_TIME,0);
		mEntity = (MeetingDetailEntity) getIntent().getSerializableExtra(KEY_EDITOR_ENTITY);
		if (mEntity != null) {
			initArrays();
			setData();
		}else{
			HeaderParam mHeaderParam = LauchrConst.getHeader(this);
			String mUserName = mHeaderParam.getUserName();
			String mStrUserName = LauchrConst.getHeader(this).getLoginName();
			String mShowId = LauchrConst.getHeader(this).getLoginName();
			UserDetailEntity entity = new UserDetailEntity();
			entity.setTrueName(mUserName);
			entity.setName(mStrUserName);
			entity.setShowId(mShowId);
			mRequireList.add(entity);
			mRequireStrList.add(mUserName);
			mTvHaveToName.setText(mRequireStrList);
		}
	}
	
	private void setData(){
		
		// 标题
		mEtTitle.setText(mEntity.getM_TITLE());
		// 内容
		mEtRemark.setText(mEntity.getM_CONTENT());
		// 必须参加会议的
		List<RequireEntity> reList = mEntity.getREQUIRE_JOIN();
		List<JoinEntity> reNameList = mEntity.getREQUIRE_JOIN_NAME();
		StringBuilder requireName = new StringBuilder();
		if (reNameList != null) {
			for (int i = 0; i < reNameList.size(); i++) {
				RequireEntity re = reList.get(i);
				JoinEntity entity = reNameList.get(i);
				String str = entity.getName();
				if (i < reNameList.size() - 1) {
					requireName.append(str).append("、");
				}
				mRequireStrList.add(str);
				mRequireIdList.add(re.getNAME());
			}
			if (requireName.length() != 0) {
				requireName.deleteCharAt(requireName.length() - 1);
			}
			if(!mRequireStrList.isEmpty()){
				mTvHaveToName.setText(mRequireStrList);
			}
		}


		
		//非必须参加的
		List<RequireEntity> joList = mEntity.getJOIN();
		StringBuilder joinNameBuild = new StringBuilder();
		List<JoinEntity> joinNameList = mEntity.getJOIN_NAME();
		if (joinNameList != null) {
			for (int i = 0; i < joinNameList.size(); i++) {
				JoinEntity entity = joinNameList.get(i);
				String str = entity.getName();
				if (i < joinNameList.size()-1) {
					joinNameBuild.append(str).append("、");
				}
				RequireEntity re = joList.get(i);
				mJoinStrList.add(str);
				mJoinIdList.add(re.getNAME());
			}
			if (joinNameBuild.length() != 0) {
				joinNameBuild.deleteCharAt(joinNameBuild.length() - 1);
			}
			if(!joinNameList.isEmpty()){
				mTvChooseName.setText(mJoinStrList);
			}
		}


		// 设置会议室，是不是 内部或者外部会议
		String room = mEntity.getR_SHOW_NAME();
		if ((room != null) && !room.equals("")) {
			mRoomEntity = new RoomEntity();
			mRoomEntity.setR_NAME(room);
			mBoolMeetingAddress = true;
			mRoomEntity.setSHOW_ID(mEntity.getR_SHOW_ID());
			mTvChooseRoom.setText(room);
		}else{
			mBoolMeetingAddress = false;
			strPlace = mEntity.getM_EXTERNAL();
			mTvChooseRoom.setText(strPlace);
		}
		// 设置显示时间
		mBeginCal.setTimeInMillis(mEntity.getM_START());
		mEndCal.setTimeInMillis(mEntity.getM_END());
		mLongStart = mEntity.getM_START();
		mLongEnd = mEntity.getM_END();
//		chooseTimeAndPlace();
		// 设置提醒方式和重复方式
		mTvRepeatName.setText(mRepeatArray[mEntity.getM_RESTART_TYPE()]);
		String remint = getReminType(mEntity.getM_REMIND_TYPE());
		mTvNoticeName.setText(remint);
		// 设置是否可见
		int mIntVisible = mEntity.getM_IS_VISIBLE();
		if(mIntVisible == 1){
			mCbSee.setChecked(true);
		}else{
			mCbSee.setChecked(false);
		}
		if(mEntity != null){
			mLatitude = mEntity.getM_LATY();
			mLongitude = mEntity.getM_LNGX();
			setMeetingAddress();
		}
	}
	
	
	private void initTime(){

		if(mTodayTime != 0) {
			mBeginCal.setTimeInMillis(mTodayTime);
			mEndCal.setTimeInMillis(mTodayTime + 3600 * 1000);
			mLongStart = mBeginCal.getTimeInMillis();
			mLongEnd = mEndCal.getTimeInMillis();
		}else{
			Calendar cal = Calendar.getInstance();
			mYear = cal.get(Calendar.YEAR);
			mMonth = cal.get(Calendar.MONTH) + 1;
			mDay = cal.get(Calendar.DAY_OF_MONTH);
			mHour = cal.get(Calendar.HOUR_OF_DAY);
			mMinte = cal.get(Calendar.MINUTE);
			mBeginCal = cal;
			mEndCal = Calendar.getInstance();
			mEndCal.set(mYear, mMonth - 1, mDay, mHour + 1, mMinte);
		}
	}


//	int mOldYear;
//	int mOldMonth;
//	int mOldDay;

//	private long mNow = System.currentTimeMillis();
	
//	@Override
//	public void OnTimeChangeListenner(View view, int year, int month, int day,
//			int hour, int minute, int type, boolean isEnd) {


//		// 必须参加会议的
//			List<String> userName = new ArrayList<>();
//		for (int i = 0; i < mRequireList.size(); i++) {
//			UserDetailEntity entity = mRequireList.get(i);
//			String str = entity.getName();
//			String name = entity.getTrueName();
//			userName.add(str);
//		}
//			Calendar c = Calendar.getInstance();
//			c.clear();
//			c.set(Calendar.YEAR, year);
//			c.set(Calendar.MONTH, month-1);
//			c.set(Calendar.DAY_OF_MONTH, day);
//			long startTime = c.getTimeInMillis();
//			long endTime = startTime + 24 * 3600 * 1000;
//
//			ScheduleApi.getInstance().getScheduleList(this, userName, startTime, endTime,null);
//		if (isEnd) {
//			mEndCal.set(year, month - 1, day, hour, minute);
//			// 最近
//			Calendar setTime = Calendar.getInstance();
//			setTime.clear();
//			setTime.set(year, month - 1, day, hour, minute);
//			mNow = setTime.getTimeInMillis();
//			mDialog.setNowTime(mNow);
////			MeetingApi.getInstance().getNoFreeMeetingRoom(this, mBeginCal.getTimeInMillis(), mEndCal.getTimeInMillis());
//
//		}else{
//			mBeginCal.set(year, month - 1, day, hour, minute);
//			Calendar setTime = Calendar.getInstance();
//			setTime.clear();
//			setTime.set(year, month - 1, day, hour, minute);
//			mNow = setTime.getTimeInMillis();
//			mDialog.setNowTime(mNow);
//
//		}
//
//		mOldYear = year;
//		mOldMonth = month;
//		mOldDay = day;
//
//		int count = mDialog.getConflictCount();
//		mDialog.setConflictText(count);
//	}

	/**  添加必须参加人操作*/
	@OnClick(R.id.rel_have_to)
	protected void selectHaveToPerson(){
		accessUser(REQUIRE_TYPE);
	}
	/**  添加选择参加人操作*/
	@OnClick(R.id.rel_choose)
	protected void selectChosePerson(){
		accessUser(JOIN_TYPE);
	}
	/** 重复操作*/
	@OnClick(R.id.rel_repeat)
	protected void repeatChose(){
		Intent intent=new Intent(this,RestartActivity.class);
		startActivityForResult(intent, RESTART_CODE);
	}
	/** 提醒操作*/
	@OnClick(R.id.rel_notice)
	protected void noticeChose(){
		Intent intent=new Intent(this,RemindActivity.class);
		intent.putExtra(RemindActivity.REMIND_INFO,mTvNoticeName.getText());
		startActivityForResult(intent, REMIND_CODE);
	}
	@OnClick(R.id.tv_back)
	protected void back(){
		onBackPressed();
	}
	@OnClick(R.id.tv_save)
	protected void saveMeeting(){
		if (isTrue()) {
			if (mEntity != null) {
				editMeeting();
			}else{
				save();
			}
		}
	}




	/*** 清除焦点*/
	private void clearFocus(){
		if (mEtTitle.hasFocus()) {
			mEtTitle.clearFocus();
			hideSoftInputWindow(mEtTitle);
		}else
		
		if (mEtRemark.hasFocus()) {
			mEtRemark.clearFocus();
			hideSoftInputWindow(mEtRemark);
		}
	}
	
	/*** 隐藏软键盘*/
	private void hideSoftInputWindow(View v){
		 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	/** 保存会议内容*/
	private void save(){
		String title = mEtTitle.getText().toString().trim();
		// 内容
		String content = mEtRemark.getText().toString().trim();
		// 必须参加会议的
		StringBuilder requireBuilder = new StringBuilder();
		StringBuilder requireName = new StringBuilder();
		if (!mRequireList.isEmpty()) {
				for (int i = 0; i < mRequireList.size(); i++) {
					UserDetailEntity entity = mRequireList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					requireBuilder.append(str).append("●");
					requireName.append(name).append("●");
				}
				requireBuilder.deleteCharAt(requireBuilder.length() - 1);
				requireName.deleteCharAt(requireName.length() - 1);
		}else{
			if(mEntity != null){
				List<RequireEntity> reList = mEntity.getREQUIRE_JOIN();
				List<JoinEntity> reNameList = mEntity.getREQUIRE_JOIN_NAME();
				if (reNameList != null) {
					for (int i = 0; i < reNameList.size(); i++) {
						RequireEntity re = reList.get(i);
						JoinEntity entity = reNameList.get(i);
						String str = entity.getName();

						requireBuilder.append(re.getNAME()).append("●");
						requireName.append(str).append("●");
					}
					requireBuilder.deleteCharAt(requireBuilder.length() - 1);
					requireName.deleteCharAt(requireName.length() - 1);
				}
			}
		}
		
		
		
		//非必须参加的
		StringBuilder joinBuild = new StringBuilder();
		StringBuilder joinNameBuild = new StringBuilder();
		
			if (!mJoinList.isEmpty()) {
				for (int i = 0; i < mJoinList.size(); i++) {
					UserDetailEntity entity = mJoinList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					joinBuild.append(str).append("●");
					joinNameBuild.append(name).append("●");
				}
				joinBuild.deleteCharAt(joinBuild.length() - 1);
				joinNameBuild.deleteCharAt(joinNameBuild.length() - 1);
		}else{
				if(mEntity != null){
					List<RequireEntity> joList = mEntity.getJOIN();
					List<JoinEntity> joinNameList = mEntity.getJOIN_NAME();
					if (joinNameList != null) {
						for (int i = 0; i < joinNameList.size(); i++) {
							JoinEntity entity = joinNameList.get(i);
							String str = entity.getName();
							RequireEntity re = joList.get(i);

							joinBuild.append(re.getNAME()).append("●");
							joinNameBuild.append(str).append("●");
						}
						joinBuild.deleteCharAt(joinBuild.length() - 1);
						joinNameBuild.deleteCharAt(joinNameBuild.length() - 1);
					}
				}
		}
		boolean visible = mCbSee.isChecked();
		int  mIntVisible = 0;
		if(visible){
			mIntVisible = 1;
		}
		showLoading();
		
		if (mBoolMeetingAddress) {
			MeetingApi.getInstance().addNewMeeting(this, title, content,mLongStart,mLongEnd, mRoomEntity.getSHOW_ID(), "", "", "", requireBuilder.toString(), requireName.toString(), joinBuild.toString(), joinNameBuild.toString(), mRestartType + "", mReminType+ "", endTime,mIntVisible);
		}else{
			MeetingApi.getInstance().addNewMeeting(this, title, content,mLongStart,mLongEnd, "", strPlace, mLongitude, mLatitude, requireBuilder.toString(), requireName.toString(), joinBuild.toString(), joinNameBuild.toString(), mRestartType + "", mReminType + "", endTime, mIntVisible);
		}
		
		
	}
	
	private boolean isTrue(){
		if (mRoomEntity == null && strPlace.equals("")) {
			String strRoom = getResources().getString(R.string.room_empty);
			toast(strRoom);
			return false;
		}
		String title = mEtTitle.getText().toString().trim();
		
		if (title.equals("")) {
			String strTitle = getResources().getString(R.string.title_empty);
			toast(strTitle);
			return false;
		}
		if (mRequireStrList.isEmpty() && mRequireList.isEmpty() ) {
			String strTitle = getResources().getString(R.string.add_meeting_member_empty);
			toast(strTitle);
			return false;
		}
		
		return true;
	}
	
	
	MeetingTimerDialog mDialog;

	/**
	 * 显示时间操作
	 */
	@OnClick(R.id.rel_time)
	protected void showTime(){
		

		List<String> userName = new ArrayList<>();

		// 必须参加会议的
//		for (int i = 0; i < mRequireList.size(); i++) {
//			UserDetailEntity entity = mRequireList.get(i);
//			String str = entity.getName();
//			String name = entity.getTrueName();
//			userName.add(str);
//		}
//
		for(UserDetailEntity entity:mRequireList){
			userName.add(entity.getName());
		}

		long startTime;
		long endTime;
		if(mTodayTime != 0){
			 startTime = mTodayTime;
			 endTime = startTime + 24 * 3600 * 1000;
			mBeginCal.setTimeInMillis(mTodayTime);
			mEndCal.setTimeInMillis(endTime);
		}else{
			Calendar today = Calendar.getInstance();
			startTime = today.getTimeInMillis();
			endTime = startTime + 24 * 3600 * 1000;
		}
		mDialog = new MeetingTimerDialog(this, R.style.my_dialog);
		mDialog.show();
		mDialog.setTimeAndPerson(mLongStart,mLongEnd,userName,mRoomEntity,mTvChooseRoom.getText().toString(),mBoolMeetingAddress,mRequireList);
	}

	public static class IntentData implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8247010101553887266L;
		public List<EventEntity> list;
	}


	private List<EventEntity> mEventDatas = new ArrayList<>();

	/* 返回restart和remind*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=RESULT_OK){
			return;
		}
		if(requestCode == RESTART_CODE){
			Bundle bundle=data.getExtras();
			String result=bundle.getString(RestartActivity.RESTART_INFO);
			mTvRepeatName.setText(result+"");
			mRestartType = bundle.getInt(RestartActivity.REPEATTYPE, 0);
		}else if(requestCode == REMIND_CODE){
			Bundle bundle=data.getExtras();
			String result=bundle.getString(RemindActivity.REMIND_INFO);
			mReminType = bundle.getInt(RemindActivity.REMINDTYPE, 0);
			mTvNoticeName.setText(result+"");
		}else if (requestCode == REQ_ADD_PLACE) {
			// 添加外部会议场所返回处理
			String room = data.getStringExtra(Const.STR_PLACE);
			mLatitude = data.getStringExtra(Const.LATITUDE);
			mLongitude = data.getStringExtra(Const.LONGITUDE);
			if(!TextUtils.isEmpty(room)){
//				mTvRoom.setText(room);
				strPlace = room;
				mDialog.setOutAddress(room);
			}
			setMeetingAddress();

		}else if (requestCode == REQ_MEETING_DETAIL) {
			// 会议详情返回
			finish();
		}
	}


	/**
	 * 选取联系人操作
	 * @param type
	 */
	private void accessUser(int type){
		mType = type;
		mFragManger.beginTransaction().show(mDeptFragment).commit();
		mDeptFragment.startReciver(this);
		if (type == REQUIRE_TYPE) {
			if(mRequireIdList != null && mRequireIdList.size() >0){
				mDeptFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE,mRequireIdList);
			}else{
				mDeptFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, mRequireList);//(DeptFragment.SELECT_STATE, mRequirePositionList);
			}
		}else if (type == JOIN_TYPE) {
			if(mJoinIdList != null && mJoinIdList.size() >0){
				mDeptFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE,mJoinIdList);
			}else{
				mDeptFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, mJoinList);//(DeptFragment.SELECT_STATE, mRequirePositionList);
			}
		}
	}
	
	/**
	 * 显示必须要参加的人操作
	 * @param userList
	 */
	private void selectRequireUser(List<UserDetailEntity> userList){
		if (userList != null) {
			mRequireList.clear();
			mRequireList.addAll(userList);
			// 显示操作
			setText(mTvHaveToName, mRequireList);
		}
		
	}
	
	/**
	 * 显示要参加的人操作
	 * @param userList
	 */
	private void selectJoinUser(List<UserDetailEntity> userList){
		if (userList != null) {
			mJoinList.clear();
			mJoinList.addAll(userList);
			
//			mJoinPositionList.clear();
//			mJoinPositionList.addAll(positionList);
			// 显示要参加的人的姓名
			setText(mTvChooseName, mJoinList);
		}
		
	}
	
	/**
	 * 设置显示要参加的人
	 * @param textView
	 * @param userList
	 */
	private void setText(CollapsibleTextView textView,List<UserDetailEntity> userList){
		List<String> nameList = new ArrayList<>();
		if(userList!= null && !userList.isEmpty()){
			String name = userList.get(0).getTrueName();
			nameList.add(name);
			for (int i = 1; i < userList.size(); i++) {
				UserDetailEntity en = userList.get(i);
				name  = name + "、" + en.getTrueName();
				nameList.add(en.getTrueName());
			}
			textView.setText(nameList);
		}
	}
	
	
	@Override
	public void onBackPressed() {
		boolean hidden = mDeptFragment.isHidden();
		if (hidden) {
			finish();
		}else{
			mFragManger.beginTransaction().hide(mDeptFragment).commitAllowingStateLoss();
		}
	}

	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if (response == null) {
			showNoNetWork();
			dismissLoading();
			return;
		}
		
		// 判断是否是会议室列表返回
		if (taskId.equals(TaskId.TASK_URL_GET_MEETINGROOM_LIST)) {
			handleGetRoomListResult(response);
		}else
		
		// 会议新建返回
		if (taskId.equals(TaskId.TASK_URL_NEW_MEETING)) {
			
			handleAddNewMeetingResult(response);
			dismissLoading();
		}else
		
		if (taskId.equals(URL.GET_LIST)) {
			SchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(
					response.toString(), SchedulePOJO.class);
			if (pojo != null && pojo.isResultSuccess()) {
				List<EventEntity> data = pojo.getBody().getResponse().getData();
				mEventDatas.clear();
				for(EventEntity entity: data){
					if(! entity.getType().equals("company_festival") && ! entity.getType().equals("statutory_festival") ){
						mEventDatas.add(entity);
					}
				}
				if(mDialog != null){
					mDialog.setEventDatas(mEventDatas);
				}
			}
		} else
			
		// 判断是否是删除会议
		if (taskId.equals(TaskId.TASK_URL_DEL_MEETING_OR_SCHEDULE)) {
			handleDeleteMeeting(response);
			dismissLoading();
		}else
		// 判断是否是非空会议室返回
		if (taskId.equals(TaskId.TASK_URL_GET_NOFREE_MEETINGROOM)) {
			handleMeetingUnFreeRoomResult(response);
			dismissLoading();
		}else if(taskId.equals(TaskId.TASK_URL_EDIT_MEETING)){
			handleAddNewMeetingResult(response);
			dismissLoading();
		}
		
		
	}
	
	/**
	 * 处理非空会议室
	 * @param response
	 */
	private void handleMeetingUnFreeRoomResult(Object response){
		MeetingUnFreePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingUnFreePOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<MeetingUnFreeRoomEntity> list = pojo.getBody().getResponse().getData();
				selectRoom(list);
			}
		}
		else{
			showNetWorkGetDataException();
		}
	}
	
	/**
	 * 处理删除会议返回逻辑
	 * @param response
	 */
	private void handleDeleteMeeting(Object response){
		MeetingDeletePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingDeletePOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 删除完成操作
				MeetingDeleteEntity entity = pojo.getBody().getResponse().getData();
				if (entity != null) {
					endTime = entity.getEndTime();
				}
				save();
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}
	}
	
	/**
	 * 
	 */
	private void handleAddNewMeetingResult(Object response){
		MeetingPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingPOJO.class);
		
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 新建完成操作
//				if (mEntity == null) {
					MeetingEntity entity = pojo.getBody().getResponse().getData();
					if (entity != null) {
						Intent intent = new Intent(this, MeetingDetailActivity.class);
						intent.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, entity.getSHOW_ID());
						intent.putExtra(MeetingDetailActivity.KEY_FACT_START_TIME, entity.getM_START());
//						startActivityForResult(intent, REQ_MEETING_DETAIL);
						startActivity(intent);
						this.finish();
					}else{
						toast(pojo.getBody().getResponse().getReason());
					}
//				}
				
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	/**
	 * 判断是否是获取会议室列表返回
	 * @param response
	 */
	private void handleGetRoomListResult(Object response){
		RoomListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), RoomListPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 获取会议室列表操作
				mRoomList = pojo.getBody().getResponse().getData();
				
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	
	
	private void initArrays(){
		mRepeatArray = new String[]{ getResources().getString(R.string.calendar_never),
									 getResources().getString(R.string.calendar_everyday),
									 getResources().getString(R.string.calendar_everyweek),
									 getResources().getString(R.string.calendar_eveymonth),
									 getResources().getString(R.string.calendar_everyyear)};
//
		mRemintArray = new String[]{

									getResources().getString(R.string.calendar_happen),
									getResources().getString(R.string.calendar_before_5),
									getResources().getString(R.string.calendar_before_15),
									getResources().getString(R.string.calendar_before_30),
									getResources().getString(R.string.calendar_before_one_hour),
									getResources().getString(R.string.calendar_before_two_hour),
									getResources().getString(R.string.calendar_before_one_day),
									getResources().getString(R.string.calendar_before_two_day),
									getResources().getString(R.string.calendar_before_one_week),
									
									getResources().getString(R.string.calendar_the_day),
									getResources().getString(R.string.calendar_before_one_day),
									getResources().getString(R.string.calendar_before_two_week),
									getResources().getString(R.string.calendar_before_one_week) 
									};
	}
	
	private String getReminType(int index){
		String remin = getResources().getString(R.string.calendar_not_remind);
		for (int i = 0; i < mStatus.length; i++) {
			int status = mStatus[i];
			if (status == index) {
				remin = mRemintArray[i];
				break;
			}
		}
		return remin;
		
	}
	
	
	/**
	 * 保留编辑后的新会议 老接口 目前抛弃不用了 16.3.30
	 */
	private void editRepectMeeting(){
		 if (mEntity != null) {
			showLoading();
			MeetingApi.getInstance().delMeetingOrSchedule(this, mEntity.getSHOW_ID(), mEntity.getM_START(), 1);
		}
	}
	/** 编辑会议*/
	private void editMeeting() {
		if(mEntity != null){
			String showId = mEntity.getSHOW_ID();
			String title = mEtTitle.getText().toString().trim();
			// 内容
			String content = mEtRemark.getText().toString().trim();
			// 必须参加会议的
			List<String> requst = new ArrayList<>();
			List<String> requstName = new ArrayList<>();
			StringBuilder requireBuilder = new StringBuilder();
			StringBuilder requireName = new StringBuilder();
			if (!mRequireList.isEmpty()) {
				for (int i = 0; i < mRequireList.size(); i++) {
					UserDetailEntity entity = mRequireList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					requst.add(str);
					requstName.add(name);
					requireBuilder.append(str).append("●");
					requireName.append(name).append("●");
				}
				requireBuilder.deleteCharAt(requireBuilder.length() - 1);
				requireName.deleteCharAt(requireName.length() - 1);
			}else{
				if(mEntity != null){
					List<RequireEntity> reList = mEntity.getREQUIRE_JOIN();
					List<JoinEntity> reNameList = mEntity.getREQUIRE_JOIN_NAME();
					if (reNameList != null) {
						for (int i = 0; i < reNameList.size(); i++) {
							RequireEntity re = reList.get(i);
							JoinEntity entity = reNameList.get(i);
							String str = entity.getName();
							requst.add(re.getNAME());
							requstName.add(str);
							requireBuilder.append(re.getNAME()).append("●");
							requireName.append(str).append("●");
						}
						requireBuilder.deleteCharAt(requireBuilder.length() - 1);
						requireName.deleteCharAt(requireName.length() - 1);
					}
				}
			}

			//非必须参加的
			StringBuilder joinBuild = new StringBuilder();
			StringBuilder joinNameBuild = new StringBuilder();
			List<String> select = new ArrayList<>();
			List<String> selectName = new ArrayList<>();
			if (!mJoinList.isEmpty()) {
				for (int i = 0; i < mJoinList.size(); i++) {
					UserDetailEntity entity = mJoinList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					select.add(str);
					selectName.add(name);
					joinBuild.append(str).append("●");
					joinNameBuild.append(name).append("●");
				}
				joinBuild.deleteCharAt(joinBuild.length() - 1);
				joinNameBuild.deleteCharAt(joinNameBuild.length() - 1);
			}else{
				if(mEntity != null){
					List<RequireEntity> joList = mEntity.getJOIN();
					List<JoinEntity> joinNameList = mEntity.getJOIN_NAME();
					if (joinNameList != null) {
						for (int i = 0; i < joinNameList.size(); i++) {
							JoinEntity entity = joinNameList.get(i);
							String str = entity.getName();
							RequireEntity re = joList.get(i);
							select.add(re.getNAME());
							selectName.add(str);
							joinBuild.append(re.getNAME()).append("●");
							joinNameBuild.append(str).append("●");
						}
						joinBuild.deleteCharAt(joinBuild.length() - 1);
						joinNameBuild.deleteCharAt(joinNameBuild.length() - 1);
					}
				}
			}
			int visible = mCbSee.isChecked() == true?1:0;
			showLoading();
			if (mBoolMeetingAddress) {
				MeetingApi.getInstance().editMeeting(this,showId, title, content,mLongStart,mLongEnd, mRoomEntity.getSHOW_ID(), "", "", "",requireBuilder.toString(), requireName.toString(), joinBuild.toString(), joinNameBuild.toString(), mRestartType + "", mReminType + "",visible);
			}else{
				MeetingApi.getInstance().editMeeting(this, showId, title, content,mLongStart, mLongEnd, "", strPlace, mLongitude, mLatitude,requireBuilder.toString(), requireName.toString(), joinBuild.toString(), joinNameBuild.toString(), mRestartType + "", mReminType + "",visible);
			}

		}
	}

	public void setTimeAndAddress(long beginTime,long endTime,RoomEntity roomEntity ,String address){
		mLongStart = beginTime;
		mLongEnd = endTime;
		mTvChooseTime.setText(TimeFormatUtil.getStartEndTimeFormat(mLongStart,mLongEnd));
		mRoomEntity = roomEntity;
		strPlace = address;
		if(mRoomEntity != null){
			mBoolMeetingAddress = true;
			mTvChooseRoom.setText(mRoomEntity.getR_NAME());
		}else{
			mBoolMeetingAddress = false;
			mTvChooseRoom.setText(strPlace);
		}

	}
	private void selectRoom(List<MeetingUnFreeRoomEntity> list){
		mShowRoomList.clear();
		if (list != null && !list.isEmpty()) {
			for (int j = 0; j < mRoomList.size(); j++) {
				RoomEntity room = mRoomList.get(j);
				boolean b = false;
				for (int i = 0; i < list.size(); i++) {
					MeetingUnFreeRoomEntity entity = list.get(i);
					if (room.getSHOW_ID().equals(entity.getMeetingRoomNo())) {
						b = true;
						break;
					}		
				}
				if (!b) {
					mShowRoomList.add(room);
				}
			}	
			
		}else{
			mShowRoomList.addAll(mRoomList);
		}
		
	}

	@Override
	public void onSelectContact(List<UserDetailEntity> userList) {
		if (mType == REQUIRE_TYPE) {
			mRequireStrList.clear();
			mRequireIdList.clear();
			selectRequireUser(userList);
		}else if (mType == JOIN_TYPE) {
			mJoinStrList.clear();
			mJoinIdList.clear();
			selectJoinUser(userList);
		}
		mType = 0;
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mLlMap.onResumeMap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mLlMap.onDestroyMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mLlMap.onPauseMap();
	}

	/** 显示地图*/
	private void setMeetingAddress(){
		if(mLatitude==null || mLongitude==null || mLatitude.equals("") || mLongitude.equals("")){
			mLlMap.setVisibility(View.GONE);
			return;
		}
		mLlMap.setVisibility(View.VISIBLE);
		double laty = Double.parseDouble(mLatitude);
		double lngx = Double.parseDouble(mLongitude);
		mLlMap.getMap(lngx,laty);
	}

}

















