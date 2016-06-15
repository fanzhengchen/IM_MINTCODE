package com.mintcode.launchr.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;

import com.mintcode.launchr.app.newApproval.activity.ApprovalMainActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;
import com.mintcode.launchr.app.newTask.activity.TaskMainActivity;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.util.CompanyAppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;

/**
 * 应用主界面
 * @author KevinQiao
 *
 */
public class AppFragment extends BaseFragment implements OnItemClickListener{

	/** 应用名称资源数组 */
	private int[] mStrResouceArray = {R.string.app_apply,R.string.app_schedule,R.string.app_task};
	
	/** 应用图标资源数组 */
	private int[] mImgResouceArray = {R.drawable.icon_app_apply,R.drawable.icon_app_schedule,R.drawable.icon_app_task};
	
	/** 应用类数组*/
//	private Class[] mClaArray = {NoteActivity.class,ScheduleActivity.class,DepartmentActivity.class};TaskActivity
	private Class[] mClaArray = {ApprovalMainActivity.class,ScheduleMainActivity.class,TaskMainActivity.class};
	//ApprovalMainActivity ApprovalActivity

	
	/** gridview */
	private GridView mGvApp;
	
	/** 根view */
	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// 实例化view
//		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_app, null);
//		}
		
		// 判断是否加载过
//		ViewGroup parent = (ViewGroup) mRootView.getParent();
//		if (parent != null) {
//			parent.removeView(mRootView);
//		}
		
		return mRootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView();
	}
	
	/**
	 * 实例化view
	 */
	private void initView(){
		mGvApp = (GridView) mRootView.findViewById(R.id.gv_app);
		
		AppAdapter adapter = new AppAdapter(getActivity().getLayoutInflater());
		mGvApp.setAdapter(adapter);
		mGvApp.setOnItemClickListener(this);
		mGvApp.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	
	/**
	 * 适配器
	 * @author KevinQiao
	 *
	 */
	private class AppAdapter extends BaseAdapter{
		
		public LayoutInflater inflat;
		
		public AppAdapter(LayoutInflater inflat) {
			this.inflat = inflat;
		}
		
		@Override
		public int getCount() {
			return mStrResouceArray.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflat.inflate(R.layout.item_app, null);
			ImageView ivApp = (ImageView) convertView.findViewById(R.id.iv_app);
			TextView tvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
			
			//TODO 设置数据
			
			// 获取应用名称
			int strId = mStrResouceArray[position];
			String name = getResources().getString(strId);
			tvAppName.setText(name);
			tvAppName.setTextColor(getResources().getColor(R.color.black));
			
			// 获取应用图标
			setAppIcon(position, ivApp);
			
			return convertView;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent intent = new Intent(getActivity(), mClaArray[position]);
		startActivity(intent);
	}
	
	/** 显示app的图标*/
	private void setAppIcon(int position, ImageView mIvIcon){
		String appId = null;
		if(position == 0){
			appId = CompanyAppUtil.getInstance(getActivity()).getAppIcon(Const.SHOWID_APPROVE);
		}else if(position == 1){
			appId = CompanyAppUtil.getInstance(getActivity()).getAppIcon(Const.SHOW_SCHEDULE);
		}else if(position == 2){
			appId = CompanyAppUtil.getInstance(getActivity()).getAppIcon(Const.SHOWID_TASK);
		}
		HeadImageUtil.getInstance(getActivity()).setChatAppIcon(mIvIcon, appId, 2, 150, 150);
	}
}
