
package com.mintcode.launchr.app.newTask.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.activity.TaskDetailActivity;
import com.mintcode.launchr.app.newTask.activity.TaskMainActivity;
import com.mintcode.launchr.app.newTask.adapter.TaskExpandAdapter;
import com.mintcode.launchr.app.newTask.view.TaskEditWindow;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.pojo.TaskListPOJO;
import com.mintcode.launchr.pojo.entity.TaskListEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/22.
 */
public class SubTaskFragment extends BaseFragment implements AbsListView.OnScrollListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, AdapterView.OnItemLongClickListener {

    /**
     * 根view
     */
    private View mRootView;

    /**
     * ExpandableListView
     */
    private ExpandableListView mExpandLvTask;

    /**
     * ExpandableListView 适配器
     */
    private TaskExpandAdapter mExpandAdapter;
    private List<TaskListEntity> mGroupList = new ArrayList<>();
    private List<List<TaskListEntity>> mChildList = new ArrayList<>();
    private String mProjectId = "";
    private int mIntType;
    private GetHandler mHandler = new GetHandler();
    private int mPage = 1;
    private RelativeLayout mTvNoTask;
    //是否更新项目列表任务
    private boolean mThisBoolRest = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_task, null);
        }
//        clear();
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Bundle mBundle = getArguments();
//        mProjectId = mBundle.getString("arg");
//        mIntType = mBundle.getInt("type") + 1;
        initView();
    }


    private void initView() {
        mExpandLvTask = (ExpandableListView) mRootView.findViewById(R.id.expand_listview);
        mTvNoTask = (RelativeLayout) mRootView.findViewById(R.id.rel_no_task);
        mExpandAdapter = new TaskExpandAdapter(getActivity(), mExpandLvTask);
        mExpandLvTask.setAdapter(mExpandAdapter);
        mExpandLvTask.setOnChildClickListener(this);
        mExpandLvTask.setOnGroupClickListener(this);
        mExpandLvTask.setOnItemLongClickListener(this);
        mExpandLvTask.setOnScrollListener(this);
    }

    public void updateTaskList() {

        mChildList.clear();
        mGroupList.clear();
        showLoading();
        if (mProjectId == null || "".equals(mProjectId)) {
            TaskApi.getInstance().getProjectTaskList(this, mPage, 50, mIntType, System.currentTimeMillis(), null, "");
        } else {
            TaskApi.getInstance().getProjectTaskList(this, mPage, 50, 7, System.currentTimeMillis(), mProjectId, "");
        }
    }

    public void setmProjectId(String projectId) {
        mProjectId = projectId;
//        updateTaskList();
    }

    private class GetHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

//            if (msg.what == TaskUtil.UPDATE_STATE) {
//                if (isGroup) {
//                    mGroupList.remove(groupPosition);
//                }else{
//                    mChildList.get(groupPosition).remove(childPosition);
//                }
//                mExpandAdapter.notifyDataSetChanged();
//
//            }else{
            super.handleMessage(msg);
//            }
        }

    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        // 判断是否为空
        if (response == null) {
            showNoNetWork();
            return;
        }
        // 判断是否是搜索及获取任务列表返回
        if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_PROJECT_TASK_LIST)) {
            handleSearchAndGetTaskListResult(response);
        } else if (taskId.equals(TaskApi.TaskId.TASK_URL_CHANGE_TASK_STATE)) {
            mExpandAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 处理搜索及获取任务列表返回数据
     *
     * @param response
     */
    private void handleSearchAndGetTaskListResult(Object response) {
        TaskListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskListPOJO.class);
//        TaskListPOJO pojo = JSON.parseObject(response.toString(), TaskListPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                mThisBoolRest = false;
                List<TaskListEntity> list = pojo.getBody().getResponse().getData();
                if (list != null) {
                    if (list.isEmpty()) {
                        mTvNoTask.setVisibility(View.VISIBLE);
                        mExpandLvTask.setVisibility(View.GONE);
                    } else {
                        mTvNoTask.setVisibility(View.GONE);
                        mExpandLvTask.setVisibility(View.VISIBLE);
                        selectData(list);
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

    private void selectData(List<TaskListEntity> list) {

        mGroupList.clear();
        mChildList.clear();
        List<TaskListEntity> childList = new ArrayList<TaskListEntity>();
        while (!list.isEmpty()) {
            TaskListEntity e = list.remove(0);
            if (e.getLevel() == 1) {
                mGroupList.add(e);
            } else {
                childList.add(e);
            }
        }
        for (int i = 0; i < mGroupList.size(); i++) {
            List<TaskListEntity> l = new ArrayList<TaskListEntity>();
            TaskListEntity entity = mGroupList.get(i);
            int finish = 0;
            int all = 0;
            // 筛选父任务下的子任务
            for (int j = 0; j < childList.size(); j++) {
                TaskListEntity e = childList.get(j);
                if (entity.getShowId().equals(e.getParentTaskId())) {
                    all++;
                    if (e.getType().equals("FINISH")) {
                        finish++;
                    }
                    l.add(e);
                }
            }
            entity.setAllTask(all);
            entity.setFinishedTask(finish);
            // 筛选出的结果从原集合中移除
            for (int j = 0; j < l.size(); j++) {
                TaskListEntity e = l.get(j);
                childList.remove(e);
            }
            mChildList.add(l);
        }
        mExpandAdapter.setData(mGroupList, mChildList);
//      mExpandAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        mThisBoolRest = true;
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        TaskListEntity entity = (TaskListEntity) mExpandAdapter.getGroup(groupPosition);
        intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
        intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
        startActivity(intent);
        return true;
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mThisBoolRest = true;
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        TaskListEntity entity = (TaskListEntity) mExpandAdapter.getChild(groupPosition, childPosition);
        intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
        intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
        startActivity(intent);
        return true;
    }

    /**
     * 长按groupItem 操作
     *
     * @param parent
     * @param view
     * @param groupPosition
     * @param childPosition
     */
    private void onGroupItemLongClick(AdapterView<?> parent, View view, int groupPosition, int childPosition) {

        final TaskEditWindow taskMenu = new TaskEditWindow(getActivity(), this);
        taskMenu.show();
        TaskListEntity entity = (TaskListEntity) mExpandAdapter.getGroup(groupPosition);
        taskMenu.setTaskListEntity(entity);
    }

    /**
     * 长按childItem 操作
     *
     * @param parent
     * @param view
     * @param groupPosition
     * @param childPosition
     */
    private void onChildItemLongClick(AdapterView<?> parent, View view, int groupPosition, int childPosition) {
        final TaskEditWindow taskMenu = new TaskEditWindow(getActivity(), this);
        taskMenu.show();
        TaskListEntity entity = (TaskListEntity) mExpandAdapter.getChild(groupPosition, childPosition);
        taskMenu.setTaskListEntity(entity);
    }


    //	int childId = (int) view.getTag(R.integer.child_id);
//	int groupId = (int) view.getTag(R.integer.group_id);
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int childId = (Integer) view.getTag(R.id.child_id);
        int groupId = (Integer) view.getTag(R.id.group_id);
        Log.i("index", "===child==" + childId);


        if (childId == -1) {
            onGroupItemLongClick(parent, view, groupId, childId);
        } else {
            onChildItemLongClick(parent, view, groupId, childId);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mThisBoolRest) {
//            updateTaskList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == TaskMainActivity.DELECT_PROJECT && data != null) {
            if (data.getIntExtra("type", 1) == 1) {
                // 删除项目
                toast(getResources().getString(R.string.apply_comment_delect_success_toast));
            } else if (data.getIntExtra("type", 1) == 2) {
                // 编辑项目
//                updateTaskList();
            }
        } else if (requestCode == TaskMainActivity.NEW_TASK) {
            if (data != null) {
                if (data.getStringExtra("result_project_id") != null) {
                    mProjectId = data.getStringExtra("result_project_id");
                }
            }

            updateTaskList();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                mI++;
//                showLoading();
//                initData();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
    }
}

