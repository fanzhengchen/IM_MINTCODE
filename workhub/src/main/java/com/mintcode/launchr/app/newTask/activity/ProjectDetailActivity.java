package com.mintcode.launchr.app.newTask.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.adapter.ProjectPersonAdapter;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.pojo.TaskAddProjectPOJO;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/25.
 * 任务项目的详情页面
 */
public class ProjectDetailActivity extends BaseActivity implements  ContactFragment.OnSelectContactListner, ProjectPersonAdapter.OnGroupShowFragmentListener, ContactFragment.OnIsActivityListener {

    private EditText mTvProjectName;
    private ImageView mIvBack;
    private TextView mTvSave;
    private GridView mGvPerson;
    private TextView mTvDelect;
    private ProjectPersonAdapter mAdapter;

    private List<TaskAddProjectEntity.TaskAddProjectMembersEntity> mOldPersonList = new ArrayList<>();
    private TaskAddProjectEntity mProjectDetail;

    // 通讯录
    private ContactFragment mContactFragment;
    private FragmentManager mFragManger;

    public static String PROJECT_DETAIL = "project_detail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_project_detail);
        initView();
        initData();
    }



    private void initView(){
        mTvProjectName = (EditText)findViewById(R.id.tv_project_name);
        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mTvSave = (TextView)findViewById(R.id.tv_save);
        mTvDelect = (TextView)findViewById(R.id.tv_delect);
        mGvPerson = (GridView)findViewById(R.id.gv_person);

        mTvDelect.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mContactFragment = new ContactFragment();
        mContactFragment.setOnSelectContactListner(this);
        mContactFragment.setOnIsActivityListener(this);
        mContactFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE, null);
        mFragManger = getSupportFragmentManager();
        mFragManger.beginTransaction().add(R.id.lin_group_create_group, mContactFragment)
                .hide(mContactFragment).commit();

        hideSoftInput();
    }

    private void initData() {
        Intent detail = getIntent();
        mProjectDetail = (TaskAddProjectEntity)detail.getSerializableExtra(PROJECT_DETAIL);
        if(mProjectDetail != null){
            mOldPersonList = mProjectDetail.getMembers();
            mTvProjectName.setText(mProjectDetail.getName());
        }
        mAdapter = new ProjectPersonAdapter(this, mOldPersonList);
        mGvPerson.setAdapter(mAdapter);
        mAdapter.setOnGroupShowFragment(this);
    }

    /** 隐藏输入法*/
    private void hideSoftInput(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mTvProjectName.getWindowToken(), 0);
    }

    private void editProject() {
        String name = mTvProjectName.getText().toString();
        if(name == null || name.equals("")){
            toast(getString(R.string.project_name_not_space));
            return;
        }
        String showId = mProjectDetail.getShowId();
        List<TaskAddProjectEntity.TaskAddProjectMembersEntity> newList = new ArrayList<>();
        List<TaskAddProjectEntity.TaskAddProjectMembersEntity> delectList = new ArrayList<>();
        newList.addAll(mAdapter.getPersonList());
        delectList.addAll(mAdapter.getPersonList());
        //新增人员
        newList.removeAll(mOldPersonList);
        //删除人员
        mOldPersonList.removeAll(delectList);
        if (mOldPersonList != null){
            List<String> namelist = new ArrayList<>();
            for(TaskAddProjectEntity.TaskAddProjectMembersEntity entity : mOldPersonList){
                namelist.add(entity.getMemberName());
            }
            showLoading();
            TaskApi.getInstance().editProject(this, showId, name, namelist, newList);
        }else{
            showLoading();
            TaskApi.getInstance().editProject(this, showId, name, null, newList);
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){// 返回
            this.finish();
        }else if(v == mTvDelect){// 删除项目
            showLoading();
            String showId = mProjectDetail.getShowId();
            TaskApi.getInstance().deleteProject(this,showId);
        }else if(v == mTvSave){// 编辑修改
            editProject();
        }

    }



    // 处理删除某个项目返回数据
    public void handleResultTaskDeleteProject(Object response) {
        TaskAddProjectPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskAddProjectPOJO.class);
        if (pojo != null) {
            if (pojo.getBody() != null) {
                if (pojo.getBody().getResponse().isIsSuccess()) {
                    toast(getResources().getString(R.string.apply_comment_delect_success_toast));
                    Intent intent = new Intent();
                    intent.putExtra("type", 1);
                    setResult(RESULT_OK, intent);
                    this.finish();
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
    // 编辑项目返回
    private void handleResultTaskEditProject(Object response) {
        TaskAddProjectPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskAddProjectPOJO.class);
        if (pojo != null) {
            if (pojo.getBody() != null) {
                if (pojo.getBody().getResponse().isIsSuccess()) {
                    Intent intent = new Intent();
                    intent.putExtra("type", 2);
                    intent.putExtra("name", mTvProjectName.getText().toString());
                    setResult(RESULT_OK, intent);
                    this.finish();
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
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        // 判断是否为空
        if (response == null) {
            showNoNetWork();
            return;
        }

        if (taskId.equals(TaskApi.TaskId.TASK_URL_DELETE_PROJECT)) {// 删除某个项目
            handleResultTaskDeleteProject(response);
        }else if(taskId.equals(TaskApi.TaskId.TASK_URL_EDIT_PROJECT)){// 编辑项目
            handleResultTaskEditProject(response);
        }
    }




    @Override
    public void onGroupShowFragment() {
            mFragManger.beginTransaction().show(mContactFragment).commit();
             mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, null);
            setUnSelectUser();
    }

    /**
     * 设置不可选择的人
     */
    private void setUnSelectUser(){
        List<TaskAddProjectEntity.TaskAddProjectMembersEntity> list = mAdapter.getPersonList();
        if(list != null && !list.isEmpty()){
            List<UserDetailEntity> l = new ArrayList<>();
            for(int i = 0;i < list.size();i ++){
                TaskAddProjectEntity.TaskAddProjectMembersEntity g = list.get(i);
                UserDetailEntity u = new UserDetailEntity();
                u.setShowId(g.getMemberName());
                l.add(u);
            }
            mContactFragment.setCannotSelectUser(l);

        }
    }
    @Override
    public void onSelectContact(List<UserDetailEntity> userList) {
        if(userList!=null && userList.size()>0){
            selectRequireUser(userList);
        }
    }
    private void selectRequireUser(List<UserDetailEntity> userList) {
        if (userList != null) {
            List<TaskAddProjectEntity.TaskAddProjectMembersEntity> PersonList = new ArrayList<>();
            for (UserDetailEntity entity : userList) {
                TaskAddProjectEntity.TaskAddProjectMembersEntity person = new TaskAddProjectEntity.TaskAddProjectMembersEntity();
                String showId = entity.getShowId();
                String userName = entity.getTrueName();
                person.setMemberName(showId);
                person.setMemberTrueName(userName);
                PersonList.add(person);
            }
            mAdapter.addPersonList(PersonList);
        }
    }

    @Override
    public void onBackPressed() {
        boolean hidden = mContactFragment.isHidden();
        if (hidden) {
            ContactFragment.mSelectState = 1;
            finish();
        } else {
            mFragManger.beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
        }
    }
    @Override
    public void onIsActivity() {
        boolean hidden = mContactFragment.isHidden();
        if (hidden) {
        } else {
            mFragManger.beginTransaction().hide(mContactFragment).commit();
        }
    }
}
