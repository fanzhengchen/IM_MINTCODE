package com.mintcode.launchr.app.newSchedule.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.api.ScheduleApi.URL;
import com.mintcode.launchr.app.CommentsListView;
import com.mintcode.launchr.app.CommentsView;
import com.mintcode.launchr.app.MapLoadView;
import com.mintcode.launchr.app.meeting.view.HandleMeetingPopWindow;
import com.mintcode.launchr.app.newSchedule.fragment.NewWeekFragment;
import com.mintcode.launchr.app.newSchedule.view.NewTimeSelectDialog;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.ScheduleDetailPOJO;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;
import com.mintcode.launchr.pojo.entity.ScheduleEntity.Times;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.DialogHintUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日程事件详情页面
 * 
 * @author JulyYu
 *         d2015-8-7
 */
public class ScheduleEventDetailActivity extends BaseActivity  implements CommentsView.SendMessageListener{




	/* 数据显示 */
	/*** 所选时间1 */
	@Bind(R.id.rg_schedule_event_detail_alternate1)
	protected RadioButton mRbAlternate1;
	/*** 所选时间2*/
	@Bind(R.id.rg_schedule_event_detail_alternate2)
	protected RadioButton mRbAlternate2;
	/*** 所选时间3 */
	@Bind(R.id.rg_schedule_event_detail_alternate3)
	protected RadioButton mRbAlternate3;
	/*** 重要/全天 标记*/
	@Bind(R.id.iv_schedule_event_circle)
	protected ImageView mIvImprotantCircle;
	/*** 重要/全天 textview*/
	@Bind(R.id.tv_schdule_event_text)
	protected TextView mTvImprotantText;
	/*** 事件标题*/
	@Bind(R.id.tv_schedule_event_detail_title)
	protected TextView mTvTitle;

	/*** 候补时间1 显示*/
	@Bind(R.id.tv_schedule_event_detail_time1)
	protected TextView mTvTime1;
	/*** 候补时间2 显示*/
	@Bind(R.id.tv_schedule_event_detail_time2)
	protected TextView mTvTime2;
	/*** 候补时间3 显示*/
	@Bind(R.id.tv_schedule_event_detail_time3)
	protected TextView mTvTime3;

	/* 数据显示 */

	/* 可选控件 */
	/*** 时间1的布局*/
	@Bind(R.id.rl_detail_time1)
	protected RelativeLayout mRlTime1;
	/*** 时间2的布局*/
	@Bind(R.id.rl_detail_time2)
	protected RelativeLayout mRlTime2;
	/*** 时间3的布局*/
	@Bind(R.id.rl_detail_time3)
	protected RelativeLayout mRlTime3;
	/*** 详细地址*/
	@Bind(R.id.tv_schedule_event_detail_placedetail)
	protected TextView mTvPlace;
	/*** 地址省*/
	@Bind(R.id.tv_schedule_event_detail_place)
	protected TextView mTvProvience;
	/** 放置地图布局*/
	@Bind(R.id.ll_map)
	protected MapLoadView mLlMap;
	/** 提醒*/
	@Bind(R.id.tv_schedule_event_detail_remind)
	protected TextView mTvRemind;
	/** 评论输入框*/
	@Bind(R.id.layout_comments)
	protected CommentsView mCommentView;
	/** 评论列表*/
	@Bind(R.id.layout_comments_list)
	protected CommentsListView mCommentListView;
	/** 备注*/
	@Bind(R.id.ll_schedule_detail_remark)
	protected LinearLayout mLinRemark;
	/** 备注*/
	@Bind(R.id.tv_schedule_event_detail_remark)
	protected TextView mTvRemark;
	/** 更多操作*/
	@Bind(R.id.iv_more_handle)
	protected ImageView mIvMoreHandle;
	protected HandleMeetingPopWindow mHandleMeetingPopWindow;


	private ScheduleEntity mEvent;
	/*** 时间*/
	private List<Times> mTimeList;
	private LinkedList<RelativeLayout> mLinkedListDisplay;

	private LinkedList<TextView> mLinkedListGetTime;
	
	public static final String DETAIL_CODE = "ScheduleEventDetailActivity_edit";
	
	public static final int APPROVE_EDIT = 1;
	private boolean mIsAllDay;
	/*** 是否重要*/
	private boolean mIsImprotant;

	/*** 是否全天*/
	/** 当前事件的showid*/
	private String mCurrentShowId;

	/** 百度地图 */
//	private MapView mMapView = null;
	private static final String EVENT_APP_KEY = Const.SHOW_SCHEDULE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_event_detail);
		ButterKnife.bind(this);
		mCurrentShowId = getIntent().getStringExtra("event_showid");
		mCommentView.setMessageLisener(this);
		getEventDetail();
	}

	/** 获取时间详情*/
	private void getEventDetail(){
		showLoading();
		ScheduleApi.getInstance().getScheduleDetail(this, mCurrentShowId, null);
	}

	public void initDate() {
		mLinkedListDisplay = new LinkedList<RelativeLayout>();
		mLinkedListGetTime = new LinkedList<TextView>();

		mLinkedListDisplay.add(mRlTime1);
		mLinkedListDisplay.add(mRlTime2);
		mLinkedListDisplay.add(mRlTime3);
		mLinkedListGetTime.add(mTvTime1);
		mLinkedListGetTime.add(mTvTime2);
		mLinkedListGetTime.add(mTvTime3);
		if(mEvent != null){
			int visible = mEvent.getIsVisible();
			if(visible == 1){
				findViewById(R.id.iv_icon_private).setVisibility(View.GONE);
			}else{
				findViewById(R.id.iv_icon_private).setVisibility(View.VISIBLE);
			}
			mTvTitle.setText(mEvent.getTitle());
			mTimeList = mEvent.getTimes();
			mIsAllDay = mEvent.getIsAllDay() == 1;
			mIsImprotant = mEvent.getIsImportant() == 1;
			
			SimpleDateFormat sf = null;
			sf = new SimpleDateFormat("MM/dd");
			// 设置所显示的时间
			for (int i = 0; i < mLinkedListDisplay.size(); i++) {
				mLinkedListDisplay.get(i).setVisibility(View.GONE);
			}
			for (int i = 0; i < mTimeList.size(); i++) {
				String time = NewTimeSelectDialog.gettime(mTimeList, i, mIsAllDay);
				mLinkedListGetTime.get(i).setText(time);
				mLinkedListDisplay.get(i).setVisibility(View.VISIBLE);
			}
			if(mTimeList.size() == 1){
				mRbAlternate1.setVisibility(View.INVISIBLE);
			}else{
				mRbAlternate1.setVisibility(View.VISIBLE);
			}

			// 设置标记
			if (mIsImprotant) {
				mIvImprotantCircle.setVisibility(View.VISIBLE);
				mTvImprotantText.setVisibility(View.VISIBLE);
				mIvImprotantCircle.setImageResource(R.drawable.icon_task_clock);
				mTvImprotantText.setText(R.string.calendar_improtant);
			} else {
				mIvImprotantCircle.setVisibility(View.INVISIBLE);
				mTvImprotantText.setVisibility(View.INVISIBLE);
			}

			// 备注
			if(mEvent.getContent() == null || ("").equals(mEvent.getContent())){
				mLinRemark.setVisibility(View.GONE);
			}else{
				mLinRemark.setVisibility(View.VISIBLE);
				mTvRemark.setText(mEvent.getContent());
			}

			setMapAddress();
			// 地址
			String addressName = mEvent.getPlace();
			if(addressName != null && !"".equals(addressName)){
				mTvProvience.setText(mEvent.getPlace());
				findViewById(R.id.rl_place).setVisibility(View.VISIBLE);
				mTvPlace.setVisibility(View.GONE);
			}else{
				findViewById(R.id.rl_place).setVisibility(View.GONE);
			}
			// 提醒
			setRemindByType(mEvent.getRemindType());
		}
		
		// 别人的日程，不显示删除按钮		
		if(!ScheduleMainActivity.isMySchedule){
			mIvMoreHandle.setVisibility(View.GONE);
		}
		mCommentListView.setAppInfo(EVENT_APP_KEY, mEvent.getShowId());
		List<String> person = new ArrayList<>();
		List<String> personName = new ArrayList<>();
		person.add(mEvent.getCreateUser());
		personName.add(mEvent.getCreateUserName());
		mCommentView.setScheduleAppInfo(EVENT_APP_KEY,mEvent.getShowId(),mEvent.getTitle(),1,person,personName,CommentsView.EVENT);
		mCommentListView.updateComment();
	}

	/** 拷贝信息*/
	@OnClick(R.id.tv_schdule_copy)
	protected void copyTime() {
		ClipboardManager cmb = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
		String getStringTimes = "";
		if(mRbAlternate1.isChecked()){
			getStringTimes += mTvTime1.getText();
			cmb.setText(getStringTimes);
			toast(getString(R.string.calendar_timelist_copy));
			return;
		}
		if(mRbAlternate2.isChecked()){
			getStringTimes += mTvTime2.getText();
			cmb.setText(getStringTimes);
			toast(getString(R.string.calendar_timelist_copy));
			return;
		}
		if(mRbAlternate3.isChecked()){
			getStringTimes += mTvTime3.getText();
			cmb.setText(getStringTimes);
			toast(getString(R.string.calendar_timelist_copy));
			return;
		}else if(!"".equals(mTvTime1.getText())){
			getStringTimes += mTvTime1.getText();
			cmb.setText(getStringTimes);
			toast(getString(R.string.calendar_timelist_copy));
			return;
		}	
	}

	/**  返回到编辑页面 */
	private void editEvent() {
		if (mEvent != null) {
			Intent edit = new Intent(this,AddScheduleEventActivity.class);
			Bundle getevnet = new Bundle();
			getevnet.putSerializable("event", mEvent);
			edit.putExtras(getevnet);
			edit.putExtra(AddScheduleEventActivity.EDIT_EVENT_KEY, true);
			startActivityForResult(edit, APPROVE_EDIT);
		}else{
			toast(getString(R.string.no_event));
		}
	}
	@OnClick(R.id.iv_schedule_event_detail_back)
	void back(){
		this.finish();
	}
	@OnClick(R.id.rl_detail_time1)
	void selectTime1(){
		if(mRbAlternate1.isChecked() == true){
			mRbAlternate1.setChecked(false);
		}else{
			mRbAlternate1.setChecked(true);
		}
		mRbAlternate2.setChecked(false);
		mRbAlternate3.setChecked(false);
		DialogHintUtil.selectStandbyTime(this,mRbAlternate1);
	}

	@OnClick(R.id.rl_detail_time2)
	void selectTime2(){
		if(mRbAlternate2.isChecked() == true){
			mRbAlternate2.setChecked(false);
		}else{
			mRbAlternate2.setChecked(true);
		}
		mRbAlternate1.setChecked(false);
		mRbAlternate3.setChecked(false);
		DialogHintUtil.selectStandbyTime(this, mRbAlternate2);
	}

	@OnClick(R.id.rl_detail_time3)
	void selectTime3(){
		if(mRbAlternate3.isChecked() == true){
			mRbAlternate3.setChecked(false);
		}else{
			mRbAlternate3.setChecked(true);
		}
		mRbAlternate1.setChecked(false);
		mRbAlternate2.setChecked(false);
		DialogHintUtil.selectStandbyTime(this,mRbAlternate3);
	}
	@OnClick(R.id.iv_more_handle)
	protected void doMoreHandle(){
		mHandleMeetingPopWindow = new HandleMeetingPopWindow(ScheduleEventDetailActivity.this);
		mHandleMeetingPopWindow.showAsDropDown(mIvMoreHandle);

		View window = mHandleMeetingPopWindow.getContentView();
		TextView handleEdit = (TextView)window.findViewById(R.id.tv_meeting_edit);
		handleEdit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				editEvent();
				mHandleMeetingPopWindow.dismiss();
			}
		});
		TextView handleDelete = (TextView)window.findViewById(R.id.tv_meeting_delete);
		handleDelete.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				DialogHintUtil.delectEvent(ScheduleEventDetailActivity.this);
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

	public void delSchedule(int all) {
		showLoading();
		ScheduleApi.getInstance().delSchedule(this, mEvent.getShowId(), null, mEvent.getTimes().get(0).getStart(), all);
	}
	//选择候补时间
	public void selectWaitTime(){
		if (!mRbAlternate1.isChecked() && !mRbAlternate2.isChecked()&& !mRbAlternate3.isChecked()) {
				this.finish();
			} else {
				String showId = null;
				if (mRbAlternate1.isChecked()) {
					showId = mTimeList.get(0).getShowId();
				} else if (mRbAlternate2.isChecked()) {
					showId = mTimeList.get(1).getShowId();
				} else if (mRbAlternate3.isChecked()) {
					showId = mTimeList.get(2).getShowId();
				}
				ScheduleApi.getInstance().sureSchedule(this, showId);
			}
	}
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if (taskId.equals(URL.DELECT_SCHEDULE)) {
			NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
			if (pojo != null && pojo.isResultSuccess()) {
				dismissLoading();
				// 删除事件成功，要刷新
				NewWeekFragment.newEventResult = 1;
				finish();
			}
		} else if (taskId.equals(URL.SURE_SCHEDULE)) {
			NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
			if (pojo != null && pojo.isResultSuccess()) {
				dismissLoading();
				finish();
			}
		} else if(taskId.equals(URL.GET_DETAIL_TASKID)){
			// 获取事件详情
			dismissLoading();
			handleGetEventDetail(response);
		}
	}


	private void handleGetEventDetail(Object response){
		ScheduleDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ScheduleDetailPOJO.class);
		if(pojo == null){
			return;
		}else if(!pojo.isResultSuccess()){
			return;
		}else if(pojo.getBody().getResponse().getDate() != null){
			mEvent = pojo.getBody().getResponse().getDate();
			initDate();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK){
			return;
		}
		if(requestCode == APPROVE_EDIT){
			if(data != null){
				mCurrentShowId = data.getStringExtra("back_show_id");
				getEventDetail();
			}
		}else if(requestCode == CommentsView.TAKE_PICTURE){
			mCommentView.setImageMessage();
		}else if(requestCode == CommentsView.GET_ALBUM){
			if(data == null){
				return;
			}
			List<String> imagePath = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
			if(imagePath != null){
				mCommentView.setAublmImageMessage(imagePath.get(0));
			}
		}
	}


	@Override
	public void sendMessage(String message) {
		mCommentListView.updateComment();
	}



	/** 设置提醒*/
	private void setRemindByType(int type){
		mTvRemind.setVisibility(View.VISIBLE);
		switch (type){
			case 0:
				mTvRemind.setVisibility(View.GONE);
				break;
			case 200:
				mTvRemind.setText(getString(R.string.calendar_the_day));
				break;
			case 201:
				mTvRemind.setText(getString(R.string.calendar_before_one_day));
				break;
			case 202:
				mTvRemind.setText(getString(R.string.calendar_before_two_week));
				break;
			case 203:
				mTvRemind.setText(getString(R.string.calendar_before_one_week));
				break;
			default:
				mTvRemind.setVisibility(View.GONE);
				break;
		}
	}

	/** 显示地图*/
	private void setMapAddress(){
		if(mEvent.getLaty()==null || mEvent.getLngx()==null || mEvent.getLaty().equals("") || mEvent.getLngx().equals("")){
			mLlMap.setVisibility(View.GONE);
			return;
		}
		mLlMap.setVisibility(View.VISIBLE);
		double laty = Double.parseDouble(mEvent.getLaty());
		double lngx = Double.parseDouble(mEvent.getLngx());
		mLlMap.getMap(lngx,laty);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLlMap.onDestroyMap();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLlMap.onResumeMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLlMap.onPauseMap();
	}
}
