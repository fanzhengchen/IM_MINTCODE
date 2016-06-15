
package com.mintcode.launchr.app.newTask.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.UpdateTaskListen;
import com.mintcode.launchr.app.newTask.activity.TaskDetailActivity;
import com.mintcode.launchr.app.newTask.adapter.TaskListAdapter;
import com.mintcode.launchr.app.newTask.view.TaskEditWindow;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.pojo.TaskListPOJO;
import com.mintcode.launchr.pojo.entity.TaskEntity;
import com.mintcode.launchr.pojo.entity.TaskListEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by JulyYu on 2016/2/24.
 */
public class TimeTaskFragment extends BaseFragment implements AdapterView.OnItemLongClickListener,
        ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, UpdateTaskListen {

    /**
     * 根view
     */
    private View mRootView;

    /**
     * 其他时间ExpandListView
     */
    private ExpandableListView mLvTask;
    /**
     * 其他时间适配器
     */
    private TaskListAdapter mAdapter;

    /**
     * 今天ExpandListView
     */
    private ExpandableListView mElvToday;
    /**
     * 今天适配器
     */
    private TaskListAdapter mTodayAdapter;
    /**
     * 父任务
     */
    private List<TaskListEntity> mGroupList = new ArrayList<>();
    /**
     * 子任务
     */
    private List<List<TaskListEntity>> mChildList = new ArrayList<>();

    /**
     * 今日父任务
     */
    private List<TaskListEntity> mTodayGroupList = new ArrayList<>();
    /**
     * 今日子任务
     */
    private List<List<TaskListEntity>> mTodayChildList = new ArrayList<>();

    private TextView mTvOverTime;
    private TextView mTvTodayTime;

    private int mIntType = 1;
    private int mPage = 1;
    private RelativeLayout mTvNoTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_time_task, null);
        }
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
//        mIntType = mBundle.getInt("type") + 1;
        initView();
    }

    public void setmIntType(int intType) {
        mIntType = intType + 1;
//        updateTaskList();
    }


    private void initView() {

        mTvOverTime = (TextView) mRootView.findViewById(R.id.tv_over_time);
        mTvTodayTime = (TextView) mRootView.findViewById(R.id.tv_today_time);
        mLvTask = (ExpandableListView) mRootView.findViewById(R.id.lv_timelistview);
        mTvNoTask = (RelativeLayout) mRootView.findViewById(R.id.rel_no_task);
        mElvToday = (ExpandableListView) mRootView.findViewById(R.id.elv_today_list);

        mTvOverTime.setVisibility(View.GONE);
        mTvTodayTime.setVisibility(View.GONE);
        if (mIntType == 1) {
            mTodayAdapter = new TaskListAdapter(getActivity(), mElvToday, this);
            mElvToday.setAdapter(mTodayAdapter);
            mElvToday.setOnItemLongClickListener(this);
            mElvToday.setOnChildClickListener(this);
            mElvToday.setOnGroupClickListener(this);

            mAdapter = new TaskListAdapter(getActivity(), mLvTask, this);
            mLvTask.setAdapter(mAdapter);
            mLvTask.setOnItemLongClickListener(this);
            mLvTask.setOnGroupClickListener(this);
            mLvTask.setOnChildClickListener(this);
        } else {

            mAdapter = new TaskListAdapter(getActivity(), mLvTask, this);
            mLvTask.setAdapter(mAdapter);
            mLvTask.setOnItemLongClickListener(this);
            mLvTask.setOnGroupClickListener(this);
            mLvTask.setOnChildClickListener(this);
        }
    }

    public void updateTaskList() {
        mGroupList.clear();
        mChildList.clear();
        mTodayGroupList.clear();
        mTodayChildList.clear();
        showLoading();
        TaskApi.getInstance().getProjectTaskList(this, mPage, 50, mIntType, System.currentTimeMillis(), null, "");
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (parent == mElvToday) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            TaskListEntity entity = (TaskListEntity) mTodayAdapter.getChild(groupPosition, childPosition);
            if (entity != null) {
                intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
                intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
                startActivityForResult(intent, 0);
            }
        } else if (parent == mLvTask) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            TaskListEntity entity = (TaskListEntity) mAdapter.getChild(groupPosition, childPosition);
            if (entity != null) {
                intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
                intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
                startActivityForResult(intent, 0);
            }
        }
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (parent == mLvTask) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            TaskListEntity entity = (TaskListEntity) mAdapter.getGroup(groupPosition);
            if (entity != null) {
                intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
                intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
                startActivityForResult(intent, 0);
            }
        } else if (parent == mElvToday) {
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            TaskListEntity entity = (TaskListEntity) mTodayAdapter.getGroup(groupPosition);
            if (entity != null) {
                intent.putExtra(TaskDetailActivity.KEY_TASK_PARENT_ID, entity.getParentTaskId());
                intent.putExtra(TaskDetailActivity.KEY_TASK_ID, entity.getShowId());
                startActivityForResult(intent, 0);
            }
        }
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mLvTask) {
            int childId = (Integer) view.getTag(R.id.child_id);
            int groupId = (Integer) view.getTag(R.id.group_id);
            if (childId != -1) {
                onItemChildLongClick(groupId, childId);
            } else {
                onItemGroupLongClick(groupId);
            }
        } else if (parent == mElvToday) {
            int childId = (Integer) view.getTag(R.id.child_id);
            int groupId = (Integer) view.getTag(R.id.group_id);
            if (childId != -1) {
                final TaskEditWindow taskMenu = new TaskEditWindow(getActivity(), this);
                taskMenu.show();
                TaskListEntity entity = (TaskListEntity) mTodayAdapter.getChild(groupId, childId);
                taskMenu.setTaskListEntity(entity);
            }

        }
        return true;
    }

    private void onItemChildLongClick(int group, int child) {
        final TaskEditWindow taskMenu = new TaskEditWindow(getActivity(), this);
        taskMenu.show();
        TaskListEntity entity = (TaskListEntity) mAdapter.getChild(group, child);
        taskMenu.setTaskListEntity(entity);
    }

    private void onItemGroupLongClick(int group) {
        final TaskEditWindow taskMenu = new TaskEditWindow(getActivity(), this);
        taskMenu.show();
        TaskListEntity entity = (TaskListEntity) mAdapter.getGroup(group);
        taskMenu.setTaskListEntity(entity);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        // 判断是否为空
        dismissLoading();
        if (response == null) {
            showNoNetWork();
            return;
        }
        // 判断是否是搜索及获取任务列表返回
        if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_PROJECT_TASK_LIST)) {
            handleSearchAndGetTaskListResult(response);
            dismissLoading();
        } else if (taskId.equals(TaskApi.TaskId.TASK_URL_CHANGE_TASK_STATE)) {
//            mExpandAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 处理搜索及获取任务列表返回数据
     *
     * @param response
     */
    private void handleSearchAndGetTaskListResult(Object response) {
        TaskListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskListPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                List<TaskListEntity> list = pojo.getBody().getResponse().getData();
                if (list != null) {
                    if(list.isEmpty()){
                        mTvNoTask.setVisibility(View.VISIBLE);
                        mLvTask.setVisibility(View.GONE);
                        mElvToday.setVisibility(View.GONE);
                        mTvOverTime.setVisibility(View.GONE);
                        mTvTodayTime.setVisibility(View.GONE);
                    }else{
                        mTvNoTask.setVisibility(View.GONE);
                        // 判断是否为今天的任务
                        IsToday(list);
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


    private void IsToday(final List<TaskListEntity> list) {
        mGroupList.clear();
        mChildList.clear();
        mTodayGroupList.clear();
        mTodayChildList.clear();
        if(mIntType == 1){ //今日任务列表
            OrganizationTodayTaskTree(list);
            if(mTodayGroupList.size() > 0){
                mTvTodayTime.setVisibility(View.VISIBLE);
                mElvToday.setVisibility(View.VISIBLE);
                mTodayAdapter.setData(mTodayGroupList,mTodayChildList);
            }else{
                mTvTodayTime.setVisibility(View.GONE);
                mElvToday.setVisibility(View.GONE);
            }
            if(mGroupList.size() > 0){
                mTvOverTime.setVisibility(View.VISIBLE);
                mLvTask.setVisibility(View.VISIBLE);
                mAdapter.setData(mGroupList, mChildList);
            }else{
                mTvOverTime.setVisibility(View.GONE);
                mLvTask.setVisibility(View.GONE);
            }
        }else{//除今日以外任务列表
            mTvTodayTime.setVisibility(View.GONE);
            mTvOverTime.setVisibility(View.GONE);
            mLvTask.setVisibility(View.VISIBLE);
            mElvToday.setVisibility(View.GONE);
            OrganizationTaskTree(list);
            mAdapter.setData(mGroupList, mChildList);
        }
    }


    private void OrganizationTodayTaskTree(List<TaskListEntity> list) {

        List<TaskListEntity> fatherList = new ArrayList<TaskListEntity>();
        List<TaskListEntity> childList = new ArrayList<TaskListEntity>();
        while (!list.isEmpty()) {
            TaskListEntity e = list.remove(0);
            if (e.getLevel() == 1) {
                fatherList.add(e);
            } else {
                childList.add(e);
            }
        }
        //分类过期与未过期任务父
        long nowTime = System.currentTimeMillis();
        for (int i = 0; i < fatherList.size(); i++) {
            long endTime = fatherList.get(i).getEndTime();
            if (Long.valueOf(endTime) != null && endTime > 0) {
                if (TimeFormatUtil.compareTime(nowTime, endTime)) {
                    mGroupList.add(fatherList.get(i));
                } else {
                    mTodayGroupList.add(fatherList.get(i));
                }
            } else {
                mTodayGroupList.add(fatherList.get(i));
            }
        }
        //分类过期父的子任务
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
            mChildList.add(l);
            // 筛选出的结果从原集合中移除
            for (int j = 0; j < l.size(); j++) {
                TaskListEntity e = l.get(j);
                childList.remove(e);
            }
        }
        //分类为过去父的子任务
        for (int i = 0; i < mTodayGroupList.size(); i++) {
            List<TaskListEntity> l = new ArrayList<TaskListEntity>();
            TaskListEntity entity = mTodayGroupList.get(i);
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
            mTodayChildList.add(l);
            // 筛选出的结果从原集合中移除
            for (int j = 0; j < l.size(); j++) {
                TaskListEntity e = l.get(j);
                childList.remove(e);
            }
        }

        if (!childList.isEmpty()) { //将没有没有项目的任务归类
            for (TaskListEntity entity : childList) {
                if (entity.getEndTime() > nowTime) {
                    mTodayGroupList.add(entity);
                } else {
                    mGroupList.add(entity);
                }
            }
        }
//        return list;

    }

    /**
     * 组装其他时间的任务父子列表
     */
    private void OrganizationTaskTree(List<TaskListEntity> list) {
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
        if (!childList.isEmpty()) {
            mGroupList.addAll(childList);
        }
        if (mIntType == 4) {
            Iterator<TaskListEntity> iter = mGroupList.iterator();
            while (iter.hasNext()) {
                TaskListEntity entity = iter.next();
                if (entity.getType().equals("FINISH")) {
                    iter.remove();
                }
            }
        }
    }


    @Override
    public void updateList() {
        updateTaskList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
























