package com.mintcode.launchr.app.newTask.activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;

import com.mintcode.launchr.app.newTask.adapter.ProjectAdapter;
import com.mintcode.launchr.app.newTask.fragment.SubTaskFragment;
import com.mintcode.launchr.app.newTask.fragment.TimeTaskFragment;
import com.mintcode.launchr.app.newTask.view.ProjectMemberPopWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.TaskAddProjectPOJO;
import com.mintcode.launchr.pojo.TaskCountPOJO;
import com.mintcode.launchr.pojo.TaskProjectListPOJO;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;
import com.mintcode.launchr.pojo.entity.TaskCountEntity;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JulyYu on 2016/2/22.
 */
public class TaskMainActivity extends BaseActivity implements AdapterView.OnItemClickListener {


	private OnResponseListener mContext;
	private DrawerLayout mDrawerLayout;
	private ListView mTimeTaskList;
	private ListView mDrawerList;
	/**
	 * 日期选择项目文本
	 ***/
	private int[] mIntString;
	/**
	 * 日期选择项目文本logo
	 */
	private int[] mIntDrawableResoure;
	private List<TaskProjectListEntity> mListTop;
	/**
	 * 日期选择项目
	 */
	private ProjectAdapter mTimeTaskAdapter;
	/**
	 * 项目
	 **/
	private ProjectAdapter mTaskAdapter;
	/***
	 * 添加新项目
	 */
	private LinearLayout mLlAddNewProject;
	/***
	 * 项目内容显示
	 **/
	private Fragment mTaskFragment;
	/**
	 * 侧滑菜单
	 **/
	private LinearLayout mLlSlidingMenu;
	/***
	 * 呼出项目菜单
	 */
	private ImageView mIvSlidingMenu;
//    private RelativeLayout mRlMenu;
	/**
	 * 添加项目任务
	 */
	private ImageView mIvAddTask;
//    private RelativeLayout mRlAddTask;
	/**
	 * 新增项目
	 */
	private EditText mEdtNewProject;

	private TextView mTvBack;
	private ImageView mIvSearch;
	private ImageView mIvProjectEdit;
	private TextView mTvProjectName;
	private TimeTaskFragment timeTaskFragment;
	private SubTaskFragment subTaskFragment;

	private boolean timeTaskIsShow = false;

	/**
	 * 项目ID
	 */
	private String mShowId;
	//删除项目
	public static final int DELECT_PROJECT = 0x0001;
	//新建任务
	public static final int NEW_TASK = 0x0002;
	//重新获取侧滑列表数据
	private boolean mBoolReset = true;

	/**
	 * 当前项目
	 */
	public static TaskProjectListEntity mCurrentProject;
	/**
	 * 当前所在时间列表
	 */
	public static String mTimeName = "";
	public final static String GET_ALL_TASK_LIST = "get_all_task_list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main);
		mContext = this;
		initView();
		initData();
		MixPanelUtil.sendEvent(this, MixPanelUtil.APPCENTER_TASK_EVENT);
	}


	private void initView() {
		mIntString = new int[]{
				R.string.task_today
				, R.string.task_tomorrow
				, R.string.task_all
				, R.string.task_one_day
				, R.string.task_my_send
				, R.string.task_finish
		};

		subTaskFragment = new SubTaskFragment();
		timeTaskFragment = new TimeTaskFragment();
//        getSupportFragmentManager().beginTransaction()
//                .hi
//                .hide(subTaskFragment)
//                .add(R.id.content_frame, timeTaskFragment).commit();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, timeTaskFragment)
				.commit();

		mTaskFragment = timeTaskFragment;

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mTimeTaskList = (ListView) findViewById(R.id.left_time_task);
		mLlAddNewProject = (LinearLayout) findViewById(R.id.ll_task_add_new_project);
		mLlSlidingMenu = (LinearLayout) findViewById(R.id.ll_sliding_menu);
		mTvBack = (TextView) findViewById(R.id.tv_back);
		mIvSearch = (ImageView) findViewById(R.id.iv_task_search);
		mIvProjectEdit = (ImageView) findViewById(R.id.iv_task_member);
		mTvProjectName = (TextView) findViewById(R.id.tv_task_project_title);
		mIvAddTask = (ImageView) findViewById(R.id.iv_task_add);
		mIvSlidingMenu = (ImageView) findViewById(R.id.iv_task_menu);
		mEdtNewProject = (EditText) findViewById(R.id.edit_new_project);

		mTaskAdapter = new ProjectAdapter(this);
		mDrawerList.setAdapter(mTaskAdapter);
		mDrawerList.setOnItemClickListener(this);

		mTimeTaskAdapter = new ProjectAdapter(this);
		mTimeTaskList.setAdapter(mTimeTaskAdapter);
		mTimeTaskList.setOnItemClickListener(this);

		mLlAddNewProject.setOnClickListener(this);
		mIvAddTask.setOnClickListener(this);
		mIvSlidingMenu.setOnClickListener(this);
		mTvBack.setOnClickListener(this);
		mIvSearch.setOnClickListener(this);
		mIvProjectEdit.setOnClickListener(this);
		mEdtNewProject.setInputType(InputType.TYPE_CLASS_TEXT);
		mEdtNewProject.setImeOptions(EditorInfo.IME_ACTION_SEND);
		mEdtNewProject.setOnEditorActionListener(mOnEnter);
		mDrawerLayout.addDrawerListener(listen);
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);

		mTimeName = getString(R.string.task_today);
	}

	private void initData() {
		TaskApi.getInstance().getProjectList(this, "0", "0", "");
	}


	private EditText.OnEditorActionListener mOnEnter = new EditText.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_NULL) {
				return false;
			} else if (actionId == EditorInfo.IME_ACTION_SEND) {//新建项目
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				mEdtNewProject.setVisibility(View.GONE);
				String name = mEdtNewProject.getText().toString();
				if (!"".equals(name)) {
					showLoading();
					ArrayList<TaskAddProjectEntity.TaskAddProjectMembersEntity> mMemberUserName = new ArrayList<>();
					TaskAddProjectEntity.TaskAddProjectMembersEntity member = new TaskAddProjectEntity.TaskAddProjectMembersEntity();
					String uTrueName = LauchrConst.header.getUserName();
					String uName = LauchrConst.header.getLoginName();
					member.setMemberName(uName);
					member.setMemberTrueName(uTrueName);
					mMemberUserName.add(member);
					TaskApi.getInstance().addProject(mContext, name, mMemberUserName);
					mEdtNewProject.setText("");
				}
				return true;
			} else if (actionId == EditorInfo.IME_ACTION_DONE) {
				return false;
			} else {
				return false;
			}


		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mTaskFragment != null) {
			getSupportFragmentManager().beginTransaction().remove(mTaskFragment).commitAllowingStateLoss();
		}
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mDrawerLayout.closeDrawer(mLlSlidingMenu);
		if (parent == mDrawerList) {//打开项目任务列表
			TaskProjectListEntity entity = (TaskProjectListEntity) mTaskAdapter.getItem(position);
			String projectId = entity.getShowId();
			mShowId = entity.getShowId();
			String projectName = entity.getName();
			if (mTaskFragment != subTaskFragment) {
				if (!subTaskFragment.isAdded()) {
					getSupportFragmentManager()
							.beginTransaction()
							.hide(mTaskFragment)
							.add(R.id.content_frame, subTaskFragment)
							.commit();
				} else {
					getSupportFragmentManager()
							.beginTransaction()
							.hide(mTaskFragment)
							.show(subTaskFragment)
							.commit();
				}
				mTaskFragment = subTaskFragment;
			}
			subTaskFragment.setmProjectId(projectId);
			mTvProjectName.setText(projectName);
			mIvProjectEdit.setVisibility(View.VISIBLE);
			mCurrentProject = entity;
			mTimeName = "";
			mTaskAdapter.setSelectPosition(position);
			mTimeTaskAdapter.setSelectPosition(-1);
		} else if (parent == mTimeTaskList) {//打开时间任务列表
			TaskProjectListEntity entity = (TaskProjectListEntity) mTimeTaskAdapter.getItem(position);
			String name = entity.getName();
			mTvProjectName.setText(name);
			if (mTaskFragment != timeTaskFragment) {
				getSupportFragmentManager()
						.beginTransaction()
						.hide(mTaskFragment)
						.show(timeTaskFragment)
						.commit();
				mTaskFragment = timeTaskFragment;
			}
			int intType = position;
			if (position == 2) {
				intType = 3;
			} else if (position == 3) {
				intType = 2;
			}
			timeTaskFragment.setmIntType(intType);
//            mTaskFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mTaskFragment).show(mTaskFragment).commitAllowingStateLoss();
			mIvProjectEdit.setVisibility(View.GONE);
			mCurrentProject = null;
			mTimeName = name;
			mTimeTaskAdapter.setSelectPosition(position);
			mTaskAdapter.setSelectPosition(-1);
		}
	}

	private void initTaskProject(List<TaskCountEntity> TaskEntity) {
		mListTop = new ArrayList<>();
		for (int i = 0; i < mIntString.length; i++) {
			TaskProjectListEntity entity = new TaskProjectListEntity();
			String name = getResources().getString(mIntString[i]);
			entity.setName(name);
			// 所有任务和无开始时间需要调换位置
			if (i == 2) {
				entity.setAllTask(TaskEntity.get(3).getCount());
			} else if (i == 3) {
				entity.setAllTask(TaskEntity.get(2).getCount());
			} else {
				entity.setAllTask(TaskEntity.get(i).getCount());
			}
			mListTop.add(entity);
		}
		mIntDrawableResoure = new int[]{
				R.drawable.icon_task_today
				, R.drawable.icon_task_tomorrow
				, R.drawable.icon_task_calender
				, R.drawable.icon_task_oneday
				, R.drawable.icon_task_my_send
				, R.drawable.icon_task_finish};
		mTimeTaskAdapter.setTimeProjectData(mListTop, mIntDrawableResoure);
//		if (mBoolReset) {
//			mTaskFragment = new TimeTaskFragment();
//			Bundle bundle = new Bundle();
//			int type = getIntent().getIntExtra(GET_ALL_TASK_LIST, 0);
//			bundle.putInt("type", type);
//			mDrawerLayout.closeDrawer(mLlSlidingMenu);
//			if (type == 3) type = 2;
//			TaskProjectListEntity entity = (TaskProjectListEntity) mTimeTaskAdapter.getItem(type);
//			String name = entity.getName();
//			mTvProjectName.setText(name);
//			mTaskFragment.setArguments(bundle);
//			mDrawerLayout.closeDrawer(mLlSlidingMenu);
//			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//			if (fragmentManager != null) {
//				if (!isFinishing()) { //如果Activity被finish掉后加入commit Fragment再次启动Activity会出现异常崩溃
//					getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mTaskFragment).commitAllowingStateLoss();
//					mIvProjectEdit.setVisibility(View.GONE);
//					mTimeTaskAdapter.setSelectPosition(type);
//				}
//			}
//		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mLlAddNewProject) {//新建项目
			mEdtNewProject.setVisibility(View.VISIBLE);
			mEdtNewProject.setFocusable(true);
			mEdtNewProject.setFocusableInTouchMode(true);
			mEdtNewProject.requestFocus();
			displayInputMethod(true);
		} else if (v == mIvAddTask) {//新建任务
			Intent intent = new Intent(this, NewTaskActivity.class);
			intent.putExtra("CurrentProject", mCurrentProject);
			intent.putExtra("TimeProject", mTimeName);
			startActivityForResult(intent, NEW_TASK);
		} else if (v == mIvSlidingMenu) {//菜单
			mDrawerLayout.openDrawer(mLlSlidingMenu);
		} else if (v == mTvBack) {//返回
			onBackPressed();
		} else if (v == mIvProjectEdit) {//编辑项目
			showLoading();
			TaskApi.getInstance().getProjectDetail(this, mShowId);
		}
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		dismissLoading();


		if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_PROJECT_LIST)) {// 得到项目列表
			handleResultTaskList(response);
		} else if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_TASK_COUNT)) {// 获取任务数量
			handleResultTaskCount(response);
		} else if (taskId.equals(TaskApi.TaskId.TASK_URL_ADD_PROJECT)) {// 新建项目
			handleAddProject(response);
		} else if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_PROJECT_DETAIL)) {//获取项目详情
			handleGetProjectDetail(response);
		}
	}

	// 获取项目详情
	private void handleGetProjectDetail(Object response) {
		TaskAddProjectPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskAddProjectPOJO.class);
		if (pojo != null) {
			if (pojo.getBody() != null) {
				if (pojo.getBody().getResponse().isIsSuccess()) {
					TaskAddProjectEntity entity = pojo.getBody().getResponse().getData();
					if (entity != null) {
						String createrName = entity.getCreateUser();
						String userName = LauchrConst.getHeader(this).getLoginName();
						if (userName.equals(createrName)) {
							Intent i = new Intent(this, ProjectDetailActivity.class);
							i.putExtra(ProjectDetailActivity.PROJECT_DETAIL, entity);
							startActivityForResult(i, DELECT_PROJECT);
						} else {
							ProjectMemberPopWindow window = new ProjectMemberPopWindow(this);
							window.showAsDropDown(mIvProjectEdit);
							window.showProjectMembers(entity.getMembers());
						}
					}
				} else {
					toast(pojo.getBody().getResponse().getReason());
				}
			} else {
				toast(pojo.getHeader().getReason());
			}
		} else {
			showNetWorkGetDataException();
		}
	}

	// 新建项目
	private void handleAddProject(Object response) {
		TaskAddProjectPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskAddProjectPOJO.class);
		if (pojo != null) {
			if (pojo.getBody() != null) {
				if (pojo.getBody().getResponse().isIsSuccess()) {
					TaskApi.getInstance().getProjectList(this, "0", "0", "");
				} else {
					toast(pojo.getBody().getResponse().getReason());
				}
			} else {
				toast(pojo.getHeader().getReason());
			}
		} else {
			showNetWorkGetDataException();
		}
	}

	// 获取日期任务数量
	private void handleResultTaskCount(Object response) {
		TaskCountPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskCountPOJO.class);
		if (pojo != null) {
			if (pojo.getBody() != null) {
				if (pojo.getBody().getTaskResponse().isIsSuccess()) {
					List<TaskCountEntity> entity = pojo.getBody().getTaskResponse().getData();
					if (entity != null) {
						initTaskProject(entity);
					}
				} else {
					toast(pojo.getBody().getTaskResponse().getReason());
				}
			} else {
				toast(pojo.getHeader().getReason());
			}
		} else {
			showNetWorkGetDataException();
		}
	}

	// 处理项目列表返回数据
	public void handleResultTaskList(Object response) {
		TaskProjectListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskProjectListPOJO.class);
		if (pojo != null) {
			if (pojo.getBody() != null) {
				if (pojo.getBody().getResponse().isIsSuccess()) {
					List<TaskProjectListEntity> mList = pojo.getBody().getResponse().getData();
					if (mList != null) {
						// 更新列表
						mTaskAdapter.setData(mList);
						TaskApi.getInstance().getTaskCount(this, System.currentTimeMillis());
					}
				} else {
					toast(pojo.getBody().getResponse().getReason());
				}
			} else {
				toast(pojo.getHeader().getReason());
			}
		} else {
			showNetWorkGetDataException();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == DELECT_PROJECT && data != null) {
			if (data.getIntExtra("type", 1) == 1) { //删除项目显示今日任务
				mTvProjectName.setText(getResources().getString(R.string.task_today));

				if (mTaskFragment != timeTaskFragment) {
					timeTaskFragment.setmIntType(0);
					getSupportFragmentManager()
							.beginTransaction()
							.hide(mTaskFragment)
							.show(timeTaskFragment)
							.commit();
					mTaskFragment = timeTaskFragment;
				}
//				mTaskFragment = new TimeTaskFragment();
//				Bundle bundle = new Bundle();
//				bundle.putInt("type", 0);
//				mTaskFragment.setArguments(bundle);
				mDrawerLayout.closeDrawer(mLlSlidingMenu);
//				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mTaskFragment).commitAllowingStateLoss();
				mIvProjectEdit.setVisibility(View.GONE);
				mTaskAdapter.setSelectPosition(-1);
				mTimeTaskAdapter.setSelectPosition(0);
				super.onActivityResult(requestCode, resultCode, data);
			} else if (data.getIntExtra("type", 1) == 2) { // 编辑项目
				if (mTaskFragment instanceof SubTaskFragment) {
					String projectName = data.getStringExtra("name");
					if (projectName != null) {
						mTvProjectName.setText(projectName);
					}
				}
			}
		} else if (requestCode == NEW_TASK) { //新建任务
			if (data != null) {
				if (data.getStringExtra("result_project_name") != null) {
					mTvProjectName.setText(data.getStringExtra("result_project_name"));
				}
				if (data.getIntExtra("result_project_type", -1) != -1) {
					int position = data.getIntExtra("result_project_position", -1);

					if (mTaskFragment != timeTaskFragment) {
						getSupportFragmentManager()
								.beginTransaction()
								.hide(mTaskFragment)
								.show(timeTaskFragment)
								.commit();
						mTaskFragment = timeTaskFragment;
					}
//					int intType = position;
//					if (position == 2) {
//						intType = 3;
//					} else if (position == 3) {
//						intType = 2;
//					}
					timeTaskFragment.setmIntType(position);

//					mTaskFragment = new TimeTaskFragment();
//					Bundle bundle = new Bundle();

//					bundle.putInt("type", position);
//					mTaskFragment.setArguments(bundle);
//					mTaskFragment.setm
//					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mTaskFragment).show(mTaskFragment).commitAllowingStateLoss();
					mTvProjectName.setText(data.getStringExtra("result_project_title"));
					mIvProjectEdit.setVisibility(View.GONE);
				}
			}
			if (mTaskFragment instanceof SubTaskFragment) {
				mTaskFragment.onActivityResult(requestCode, resultCode, data);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	DrawerLayout.DrawerListener listen = new DrawerLayout.DrawerListener() {

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			View mContent = mDrawerLayout.getChildAt(0);
			View mMenu = drawerView;
			float scale = 1 - slideOffset;
			ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
			ViewHelper.setTranslationX(mContent,
					mMenu.getMeasuredWidth() * (1 - scale));
			ViewHelper.setPivotX(mContent, 0);
			ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
			mContent.invalidate();
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			mBoolReset = false;
			TaskApi.getInstance().getProjectList(mContext, "0", "0", "");
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mEdtNewProject.setVisibility(View.GONE);
			mEdtNewProject.setText("");
			displayInputMethod(false);
			if (mTaskFragment == subTaskFragment) {
				subTaskFragment.updateTaskList();
			} else if(mTaskFragment == timeTaskFragment){
				timeTaskFragment.updateTaskList();
			}
//          ((TimeTaskFragment)mTaskFragment).updateTaskList();
		}

		@Override
		public void onDrawerStateChanged(int newState) {

		}
	};

	private void displayInputMethod(boolean isOpen) {
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isOpen) {
			inputMethodManager.showSoftInput(mEdtNewProject, InputMethodManager.SHOW_FORCED);
		} else {
			inputMethodManager.hideSoftInputFromWindow(
					mEdtNewProject.getWindowToken(), 0);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}



















