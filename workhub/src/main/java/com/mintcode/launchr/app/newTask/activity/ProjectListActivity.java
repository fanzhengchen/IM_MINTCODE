package com.mintcode.launchr.app.newTask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.api.TaskApi.TaskId;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.adapter.AllTaskListAdapter;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.TaskAddProjectPOJO;
import com.mintcode.launchr.pojo.TaskProjectListPOJO;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目列表
 * @author KevinQiao
 *
 */
public class ProjectListActivity extends BaseActivity implements OnItemClickListener{
	/** 返回按钮  */
	private ImageView mIvRightBack;
	
	private ListView mLvTop;
	
	private AllTaskListAdapter mAdapterTop;
	
	private List<TaskProjectListEntity> mListTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);
		
		initAllView();

		initListView();
		
		initData();
	}

	public void initAllView() {
		mLvTop = (ListView) findViewById(R.id.lv_project);
		
		mIvRightBack = (ImageView) findViewById(R.id.iv_back);
		mIvRightBack.setOnClickListener(this);
	}

	public void initListView() {	
		mListTop = new ArrayList<TaskProjectListEntity>();
		
		
		mAdapterTop = new AllTaskListAdapter(getBaseContext(), mListTop);
		mLvTop.setAdapter(mAdapterTop);
		mLvTop.setOnItemClickListener(this);
	}

	public void initData() {
		showLoading();
		TaskApi.getInstance().getProjectList(this, "0", "0", "");
	}

	@Override
	public void onClick(View v) {
		if (v == mIvRightBack) {
			finish();
		} 
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		// TODO Auto-generated method stub
		super.onResponse(response, taskId, rawData);
		//判断是否为空
		if(response == null){
			showNoNetWork();
			return;
		}
		
		//得到项目列表
		if(taskId.equals(TaskId.TASK_URL_GET_PROJECT_LIST)){
			handleResultTaskList(response);			
			dismissLoading();
		}
		
	}
	
	//处理项目列表返回数据
	public void handleResultTaskList(Object response){		
		TaskProjectListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskProjectListPOJO.class);
        if(pojo != null){
        	if(pojo.getBody().getResponse().isIsSuccess()){
        		mListTop = pojo.getBody().getResponse().getData();
        		if(mListTop != null){
        			// 更新列表
					TaskProjectListEntity entity = new TaskProjectListEntity();
					entity.setName(getResources().getString(R.string.calendar_nothing));
					mListTop.add(0,entity);
        	        mAdapterTop = new AllTaskListAdapter(getBaseContext(), mListTop);
        			mLvTop.setAdapter(mAdapterTop);
        		}
        	} else {       		
        		toast(pojo.getBody().getResponse().getReason());
        	}
        } else {
        	showNetWorkGetDataException();
        }      
	}
	
	// 处理删除某个项目返回数据
	public void handleResultTaskDeleteProject(Object response){
		TaskAddProjectPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskAddProjectPOJO.class);
		if(pojo != null){
	    	if(pojo.getBody().getResponse().isIsSuccess()){
	    		initData();
	    	}else{
	    		toast(pojo.getBody().getResponse().getReason());
	    	}
	    }else{
	    	showNetWorkGetDataException();
	    }
	}

	// ListView的监听事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent == mLvTop) {
			Intent intent = new Intent();
			TaskProjectListEntity entity = mListTop.get(position);
			intent.putExtra(TaskUtil.KEY_TASK_PROJECT, entity);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

}



