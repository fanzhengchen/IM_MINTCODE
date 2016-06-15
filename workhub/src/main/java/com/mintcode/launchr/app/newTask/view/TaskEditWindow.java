package com.mintcode.launchr.app.newTask.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.activity.NewTaskActivity;
import com.mintcode.launchr.app.newTask.fragment.SubTaskFragment;
import com.mintcode.launchr.app.newTask.fragment.TimeTaskFragment;
import com.mintcode.launchr.pojo.TaskDetailPOJO;
import com.mintcode.launchr.pojo.entity.TaskListEntity;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.DialogLoading;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * Created by JulyYu on 2016/2/24.
 */
public class TaskEditWindow extends Dialog implements View.OnClickListener{
    private Context mContext;

    SubTaskFragment mSubTaskFragment;
    TimeTaskFragment mTimeTaskFragment;
    /** loading */
    private DialogLoading mLoading;
    private TextView mIvEdit;
    private TextView mIvDelect;
    private TaskListEntity mEntity = new TaskListEntity();
    DelectTaskResponse mDelectTaskResponse = new DelectTaskResponse();
    public TaskEditWindow(Context context){
        super(context);
        mContext = context;
        initLoading();
    }

    public TaskEditWindow(Context context, SubTaskFragment Fragment) {
        super(context);
        mContext = context;
        mSubTaskFragment = Fragment;
        initLoading();
    }
    public TaskEditWindow(Context context, TimeTaskFragment Fragment) {
        super(context);
        mContext = context;
        mTimeTaskFragment = Fragment;
        initLoading();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popwindow_edit_task);

        initViews();
    }

    private void initViews() {
        mIvEdit = (TextView) findViewById(R.id.edit_task);
        mIvDelect = (TextView) findViewById(R.id.tv_delete);

        mIvDelect.setOnClickListener(this);
        mIvEdit.setOnClickListener(this);
    }

    public void setTaskListEntity(TaskListEntity entity){
        mEntity = entity;
    }

    @Override
    public void onClick(View v) {
        if(v == mIvDelect){
            String showId = mEntity.getShowId();
            showLoading();
            TaskApi.getInstance().deleteTask(mDelectTaskResponse,showId);
        }else if(v == mIvEdit){
            Intent intent = new Intent(mContext, NewTaskActivity.class);
            intent.putExtra(TaskUtil.KEY_TASK_ID, mEntity.getShowId());
            String parent = mEntity.getProjectId();
            if (parent != null && !parent.equals("")) {
                intent.putExtra(TaskUtil.KEY_PARENT_TASK_ID, parent);
            }
            mContext.startActivity(intent);
            dismiss();
        }
    }

    class DelectTaskResponse implements OnResponseListener{

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            dismissLoading();
            handleDeleteTask(response);
        }

        @Override
        public boolean isDisable() {
            return false;
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
            Toast.makeText(mContext,pojo.getBody().getResponse().getReason(),Toast.LENGTH_SHORT).show();
        }else if(pojo.getBody().getResponse().getData() == null){
            return;
        }else{
            dismiss();
            if(mSubTaskFragment != null){
                mSubTaskFragment.updateTaskList();
            }else if(mTimeTaskFragment != null){
                mTimeTaskFragment.updateTaskList();
            }
        }
    }

    /**
     * 实例化loding
     */
    private void initLoading(){
        if (mLoading == null) {
            mLoading = DialogLoading.creatDialog(mContext);
        }
    }

    /**
     * 显示loading框
     */
    public void showLoading(){
        if(mContext != null){
            if (mLoading != null) {
                if (!mLoading.isShowing()) {
                    mLoading.show();
                    mLoading.start();
                }
            }
        }
    }

    /**
     * 隐藏loading
     */
    public void dismissLoading(){
        if(mContext != null ){
            if (mLoading != null) {
                if (mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }

    }
}
