package com.mintcode.launchr.contact.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 组织架构activity
 * @author KevinQiao
 *
 */
public class ContactSearchActivity extends BaseActivity implements OnItemClickListener{

	/** 返回按钮 */
	private ImageView mIvBack;
	
	/** 组织架构listview*/
	private ListView mLvOrganization;
	
	/** 搜索框 */
	private EditText mEtSearch;
	
	/** 搜索按钮 */
	private TextView mTvSearch;
	
	/** 搜索  */
	private RelativeLayout mRelSearch;
	
	/** 组织架构适配器 */
	private OrganizationAdapter mAdapter;
	
	/** 数据集 */
	private List<UserDetailEntity> mSearchList;
	
	
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
		mTvSearch = (TextView) findViewById(R.id.tv_search);
		mEtSearch = (EditText) findViewById(R.id.et_search);
		mRelSearch = (RelativeLayout) findViewById(R.id.rel_search);
		
		mAdapter = new OrganizationAdapter();
		mLvOrganization.setAdapter(mAdapter);
		mLvOrganization.setOnItemClickListener(this);
		mIvBack.setOnClickListener(this);
		mTvSearch.setOnClickListener(this);
		mRelSearch.setVisibility(View.VISIBLE);
	}
	
	private void initData(){
		mSearchList = new ArrayList<UserDetailEntity>();
		
	}
	
	private void search(){
		showLoading();
		String searchKey = mEtSearch.getText().toString().trim();
		mSearchList.clear();
		UserApi.getInstance().getSearchUserList(this, searchKey);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			finish();
		}else if(v == mTvSearch){
			search();
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
		if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
			handleResultUserList(response);
			dismissLoading();
		}
		
		
	}
	
	/**
	 * 处理我的部门返回
	 * @param response
	 */
	private void handleResultUserList(Object response){
		UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<UserDetailEntity> list = pojo.getBody().getResponse().getData();
				
				if (list != null) {
					mSearchList.addAll(list);
					mAdapter.notifyDataSetChanged();
				}
				
			}else{
				HeaderEntity entity = pojo.getHeader();
				toast(entity.getReason());
			}
			
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	
	String path = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80";
	class OrganizationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mSearchList.size();
		}

		@Override
		public Object getItem(int position) {
			return mSearchList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_contact, null);
				hold = new ViewHold();
				hold.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				hold.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				hold.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				
				convertView.setTag(hold);
				
			}else{
				hold = (ViewHold) convertView.getTag();
			}
			
			String url = path + "/7ae0c298-4e51-49c1-a2e6-31509069dd32.jpg";;
			
			// 处理圆形图片
			RequestManager man = Glide.with(ContactSearchActivity.this);
			GlideRoundTransform g =  new GlideRoundTransform(ContactSearchActivity.this,18);
			man.load(url).transform(g).into(hold.ivIcon);
			
			UserDetailEntity entity = mSearchList.get(position);
			
			hold.cbCheck.setVisibility(View.GONE);
			
			
			
			hold.tvName.setText(entity.getTrueName());
			
			return convertView;
		}
		
		
	}
	
	class ViewHold{
		TextView tvName;
		ImageView ivIcon;
		ImageView cbCheck;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 跳转详情页
		
		
		
	}
	
	
	
	
	
}
