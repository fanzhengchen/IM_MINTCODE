package com.mintcode.launchr.contact.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.DeptFragment;
import com.mintcode.launchr.pojo.OrganizationPOJO;
import com.mintcode.launchr.pojo.entity.OrganizationEntity;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 组织架构activity
 * @author KevinQiao
 *
 */
public class OrganizationActivity extends BaseActivity implements OnItemClickListener{

	/** 返回按钮 */
	private ImageView mIvBack;
	
	/** 组织架构listview*/
	private ListView mLvOrganization;
	
	/** 组织架构适配器 */
	private OrganizationAdapter mAdapter;
	
	/** 数据集 */
	private List<OrganizationEntity> mList;
	
	
	private List<DeptFragment> mDList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization);
		
		initData();
		initView();
		
	}
	
	
	private void initView(){
		mLvOrganization = (ListView) findViewById(R.id.lv_organize);
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		
		mAdapter = new OrganizationAdapter();
		mLvOrganization.setAdapter(mAdapter);
		mLvOrganization.setOnItemClickListener(this);
		mIvBack.setOnClickListener(this);
	}
	
	private void initData(){
		mList = new ArrayList<OrganizationEntity>();
		mDList = new ArrayList<DeptFragment>();
		
		showLoading();
		UserApi.getInstance().getComanyDeptList(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			finish();
		}
	}
	
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		// 判断是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}
		
		// 判断是否是获取部门列表返回
		if (taskId.equals(TaskId.TASK_URL_GET_COMANY_DEPT_LIST)) {
			handleDeptListResult(response);
			dismissLoading();
		}
		
		
	}
	
	/**
	 * 处理部门返回
	 * @param response
	 */
	private void handleDeptListResult(Object response){
		OrganizationPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), OrganizationPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<OrganizationEntity> list = pojo.getBody().getResponse().getData();
				if (list != null) {
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				}
			}
			
			toast(pojo.getHeader().getReason());
		}else{
			showNetWorkGetDataException();
		}
		
		
	}
	
	
	class OrganizationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_textview, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// 绑定数据集
			OrganizationEntity entity = mList.get(position);
			
			holder.tvName.setText(entity.getD_NAME());
			
			
			return convertView;
		}
		
	}
	
	class ViewHolder{
		TextView tvName;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OrganizationEntity entity = mList.get(position);
		
		FragmentManager manger = getSupportFragmentManager();
		DeptFragment d = new DeptFragment(entity.getSHOW_ID());
		FragmentTransaction transaction = manger.beginTransaction();
		// 添加动画

		
		transaction.add(R.id.ll_fragment, d)
								 .show(d).commit();
		mDList.add(d);
	}
	
	@Override
	public void onBackPressed() {
		if (mDList.isEmpty()) {
			super.onBackPressed();
		}else{
			remove();
		}
	}
	
	private void remove(){
		FragmentManager manger = getSupportFragmentManager();
		DeptFragment d = mDList.remove(0);
		FragmentTransaction transaction = manger.beginTransaction();
		// 添加动画
//		transaction.setCustomAnimations(R.anim.slid_in_right, R.anim.slid_out_right);
		transaction.remove(d) .commit();
	}
	
}
