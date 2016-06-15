package com.mintcode.launchr.app.newTask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.AttachmentApi;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.api.TaskApi.TaskId;
import com.mintcode.launchr.app.meeting.activity.RemindActivity;
import com.mintcode.launchr.app.meeting.activity.RestartActivity;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.view.FileSelectLayoutView;
import com.mintcode.launchr.app.newTask.view.FinalTimeDialog;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.AttachmentListPOJO;
import com.mintcode.launchr.pojo.NewTaskPOJO;
import com.mintcode.launchr.pojo.TaskDetailPOJO;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.pojo.entity.NewTaskEntity;
import com.mintcode.launchr.pojo.entity.TaskDetailEntity;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.view.SelectPicTypeWindowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * create by Stephen he 2015/9/7
 */
public class NewTaskActivity extends BaseActivity implements OnSelectContactListner
		,OnCheckedChangeListener,FinalTimeDialog.OnGetTimeListener{
	public static final int PROJECT_REQ_CODE = 0x11;
	
	// 取消
	private TextView mTvBack;

	// 保存
	private TextView mTvSave;

	private TextView mTvTaskTitle;


	// 标题
	private EditText mEtTitle;

	// 分配项目
	private RelativeLayout mRlProject;
	private TextView mTvProjectText;
	private TextView mTvMainTask;
	private ImageView mIvProjectRight;

	// 标签
	private RelativeLayout mRlTag;
	private TextView mTvTagText;
	private static final int LABEL_CODE = 6;

	@Bind(R.id.ll_task_content)
	 LinearLayout mTaskContent;
	 private FileSelectLayoutView mFileSelectLayoutView;
	/** 备注 */
	private EditText mEtRemark;
	/**
	 * 拍照
	 */
	public static final int TAKE_PICTURE = 0x000001;
	/**
	 * 预览照片
	 */
	public static final int PREVIEW_PIC = 0x000002;
	/**
	 * 打开相册选择照片
	 */
	public static final int GET_ALBUM = 0x000003;

	// 截止时间
	private RelativeLayout mRlFinalTime;
	private TextView mTvFinalTime;
	private Long mLFinalTime;
	private int mFinalTimeAllDay = 1;
	//开始时间
	private RelativeLayout mRelStartTime;
	private TextView mTvStartTime;
	private Long mLStartTime;
	private int mStartTimeAllDay = 1;

	// 优先度
	private RadioGroup mRgPrior;
	// 优先级中，默认选中
	private RadioButton mRbPriorMiddle;
	private RadioButton mRBtnHigh;
	private RadioButton mRbtnLow;
	private RadioButton mRbtNone;
	
	private FragmentManager mFragManger;

	// 联系人
	private ContactFragment mDeptFragment;

	// 分配
	private LinearLayout mRlAssign;
	// 分配方式
	private TextView mTvAssignText;
	// 分配任务给成员
	private List<UserDetailEntity> mAssignUserList;
	private List<Integer> mAssignList;
	/** 照片地址*/
	private List<AttachmentListEntity> mPicUrl = new ArrayList<>();
//	private List<String> requireList;


	// 提醒
	private RelativeLayout mRlRemind;
	// 提醒方式
	private TextView mTvRemindText;
	// 提醒返回码
	private static final int REMIND_CODE = 4;
	// 提醒类型
	private int mReminType = 0;

	// 重复
	private RelativeLayout mRlRepeat;
	// 重复方式
	private TextView mTvRepeatText;
	// 重复返回码
	private static final int REPEAT_CODE = 5;
	// 重复类型
	private int mRestartType = 0;

	/** 任务id */
	private String mEditorTaskId = "";
	
	/** 父任务id */
	private String mEditorParentTaskId = "";
	
	/** 详情实体 */
	private TaskDetailEntity mEntity;
	
	private List<String> mNameList;
	
	private List<String> mUserList;
	
	/** 父任务ID */
	private String mParentTaskId = "";
	
	/**　项目id */
	private String mProjectId = "";
	private TaskProjectListEntity mCurrentProject;

	/** 从时间进入*/
	private String mCurrentTime = null;
	

	/** 优先级 */
	private String mProperty = TaskUtil.NONE_PRIORITY;
	/** 应用Id*/
	public static final String TASK_APP_KEY = Const.SHOWID_TASK;
	/** 编辑任务*/
	private boolean mBoolEditTag = false;

	private FinalTimeDialog mTimeDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_new_task);
		ButterKnife.bind(this);
		initArrays();
		initAllView();
		initData();
		MixPanelUtil.sendEvent(this,MixPanelUtil.APP_TASK_NEW_EVENT);
	}

	public void initAllView() {
		mTvBack = (TextView) findViewById(R.id.new_task_back);
		mTvSave = (TextView) findViewById(R.id.new_task_save);
		mTvTaskTitle = (TextView) findViewById(R.id.tv_new_task_title);

		mTvProjectText = (TextView) findViewById(R.id.new_task_project_text);
		mTvAssignText = (TextView) findViewById(R.id.new_task_assign_text);
		mTvFinalTime = (TextView) findViewById(R.id.new_task_finaltime_text);
		mTvTagText = (TextView) findViewById(R.id.new_task_tag_text);
		mTvRepeatText = (TextView) findViewById(R.id.new_task_repeat_text);
		mTvRemindText = (TextView) findViewById(R.id.new_task_remind_text);


		mEtTitle = (EditText) findViewById(R.id.new_task_title_edit);
		mEtRemark = (EditText) findViewById(R.id.et_remark);
		
		mRlProject = (RelativeLayout) findViewById(R.id.new_task_project);
		mRlAssign = (LinearLayout) findViewById(R.id.new_task_assign);
		mRlFinalTime = (RelativeLayout) findViewById(R.id.new_task_finaltime);
		mRlTag = (RelativeLayout) findViewById(R.id.new_task_label);
		mRlRepeat = (RelativeLayout) findViewById(R.id.new_task_repeat);
		mRlRemind = (RelativeLayout) findViewById(R.id.new_task_remind);
		mRelStartTime = (RelativeLayout) findViewById(R.id.new_task_starttime);
		mTvStartTime = (TextView) findViewById(R.id.new_task_starttime_text);
		mTvMainTask = (TextView)findViewById(R.id.tv_main_task);

		mRgPrior = (RadioGroup) findViewById(R.id.new_task_prior_radio);
		mRbPriorMiddle = (RadioButton) findViewById(R.id.rbtn_medium);
		mRBtnHigh = (RadioButton) findViewById(R.id.rbtn_high);
		mRbtnLow = (RadioButton) findViewById(R.id.rbtn_low);
		mRbtNone = (RadioButton) findViewById(R.id.rbtn_none);
		mIvProjectRight = (ImageView) findViewById(R.id.iv_roght_project);
		mFileSelectLayoutView = new FileSelectLayoutView(this);
		mTaskContent.addView(mFileSelectLayoutView);

		mRbtNone.setChecked(true);
		mRgPrior.setOnCheckedChangeListener(this);

		mTvBack.setOnClickListener(this);
		mTvSave.setOnClickListener(this);
		mEtTitle.setOnClickListener(this);
		
		mRlAssign.setOnClickListener(this);
		mRlFinalTime.setOnClickListener(this);
		mRlTag.setOnClickListener(this);
		mRlRepeat.setOnClickListener(this);
		mRlRemind.setOnClickListener(this);
		mRelStartTime.setOnClickListener(this);

		mDeptFragment = new ContactFragment();
		mDeptFragment.setOnSelectContactListner(this);
		mDeptFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE, null);
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction()
				.add(R.id.new_task_assign_fragment, mDeptFragment)
				.hide(mDeptFragment).commitAllowingStateLoss();


	}

	public void initData() {
		mAssignUserList = new ArrayList<UserDetailEntity>();
		mAssignList = new ArrayList<Integer>();
//		requireList = new ArrayList<String>();
		mNameList = new ArrayList<String>();
		mUserList = new ArrayList<String>();
		
		mEditorTaskId = getIntent().getStringExtra(TaskUtil.KEY_TASK_ID);
		mEditorParentTaskId = getIntent().getStringExtra(TaskUtil.KEY_PARENT_TASK_ID);
		mParentTaskId = mEditorParentTaskId;
		if (mEditorTaskId != null && !mEditorTaskId.equals("")) {//为编辑任务时
			mBoolEditTag = true;
			mTvTaskTitle.setText(R.string.edit_task);
			showLoading();
			TaskApi.getInstance().getTask(this, mEditorTaskId);
		}else{ //为新建内容时默认参与者是自己
			mBoolEditTag = false;
			String uTrueName = LauchrConst.header.getUserName();
			String uName = LauchrConst.header.getLoginName();
			mNameList.clear();
			mUserList.clear();
			mNameList.add(uTrueName);
			mUserList.add(uName);
			mTvAssignText.setText(uTrueName);
			UserDetailEntity entity = new UserDetailEntity();
			entity.setName(uName);
			entity.setTrueName(uTrueName);
			mAssignUserList.add(entity);
		}

		if (mParentTaskId == null || mParentTaskId.equals("")) {//是否是子任务
			mParentTaskId = "";
			mRlProject.setOnClickListener(this);
		}else{
			mProjectId = getIntent().getStringExtra(TaskUtil.KEY_CHILD_PROJECT_ID);
			String name = getIntent().getStringExtra(TaskUtil.KEY_CHILD_PROJECT_NAME);
			if(name != null && !name.equals("")){
				mTvTaskTitle.setText(getString(R.string.new_sub_task));
				mTvMainTask.setText(getString(R.string.parent_task));
				mTvProjectText.setText(name);
				mIvProjectRight.setVisibility(View.INVISIBLE);
			}else if(name != null && name.equals("")) {
				mTvProjectText.setText(getString(R.string.calendar_nothing));
			}else{
				mTvProjectText.setText(getResources().getString(R.string.calendar_nothing));
			}
		}
		mCurrentProject = (TaskProjectListEntity)getIntent().getSerializableExtra("CurrentProject");
		if(mCurrentProject != null){//获取所在项目的信息,默认无时间
			mProjectId = mCurrentProject.getShowId();
			mTvProjectText.setText(mCurrentProject.getName());
		}else{ //项目信息为无，时间项目设置默认时间
			mCurrentTime = getIntent().getStringExtra("TimeProject");
			String today = getResources().getString(R.string.task_today);
			String tomorrow = getResources().getString(R.string.task_tomorrow);
			if(mCurrentTime != null){
				if(mCurrentTime.equals(today)){//时间为今天
					Calendar c = Calendar.getInstance();
					Calendar b = Calendar.getInstance();
					b.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					mLStartTime = b.getTimeInMillis();
					mTvStartTime.setText(today);
				}else if(mCurrentTime.equals(tomorrow)){// 时间为明天
					Calendar c = Calendar.getInstance();
					Calendar b = Calendar.getInstance();
					b.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
					mLStartTime = b.getTimeInMillis() + 86400000;
					mTvStartTime.setText(tomorrow);
				}else{
					mTvStartTime.setText(getResources().getString(R.string.calendar_nothing));
				}
			}
		}
		TaskDetailEntity entity = (TaskDetailEntity)getIntent().getSerializableExtra("add_task");
		if(entity != null){
			setDataFromMsg(entity);
		}
	}

	/** 消息转任务*/
	private void setDataFromMsg(TaskDetailEntity entity){
        // 设置标题
		if(entity.gettTitle() != null){
			mEtTitle.setText(entity.gettTitle());
		}

		// 设置项目所属
		if(entity.getpName() != null){
			mProjectId = entity.getpShowId();
			mTvProjectText.setText(entity.getpName());
		}

		// 设置截止时间
		Calendar c = Calendar.getInstance();
		mStartTimeAllDay = entity.getIsStartTimeAllDay();
		mFinalTimeAllDay = entity.getIsEndTimeAllDay();
		if (entity.gettStartTime() != 0) {
			mLStartTime = entity.gettStartTime();
			mLFinalTime = entity.gettEndTime();
			TimeFormatUtil.setAllDayTimeText(this,mStartTimeAllDay, mTvStartTime,mLStartTime);
			TimeFormatUtil.setAllDayTimeText(this,mFinalTimeAllDay, mTvFinalTime,mLFinalTime);
		}

		// 设置分配人员
		if(entity.gettUserName()!=null && entity.gettUser()!=null){
			mNameList.clear();
			mUserList.clear();
			mNameList.add(entity.gettUserName());
			mUserList.add(entity.gettUser());
			UserDetailEntity userEntity = new UserDetailEntity();
			userEntity.setName(entity.gettUser());
			userEntity.setTrueName(entity.gettUserName());
			mAssignUserList.clear();
			mAssignUserList.add(userEntity);
			StringBuilder build = new StringBuilder();
			for (int i = 0; i < mNameList.size(); i++) {
				if (i < 2) {
					String str = mNameList.get(i);
					build.append(str).append("、");
				}else{
					break;
				}
			}
			if (build.length() > 0) {
				build.deleteCharAt(build.length() - 1);
			}

			if (mNameList.size() > 2) {
				build.append("  +" ).append(mNameList.size() - 2);
			}

			mTvAssignText.setText(build.toString());
		}
	}

	private void setData(TaskDetailEntity entity){
		// 设置标题
		mEtTitle.setText(entity.gettTitle());
		
		// 设置备注
		mEtRemark.setText(entity.gettContent());
		
		// 设置项目所属
		mProjectId = entity.getpShowId();
		if(entity.getpName()!=null && !entity.getpName().equals("")){
			mTvProjectText.setText(entity.getpName());
		}else if(entity.getpName()!=null && entity.getpName().equals("")){
			mTvProjectText.setText(getString(R.string.calendar_nothing));
		}
		if(entity!=null && (entity.gettParentShowId()==null || entity.gettParentShowId().equals(""))){
			mRlProject.setOnClickListener(this);
		}else{
			mTvTaskTitle.setText(getString(R.string.task_pop_edit_subtask));
			mTvMainTask.setText(getString(R.string.parent_task));
			mTvProjectText.setText(entity.getParentTask().gettTitle());
			mIvProjectRight.setVisibility(View.INVISIBLE);
			mRlProject.setClickable(false);
		}
		
		// 设置开始结束时间
		Calendar c = Calendar.getInstance();
		mStartTimeAllDay = entity.getIsStartTimeAllDay();
		if (entity.gettStartTime() != 0) {
			mLStartTime = entity.gettStartTime();
			TimeFormatUtil.setAllDayTimeText(this,mStartTimeAllDay,mTvStartTime,mLStartTime);
//			c.setTimeInMillis(entity.gettStartTime());
//			showFinalTime(mStartTimeAllDay,c, mTvStartTime);
		}else{
			mLStartTime = null;
			mTvStartTime.setText(getString(R.string.some_day));
		}
		mFinalTimeAllDay = entity.getIsEndTimeAllDay();
		if(entity.gettEndTime()!=0){
			mLFinalTime = entity.gettEndTime();
			TimeFormatUtil.setAllDayTimeText(this,mFinalTimeAllDay,mTvFinalTime,mLFinalTime);
//			c.setTimeInMillis(entity.gettEndTime());
//			showFinalTime(mFinalTimeAllDay,c, mTvFinalTime);
		}
		
		// 设置优先级
		mProperty = entity.gettPriority();
		if (mProperty.equals(TaskUtil.HIGH_PRIORITY)) {
			mRBtnHigh.setChecked(true);
		}else if (mProperty.equals(TaskUtil.MEDIUM_PRIORITY)) {
			mRbPriorMiddle.setChecked(true);
		}else if (mProperty.equals(TaskUtil.LOW_PRIORITY)) {
			mRbtnLow.setChecked(true);
		}else if(mProperty.equals(TaskUtil.NONE_PRIORITY)){
			mRbtNone.setChecked(true);
		}
		// 设置分配人员
		mNameList.clear();
		mUserList.clear();
		mNameList.add(entity.gettUserName());
		mUserList.add(entity.gettUser());
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < mNameList.size(); i++) {
				String str = mNameList.get(i);
				build.append(str).append("、");
		}
		if (build.length() > 0) {
			build.deleteCharAt(build.length() - 1);
		}

		// 设置提醒事件
		mReminType = entity.gettRemindType();
		for(int i = 0;i < state.length ; i ++ ){
			if(state[i] == mReminType){
				String text = mRemintArray[i];
				mTvRemindText.setText(text);
			}
		}
		mTvAssignText.setText(build.toString());
		// 附件

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			boolean hidden = mDeptFragment.isHidden();
			if(!hidden){
				mFragManger.beginTransaction().hide(mDeptFragment).commitAllowingStateLoss();
			}else{
				this.finish();
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		clearFocus();
		if (v == mTvBack) {
			// 取消
			onBackPressed();
		} else if (v == mTvSave) {
			// 保存
			if (isSuccess()) {
				if (mBoolEditTag) {
					mFileSelectLayoutView.uploadEditImage();
				}else{
					mFileSelectLayoutView.uploadImage();
				}
				MixPanelUtil.sendEvent(this,MixPanelUtil.APP_TASK_SAVE_EVENT);
			}
		} else if (v == mRlProject) {
			chooseProject();
		} else if (v == mRlAssign) {
			// 分配任务
			assigntask();
		} else if (v == mRlFinalTime) {
			// 截止时间操作
			chooseFinalTime();
		} else if (v == mRlTag) {
			// 选择标签
//			Intent intent = new Intent(this, ChooseLabelActivity.class);
//			startActivityForResult(intent, LABEL_CODE);
		} else if (v == mRlRepeat) {
			// 重复操作
			Intent intent = new Intent(this, RestartActivity.class);
			startActivityForResult(intent, REPEAT_CODE);
		} else if (v == mRlRemind) {
			// 提醒操作
			Intent intent = new Intent(this, RemindActivity.class);
			if(mFinalTimeAllDay == 1){
				intent.putExtra("isAllDay",true);
			}else{
				intent.putExtra("isAllDay",false);
			}
			intent.putExtra(RemindActivity.REMIND_INFO,mTvRemindText.getText());
			startActivityForResult(intent, REMIND_CODE);
		} else if (v == mRgPrior) {

		} else if (v == mRelStartTime){
			// 开始时间
			chooseStartTime();
		}
	}
	
	
	
	private void clearFocus(){
		TaskUtil.hideSoftInputWindow(this, mEtTitle);
	}

	// 选择任务所属的项目
	public void chooseProject() {
		Intent intent = new Intent(this, ProjectListActivity.class);
		startActivityForResult(intent, PROJECT_REQ_CODE);
	}

	// 将任务分配给员工
	public void assigntask() {
		// 判断是否是编辑
		mDeptFragment.startReciver(this);
		if (mUserList != null && !mUserList.isEmpty()) {
			mFragManger.beginTransaction().show(mDeptFragment).commitAllowingStateLoss();
			mDeptFragment.setSelectState(ContactFragment.SINGLE_SELECT_STATE, mUserList);
		}else{
			mFragManger.beginTransaction().show(mDeptFragment).commitAllowingStateLoss();
			mDeptFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE, mAssignUserList);//(DeptFragment.EDITOR_SELECT_STATE, mUserList,"");
			if (!mNameList.isEmpty()) {
			}
		}
		
	}


	@Override
	public void onSelectContact(List<UserDetailEntity> userList) {
		mNameList.clear();
		mUserList.clear();
		selectRequireUser(userList);
	}

	private void selectRequireUser(List<UserDetailEntity> userList) {
		if (userList != null) {
			mAssignUserList.clear();
			mAssignUserList.addAll(userList);
			// 显示操作
			setText(mTvAssignText, mAssignUserList);
		}
	}

	/**
	 * 设置显示要参加的人
	 * 
	 * @param textView
	 * @param userList
	 */
	private void setText(TextView textView, List<UserDetailEntity> userList) {
		StringBuilder build = new StringBuilder();
		List<String> name = new ArrayList<>();
		for (int i = 0; i < userList.size(); i++) {
				UserDetailEntity en = userList.get(i);
				build.append(en.getTrueName()).append("、");
				name.add(en.getTrueName());
		}
		if (build.length() != 0) {
			build.deleteCharAt(build.length() - 1);
		}
			textView.setText(build.toString());
	}

	@Override
	public void onBackPressed() {
		boolean hidden = mDeptFragment.isHidden();
		if (hidden) {
			ContactFragment.mSelectState = 1;
			MixPanelUtil.sendEvent(this,MixPanelUtil.APP_TASK_CANCEL_EVENT);
			super.onBackPressed();
		} else {
			mFragManger.beginTransaction().hide(mDeptFragment).commitAllowingStateLoss();
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
		initTimeDialog(getString(R.string.choose_start_time));
		mTimeDialog.setStartTime(mLStartTime);
		if(mStartTimeAllDay == 1){
			mTimeDialog.setAllDay(true);
		}else{
			mTimeDialog.setAllDay(false);
		}


//		final FinalTimeDialog finalTime = new FinalTimeDialog(this, getString(R.string.choose_start_time));
//		finalTime.show();
//		finalTime.setStartTime(mLStartTime);
//
//		Window window = finalTime.getWindow();
//		final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//		OnClickListener listener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (v == finalOk) {
//					mLStartTime = finalTime.getTime().getTimeInMillis();
////					showFinalTime(mStartTimeAllDay, finalTime.getTime(), mTvStartTime);
//					setTime(mStartTimeAllDay,mLStartTime,mLFinalTime);
//					finalTime.dismiss();
//				}
//			}
//		};
//		finalOk.setOnClickListener(listener);
//		final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//		finalSetting.setText(getString(R.string.some_day));
//		finalSetting.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mTvStartTime.setText(getString(R.string.some_day));
//				mLStartTime = null;
//				finalTime.dismiss();
//			}
//		});
//		final CheckBox startAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//		if(mStartTimeAllDay == 1){
//			startAllDay.setChecked(true);
//			finalTime.setAllDay(true);
//		}else{
//			startAllDay.setChecked(false);
//			finalTime.setAllDay(false);
//		}
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

		initTimeDialog(getString(R.string.choose_stop_time));
		mTimeDialog.setEndTime(mLFinalTime);
		if(mFinalTimeAllDay == 1){
			mTimeDialog.setAllDay(true);
		}else{
			mTimeDialog.setAllDay(false);
		}



//		final FinalTimeDialog finalTime = new FinalTimeDialog(this, getString(R.string.choose_stop_time));
//		finalTime.show();
//		if(mLFinalTime != null ){
//			finalTime.setEndTime(mLFinalTime);
//		}
//		Window window = finalTime.getWindow();
//		final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//		OnClickListener listener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (v == finalOk) {
//					mLFinalTime = finalTime.getTime().getTimeInMillis();
//						if(mLStartTime > mLFinalTime){
//							toast(getString(R.string.end_time_must_not_be_later_than_begin_time));
//							return;
//						}
//					setTime(mFinalTimeAllDay,mLStartTime,mLFinalTime);
////					showFinalTime(mFinalTimeAllDay,finalTime.getTime(), mTvFinalTimeText);
//					finalTime.dismiss();
//				}
//			}
//		};
//		finalOk.setOnClickListener(listener);
//		final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//		finalSetting.setText(getString(R.string.no_set_time));
//		finalSetting.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mTvFinalTime.setText(getString(R.string.no_set_time));
//				mLFinalTime = null;
//				finalTime.dismiss();
//			}
//		});
//		final CheckBox finalAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//		if(mFinalTimeAllDay == 1){
//			finalAllDay.setChecked(true);
//			finalTime.setAllDay(true);
//		}else{
//			finalAllDay.setChecked(false);
//			finalTime.setAllDay(false);
//		}
//		finalAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked){
//					finalTime.setAllDay(isChecked);
//					mFinalTimeAllDay = 1;
//				}else{
//					finalTime.setAllDay(isChecked);
//					mFinalTimeAllDay = 0;
//				}
//			}
//		});
	}

	// 判断截止时间是否晚于当前时间
	public boolean isAfterCurrent() {
		return false;
	}

	// 显示时间
//	public void showFinalTime(int allDay,Calendar calendar, TextView textView) {
//		int m = calendar.get(Calendar.MONTH) + 1;
//		int d = calendar.get(Calendar.DAY_OF_MONTH);
//		int h = calendar.get(Calendar.HOUR_OF_DAY);
//		int mm = calendar.get(Calendar.MINUTE);
//		if(calendar.getTimeInMillis() == 0L){
//			textView.setText(getString(R.string.some_day));
//		}else if (allDay == 1) {
//			textView.setText(m + "/" + d);
//		}else{
//			textView.setText(m + "/" + d + " " + h + ":" + mm);
//		}
//
//	}
//	public void setTime(int allDay,Long startTime,Long finaTime){
//
//		SimpleDateFormat dateFormat;
//		mLStartTime = startTime;
//		mLFinalTime = finaTime;
//		if (allDay == 1) {
//			mStartTimeAllDay = 1;
//			mFinalTimeAllDay = 1;
//			dateFormat = new SimpleDateFormat("MM/dd");
//			if(mLFinalTime != null){
//				mTvFinalTime.setText(dateFormat.format(mLFinalTime));
//			}else{
//				mTvFinalTime.setText(getString(R.string.no_set_time));
//			}
//			if(mLStartTime != null){
//				mTvStartTime.setText(dateFormat.format(mLStartTime));
//			}else{
//				mTvStartTime.setText(getString(R.string.no_set_time));
//			}
//		}else{
//			mStartTimeAllDay = 0;
//			mFinalTimeAllDay = 0;
//			dateFormat = new SimpleDateFormat("MM/dd HH:mm");
//			if(mLFinalTime != null){
//				mTvFinalTime.setText(dateFormat.format(mLFinalTime));
//			}else{
//				mTvFinalTime.setText(getString(R.string.no_set_time));
//			}
//			if(mLStartTime != null){
//				mTvStartTime.setText(dateFormat.format(mLStartTime));
//			}else{
//				mTvStartTime.setText(getString(R.string.no_set_time));
//			}
//		}
//	}

	//
	public void chooseTag() {

	}

	// 选择附件
	public void chooseAttach(View view) {
		SelectPicTypeWindowView popWindow = new SelectPicTypeWindowView(this);
		popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	// 保存新建的任务
	public void saveTask(List<String> pic) {
		// 获取标题
		String title = mEtTitle.getText().toString().trim();
		
		// 必须参加会议的
		StringBuilder requireBuilder = new StringBuilder();
		StringBuilder requireName = new StringBuilder();
//		if (requireList.isEmpty()) {
			if (!mAssignUserList.isEmpty()) {
				for (int i = 0; i < mAssignUserList.size(); i++) {
					UserDetailEntity entity = mAssignUserList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					requireBuilder.append(str).append("●");
					requireName.append(name).append("●");
				}
				if (requireBuilder.length() > 0) {
					requireBuilder.deleteCharAt(requireBuilder.length() - 1);
					requireName.deleteCharAt(requireName.length() - 1);
				}
			}
//		}	
		
		String content = mEtRemark.getText().toString().trim();

		showLoading();
		TaskApi.getInstance().newTask(this, mProjectId, title, content, mLStartTime, mLFinalTime, mStartTimeAllDay, mFinalTimeAllDay,
				requireBuilder.toString(), requireName.toString(), mProperty, mReminType, mEditorParentTaskId, pic);
	}
	
	/**
	 * 保存编辑任务
	 */
	public void saveEditor(List<String> pic){
		// 获取标题
		String title = mEtTitle.getText().toString().trim();
		
		// 必须参加会议的
		StringBuilder requireBuilder = new StringBuilder();
		StringBuilder requireName = new StringBuilder();
		if (mNameList.isEmpty()) {
//			if (requireList.isEmpty()) {
			if (!mAssignUserList.isEmpty()) {
				for (int i = 0; i < mAssignUserList.size(); i++) {
					UserDetailEntity entity = mAssignUserList.get(i);
					String str = entity.getName();
					String name = entity.getTrueName();
					requireBuilder.append(str).append("●");
					requireName.append(name).append("●");
				}
				if (requireBuilder.length() > 0) {
					requireBuilder.deleteCharAt(requireBuilder.length() - 1);
					requireName.deleteCharAt(requireName.length() - 1);
				}
			}
//			}	
		}else{
			for (int i = 0; i < mNameList.size(); i++) {
				String name = mNameList.get(i);
				String user = mUserList.get(i);
				requireBuilder.append(user).append("●");
				requireName.append(name).append("●");
			}
			if (requireBuilder.length() > 0) {
				requireBuilder.deleteCharAt(requireBuilder.length() - 1);
				requireName.deleteCharAt(requireName.length() - 1);
			}
		}
		showLoading();
		String content = mEtRemark.getText().toString().trim();
		String projectId = null;
		if(mProjectId != null && !mTvProjectText.getText().toString().equals(getString(R.string.calendar_nothing))){
			projectId = mProjectId;
		}else if(mProjectId==null && mTvProjectText.getText().toString().equals(getString(R.string.calendar_nothing))) {
			projectId = null;
		}else{
			projectId = mEntity.getpShowId();
		}
		TaskApi.getInstance().updateTask(this, mEditorTaskId, projectId, title, content, mLStartTime, mLFinalTime, mStartTimeAllDay, mFinalTimeAllDay,
				requireBuilder.toString(), requireName.toString(), mProperty, mReminType, pic);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			 super.onActivityResult(requestCode, resultCode, data);
			 return;
		}
		switch (requestCode) {
		case REPEAT_CODE:
			if (data != null) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString(RestartActivity.RESTART_INFO);
				mTvRepeatText.setText(result + "");
				mRestartType = bundle.getInt(RestartActivity.REPEATTYPE, 0);
			}
			break;
		case REMIND_CODE:
			if (data != null) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString(RemindActivity.REMIND_INFO);
				mReminType = bundle.getInt(RemindActivity.REMINDTYPE, 0);
				mTvRemindText.setText(result + "");
			}
			break;
		case TAKE_PICTURE:
			takePictureResult(resultCode);
			break;
		case PREVIEW_PIC:
				if(data != null){
					List<String> pic = data.getStringArrayListExtra(GalleryActivity.KEY_PHOTO_URL);
					mFileSelectLayoutView.setImageStrList(pic);
					mFileSelectLayoutView.updateAdapter();
				}else{
					mFileSelectLayoutView.setImageStrList(null);
					mFileSelectLayoutView.updateAdapter();
				}
			break;
		case GET_ALBUM:
			List<String> list = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
			if (list != null) {
				mFileSelectLayoutView.setImageStrList(list);
				mFileSelectLayoutView.updateAdapter();
			}
			break;
		
		case PROJECT_REQ_CODE:
			// 是否是选择项目返回
			TaskProjectListEntity entity = (TaskProjectListEntity) data.getSerializableExtra(TaskUtil.KEY_TASK_PROJECT);
			if (entity != null && entity.getShowId()!=null) {
				mProjectId = entity.getShowId();
				mTvProjectText.setText(entity.getName());
			}else{
				mProjectId = null;
				mTvProjectText.setText(entity.getName());
			}
			break;
		case LABEL_CODE:
				break;
		default:
			break;
		}	
	}
	/** 拍照返回*/
	private void takePictureResult(int resultCode) {

		if (resultCode == RESULT_OK ) {
			if(!"".equals(SelectPicTypeWindowView.mStrPicPath)){
				mFileSelectLayoutView.addImageStr(SelectPicTypeWindowView.mStrPicPath);
				mFileSelectLayoutView.updateAdapter();
			}else{
				toast(getString(R.string.photo_unget_photo));
			}
		}
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		// 判断是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}else
		
		// 判断是否是新建任务返回
		if (taskId.equals(TaskId.TASK_URL_ADD_TASK)) {
			handleAddTaskResult(response);
			dismissLoading();
		}else
			
		// 判断是否是获取任务详情返回	
		if(taskId.equals(TaskId.TASK_URL_GET_TASK)){
			handleTaskDetail(response);
			dismissLoading();
		}else
			
		// 判断是否是更新任务返回	
		if (taskId.equals(TaskId.TASK_URL_UPDATE_TASK)) {
			handleAddTaskResult(response);
			dismissLoading();
		}

		//加载附件
		else if(taskId.equals(AttachmentApi.TaskId.getList)){
			AttachmentListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AttachmentListPOJO.class);
			handleAttachmentList(pojo);
		}
	}

	/**
	 * 获取附件
	 */
	private void handleAttachmentList(AttachmentListPOJO pojo) {
		if (pojo == null) {
			showNetWorkGetDataException();
			return;
		} else if (pojo.getBody() == null) {
			toast(pojo.getHeader().getReason());
			return;
		} else if (!pojo.getBody().getResponse().isIsSuccess()) {
			toast(pojo.getBody().getResponse().getReason());
			return;
		} else {
			List<AttachmentListEntity> attachmentListEntities = pojo.getBody().getResponse().getDate();
			if (attachmentListEntities != null && attachmentListEntities.size() > 0) {

//				// 附件地址存放
				mPicUrl = attachmentListEntities;
				List<String> strPicUrl = new ArrayList<>();
				for(AttachmentListEntity entity : mPicUrl){
					strPicUrl.add(entity.getPath());
				}
				mFileSelectLayoutView.setImageStrList(strPicUrl);
			}
		}
	}
	
	/**
	 * 处理更新任务返回
	 * @param response
	 */
	private void handleUpdateTaskResult(Object response){
		
	}
	
	
	
	/**
	 * 处理任务详情返回
	 * @param response
	 */
	private void handleTaskDetail(Object response){
		TaskDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),TaskDetailPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				mEntity = pojo.getBody().getResponse().getData();
				if (mEntity != null) {
					setData(mEntity);
					if(mEntity.gettIsAnnex() == 1){
						AttachmentApi.getInstance().getAttachmentList(this, mEntity.getShowId(), TASK_APP_KEY);
					}

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
	
	
	/**
	 * 处理新建任务返回
	 * @param response
	 */
	private void handleAddTaskResult(Object response){
		NewTaskPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NewTaskPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				NewTaskEntity entity = pojo.getBody().getResponse().getData();
				if (entity != null) {
					newTaskSuccessBack();
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

	/** 新建成功后返回数据要进行处理*/
	private void newTaskSuccessBack(){
		Intent intent = new Intent();
		// 从项目进入，有项目
		if(mCurrentProject!=null && mProjectId!=null){
			intent.putExtra("result_project_name", mTvProjectText.getText().toString());
			intent.putExtra("result_project_id", mProjectId);
		}else{
			// 从项目进入，有开始时间，无开始时间，我发出的
			String user = AppUtil.getInstance(this).getLoginName();
			if(mAssignUserList!=null && mAssignUserList.size()>0 && user.equals(mAssignUserList.get(0).getName())){
				Calendar c = Calendar.getInstance();
				if(mLStartTime != null){
					c.setTimeInMillis(mLStartTime);
				}
				Calendar b = Calendar.getInstance();
				if(mLStartTime == null){
					intent.putExtra("result_project_position", 3);
					intent.putExtra("result_project_title", getString(R.string.task_one_day));
					intent.putExtra("result_project_type", 3);
				}else if(c.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)){
					intent.putExtra("result_project_position", 0);
					intent.putExtra("result_project_title", getString(R.string.task_today));
					intent.putExtra("result_project_type", 1);
				}else if(c.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)+1){
					intent.putExtra("result_project_position", 1);
					intent.putExtra("result_project_title", getString(R.string.task_tomorrow));
					intent.putExtra("result_project_type", 2);
				}else{
					intent.putExtra("result_project_position", 2);
					intent.putExtra("result_project_title", getString(R.string.task_all));
					intent.putExtra("result_project_type", 4);
				}
			}else{
				intent.putExtra("result_project_position", 4);
				intent.putExtra("result_project_title", getString(R.string.task_my_send));
				intent.putExtra("result_project_type", 6);
			}
		}

		// 从时间进入，有开始时间，无开始时间
		if(mCurrentTime != null){
			String user = AppUtil.getInstance(this).getLoginName();
			if(mAssignUserList!=null && user.equals(mAssignUserList.get(0).getName())){
				Calendar c = Calendar.getInstance();
				if(mLStartTime != null) {
					c.setTimeInMillis(mLStartTime);
				}
				Calendar b = Calendar.getInstance();
				if(mLStartTime == null){
					intent.putExtra("result_project_position", 3);
					intent.putExtra("result_project_title", getString(R.string.task_one_day));
					intent.putExtra("result_project_type", 3);
				}else if(c.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)){
					intent.putExtra("result_project_position", 0);
					intent.putExtra("result_project_title", getString(R.string.task_today));
					intent.putExtra("result_project_type", 1);
				}else if(c.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)+1){
					intent.putExtra("result_project_position", 1);
					intent.putExtra("result_project_title", getString(R.string.task_tomorrow));
					intent.putExtra("result_project_type", 2);
				}else{
					intent.putExtra("result_project_position", 2);
					intent.putExtra("result_project_title", getString(R.string.task_all));
					intent.putExtra("result_project_type", 4);
				}
			}else{
				intent.putExtra("result_project_position", 4);
				intent.putExtra("result_project_title", getString(R.string.task_my_send));
				intent.putExtra("result_project_type", 6);
			}
		}
		// 从时间进入，我发出的

		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rbtn_high) {
			mProperty = TaskUtil.HIGH_PRIORITY;
		}else if (checkedId == R.id.rbtn_medium) {
			mProperty = TaskUtil.MEDIUM_PRIORITY;
		}else if (checkedId == R.id.rbtn_low) {
			mProperty = TaskUtil.LOW_PRIORITY;
		}else if(checkedId == R.id.rbtn_none){
			mProperty = TaskUtil.NONE_PRIORITY;
		}
	}
	
	
	private boolean isSuccess(){
		// 判断标题是否为空
		String title = mEtTitle.getText().toString().trim();
		if (title.equals("")) {
			String strTitle = getResources().getString(R.string.title_empty);
			toast(strTitle);
			return false;
		}else
		
		// 判断项目是否为空	
//		if (mProjectId.equals("")) {
//			String strTitle = getResources().getString(R.string.project_empty);
//			toast(strTitle);
//			return false;
//		}else
			
		// 判断截止时间是否为空	
//		if (mFinalTimeMills  < 0) {
//			String strTitle = getResources().getString(R.string.final_time_empty);
//			toast(strTitle);
//			return false;
//		}
		//else
			
		// 判断项目参与人是否为空	
		if (mAssignUserList.isEmpty() && mNameList.isEmpty()) {
			toast(getString(R.string.enter_people_not_void));
			return false;
		}
		return true;
		
	}

	private int[] state = { 0, 100, 101, 102, 103, 104, 105, 106, 107, 108,
			200, 201, 202, 203 };
	private String[] mRemintArray;

	private void initArrays(){
		mRemintArray = new String[]{
				getResources().getString(R.string.calendar_not_remind),
				getResources().getString(R.string.calendar_before_5),
				getResources().getString(R.string.calendar_before_10),
				getResources().getString(R.string.calendar_before_15),
				getResources().getString(R.string.calendar_before_30),
				getResources().getString(R.string.calendar_before_one_hour),
				getResources().getString(R.string.calendar_before_two_hour),
				getResources().getString(R.string.calendar_the_day),
				getResources().getString(R.string.calendar_before_two_week),
				getResources().getString(R.string.calendar_before_one_week),

				getResources().getString(R.string.calendar_the_day),
				getResources().getString(R.string.calendar_before_one_day),
				getResources().getString(R.string.calendar_before_two_day),
				getResources().getString(R.string.calendar_before_one_week)};
	}

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
	private void initTimeDialog(String title){
		if(mTimeDialog == null){
			mTimeDialog = new FinalTimeDialog(this,title);
			mTimeDialog.setGetTimeListener(this);
			mTimeDialog.show();
		}else{
			mTimeDialog.show();
			mTimeDialog.setCustomTitle(title);
		}
	}
	
}
