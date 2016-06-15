package com.mintcode.launchr.message.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.activity.NewTaskActivity;
import com.mintcode.launchr.app.newTask.activity.ProjectListActivity;
import com.mintcode.launchr.app.newTask.view.FinalTimeDialog;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.ChooseMemberActivity;
import com.mintcode.launchr.pojo.NewTaskPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.MessageEventTaskEntity;
import com.mintcode.launchr.pojo.entity.NewTaskEntity;
import com.mintcode.launchr.pojo.entity.TaskDetailEntity;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 新增任务、消息转任务
 */
public class ChatAddTaskWindow extends PopupWindow implements
		 OnClickListener,FinalTimeDialog.OnGetTimeListener {
	private static final String APP_SHOW_ID = Const.SHOWID_TASK;

	private View mContentView;

	private LayoutInflater mInflater;

	private Context mContext;

//	/** 时间控件 */
//	private Timer mTimer;

//	/** 截止时间 */
//	private Calendar finalTime;

//	/** 全天 */
//	private CheckBox mCbAllDay;

	/** 更多选项 */
	private TextView mTvMoreItem;

	/** 项目 */
	private RelativeLayout mRelProject;
	private TextView mTvProjectName;
	private TaskProjectListEntity mProjectEntity;

	/** 参与者 */
	private RelativeLayout mRelEnter;
	private TextView mTvMemberName;
	private String mUserName;
	private String mUserId;

	/** 标题 */
	private EditText mEditTitle;
	private String mTitle;

	/** 确定 */
	private TextView mIvOk;
	/** 取消*/
	private TextView mTvCancel;
	
	/** 是否是群聊*/
	private boolean isGroup;
	
	/** 某个人是否在群里面*/
	private boolean isInGroup = false;
	
	List<GroupInfo> memberList;

	/** 开始时间*/
	private RelativeLayout mRelStartTime;
	private TextView mTvStartTime;
	private Long mLStartTime;
	private int mStartTimeAllDay = 1;

	/** 截止时间*/
	private RelativeLayout mRlFinalTime;
	private TextView mTvFinalTime;
	private Long mLFinalTime;
	private int mFinalTimeAllDay = 1;

	private FinalTimeDialog mTimeDialog;

	public ChatAddTaskWindow(Context context, boolean isGroup, String toNickName, String mTo, String title) {
		super(context);
		this.isGroup = isGroup;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mContentView = mInflater.inflate(R.layout.popwindow_add_task, null);
		mTitle = title;
		memberList = new ArrayList<GroupInfo>();
		
		if(isGroup){
			IMAPI.getInstance().getGroupInfo(listener, AppUtil.getInstance(mContext).getShowId(), 
					AppUtil.getInstance(mContext).getIMToken(), toNickName);
			
			HeaderParam mHeaderParam = LauchrConst.getHeader(mContext);
			mUserId= mHeaderParam.getLoginName();
			mUserName = mHeaderParam.getUserName();
		}else{
			mUserId= mTo;
			mUserName = toNickName;
		}
				
		setWindow();
		initView();
	}

	private void setWindow() {
		this.setContentView(mContentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable color = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(color);
	}

	private void initView() {
//		mTimer = (Timer) mContentView.findViewById(R.id.add_task_timer);
//		mCbAllDay = (CheckBox) mContentView.findViewById(R.id.cb_is_allday);
		mTvMoreItem = (TextView) mContentView.findViewById(R.id.tv_more);
		mRelEnter = (RelativeLayout) mContentView.findViewById(R.id.rel_enter);
		mRelProject = (RelativeLayout) mContentView
				.findViewById(R.id.rel_project);
		mEditTitle = (EditText) mContentView.findViewById(R.id.edit_title);
		mEditTitle.setText(mTitle);
		mIvOk = (TextView) mContentView.findViewById(R.id.dialog_ok);
		mTvCancel = (TextView) mContentView.findViewById(R.id.dialog_cancel);
		mTvProjectName = (TextView) mContentView
				.findViewById(R.id.tv_project_name);
		mTvMemberName = (TextView) mContentView
				.findViewById(R.id.tv_member_name);
		mRelStartTime = (RelativeLayout) mContentView.findViewById(R.id.new_task_starttime);
		mRlFinalTime = (RelativeLayout) mContentView.findViewById(R.id.new_task_finaltime);
		mTvStartTime = (TextView) mContentView.findViewById(R.id.new_task_starttime_text);
		mTvFinalTime = (TextView) mContentView.findViewById(R.id.new_task_finaltime_text);

//		mCbAllDay.setOnCheckedChangeListener(this);
		mTvMoreItem.setOnClickListener(this);
		mRelEnter.setOnClickListener(this);
		mRelProject.setOnClickListener(this);
		mIvOk.setOnClickListener(this);
		mRelStartTime.setOnClickListener(this);
		mRlFinalTime.setOnClickListener(this);
		mTvCancel.setOnClickListener(this);

//		mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
//		mTimer.setOnTimerListener(this);
//		mTimer.setConflictViewDisplay(false);

//		finalTime = Calendar.getInstance();
		
		mTvMemberName.setText(mUserName);

		// 初始化开始、结束时间
		Calendar c = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		b.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		mLStartTime = b.getTimeInMillis();
		mLFinalTime = b.getTimeInMillis();
	}

//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		if(isChecked){
//			mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
//		}else{
//			mTimer.setStyle(Timer.SINGLE_TIME_DATE);
//		}
//	}
//
//	@Override
//	public void OnTimeChangeListenner(View view, int year, int month, int day,
//			int hour, int minute, int type, boolean isEnd) {
//		finalTime = Calendar.getInstance();
//		finalTime.set(year, month, day, hour, minute, 0);
//	}

	@Override
	public void onClick(View v) {
		if (v == mTvMoreItem) {
			doMoreChoose();
		} else if (v == mRelEnter) {
			doMemberChoose();
		} else if (v == mRelProject) {
			doProjectChoose();
		} else if (v == mIvOk) {
			sendMessage();
		}else if(v == mRelStartTime){
			// 开始时间
			chooseStartTime();
		}else if(v == mRlFinalTime){
			// 截止时间
			chooseFinalTime();
		}else if(v == mTvCancel){
			dismiss();
		}
	}

	// 选择开始时间
	public void chooseStartTime() {
		if (mLStartTime == null) {
			Calendar c = Calendar.getInstance();
			Calendar b = Calendar.getInstance();
			b.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			mLStartTime = b.getTimeInMillis();
		}
		initTimeDialog(mContext.getString(R.string.choose_start_time));
		mTimeDialog.setStartTime(mLStartTime);
		if(mStartTimeAllDay == 1){
			mTimeDialog.setAllDay(true);
		}else{
			mTimeDialog.setAllDay(false);
		}

//		final FinalTimeDialog finalTime = new FinalTimeDialog(mContext, mContext.getString(R.string.choose_start_time));
//		finalTime.show();
//		finalTime.setStartTime(mLStartTime);
//		if(mStartTimeAllDay == 1){
//			finalTime.setAllDay(true);
//		}else{
//			finalTime.setAllDay(false);
//		}
//		Window window = finalTime.getWindow();
//		final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//		View.OnClickListener listener = new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (v == finalOk) {
//					mLStartTime = finalTime.getTime().getTimeInMillis();
//					showFinalTime(finalTime.getTime(), mTvStartTime);
//
//					finalTime.dismiss();
//				}
//			}
//		};
//		finalOk.setOnClickListener(listener);
//		final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//		finalSetting.setText(mContext.getString(R.string.some_day));
//		finalSetting.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mTvStartTime.setText(mContext.getString(R.string.some_day));
//				mLStartTime = null;
//				finalTime.dismiss();
//			}
//		});
//		final CheckBox startAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//		startAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					finalTime.setAllDay(isChecked);
//					mStartTimeAllDay= 1;
//				} else {
//					finalTime.setAllDay(isChecked);
//					mStartTimeAllDay = 0;
//				}
//			}
//		});
	}

	// 选择截止时间
	public void chooseFinalTime() {

		initTimeDialog(mContext.getString(R.string.choose_stop_time));
		mTimeDialog.setEndTime(mLFinalTime);
		if(mFinalTimeAllDay == 1){
			mTimeDialog.setAllDay(true);
		}else{
			mTimeDialog.setAllDay(false);
		}

//		final FinalTimeDialog finalTime = new FinalTimeDialog(mContext, mContext.getString(R.string.choose_stop_time));
//		finalTime.show();
//		if(mLFinalTime != null  && mLFinalTime > 0){
//			finalTime.setEndTime(mLFinalTime);
//		}
//		if(mFinalTimeAllDay == 1){
//			finalTime.setAllDay(true);
//		}else{
//			finalTime.setAllDay(false);
//		}
//		Window window = finalTime.getWindow();
//		final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//		View.OnClickListener listener = new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (v == finalOk) {
//					mLFinalTime = finalTime.getTime().getTimeInMillis();
//					if(mLFinalTime >= mLStartTime){
//						showFinalTime(finalTime.getTime(), mTvFinalTime);
//						finalTime.dismiss();
//					}else{
//						Toast.makeText(mContext, mContext.getString(R.string.end_time_must_not_be_later_than_begin_time), Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		};
//		finalOk.setOnClickListener(listener);
//		final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//		finalSetting.setText(mContext.getString(R.string.no_set_time));
//		finalSetting.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mTvFinalTime.setText(mContext.getString(R.string.no_set_time));
//				mLFinalTime = null;
//				finalTime.dismiss();
//			}
//		});
//		final CheckBox finalAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//		finalAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					finalTime.setAllDay(isChecked);
//					mFinalTimeAllDay = 1;
//				} else {
//					finalTime.setAllDay(isChecked);
//					mFinalTimeAllDay = 0;
//				}
//			}
//		});
	}

	private void initTimeDialog(String title){
		if(mTimeDialog == null){
			mTimeDialog = new FinalTimeDialog(mContext,title);
			mTimeDialog.setGetTimeListener(this);
			mTimeDialog.show();
		}else{
			mTimeDialog.show();
			mTimeDialog.setCustomTitle(title);
		}
	}


	// 显示时间
//	public void showFinalTime(Calendar calendar, TextView textView) {
//		int m = calendar.get(Calendar.MONTH) + 1;
//		int d = calendar.get(Calendar.DAY_OF_MONTH);
//		int h = calendar.get(Calendar.HOUR_OF_DAY);
//		int mm = calendar.get(Calendar.MINUTE);
//
//		if (h == 0 && mm == 0) {
//			textView.setText(m + "/" + d);
//		}else{
//			textView.setText(m + "/" + d + " " + h + ":" + mm);
//		}
//	}

	/** 发送消息 */
	public void sendMessage() {
		if (mEditTitle.getText().length() <= 0) {
			Toast.makeText(mContext, mContext.getString(R.string.calendar_title_toast), Toast.LENGTH_SHORT).show();
			return;
		}

		if (mUserName == null) {
			Toast.makeText(mContext, mContext.getString(R.string.meeting_enter_people), Toast.LENGTH_SHORT).show();
			return;
		}

		if(mTvFinalTime.getText().toString().equals(mContext.getString(R.string.no_set_time))){
			mLFinalTime = null;
		}

		if(mProjectEntity != null){
			TaskApi.getInstance().newTask(addTaskResult, mProjectEntity.getShowId(), mEditTitle.getText().toString(), null, mLStartTime, mLFinalTime,
					mStartTimeAllDay, mFinalTimeAllDay, mUserId, mUserName, TaskUtil.LOW_PRIORITY, 0, null, null);
		}else{
			TaskApi.getInstance().newTask(addTaskResult, null, mEditTitle.getText().toString(), null, mLStartTime, mLFinalTime,
					mStartTimeAllDay, mFinalTimeAllDay, mUserId, mUserName, TaskUtil.LOW_PRIORITY, 0, null, null);
		}
		dismiss();
	}

	private OnResponseListener addTaskResult = new OnResponseListener() {
		
		@Override
		public void onResponse(Object response, String taskId, boolean rawData) {
			if (response == null) {
				return;
			} else if (taskId.equals(TaskApi.TaskId.TASK_URL_ADD_TASK)){
				// 判断是否是新建任务返回
				handleAddTaskResult(response);
			}
		}
		
		@Override
		public boolean isDisable() {
			return false;
		}
	};
	
	/**
	 * 处理新建任务返回
	 * @param response
	 */
	private void handleAddTaskResult(Object response){
		NewTaskPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NewTaskPOJO.class);
		if (pojo == null) {
			return;
		}else if(pojo.isResultSuccess() == false){
			return;
		}else{
			NewTaskEntity entity = pojo.getBody().getResponse().getData();
			if (entity != null) {
				sendMessageToIM(entity);
			}
		}
				
	}
	
	private void sendMessageToIM(NewTaskEntity entity){
		MessageEventTaskEntity.EventTaskCard taskCard = new MessageEventTaskEntity.EventTaskCard();
		taskCard.setTitle(mEditTitle.getText().toString());
		taskCard.setEnd(entity.gettEndTime());
		if(mProjectEntity!=null && mProjectEntity.getName() != null){
			taskCard.setProjectName(mProjectEntity.getName());
		}
		taskCard.setPriority(entity.gettPriority());
		taskCard.setId(entity.getShowId());
		taskCard.setLevel(entity.gettLevel());
		taskCard.setStateName(entity.getsType());
		taskCard.setStateType(entity.getsType());
//		String msgInfo = TTJSONUtil.convertObjToJson(taskCard);

		MessageEventTaskEntity.MessageEventTaskBackupEntity task = new MessageEventTaskEntity.MessageEventTaskBackupEntity();
		task.setMsgTitle(mEditTitle.getText().toString());
		task.setMsgContent(mEditTitle.getText().toString());
		task.setMsgAppShowID(APP_SHOW_ID);
		task.setMsgAppType("TASK");
		task.setMsgInfo(taskCard);
		task.setMsgType(1);
		task.setMsgFrom(AppUtil.getInstance(mContext).getUserName());
		task.setMsgFromID(AppUtil.getInstance(mContext).getLoginName());

		((ChatActivity) mContext).sendTaskMessage(task, isInGroup, mUserId, mUserName);
	}

	/** 更多选项 */
	private void doMoreChoose() {
		List<String> enterName = new ArrayList<String>();
		List<String> enterId = new ArrayList<String>();
		enterName.add(mUserName);
		enterId.add(mUserId);

		TaskDetailEntity entity = new TaskDetailEntity();
		entity.settTitle(mEditTitle.getText().toString());
		if(mProjectEntity != null){
			entity.setpShowId(mProjectEntity.getShowId());
			entity.setpName(mProjectEntity.getName());
		}
		if(mTvStartTime.getText().equals(mContext.getString(R.string.some_day))){
			mLStartTime = null;
		}else{
			entity.settStartTime(mLStartTime);
		}
		if(mTvFinalTime.getText().equals(mContext.getString(R.string.some_day))){
			mLFinalTime = null;
		}else{
			entity.settEndTime(mLFinalTime);
		}
		entity.settUser(enterId.get(0));
		entity.settUserName(enterName.get(0));

		Bundle bundle = new Bundle();
		bundle.putSerializable("add_task", entity);
		Intent intent = new Intent(mContext, NewTaskActivity.class);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
		dismiss();
	}

	/** 选择项目 */
	private void doProjectChoose() {
		Intent intent = new Intent(mContext, ProjectListActivity.class);
		((ChatActivity) mContext).startActivityForResult(intent, 1008);
	}

	/** 选择参与人员 */
	private void doMemberChoose() {
		Intent intent = new Intent(mContext, ChooseMemberActivity.class);
		((ChatActivity) mContext).startActivityForResult(intent, 1009);
	}

	/** 项目选择结果 */
	public void setProjectResult(TaskProjectListEntity entity) {
		if (entity != null) {
			mProjectEntity = entity;
			mTvProjectName.setText(entity.getName());
		}
	}

	/** 参与人员选择结果 */
	public void setMemberResult(List<String> userId, List<String> userName) {

		if(userId != null && userName != null){
			mUserId = userId.get(0);
			mUserName = userName.get(0);
			mTvMemberName.setText(mUserName);

			for(int i=0; i<memberList.size(); i++){
				if(mUserName.equals(memberList.get(i).getNickName())){
					isInGroup = true;
				}
			}
		}
	}
	
	private GroupInfo mGroupInfo;
	
	private OnResponseListener listener = new OnResponseListener() {
		
		@Override
		public void onResponse(Object response, String taskId, boolean rawData) {
			// TODO Auto-generated method stub
			if(response == null){
				return;
			}
			
			if (taskId.equals(IMAPI.TASKID.SESSION)) {
				GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
				if (pojo.isResultSuccess()) {
					pojo.getData().setMemberJson(TTJSONUtil.convertObjToJson(pojo.getData().getMemberList()));
					mGroupInfo = pojo.getData();	
					memberList = mGroupInfo.getMemberList();
				}
			} 
		}
		
		@Override
		public boolean isDisable() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	@Override
	public void getLongTime(int timeFlag, Long time, String strTime,int allDay) {
		if(timeFlag == FinalTimeDialog.START_TIME){
			mLStartTime = time;
			mTvStartTime.setText(strTime);
			mStartTimeAllDay = allDay;
		}else if(timeFlag == FinalTimeDialog.OVER_TIME){
			mLFinalTime = time;
			mTvFinalTime.setText(strTime);
			mFinalTimeAllDay = allDay;
		}
	}
}
