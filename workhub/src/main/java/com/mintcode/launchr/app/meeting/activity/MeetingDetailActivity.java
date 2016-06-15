package com.mintcode.launchr.app.meeting.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.MeetingApi;
import com.mintcode.launchr.api.MeetingApi.TaskId;
import com.mintcode.launchr.app.CommentsListView;
import com.mintcode.launchr.app.CommentsView;
import com.mintcode.launchr.app.MapLoadView;
import com.mintcode.launchr.app.meeting.view.HandleMeetingPopWindow;
import com.mintcode.launchr.app.newApproval.window.WriteCommentPopWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.MeetingDetailPOJO;
import com.mintcode.launchr.pojo.MeetingPOJO;
import com.mintcode.launchr.pojo.entity.JoinEntity;
import com.mintcode.launchr.pojo.entity.MeetingDetailEntity;
import com.mintcode.launchr.pojo.entity.RequireEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.widget.CollapsibleTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MeetingDetailActivity extends BaseActivity implements OnClickListener,CommentsView.SendMessageListener {


	private MeetingDetailEntity mEntity;
	/** 会议key */
	public static final String KEY_RELATIVE_ID = "key_relative_id";
	
	/** 会议起始时间 key */
	public static final String KEY_FACT_START_TIME = "key_fact_start_time";
	
	/** 提醒方式状态 */
	private int[] mStatus = {100, 101, 102, 103, 104, 105, 106, 107, 108,200, 201, 202, 203 };
	
	/** 提醒数组 */
	private String[] mRepeatArray;
	
	/** 提醒数组 */
	private String[] mRemintArray;

	private String mRelativeId = "";

	private List<String> mMessagePerson = new ArrayList();
	private List<String> mMessagePersonName = new ArrayList<>();

	private static final String MEETING_APP_KEY = Const.SHOW_SCHEDULE;
	
	/*** 会议标题*/
	@Bind(R.id.tv_meeting_detail_title)
	protected TextView mMeetingTitile;
	/*** 会议时间*/
	@Bind(R.id.tv_meeting_detail_time)
	protected TextView mMeetingTime;
	/*** 会议室*/
	@Bind(R.id.tv_meeting_detail_palce)
	protected TextView mMeetingRoom;
	/** 放置地图布局*/
	@Bind(R.id.ll_map)
	protected MapLoadView mLlMap;
	/*** 必须参加的人员*/
	@Bind(R.id.tv_meeting_have_to_attend_name)
	protected CollapsibleTextView mTvAttendPeople;
	/*** 选择参加的人员*/
	@Bind(R.id.tv_meeting_chose_to_attend_name)
	protected CollapsibleTextView mTvChosePeople;
	/*** 重复*/
	@Bind(R.id.tv_meeting_detail_repeat)
	protected TextView mTvRepeat;
	/*** 通知*/
	@Bind(R.id.tv_meeting_detail_notifaction)
	protected TextView mTvNotifaction;
	@Bind(R.id.tv_meeting_detail_mark)
	protected TextView mTvMark;

	//布局    为空隐藏
	/***选择参加布局**/
	@Bind(R.id.ll_meeting_chose_to_attend_name)
	protected LinearLayout mLlToAttend;
	/***重复布局**/
	@Bind(R.id.ll_meeting_detail_repeat)
	protected LinearLayout mLlRepeact;
	/***提醒布局**/
	@Bind(R.id.ll_meeting_detail_notifaction)
	protected LinearLayout mLlNofication;
	/***备注布局**/
	@Bind(R.id.ll_meeting_detail_remark)
	protected LinearLayout mLlRemark;
	@Bind(R.id.layout_comments)
	protected CommentsView mCommentsView;
	@Bind(R.id.layout_comments_list)
	protected CommentsListView mCommentsListView;
	
	/** 更多操作*/
	@Bind(R.id.iv_more_handle)
	protected ImageView mIvMoreHandle;
	private HandleMeetingPopWindow mHandleMeetingPopWindow;

	/** 参加、不参加*/
	@Bind(R.id.lin_enter_meeting)
	protected LinearLayout mLinEnter;
	private WriteCommentPopWindow mWriteCommentPopWindow;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_detail);
		ButterKnife.bind(this);
		initArrays();
		initView();
	}
	private void initView(){
		 //设置监听
		 mCommentsView.setMessageLisener(this);

		mRelativeId = getIntent().getStringExtra(KEY_RELATIVE_ID);
		long time = getIntent().getLongExtra(KEY_FACT_START_TIME, -1);
		if (!mRelativeId.equals("")) {
			showLoading();
			MeetingApi.getInstance().getMeeting(this, mRelativeId,time);
		}
	}
	

	/**
	 * 实例化数据
	 */
	private void initData(){
		if (mEntity != null) {
			// 会议标题
			mMeetingTitile.setText(mEntity.getM_TITLE());
			//是否是私人的
			int visible = mEntity.getM_IS_VISIBLE();
			if(visible == 1){
				findViewById(R.id.iv_icon_private).setVisibility(View.GONE);
			}else{
				findViewById(R.id.iv_icon_private).setVisibility(View.VISIBLE);
			}
			// 会议时间
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.setTimeInMillis(mEntity.getM_START());
			
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTimeInMillis(mEntity.getM_END());
			
			int y = beginCalendar.get(Calendar.YEAR);
			int m = beginCalendar.get(Calendar.MONTH) + 1;
			int d = beginCalendar.get(Calendar.DAY_OF_MONTH);
			int h = beginCalendar.get(Calendar.HOUR_OF_DAY);
			int mm = beginCalendar.get(Calendar.MINUTE);

			int yE = endCalendar.get(Calendar.YEAR);
			int mE = endCalendar.get(Calendar.MONTH) + 1;
			int dE = endCalendar.get(Calendar.DAY_OF_MONTH);
			int hE = endCalendar.get(Calendar.HOUR_OF_DAY);
			int mmE = endCalendar.get(Calendar.MINUTE);
			SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd HH:mm");
			SimpleDateFormat oneDayFormat = new SimpleDateFormat("HH:mm");
			if (yE == y && d == dE && m == mE) {//同一天
				mMeetingTime.setText(dayFormat.format(mEntity.getM_START()) + "~"+  oneDayFormat.format(mEntity.getM_END()));
			}else{// 非同一天

				mMeetingTime.setText(dayFormat.format(mEntity.getM_START()) + "~"+  dayFormat.format(mEntity.getM_END()));
			}

			// 设置会议室
			String room = mEntity.getR_SHOW_NAME();
			if (room != null && !room.equals("")) {
				mMeetingRoom.setText(mEntity.getR_SHOW_NAME());
				setMeetingAddress();
			}else{
				mMeetingRoom.setText(mEntity.getM_EXTERNAL());
				setMeetingAddress();
			}
			
			// 设置必须参加
			List<JoinEntity> list = mEntity.getREQUIRE_JOIN_NAME();
			List<String> name = new ArrayList<>();
			String requireName = "";
			if ((list != null) && !list.isEmpty()) {
				requireName += list.get(0).getName();
				name.add(requireName);
				mMessagePersonName.add(requireName);
				for (int i = 1; i < list.size(); i++) {
						JoinEntity entity = list.get(i);
					String personName = entity.getName();
					requireName +=  "、" + personName;
					name.add(personName);
					mMessagePersonName.add(personName);
				}
				mTvAttendPeople.setText(requireName,name);
			}else{
				mTvAttendPeople.setText(requireName,name);
			}
			List<RequireEntity> listId = mEntity.getREQUIRE_JOIN();
			for(RequireEntity entity : listId){
				mMessagePerson.add(entity.getNAME());
			}
			// 设置选择参加
			List<JoinEntity> joinList = mEntity.getJOIN_NAME();
			List<String> joinName = new ArrayList<>();
			String selectJoinName = "";
			if ((joinList != null) && !joinList.isEmpty()) {

				selectJoinName += joinList.get(0).getName();
				joinName.add(selectJoinName);
				for (int i = 1; i < joinList.size(); i++) {
					JoinEntity entity = joinList.get(i);
					String persoName = entity.getName();
					selectJoinName += "、" + persoName;
					joinName.add(persoName);
				}
				mTvChosePeople.setText(selectJoinName,joinName);
				mLlToAttend.setVisibility(View.VISIBLE);
			}else{
				mLlToAttend.setVisibility(View.GONE);
				mTvChosePeople.setText(selectJoinName,joinName);
			}
			// 设置重复方式
			String repeat = mRepeatArray[mEntity.getM_RESTART_TYPE()];
			if(!"".equals(repeat)){
				mTvRepeat.setText(repeat);
				mLlRepeact.setVisibility(View.GONE);
			}else{
				mLlRepeact.setVisibility(View.GONE);
			}
			// 设置提醒方式
			String notice = getReminType(mEntity.getM_REMIND_TYPE());
			if(!"".equals(notice)){
				mTvNotifaction.setText(notice);
				mLlNofication.setVisibility(View.VISIBLE);
			}else{
				mLlNofication.setVisibility(View.GONE);
			}
			
			//备注
			String remark = mEntity.getM_CONTENT();
			if(!"".equals(remark)){
				mTvMark.setText(remark);
				mLlRemark.setVisibility(View.VISIBLE);
			}else{
				mLlRemark.setVisibility(View.GONE);
			}
			// 是否选择过参加会议
			String userName = AppUtil.getInstance(MeetingDetailActivity.this).getLoginName();
			for(int i=0; i<mEntity.getREQUIRE_JOIN().size(); i++){
				if(userName!=null && userName.equals(mEntity.getREQUIRE_JOIN().get(i).getNAME())){
					if(mEntity.getREQUIRE_JOIN().get(i).getISJOIN() == 2){
						mLinEnter.setVisibility(View.VISIBLE);
					}
				}
			}
			// 是否是会议的创建者
			String userId = AppUtil.getInstance(MeetingDetailActivity.this).getLoginName();
			String createName = mEntity.getCREATE_USER();
			if(userId.equals(createName) && mEntity.getM_IS_CANCEL()!=1){
				mIvMoreHandle.setVisibility(View.VISIBLE);
			}else{
				mIvMoreHandle.setVisibility(View.GONE);
			}
		}
		mCommentsView.setScheduleAppInfo(MEETING_APP_KEY,mEntity.getSHOW_ID(),mEntity.getM_TITLE(),1,mMessagePerson,mMessagePersonName,CommentsView.MEETING);
		mCommentsListView.setAppInfo(MEETING_APP_KEY,mEntity.getSHOW_ID());
		mCommentsListView.updateComment();
	}



	@OnClick(R.id.ibn_meeting_datail_back)
	protected void back(){
		this.finish();
	}
	/** 选择参加*/
	@OnClick(R.id.tv_meeting_attend)
	protected void attendMeeting(){
		confirmMeeting(1);
	}
	/** 选择不参加*/
	@OnClick(R.id.tv_meeting_no_attend)
	protected void unAttendMeeting(){
		mWriteCommentPopWindow = new WriteCommentPopWindow(MeetingDetailActivity.this, WriteCommentPopWindow.TYPE_MEETING_REFUSE,
				mEntity.getSHOW_ID(), 0, -1);
		mWriteCommentPopWindow.showAtLocation(mCommentsView, Gravity.CENTER, 0, 0);
	}
	@OnClick(R.id.iv_more_handle)
	protected void doMoreHandle(){
		mHandleMeetingPopWindow = new HandleMeetingPopWindow(MeetingDetailActivity.this);
		mHandleMeetingPopWindow.showAsDropDown(mIvMoreHandle);

		View window = mHandleMeetingPopWindow.getContentView();
		TextView handleEdit = (TextView)window.findViewById(R.id.tv_meeting_edit);
		handleEdit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if (mEntity != null) {
					Intent intent = new Intent(MeetingDetailActivity.this, NewMeetingActivity.class);
					intent.putExtra(NewMeetingActivity.KEY_EDITOR_ENTITY, mEntity);
					startActivity(intent);
					finish();
				}
				mHandleMeetingPopWindow.dismiss();
			}
		});
		TextView handleDelete = (TextView)window.findViewById(R.id.tv_meeting_delete);
		handleDelete.setText(getResources().getString(R.string.cancel));
		handleDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showCancelWindow();
				mHandleMeetingPopWindow.dismiss();
			}
		});

		TextView handleSendOther = (TextView) window.findViewById(R.id.tv_meeting_send);
		handleSendOther.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHandleMeetingPopWindow.dismiss();
			}
		});
	}
	/** 取消会议窗口*/
	private void showCancelWindow() {
		String showId = mEntity.getSHOW_ID();
		mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_MEETING_CANCEL,showId,0,-1);
		mWriteCommentPopWindow.show(mIvMoreHandle);
		TextView refuseTitle = (TextView) mWriteCommentPopWindow.getContentView().findViewById(R.id.tv_approve_comment);
		refuseTitle.setText(this.getString(R.string.cancel_meeting));

	}


	private String getReminType(int index){
		String remin = "";
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
	 * 删除会议函数
	 */
	private void deleteMeeting(int key){
		 if (mEntity != null) {
			showLoading();
			MeetingApi.getInstance().delMeetingOrSchedule(this, mEntity.getSHOW_ID(), mEntity.getM_START(), key);
		}
	}
	
	private void confirmMeeting(int type){
		if (mEntity != null) {
			showLoading();
			MeetingApi.getInstance().confirmMeeting(this, mEntity.getSHOW_ID(), type, "No ");
		}
	}
	
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		// 判断是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}
		
		// 判断是否是删除会议
		if (taskId.equals(TaskId.TASK_URL_DEL_MEETING_OR_SCHEDULE)) {
			handleDeleteMeeting(response);
			dismissLoading();
		}else
			
		// 判断是否是参加会议返回	
		if (taskId.equals(TaskId.TASK_URL_SRUE_MEETING)) {
			handleConfirmResult(response);
			dismissLoading();
		}else
		
		// 判断是否是会议详情返回
		if (taskId.equals(TaskId.TASK_URL_GET_MEETING)) {
			handleMeetResult(response);
			dismissLoading();
		}
	}
	

	/**
	 * 处理详情返回
	 * @param response
	 */
	private void handleMeetResult(Object response){
		MeetingDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingDetailPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 获取会议详情操作
				mEntity = pojo.getBody().getResponse().getData();
				initData();
			}else{
				toast(pojo.getHeader().getReason());
				this.finish();
			}
			
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	
	/**
	 * 处理的
	 * @param response
	 */
	private void handleConfirmResult(Object response){
		MeetingPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 确认完成操作
				setResult(RESULT_OK);
				finish();
				
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	
	
	/**
	 * 处理删除会议返回逻辑
	 * @param response
	 */
	private void handleDeleteMeeting(Object response){
		MeetingPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingPOJO.class);
		
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				// 删除完成操作
				setResult(RESULT_OK);
				finish();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK){
			return;
		}
		if(requestCode == CommentsView.TAKE_PICTURE){
			mCommentsView.setImageMessage();
		}
	 if(requestCode == CommentsView.GET_ALBUM){
		if(data == null){
			return;
		}
		List<String> imagePath = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
		 if(imagePath != null){
			 mCommentsView.setAublmImageMessage(imagePath.get(0));
		 }
	  }
	}
	


	/** 显示百度地图*/
	private void setMeetingAddress(){
		if(mEntity.getM_LATY()==null || mEntity.getM_LNGX()==null || mEntity.getM_LATY().equals("") || mEntity.getM_LNGX().equals("")){
			mLlMap.setVisibility(View.GONE);
			return;
		}
		mLlMap.setVisibility(View.VISIBLE);
		double laty = Double.parseDouble(mEntity.getM_LATY());
		double lngx = Double.parseDouble(mEntity.getM_LNGX());
		mLlMap.getMap(laty,lngx);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mLlMap.onResumeMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mLlMap.onPauseMap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
			mLlMap.onDestroyMap();
	}

	@Override
	public void sendMessage(String message) {
		mCommentsListView.updateComment();
	}
}

























