package com.mintcode.launchr.app.newTask.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.cache.CacheManager;
import com.mintcode.cache.MD5;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.AttachmentApi;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.api.TaskApi.TaskId;
import com.mintcode.launchr.app.CommentsListView;
import com.mintcode.launchr.app.CommentsView;
import com.mintcode.launchr.app.meeting.view.HandleMeetingPopWindow;
import com.mintcode.launchr.app.meeting.MeetingDelectDialog;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.view.EditTaskPopWindow;
import com.mintcode.launchr.app.newTask.view.TaskEditWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.AttachmentListPOJO;
import com.mintcode.launchr.pojo.TaskDetailPOJO;
import com.mintcode.launchr.pojo.TaskPOJO;
import com.mintcode.launchr.pojo.TaskStatePOJO;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.pojo.entity.TaskDetailEntity;
import com.mintcode.launchr.pojo.entity.TaskStateEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.DialogHintUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TaskDetailActivity extends BaseActivity implements EditTaskPopWindow.TaskPopWindowListener,CommentsView.SendMessageListener
																	,OnItemClickListener,OnItemLongClickListener{
	
	public static final int ADD_CHILDTASK_REQ_CODE = 0x12;
	
	public static final int EDITOR_REQ_CODE = 0x13;

	/** 添加子任务 */
	private ImageView mIvAddTask;
	/** 添加子任务 */
	private RelativeLayout mRelAddSubTask;
	/** 任务标题*/
	private TextView mTvTitle;
	/** 等级*/
	private TextView mTvLevel;
	/** 姓名*/
	private TextView mTvName;
	/** 最后期限*/
	private TextView mTvDeadline;
	/** 循环时间*/
	private TextView mTvCycle;
	/** 提醒*/
	private TextView mTvRemind;
	private RelativeLayout mRelRemind;
	private ImageView mIvRemind;
	/** 任务已完成*/
	private CheckBox mCbFinish;
	/** 任务名 */
	private TextView mTvProjectName;
	/** 备份内容 */
	private TextView mTvContent;
	private ImageView mIvContentLine;
	/** 子任务个数 */
	private TextView mSubTasksSum;
	/** 头像 */
	private ImageView mIvHeadIcon;
	private TextView mTvListTitle;

	private ImageView mIvTasksSumExpand;
	/** 附件*/
	private GridView mGvPhoto;
	private GridAdapter mGridAdapter;
	/** 子任务列表 */
	private ListView mLvSubTask;
	/** 编辑任务窗口*/
	private EditTaskPopWindow popWindow;

	/** 子任务详情*/
	private TaskDetailEntity mTaskDetailEntity;
	/** 父任务详情*/
	private TaskDetailEntity mParentEntity;
	private LayoutInflater inflater;
	/** 子任务*/
	private List<TaskDetailEntity.TaskDetail> mTaskChildList;
	/** 任务的ID*/
	private String mTaskId = "";
	/** 父任务ID*/
	private String mParentTaskId = "";
	private boolean mTasksSumExpand = true;
	private int number = 0;
	private List<AttachmentListEntity> mPicUrl;
	private SubTaskAdapter mSubTaskAdapter;

	public static final String KEY_TASK_ID = "TaskDetailActivity_id";
	public static final String KEY_TASK_PARENT_ID = "key_task_parent_id";
	/** app的showid*/
	public static final String TASK_APP_KEY = Const.SHOWID_TASK;

	/** 是否是父任务*/
	private boolean mIsParent = true;



	/** 更多操作*/
	private ImageView mIvMoreHandle;
	private HandleMeetingPopWindow mHandleTaskPopWindow;
	/** 附件*/
	private RelativeLayout mRelAttach;
	/** 所属项目*/
	private RelativeLayout mRlProject;
	/** 评论列表*/
	private CommentsListView mCommentsList;
	/** 评论框*/
	private CommentsView mCommentView;

	private String[] mRemintArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		initView();	
		initData();
		
	}

	private void initData() {
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
				getResources().getString(R.string.calendar_not_remind),
				getResources().getString(R.string.calendar_the_day),
				getResources().getString(R.string.calendar_before_one_day),
				getResources().getString(R.string.calendar_before_two_day),
				getResources().getString(R.string.calendar_before_one_week)};
		mTaskId = getIntent().getStringExtra(KEY_TASK_ID);
		mParentTaskId = getIntent().getStringExtra(KEY_TASK_PARENT_ID);
		if (mParentTaskId != null && !mParentTaskId.equals("")) {
			mIsParent = false;
		}else{
			mIsParent = true;
		}
		accessTask(mTaskId);
	}

	private void accessTask(String taskId){
		showLoading();
		TaskApi.getInstance().getTask(this,taskId); 
	}
	
	private void initView() {
		mPicUrl = new ArrayList<AttachmentListEntity>();
		inflater = LayoutInflater.from(this);
		mIvAddTask = (ImageView) findViewById(R.id.iv_add);
		mTvListTitle = (TextView) findViewById(R.id.tv_child_list);
				
		mGvPhoto = (GridView) findViewById(R.id.gv_task_photo);
		mLvSubTask = (ListView) findViewById(R.id.subtask);

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvLevel = (TextView) findViewById(R.id.tv_level);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvDeadline = (TextView) findViewById(R.id.tv_time);
		mTvCycle = (TextView) findViewById(R.id.tv_cycle);
		mTvProjectName = (TextView) findViewById(R.id.tv_projet_name);
		mTvContent = (TextView) findViewById(R.id.tv_content);
		mSubTasksSum = (TextView) findViewById(R.id.tv_taskdetail_subtasks);
		mIvTasksSumExpand = (ImageView) findViewById(R.id.iv_taskdetail_subtasks);
		mIvMoreHandle = (ImageView) findViewById(R.id.iv_more_handle);
		mTvRemind = (TextView)findViewById(R.id.tv_remind_text);
		mRelRemind = (RelativeLayout) findViewById(R.id.rel_task_remind);
		mCbFinish = (CheckBox) findViewById(R.id.finish_this_task);
		mCbFinish.setOnCheckedChangeListener(checkedChangeListener);
		mIvContentLine = (ImageView) findViewById(R.id.iv_content_line);
		mRelAttach = (RelativeLayout) findViewById(R.id.rel_attch_photo);
		mRlProject = (RelativeLayout) findViewById(R.id.rl_project_layout);
		mIvRemind = (ImageView) findViewById(R.id.line_remind);
		mCommentsList = (CommentsListView)findViewById(R.id.layout_comments_list);
		mCommentView = (CommentsView)findViewById(R.id.layout_comments);
		mCommentView.setMessageLisener(this);

		mRelAddSubTask = (RelativeLayout) findViewById(R.id.rel_add_sub_task);
		
		// 头像
		mIvHeadIcon = (ImageView) findViewById(R.id.iv_head);
		mIvTasksSumExpand.setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);

		mGridAdapter = new GridAdapter();
		mSubTaskAdapter = new SubTaskAdapter(getApplicationContext());

		mGvPhoto.setAdapter(mGridAdapter);
		mLvSubTask.setAdapter(mSubTaskAdapter);
		mLvSubTask.setOnItemClickListener(this);
		mLvSubTask.setOnItemLongClickListener(this);
		mGvPhoto.setOnItemClickListener(this);
		mIvMoreHandle.setOnClickListener(this);
		
		popWindow = new EditTaskPopWindow(getApplicationContext());
		popWindow.setTaskPopWindowListener(this);

		mIvAddTask.setOnClickListener(this);
		mRelAddSubTask.setOnClickListener(this);
	}

	/** 点击任务完成按钮*/
	private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(mTaskDetailEntity!=null && mTaskDetailEntity.getsType().equals("WAITING")){
				showLoading();
				TaskApi.getInstance().changeTaskState(TaskDetailActivity.this, mTaskDetailEntity.getShowId(), "FINISH");
			}else{
				showLoading();
				TaskApi.getInstance().changeTaskState(TaskDetailActivity.this, mTaskDetailEntity.getShowId(), "WAITING");
			}
		}
	};
	

	private void getEntityIntoContent(TaskDetailEntity taskDetailEntity) {
		//标题
		if(taskDetailEntity.getsType() != null && "WAITING".equals(taskDetailEntity.getsType())){
			mCbFinish.setSelected(false);
			mTvTitle.setTextColor(getResources().getColor(R.color.black));
			mTvTitle.getPaint().setFlags(0);
			mTvTitle.getPaint().setAntiAlias(true);
			mTvTitle.setText(taskDetailEntity.gettTitle());
		}else{
			mCbFinish.setSelected(true);
			mTvTitle.setTextColor(getResources().getColor(R.color.gray_launchr));
			mTvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			mTvTitle.getPaint().setAntiAlias(true);
			mTvTitle.setText(taskDetailEntity.gettTitle());
		}
		
		//设置优先级
		String Rank = taskDetailEntity.gettPriority();
		int textColor = -1;
		mTvLevel.setVisibility(View.VISIBLE);
		if(Rank.equals(TaskUtil.LOW_PRIORITY)){
			mTvLevel.setText(getString(R.string.task_rank_low));
			textColor = getResources().getColor(R.color.task_priority_grey);
			mTvLevel.setTextColor(textColor);
			mTvLevel.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.shape_round_grey), null, null, null);
		}else if(Rank.equals(TaskUtil.MEDIUM_PRIORITY)){
			mTvLevel.setText(getString(R.string.task_rank_medium));
			textColor = getResources().getColor(R.color.task_priority_yellow);
			mTvLevel.setTextColor(textColor);
			mTvLevel.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.shape_round_yellow),null,  null, null);
		}else if(Rank.equals(TaskUtil.HIGH_PRIORITY)){
			mTvLevel.setText(getString(R.string.task_rank_high));
			textColor = getResources().getColor(R.color.task_priority_red);
			mTvLevel.setTextColor(textColor);
			mTvLevel.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.shape_round_red), null, null, null);
		}else{
			mTvLevel.setVisibility(View.INVISIBLE);
		}
		
		mTvName.setText(taskDetailEntity.gettUserName());
		
		//最后期限
		if (taskDetailEntity.gettStartTime() != 0 && taskDetailEntity.gettEndTime() != 0) {
			mTvDeadline.setText(TimeFormatUtil.setRemindTimeText(taskDetailEntity.gettStartTime()) + "~" + TimeFormatUtil.setRemindTimeText(taskDetailEntity.gettEndTime()));
		}else if(taskDetailEntity.gettStartTime() != 0 && taskDetailEntity.gettEndTime() == 0) {
			mTvDeadline.setText(getString(R.string.calendar_timepicker_starttime) + TimeFormatUtil.setRemindTimeText(taskDetailEntity.gettStartTime()));
		}else if(taskDetailEntity.gettStartTime() == 0 && taskDetailEntity.gettEndTime() != 0) {
			mTvDeadline.setText(getString(R.string.end_time) + TimeFormatUtil.setRemindTimeText(taskDetailEntity.gettEndTime()));
		}else{
			mTvDeadline.setText(getString(R.string.no_start_end));
		}
		
		//项目名称
		String projectName = taskDetailEntity.getpName();
		if("".equals(projectName) || projectName == null){
			mRlProject.setVisibility(View.VISIBLE);
			mTvProjectName.setText(getString(R.string.calendar_nothing));
		}else{
			mRlProject.setVisibility(View.VISIBLE);
			mTvProjectName.setText(projectName);
		}
		//标签
		if(taskDetailEntity.gettContent()!=null && !"".equals(taskDetailEntity.gettContent())){
			mIvContentLine.setVisibility(View.VISIBLE);
			mTvContent.setVisibility(View.VISIBLE);
			mTvContent.setText(taskDetailEntity.gettContent());
		}else{
			mTvContent.setVisibility(View.GONE);
			mIvContentLine.setVisibility(View.GONE);
		}
		//循环

		// 重复功能已去掉
		int reapect = 5;
		if(reapect == 1){
			mTvCycle.setText(getString(R.string.task_restart_everyday));
		}else if(reapect == 2){
			mTvCycle.setText(getString(R.string.task_restart_everyweek));
		}else if(reapect == 3){
			mTvCycle.setText(getString(R.string.task_restart_everymonth));
		}else if(reapect == 4){
			mTvCycle.setText(getString(R.string.task_restart_everyyear));
		}else{
			mTvCycle.setVisibility(View.INVISIBLE);
		}
		
		mTaskChildList = taskDetailEntity.getChildTasks();
		mSubTaskAdapter.notifyDataSetChanged();
		if(mTaskChildList != null){
			mSubTasksSum.setText(mTaskChildList.size()+"");
		}
		//是否为父任务，显示子任务列表
		String str;
		if (mIsParent) {
			mIvAddTask.setVisibility(View.VISIBLE);
			str = getString(R.string.add_sub_task);
		}else{
			mIvAddTask.setVisibility(View.GONE);
			str = getString(R.string.parent_task);
			mParentTaskId = taskDetailEntity.gettParentShowId();
		}
		mTvListTitle.setText(str); 
		
		// 设置头像
		HeadImageUtil.getInstance(this).setAvatarResourceAppendUrl(mIvHeadIcon,taskDetailEntity.gettUser(),0,50,50);

		// 提醒
		if(mTaskDetailEntity.gettRemindTime() != 0){
			mIvRemind.setVisibility(View.VISIBLE);
			mRelRemind.setVisibility(View.VISIBLE);
			int type;
			if(mTaskDetailEntity.getIsEndTimeAllDay() == 1){
				type = mTaskDetailEntity.gettRemindType()%200;
			}else{
				type = mTaskDetailEntity.gettRemindType()%100;
			}
			mTvRemind.setText(mRemintArray[type]);
		}else{
			mIvRemind.setVisibility(View.GONE);
			mRelRemind.setVisibility(View.GONE);
		}

		String createUserName = AppUtil.getInstance(TaskDetailActivity.this).getLoginName();
		if(taskDetailEntity.gettUser().equals(createUserName)){
			mIvMoreHandle.setVisibility(View.VISIBLE);
		}else if(taskDetailEntity.getCreateUser().equals(createUserName)){
			mIvMoreHandle.setVisibility(View.VISIBLE);
		}else{
			mIvMoreHandle.setVisibility(View.GONE);
		}
		mCommentsList.setAppInfo(TASK_APP_KEY,mTaskDetailEntity.getShowId());
		List<String> toUsers = new ArrayList<String>();
		List<String> toUserNames = new ArrayList<String>();
		toUsers.add(mTaskDetailEntity.gettUser());
		toUserNames.add(mTaskDetailEntity.gettUserName());
		mCommentView.setAppInfo(TASK_APP_KEY,mTaskDetailEntity.getShowId(),mTaskDetailEntity.gettTitle(),mTaskDetailEntity.gettIsComment(),toUsers,toUserNames);
	}



	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_back) {
			onBackPressed();
		}else if (v.getId() == R.id.iv_taskdetail_subtasks) {
//			expandSubTasks();
		}if (v == mRelAddSubTask) {
			if(mIsParent){ //父任务添加子任务
				Intent intent = new Intent(this, NewTaskActivity.class);
				intent.putExtra(TaskUtil.KEY_PARENT_TASK_ID, mTaskDetailEntity.getShowId());
				intent.putExtra(TaskUtil.KEY_CHILD_PROJECT_ID, mTaskDetailEntity.getpShowId());
				intent.putExtra(TaskUtil.KEY_CHILD_PROJECT_NAME, mTaskDetailEntity.gettTitle());
				intent.putExtra("TimeProject", TaskMainActivity.mTimeName);
				startActivityForResult(intent, ADD_CHILDTASK_REQ_CODE);
			}else{ // 子任务跳到父任务
				mIsParent = true;
				accessTask(mParentTaskId);
			}

		}else if(v == mIvMoreHandle){
			doMoreHandle();
		}
	}


	/** 显示删除、编辑、分享按钮*/
	private void doMoreHandle(){
		mHandleTaskPopWindow = new HandleMeetingPopWindow(TaskDetailActivity.this);
		mHandleTaskPopWindow.showAsDropDown(mIvMoreHandle);

		View window = mHandleTaskPopWindow.getContentView();
		TextView handleEdit = (TextView)window.findViewById(R.id.tv_meeting_edit);
		handleEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editorTask();
				mHandleTaskPopWindow.dismiss();
			}
		});
		TextView handleDelete = (TextView)window.findViewById(R.id.tv_meeting_delete);
		View view = window.findViewById(R.id.view_line);
		String name = AppUtil.getInstance(TaskDetailActivity.this).getLoginName();
		if(!mTaskDetailEntity.getCreateUser().equals(name) && mTaskDetailEntity.gettUser().equals(name)){
			handleDelete.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
		}
		handleDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogHintUtil.delectEvent(TaskDetailActivity.this);
				mHandleTaskPopWindow.dismiss();
			}
		});

		TextView handleSendOther = (TextView) window.findViewById(R.id.tv_meeting_send);
		handleSendOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHandleTaskPopWindow.dismiss();
			}
		});
	}
		
	/**
	 * 编辑任务
	 */
	private void editorTask(){
		if (mTaskDetailEntity != null) {
//			for(int i = 0;i < mPicUrl.size();i++){
//				Bitmap bimp = CacheManager.getInstance(getApplicationContext()).getFileImage(mPicUrl.get(i));
//				String md5Key = MD5.getMD5Str(mPicUrl.get(i));
//				//TODO 图片编辑
//			}
			Intent intent = new Intent(this, NewTaskActivity.class);
			intent.putExtra(TaskUtil.KEY_TASK_ID, mTaskDetailEntity.getShowId());
			String parent = mTaskDetailEntity.getpShowId();
			if (parent != null && !parent.equals("")) {
				intent.putExtra(TaskUtil.KEY_PARENT_TASK_ID, parent);
			}
			startActivityForResult(intent, EDITOR_REQ_CODE);
		}
	}

	public class SubTaskAdapter extends BaseAdapter {
		private Context context;
		@Override
		public int getCount() {
			return mTaskChildList == null ? 0:mTaskChildList.size();
		}

		@Override
		public Object getItem(int position) {
			return mTaskChildList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.item_list_subtask,parent, false);
				holder.tvTask = (TextView) convertView.findViewById(R.id.tv_subtask);
				holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
				holder.tvTime = (TextView) convertView.findViewById(R.id.sub_task_time);
				holder.tvName = (TextView) convertView.findViewById(R.id.sub_task_name);
				holder.cbFinish = (CheckBox) convertView.findViewById(R.id.ch_finish_sub_task);
				holder.more = (ImageView)convertView.findViewById(R.id.iv_item_subtaskmore);
				holder.more.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {			
						
						popWindow.showAtLocation(mCommentView,Gravity.BOTTOM,0,0);
						number = position;
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if(mTaskChildList.get(position).getsType() != null){
				holder.cbFinish.setVisibility(View.VISIBLE);
				if(mTaskChildList.get(position).getsType().equals("WAITING")){
					holder.cbFinish.setSelected(false);
					holder.tvTask.getPaint().setFlags(0);
					holder.tvTask.getPaint().setAntiAlias(true);
					holder.tvTask.setTextColor(getResources().getColor(R.color.black));
					holder.tvTask.setText(mTaskChildList.get(position).gettTitle());
				}else {
					holder.cbFinish.setSelected(true);
					holder.tvTask.setTextColor(getResources().getColor(R.color.gray_launchr));
					holder.tvTask.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					holder.tvTask.getPaint().setAntiAlias(true);
					holder.tvTask.setText(mTaskChildList.get(position).gettTitle());
				}
			}else{
				holder.cbFinish.setVisibility(View.GONE);
				holder.tvTask.getPaint().setFlags(0);
				holder.tvTask.getPaint().setAntiAlias(true);
				holder.tvTask.setTextColor(getResources().getColor(R.color.black));
				holder.tvTask.setText(mTaskChildList.get(position).gettTitle());
			}
			setSubTaskTime(holder.tvTime, mTaskChildList.get(position));
			holder.tvName.setText(mTaskChildList.get(position).gettUserName());
			holder.cbFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(mTaskChildList.get(position).getsType()!=null && mTaskChildList.get(position).getsType().equals("WAITING")) {
						showLoading();
						TaskApi.getInstance().changeTaskState(TaskDetailActivity.this, mTaskChildList.get(position).getShowId(), "FINISH");
					}else {
						showLoading();
						TaskApi.getInstance().changeTaskState(TaskDetailActivity.this, mTaskChildList.get(position).getShowId(), "WAITING");
					}
				}
			});
			if (mIsParent) {
				holder.more.setVisibility(View.GONE);
			}else{
				holder.more.setVisibility(View.GONE);
			}
			String showId = mTaskChildList.get(position).getShowId();
			// 设置头像
			HeadImageUtil.getInstance(TaskDetailActivity.this).setAvatarResourceAppendUrl(holder.imgAvatar,showId,0,50,50);
			return convertView;
		}
		public SubTaskAdapter(Context context){
			this.context = context;
		}
		class Holder {
			TextView tvTask;
			ImageView imgAvatar;
			ImageView more;
			TextView tvTime;
			TextView tvName;
			CheckBox cbFinish;
		}
	}

	private void setSubTaskTime(TextView time, TaskDetailEntity.TaskDetail child){
		//最后期限
		if (child.gettStartTime() != 0 && child.gettEndTime()!=0) {
			time.setText(TimeFormatUtil.setRemindTimeText(child.gettStartTime()) + "~" + TimeFormatUtil.setRemindTimeText(child.gettEndTime()));
		}else if(child.gettStartTime() != 0 && child.gettEndTime()==0) {
			time.setText(getString(R.string.calendar_timepicker_starttime) + TimeFormatUtil.setRemindTimeText(child.gettStartTime()));
		}else if(child.gettStartTime()==0 && child.gettEndTime()!=0) {
			time.setText(getString(R.string.end_time) + TimeFormatUtil.setRemindTimeText(child.gettEndTime()));
		}else{
			time.setText(getString(R.string.no_start_end));
		}
	}

	public class GridAdapter extends BaseAdapter {

		private int selectedPosition = -1;

		@Override
		public int getCount() {
			return mPicUrl == null?0:mPicUrl.size();
		}

		@Override
		public Object getItem(int position) {
			return mPicUrl.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				holder.picLoading = (ProgressBar) convertView.findViewById(R.id.item_grida_pic_loading);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(mPicUrl != null && mPicUrl.size() > 0){
				if(position < mPicUrl.size()){

				}				
			}

			// 区分内网和外网，内网totoro解析不出来
			String url = mPicUrl.get(position).getPath();
			if (LauchrConst.DEV_CODE == 2){
				String s =LauchrConst.ATTACH_PATH + url.substring(18);
				HeadImageUtil.getInstance(TaskDetailActivity.this).setImageResuces(holder.image, s);
			}else {
				HeadImageUtil.getInstance(TaskDetailActivity.this).setImageResuces(holder.image,url);
			}
			return convertView;
		}
		public class ViewHolder {
			public ImageView image;
			public ProgressBar picLoading;
		}
	}


	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if(response == null){
			showNoNetWork();
			return ;
		}else
		
		// 判读是否是获取任务详情返回
		if(taskId.equals(TaskId.TASK_URL_GET_TASK)){
			dismissLoading();
			handleDetailResult(response);
		}else
		
		// 删除子任务
		if(taskId.equals(TaskId.TASK_URL_DELETE_SUB_TASK)){
			handleDeleteChildTask(response);
			dismissLoading();
		}else
		
		// 转化子任务为父任务
		if(taskId.equals(TaskId.TASK_URL_UPDATE_TASK_TO_PARENT_TASK)){
			handleChildChangeParentTaskResult(response);
			dismissLoading();
		}else if(taskId.equals(TaskId.TASK_URL_DELETE_TASK)){
			handleDeleteTask(response);
			dismissLoading();
		}
		//加载附件
		else if(taskId.equals("AttachmentApi_getlist")){
			AttachmentListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AttachmentListPOJO.class);
			handleAttachmentList(pojo);
		}
		// 切换任务状态
		else if(taskId.equals(TaskId.TASK_URL_CHANGE_TASK_STATE)){
			dismissLoading();
			handleChangeTaskState(response);
		}
	}
	
	/** 切换任务状态*/
	private void handleChangeTaskState(Object response){
		TaskStatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskStatePOJO.class);
		if(pojo == null){
			showNetWorkGetDataException();
		}else if(pojo.getBody() == null){
			toast(pojo.getHeader().getReason());
		}else if(!pojo.getBody().getResponse().isIsSuccess()){
			toast(pojo.getBody().getResponse().getReason());
		}else if(pojo.getBody().getResponse().getData() != null){
			TaskStateEntity entity = pojo.getBody().getResponse().getData();
			if(entity.getShowId().equals(mTaskDetailEntity.getShowId())){
				// 任务切换状态
				if(entity.getsType()!=null && "WAITING".equals(entity.getsType())){
					mCbFinish.setSelected(false);
					mTvTitle.getPaint().setFlags(0);
					mTvTitle.getPaint().setAntiAlias(true);//抗锯齿
					mTvTitle.setTextColor(getResources().getColor(R.color.black));
					mTvTitle.setText(entity.gettTitle());
					mTaskDetailEntity.setsType(entity.getsType());
					entity.getShowId();
				}else{
					mCbFinish.setSelected(true);
					mTvTitle.setTextColor(getResources().getColor(R.color.gray_launchr));
					mTvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					mTvTitle.getPaint().setAntiAlias(true);//抗锯齿
					mTvTitle.setText(entity.gettTitle());
					mTaskDetailEntity.setsType(entity.getsType());
				}
//				TaskApi.getInstance().getMessageList(this,TASK_APP_KEY, mTaskDetailEntity.getShowId(), 0, 0, 0);
			}else{
				// 子任务切换状态
				for(int i=0; i<mTaskChildList.size(); i++){
					if(mTaskChildList.get(i).getShowId().equals(entity.getShowId())){
						mTaskChildList.get(i).setsType(entity.getsType());
						break;
					}
				}
				mSubTaskAdapter.notifyDataSetChanged();
			}
		}
	}


	/**
	 * 获取附件
	 */
	private void handleAttachmentList(AttachmentListPOJO pojo){
		if(pojo == null){
			showNetWorkGetDataException();
			return ;
		}else if(pojo.getBody() == null){
			toast(pojo.getHeader().getReason());
			return ;
		}else if(!pojo.getBody().getResponse().isIsSuccess()){
			toast(pojo.getBody().getResponse().getReason());
			return ;
		}else{
			  List<AttachmentListEntity> attachmentListEntities = pojo.getBody().getResponse().getDate();
			  if(attachmentListEntities != null && attachmentListEntities.size() > 0){
				  // 附件地址存放
//				  for(int i = 0;i < attachmentListEntities.size();i++){
//					  String url = attachmentListEntities.get(i).getPath();
//					  mPicUrl.add(url);
//				  }
				  mPicUrl = attachmentListEntities;
				  if(mPicUrl==null || mPicUrl.size()==0){
					  mRelAttach.setVisibility(View.GONE);
				  }else{
					  mRelAttach.setVisibility(View.VISIBLE);
				  }
				  mGridAdapter.notifyDataSetChanged();
			}
		}
		accessComment();

	}
	/**
	 * 处理详情返回
	 * @param response
	 */
	private void handleDetailResult(Object response){
		TaskDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),TaskDetailPOJO.class);
		if(pojo == null){
			return;
		}else if(pojo.getBody() == null){				
			return;
		}else if(pojo.getBody().getResponse().isIsSuccess() == false){
			toast(pojo.getBody().getResponse().getReason());
			return;
		}else if(pojo.getBody().getResponse().getData() == null){					
			return;
		}else{
			 // 获取任务详情
				mTaskDetailEntity = pojo.getBody().getResponse().getData();
				getEntityIntoContent(mTaskDetailEntity);
				if(mTaskDetailEntity.gettIsAnnex() == 1){
					AttachmentApi.getInstance().getAttachmentList(this, mTaskDetailEntity.getShowId(), TASK_APP_KEY);
				}else{
					// 主任务或子任务，其中一个有附件，加载另一个时，必须清空附件
					if(mPicUrl != null){
						mPicUrl.clear();
						if(mPicUrl==null || mPicUrl.size()==0){
							mRelAttach.setVisibility(View.GONE);
						}else{
							mRelAttach.setVisibility(View.VISIBLE);
						}
						mGridAdapter.notifyDataSetChanged();
					}
						accessComment();
				}
		}
	}
	
	private void accessComment(){
		// 获取评论
//		showLoading();
//		TaskApi.getInstance().getMessageList(this,TASK_APP_KEY, mTaskDetailEntity.getShowId(), 0, 0, 0);
		mCommentsList.updateComment();
	}
	
	/**
	 * 处理删除子任务
	 * @param response
	 */
	private void handleDeleteChildTask(Object response){
		TaskDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),TaskDetailPOJO.class);
	    if(pojo == null){
	    	return;
	    }else if(pojo.getBody() == null){
	    	return;
	    }else if(pojo.getBody().getResponse().isIsSuccess() == false){
	    	toast(pojo.getBody().getResponse().getReason());
	    }else if(pojo.getBody().getResponse().getData() == null){
	    	return;
	    }else{
			for(int i=0; i<mTaskChildList.size(); i++){
				if(mTaskChildList.get(i).getShowId().equals(pojo.getBody().getResponse().getData().getShowId())){
					mTaskChildList.remove(i);
					break;
				}
			}
			mSubTaskAdapter.notifyDataSetChanged();
	    }
	}
	
	/**
	 * 处理子任务转为父任务
	 * @param response
	 */
	private void handleChildChangeParentTaskResult(Object response){
		TaskPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),TaskPOJO.class);
	    if(pojo == null){
	    	return;
	    }else if(pojo.getBody() == null){
	    	return;    
	    }else if(pojo.getBody().getResponse().isIsSuccess() == false){
	    	toast(pojo.getBody().getResponse().getReason());
	    }else if(pojo.getBody().getResponse().getData() == null){
	    	return;
	    }else{	    	
			mTaskChildList.remove(number);
			mSubTaskAdapter.notifyDataSetChanged();
	    }
	}
	


	@Override
	public void editTask() {

	}
	
	@Override
	public void delectTask() {
		// 删除子任务
//		showLoading();
//		TaskApi.getInstance().deleteSubTask(this, mTaskChildList.get(number).gettTitle(), mTaskChildList.get(number).getShowId());
	}
	
	@Override
	public void ChnageTask() {
		// 转化子任务为父任务
		showLoading();
		TaskApi.getInstance().updateTaskToParentTask(this, mTaskChildList.get(number).getShowId(), "1");
	}


	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}else
		// 判断是否是添加子任务返回	
		if (requestCode == ADD_CHILDTASK_REQ_CODE) {
			mPicUrl.clear();
			initData();

		}else
		if(requestCode == CommentsView.TAKE_PICTURE){
			mCommentView.setImageMessage();

		} else
			if(requestCode == CommentsView.GET_ALBUM){
				if(data == null){
					return;
				}
				List<String> imagePath = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
				if(imagePath != null){
					mCommentView.setAublmImageMessage(imagePath.get(0));
				}
			}		

		else if (requestCode == EDITOR_REQ_CODE) {
			mPicUrl.clear();
			initData();
		}	

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent == mGvPhoto){
//			List<String> list = new ArrayList<String>();
//			for(int i = 0;i < mPicUrl.size();i++){
//				String strUrl = mPicUrl.get(i);
//				list.add(strUrl);
//			}
			Intent intent = new Intent(this, GalleryActivity.class);
			intent.putExtra(GalleryActivity.KEY_PHOTO_URL, (Serializable) mPicUrl);
			intent.putExtra(GalleryActivity.KEY_TYPE,GalleryActivity.HTTP_URL);
			intent.putExtra(GalleryActivity.KEY_POSITION,position);
			intent.putExtra(GalleryActivity.CAN_DELECT_IMAGE,false);
			startActivity(intent);

		}else if(parent == mLvSubTask){
			mPicUrl.clear();
			if (mIsParent) {
				TaskDetailEntity.TaskDetail entity = (TaskDetailEntity.TaskDetail) parent.getAdapter().getItem(position);
				accessTask(entity.getShowId());
				mIsParent = false;
			}else{
				mIsParent = true;
				TaskDetailEntity.TaskDetail entity = (TaskDetailEntity.TaskDetail) parent.getAdapter().getItem(position);
				accessTask(entity.getShowId());
			}
		}
	}

	/** 删除任务*/
	private void handleDeleteTask(Object response){
		TaskDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),TaskDetailPOJO.class);
		if(pojo == null){
			return;
		}else if(pojo.getBody() == null){
			return;
		}else if(pojo.getBody().getResponse().isIsSuccess() == false){
			toast(pojo.getBody().getResponse().getReason());
		}else if(pojo.getBody().getResponse().getData() == null){
			return;
		}else{			
			finish();
		}
	}
	
	/** 删除任务*/
	public void deleteTask() {


				showLoading();
				// 删除服务器数据
				TaskApi.getInstance().deleteTask(TaskDetailActivity.this, mTaskDetailEntity.getShowId());
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		if(parent == mLvSubTask){
			deleteSubTask(view, mTaskChildList.get(position).getShowId());
		}

		return true;
	}


	// 删除子任务
	private void deleteSubTask(View view, final String showId){
		final TaskEditWindow taskMenu = new TaskEditWindow(TaskDetailActivity.this);
		taskMenu.show();
		Window window = taskMenu.getWindow();
		final TextView editView = (TextView)window.findViewById(R.id.edit_task);
		editView.setVisibility(View.GONE);
		final TextView deleteText = (TextView)window.findViewById(R.id.tv_delete);
		deleteText.setText(getString(R.string.task_pop_delect_subtask));
		deleteText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskApi.getInstance().deleteSubTask(TaskDetailActivity.this, showId);
				taskMenu.dismiss();
			}
		});
	}

	@Override
	public void sendMessage(String message) {
		/** 发送评论*/
		mCommentsList.updateComment();
	}
}

























