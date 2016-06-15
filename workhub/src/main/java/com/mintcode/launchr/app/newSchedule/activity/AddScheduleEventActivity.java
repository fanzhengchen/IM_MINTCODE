package com.mintcode.launchr.app.newSchedule.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.api.ScheduleApi.URL;
import com.mintcode.launchr.app.MapLoadView;
import com.mintcode.launchr.app.meeting.MeetingDelectDialog;
import com.mintcode.launchr.app.meeting.activity.RemindActivity;
import com.mintcode.launchr.app.meeting.activity.RestartActivity;
import com.mintcode.launchr.app.newSchedule.fragment.DayFragment;
import com.mintcode.launchr.app.newSchedule.fragment.NewWeekFragment;
import com.mintcode.launchr.app.newSchedule.view.NewTimeSelectDialog;
import com.mintcode.launchr.app.place.AddPlacesActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.AddSchedulePOJO;
import com.mintcode.launchr.pojo.ScheduleDetailPOJO;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 新建事件页面
 * 
 * @author JulyYu
 * 
 *         d2015-8-7
 */
public class AddScheduleEventActivity extends BaseActivity implements
		NewTimeSelectDialog.OnTimeDialogListener, OnCheckedChangeListener{


	/** 页面标题*/
	@Bind(R.id.tv_schedule_title)
	protected TextView mTvTitle;
	/*** 添加坐标*/
	@Bind(R.id.tv_add_place_xy)
	protected TextView mTvAddPlace;
	@Bind(R.id.ll_map)
	protected MapLoadView mLlMap;
	/*** 切换是否重要*/
	@Bind(R.id.cb_schedule_set_important)
	protected CheckBox mCbSetImportant;
	/*** 候补时间1布局*/
	@Bind(R.id.ll_schedule_add_event_alternate1)
	protected LinearLayout mLlAlternateTime1;
	/*** 候补时间2布局*/
	@Bind(R.id.ll_schedule_add_event_alternate2)
	protected LinearLayout mLlAlternateTime2;
	/*** 候补时间3布局*/
	@Bind(R.id.ll_schedule_add_event_alternate3)
	protected LinearLayout mLlAlternateTime3;
	/*** 候补时间1*/
	@Bind(R.id.tv_schedule_add_event_alternate1)
	protected TextView mTvAlternateTime1;
	/*** 候补时间2*/
	@Bind(R.id.tv_schedule_add_event_alternate2)
	protected TextView mTvAlternateTime2;
	/*** 候补时间3*/
	@Bind(R.id.tv_schedule_add_event_alternate3)
	protected TextView mTvAlternateTime3;
	/*** 事件标题*/
	@Bind(R.id.edt_schedule_add_event_title)
	protected EditText mEdtTitle;
	/*** 场所选择*/
	@Bind(R.id.tv_schedule_add_event_only_place)
	protected EditText mTvPlace;
	/*** 重复*/
	@Bind(R.id.tv_schedule_add_event_repeat)
	protected TextView mTvRepeat;
	/*** 提醒*/
	@Bind(R.id.tv_schedule_add_event_notification)
	protected TextView mTvNotification;
	/*** 备注*/
	@Bind(R.id.edt_schedule_add_event_remark)
	protected EditText mEdtRemark;
	/** 仅自己可见*/
	@Bind(R.id.cb_only_me_see)
	protected CheckBox mCbOnlyMeSee;
	@Bind(R.id.ll_reapet)
	protected LinearLayout LLReapet;


	/** 重复返回码 */
	public static final int RESTART_CODE = 1;
	/** 提醒返回码 */
	public static final int REMIND_CODE = 2;
	/*** 编辑返回码*/
	public static final int EDIT_CODE = 3;
	/*** 设置是否为全天事件*/
	private boolean mBoolIsAllDay = true;
	private int repeatType;
	private int remindType;
	/** 地址返回值*/
	public static final int REQ_PLACE_CODE = 0x12;
	/** 编辑返回值*/
	public final static String EDIT_EVENT_KEY = "edit_event_key";
	/** 是否为编辑*/
	private boolean EDIT = false;
	/*** 时间数据*/
	private ArrayList<Long> mTimers = new ArrayList<>();

	private String[] mStrReapet;
	
	private String[] mStrDayType;
	
	private ScheduleEntity mEvent;
	/** 经度、维度*/
	private String mLatitude;
	private String mLongitude;

	/** 时间选择控件*/
	private NewTimeSelectDialog mDelectEventDialog;
	private long mTodayTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_add_event);
		ButterKnife.bind(this);
		initView();
		getIntentContent();
		mTodayTime = getIntent().getLongExtra(DayFragment.TODAY_TIME,0);
		if(mTodayTime > 0){
			mTimers.add(mTodayTime);
			mTimers.add(mTodayTime + 3600000);
			getIntentTimeArrayList(mTimers,mBoolIsAllDay);
		}
		MixPanelUtil.sendEvent(this,MixPanelUtil.APP_CALENDAR_NEW_EVENT);
	}

	/**
	 *非新建 编辑时载入数据到页面
	 */
	private void getIntentContent() {
		Intent Result = getIntent();
		EDIT = Result.getBooleanExtra(EDIT_EVENT_KEY, false);
		mEvent = (ScheduleEntity)Result.getSerializableExtra("event");
		if(mEvent != null){
			mTvTitle.setText(getResources().getString(R.string.calendar_edit));
			mTvPlace.setText(mEvent.getPlace());
			mEdtTitle.setText(mEvent.getTitle());
			mBoolIsAllDay = (mEvent.getIsAllDay() == 1 ? true : false);
			mCbSetImportant.setChecked(mEvent.getIsImportant() == 1 ? true:false);
			mCbOnlyMeSee.setChecked(mEvent.getIsVisible() == 1 ? false:true);
			
			if(mEvent.getTimes() != null){
				for(int i = 0;i < mEvent.getTimes().size();i++){
					mTimers.add(mEvent.getTimes().get(i).getStart());
					mTimers.add(mEvent.getTimes().get(i).getEnd());
				}
				getIntentTimeArrayList(mTimers,mBoolIsAllDay);
			}
			
			mEdtRemark.setText(mEvent.getContent());
			
			if(mTimers.size() >= 4){
				LLReapet.setVisibility(View.GONE);
			}else{
				mStrReapet = new String[]{
						getResources().getString(R.string.calendar_never),
						getResources().getString(R.string.calendar_everyday),
						getResources().getString(R.string.calendar_everyweek),
						getResources().getString(R.string.calendar_eveymonth),
						getResources().getString(R.string.calendar_everyyear)
				};
				mTvRepeat.setText(mStrReapet[mEvent.getRepeatType()]);
				mStrReapet = null;
			}			
			repeatType = mEvent.getRepeatType();
			if(mBoolIsAllDay){
				mStrDayType	= new String[]{
						getResources().getString(R.string.calendar_not_remind),
						getResources().getString(R.string.calendar_the_day),
						getResources().getString(R.string.calendar_before_one_day),
						getResources().getString(R.string.calendar_before_two_week),
						getResources().getString(R.string.calendar_before_one_week) };				
				mTvNotification.setText(mStrDayType[mEvent.getRemindType()%200]);				
			}else{
				mStrDayType	= new String[] {
						getResources().getString(R.string.calendar_not_remind),
						getResources().getString(R.string.calendar_happen),
						getResources().getString(R.string.calendar_before_5),
						getResources().getString(R.string.calendar_before_15),
						getResources().getString(R.string.calendar_before_30),
						getResources().getString(R.string.calendar_before_one_hour),
						getResources().getString(R.string.calendar_before_two_hour),
						getResources().getString(R.string.calendar_before_one_day),
						getResources().getString(R.string.calendar_before_two_day),
						getResources().getString(R.string.calendar_before_one_week) };
				if(mEvent.getRemindType() > 0){
					mTvNotification.setText(mStrDayType[mEvent.getRemindType()%100 + 1]);
				} else{
					mTvNotification.setText(mStrDayType[0]);					
				}
			}
			remindType = mEvent.getRemindType();

			mLatitude = mEvent.getLngx();
			mLongitude = mEvent.getLaty();
			setMeetingAddress();
		}
	}

	void initView() {
		mLlAlternateTime1.setVisibility(View.GONE);
		mLlAlternateTime2.setVisibility(View.GONE);
		mLlAlternateTime3.setVisibility(View.GONE);
		mTimers = new ArrayList<Long>();
	}
	
	/**
	 * 保存事件并跳转到详情页面
	 */
	private void getEventContent() {
		if(mEvent == null){
			mEvent =  new ScheduleEntity();
		}
		mEvent.setTitle(mEdtTitle.getText().toString().trim());
		if (mCbSetImportant.isChecked()) {
			mEvent.setIsImportant(1);
		} else {
			mEvent.setIsImportant(0);
		}
		if(mCbOnlyMeSee.isChecked()){
			mEvent.setIsVisible(0);
		}else{
			mEvent.setIsVisible(1);
		}
		mEvent.setPlace(mTvPlace.getText().toString().trim());
		mEvent.setLaty(mLongitude);
		mEvent.setLngx(mLatitude);

		if (mBoolIsAllDay) {
			mEvent.setIsAllDay(1);
		} else {
			mEvent.setIsAllDay(0);
		}
		List<Long> start = new ArrayList<Long>();
		List<Long> end = new ArrayList<Long>();
		for (int i = 0; i < mTimers.size(); i = i + 2) {
			start.add(mTimers.get(i));
			end.add(mTimers.get(i + 1));
		}
		mEvent.setStart(start);
		mEvent.setEnd(end);
		if (start.size() > 1) {
			mEvent.setType("event_sure");
		} else {
			mEvent.setType("event");
		}		
		mEvent.setRepeatType(repeatType);
		mEvent.setRemindType(remindType);
		mEvent.setContent(mEdtRemark.getText().toString().trim());
		
		if(EDIT == true){
			if(mEvent.getRepeatType() != 0){
				final MeetingDelectDialog dialog = new MeetingDelectDialog(this);
				dialog.show();
				Window window = dialog.getWindow();
				TextView content = (TextView) window.findViewById(R.id.tv_dialog_context);
				RelativeLayout delect = (RelativeLayout) window.findViewById(R.id.rl_dialog_meeting_delect);
				RelativeLayout canel = (RelativeLayout) window.findViewById(R.id.rl_dialog_meeting_canel);
				TextView delectonly = (TextView) window.findViewById(R.id.tv_repeact_only);
				TextView delectall = (TextView) window.findViewById(R.id.tv_repeact_all);
				RelativeLayout reapactonly = (RelativeLayout) window.findViewById(R.id.rl_dialog_reapect_only);
				RelativeLayout reapactall = (RelativeLayout) window.findViewById(R.id.rl_dialog_reapect_all);				
				
				delect.setVisibility(View.GONE);
				content.setVisibility(View.GONE);
				
				delectonly.setText(R.string.repeact_update_only);
				delectall.setText(R.string.repeact_update_all);
				
				reapactonly.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						getAddMeetingActivity(0);
						
					}
				});
				reapactall.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						getAddMeetingActivity(1);
						
					}
				});
				canel.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
			}else{
				showLoading();
				ScheduleApi.getInstance().editSchedule(this, mEvent,mEvent.getStart().get(0),0);
			}
		}else{
			showLoading();
			ScheduleApi.getInstance().saveSchedule(this,mEvent);
		}		
	}

	/**
	 * 编辑日程
	 */
	public void getAddMeetingActivity(int all) {
		showLoading();
		ScheduleApi.getInstance().editSchedule(this,mEvent,mEvent.getStart().get(0),all);
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		dismissLoading();
		if (response == null) {
			showNoNetWork();
			return;
		}
		// 判断是否保存
		if (taskId.equals(URL.SAVE_SCHEDULE)) {
			handleNewSchedule(response);
		}else
		// 判断是否是编辑
		if (taskId.equals(URL.EDIT_SCHEDULE)) {
			handleEditSchedule(response);
		}		
	}

	private void handleEditSchedule(Object response) {
			AddSchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AddSchedulePOJO.class);
			// 修改
			if (pojo != null) {
				if (pojo.isResultSuccess()) {
					ScheduleEntity data = pojo.getBody().getResponse().getData();
					if (data != null) {
						Intent ToEventDetail = new Intent();
						ToEventDetail.putExtra("back_show_id", data.getShowId());
						setResult(RESULT_OK, ToEventDetail);

						// 编辑事件并修改了时间，要刷新
						NewWeekFragment.newEventResult = 1;
						this.finish();
					}else{
						toast(pojo.getBody().getResponse().getReason());
					}
				}else{
					toast(pojo.getHeader().getReason());
				}
			}else{
				showNetWorkGetDataException();
			}
	}
	
	private void handleNewSchedule(Object response) {
		AddSchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AddSchedulePOJO.class);
		// 修改
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				ScheduleEntity data = pojo.getBody().getResponse().getData();
				if (data != null) {
					Intent ToEventDetail = new Intent(AddScheduleEventActivity.this,ScheduleEventDetailActivity.class);
					ToEventDetail.putExtra("event_showid", data.getShowId());
					startActivity(ToEventDetail);

					// 新建事件成功，要刷新
					NewWeekFragment.newEventResult = 1;
					this.finish();
				}else{
					toast(pojo.getBody().getResponse().getReason());
				}
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}
	}

	private void handleScheduledetail(Object response) {
		ScheduleDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ScheduleDetailPOJO.class);
		if (pojo != null ) {
			if (pojo.isResultSuccess()) {
				ScheduleEntity data = pojo.getBody().getResponse().getDate();
				if (data != null) {
					Intent ToEventDetail = new Intent(AddScheduleEventActivity.this,ScheduleEventDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(ScheduleEventDetailActivity.DETAIL_CODE, data);
					ToEventDetail.putExtras(bundle);
					startActivity(ToEventDetail);
					this.finish();
				}else{
					toast(pojo.getHeader().getReason());
				}
			}else{
				toast(pojo.getHeader().getReason());
			}
		}
    }

	/**
	 * 检查保存的事件是否有空
	 */
	private boolean verifyEventNoNull() {
		if (mEdtTitle.getText().toString().trim().equals("")) {
			toast(getString(R.string.calendar_title_toast));
			return false;
		}
		else if (mTvAlternateTime1.getText().toString().trim().equals("")) {
			toast(getString(R.string.calendar_time_toast));
			return false;
		}
		if(mBoolIsAllDay){
			for (int i = 0; i < mTimers.size(); i = i + 2) {
				if(mTimers.get(i) > mTimers.get(i + 1)) {
					String str = getResources().getString(R.string.end_time_must_not_be_later_than_begin_time);
					toast(str);
					return false;
				}
			}
		}else{
			for (int i = 0; i < mTimers.size(); i = i + 2) {
				if(mTimers.get(i) >= mTimers.get(i + 1)) {
					String str = getResources().getString(R.string.end_time_must_not_be_later_than_begin_time);
					toast(str);
					return false;
				}
			}
		}
		return true;
	}

	private void getIntentTimeArrayList(ArrayList<Long> time, boolean flag) {
		mTimers = time;
		// 关闭显示
		mLlAlternateTime1.setVisibility(View.GONE);
		mLlAlternateTime2.setVisibility(View.GONE);
		mLlAlternateTime3.setVisibility(View.GONE);
		if (time.size() < 3) {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
		} else if (time.size() < 5) {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mLlAlternateTime2.setVisibility(View.VISIBLE);

			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
		} else {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mLlAlternateTime2.setVisibility(View.VISIBLE);
			mLlAlternateTime3.setVisibility(View.VISIBLE);

			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
			mTvAlternateTime3.setText(NewTimeSelectDialog.gettime(time, 2, flag));
		}
	}

	/**
	 * 切换是否为全天时间 改变时间显示
	 */
	private void isAllDayEvent(ArrayList<Long> time, boolean flag) {
		if (time.size() == 0) {
			mBoolIsAllDay = flag;
			toast(getString(R.string.calendar_time_toast));
			return;
		}
		if (time.size() < 3 && time.size() > 0) {
			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
		} else if (time.size() < 5) {
			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
		} else {
			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
			mTvAlternateTime3.setText(NewTimeSelectDialog.gettime(time, 2, flag));
		}
	}
	/** 保存*/
	@OnClick(R.id.tv_schedule_add_event_save)
	protected void saveEvent(){
		if (verifyEventNoNull() == true) {
			MixPanelUtil.sendEvent(this,MixPanelUtil.APP_CALENDAR_SAVE_EVENT);
			getEventContent();
		}
	}
	/** 返回*/
	@OnClick(R.id.tv_schedule_add_event_back)
	protected void Back(){
		MixPanelUtil.sendEvent(this,MixPanelUtil.APP_CALENDAR_CANCEL_EVENT);
		this.finish();
	}
	/** */
	@OnClick(R.id.cb_schedule_set_allday)
	protected void setDayTime(){
		isAllDayEvent(mTimers,mBoolIsAllDay);
	}
	/** 设置时间*/
	@OnClick(R.id.rl_schedule_add_event_set_time)
	protected void addTime(){
		mDelectEventDialog = new NewTimeSelectDialog();
		Bundle bundle = new Bundle();
		bundle.putLong(NewTimeSelectDialog.TODAT_TIME,mTodayTime);
		bundle.putSerializable(NewTimeSelectDialog.TIME_List,(Serializable) mTimers);
		mDelectEventDialog.setArguments(bundle);
//		mDelectEventDialog.setList(mTimers);
		mDelectEventDialog.setOnTimeDialogListener(this);
		mDelectEventDialog.show(getSupportFragmentManager(), "add_time");
		mDelectEventDialog.getIsAllDay(mBoolIsAllDay);
	}
	/** 设置重复*/
	@OnClick(R.id.rl_schedule_add_event_repeat)
	protected void setRepeat(){
		Intent intentToRestart = new Intent(AddScheduleEventActivity.this,RestartActivity.class);
		startActivityForResult(intentToRestart, RESTART_CODE);
	}
	/** 设置提醒*/
	@OnClick(R.id.rl_schedule_add_event_notification)
	protected void setNotification(){
		Intent intentToRemind = new Intent(AddScheduleEventActivity.this,RemindActivity.class);
		intentToRemind.putExtra("isAllDay",mBoolIsAllDay);
		intentToRemind.putExtra(RemindActivity.REMIND_INFO,mTvNotification.getText());
		startActivityForResult(intentToRemind, REMIND_CODE);
	}
	@Override
	public void onClick(View v) {
		clearFocus();
		super.onClick(v);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == RESTART_CODE) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString(RestartActivity.RESTART_INFO);
			repeatType = bundle.getInt("repeatType");
			mTvRepeat.setText(result + "");
		} else if (requestCode == REMIND_CODE) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString(RemindActivity.REMIND_INFO);
			remindType = bundle.getInt(RemindActivity.REMINDTYPE);
			mTvNotification.setText(result + "");
		} else if (requestCode == REQ_PLACE_CODE && data!=null) {
			mLongitude = data.getStringExtra(Const.LATITUDE);
			mLatitude = data.getStringExtra(Const.LONGITUDE);
			mTvAddPlace.setText(getString(R.string.delete_place_xy));
			setMeetingAddress();
			clearFocus();
		}
	}

	// 获取时间Dialog时间列表接口 flag 是否为全天
	@Override
	public void getTimeArrayList(ArrayList<Long> time, boolean flag) {
		mBoolIsAllDay = flag;
		mTimers = time;
		// 关闭显示
		mLlAlternateTime1.setVisibility(View.GONE);
		mLlAlternateTime2.setVisibility(View.GONE);
		mLlAlternateTime3.setVisibility(View.GONE);
		if (time.size() < 3) {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			LLReapet.setVisibility(View.GONE);
		} else if (time.size() < 5) {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mLlAlternateTime2.setVisibility(View.VISIBLE);

			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
			LLReapet.setVisibility(View.GONE);
		} else {
			mLlAlternateTime1.setVisibility(View.VISIBLE);
			mLlAlternateTime2.setVisibility(View.VISIBLE);
			mLlAlternateTime3.setVisibility(View.VISIBLE);

			mTvAlternateTime1.setText(NewTimeSelectDialog.gettime(time, 0, flag));
			mTvAlternateTime2.setText(NewTimeSelectDialog.gettime(time, 1, flag));
			mTvAlternateTime3.setText(NewTimeSelectDialog.gettime(time, 2, flag));
			LLReapet.setVisibility(View.GONE);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		remindType = 0;
	}
	
	/**
	 * 清除焦点
	 */
	private void clearFocus(){
		if (mEdtTitle.hasFocus()) {
			mEdtTitle.clearFocus();
			hideSoftInputWindow(mEdtTitle);
		}else if (mEdtRemark.hasFocus()) {
			mEdtRemark.clearFocus();
			hideSoftInputWindow(mEdtRemark);
		}
	}
	
	/**
	 * 隐藏软键盘
	 */
	private void hideSoftInputWindow(View v){
		 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/** 添加坐标*/
	@OnClick(R.id.tv_add_place_xy)
	protected void addPlaceXY(){
		if(mTvAddPlace.getText().toString().equals(getString(R.string.add_place_xy))){
			Intent intent = new Intent(this, AddPlacesActivity.class);
			intent.putExtra(Const.ADDRESS, mTvPlace.getText().toString());
			startActivityForResult(intent, REQ_PLACE_CODE);
		}else{
			mTvAddPlace.setText(getString(R.string.add_place_xy));
			mTvPlace.setText("");
			mLatitude = null;
			mLongitude = null;
			setMeetingAddress();
		}
	}

	/** 显示地图*/
	private void setMeetingAddress() {
		if (mLongitude == null || mLatitude == null || mLongitude.equals("") || mLatitude.equals("")) {
			mLlMap.setVisibility(View.GONE);
			return;
		}
		double laty = Double.parseDouble(mLongitude);
		double lngx = Double.parseDouble(mLatitude);
		mTvAddPlace.setText(getString(R.string.delete_place_xy));
		mLlMap.setVisibility(View.VISIBLE);
		mLlMap.getMap(lngx,laty);
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

}
