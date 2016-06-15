package com.mintcode.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class GroupInfoListActivtiy extends BaseActivity implements AdapterView.OnItemClickListener,OnResponseListener {

	/** 返回按钮 */
	private ImageView mIvBack;

	/** 搜索按钮 */
	private ImageView mIvSearch;

	/** 搜索输入框 */
	private EditText mEtSearch;


	/** listview */
	private ListView mLvGroupList;

	/** 数据适配器 */
	private GroupListAdapter mAdapter;

	/** 数据集 */
	private List<GroupInfo> mGroupList = null;

	/** 群信息 */
	private GroupInfo mGroupInfo;

	/** session*/
	private String mSessionName;

	/** username */
	private String mUserName;

	/** Token */
	private String mUserToken;

	/** 所有人info */
	private GroupInfo mAllInfo;

	/** handler */
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info_list);

		initData();
		initView();

	}

	/**
	 * 实例化数据
	 */
	private void initData(){
		Intent intent = getIntent();
		mSessionName = intent.getStringExtra("sessionName");
		AppUtil util = AppUtil.getInstance(this);
		mUserName = util.getShowId();
		mUserToken = util.getIMToken();
		mGroupInfo = (GroupInfo) intent.getSerializableExtra("groupInfo");
		mAllInfo = new GroupInfo();
		mAllInfo.setUserName("All");
		mAllInfo.setNickName("全体成员");

		if(mGroupInfo != null){
			String memberJson = mGroupInfo.getMemberJson();
			mGroupList = JsonUtil.convertJsonToObj(memberJson, new TypeReference<List<GroupInfo>>() {});

			// 判断是否是群主
			boolean b = isTheFirstCaptain(mGroupList);
			mGroupList.add(0,mAllInfo);
			if(b){
//				mGroupList.add(0,mAllInfo);
			}
			if(mGroupList != null){
				String username = LauchrConst.header.getLoginName();
				for(int i = 0;i < mGroupList.size();i++){
					GroupInfo g = mGroupList.get(i);
					if(username.equals(g.getUserName())){
						mGroupList.remove(g);
						break;
					}
				}
			}


		}else{
			IMAPI.getInstance().getGroupInfo(this, mUserName, mUserToken, mSessionName);
		}

		mHandler = new Handler();
	}


	/**
	 * 返回是否是群第一人（群主）
	 * @param list
	 * @return
	 */
	private boolean isTheFirstCaptain(List<GroupInfo> list){
		if(mGroupList != null && !mGroupList.isEmpty()){
			GroupInfo g = mGroupList.get(0);
			String gUserName = g.getUserName();
			String loginName = LauchrConst.header.getLoginName();
			if(loginName != null && gUserName != null){
				if(loginName.equals(gUserName)){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 实例化view
	 */
	private void initView(){
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mLvGroupList = (ListView) findViewById(R.id.lv_group_list);
		mAdapter = new GroupListAdapter(mGroupList);
		mLvGroupList.setAdapter(mAdapter);
		mIvSearch = (ImageView) findViewById(R.id.iv_search);
		mEtSearch = (EditText) findViewById(R.id.edt_search);
		Watcher watcher = new Watcher();
		mEtSearch.addTextChangedListener(watcher);


		mIvBack.setOnClickListener(this);
		mIvSearch.setOnClickListener(this);
		mLvGroupList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GroupInfo info = (GroupInfo) parent.getAdapter().getItem(position);
		Intent intent = new Intent();
		intent.putExtra("choose_person",info);
		setResult(RESULT_OK, intent);
		finish();
	}


	@Override
	public void onClick(View v) {
		if (v == mIvBack){
			onBackPressed();
		}else if(v == mIvSearch){
			if(View.VISIBLE == mIvSearch.getVisibility()){
				mIvSearch.setVisibility(View.GONE);
				mEtSearch.setVisibility(View.VISIBLE);
			}
		}
	}

	private class GroupListAdapter extends BaseAdapter{

		private List<GroupInfo> groupList = null;

		public GroupListAdapter(List<GroupInfo> list) {
			if(list != null){
				groupList = list;
			}

		}


		public void changeData(List<GroupInfo> list){
			if(list != null){
				groupList = list;
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			int count = groupList == null? 0 : groupList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return groupList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.item_group_info_list,null);
				ViewHolder holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);


				holder.tvWord.setVisibility(View.GONE);
				holder.cbCheck.setVisibility(View.GONE);
				holder.tvDept.setVisibility(View.GONE);


				convertView.setTag(holder);
			}

			//
			ViewHolder holder = (ViewHolder) convertView.getTag();
			GroupInfo info = (GroupInfo) getItem(position);

			// 判断当@所有人时候头像不显示
			if(info.getNickName().equals("全体成员")){
				holder.ivIcon.setVisibility(View.GONE);
			}else{
				holder.ivIcon.setVisibility(View.VISIBLE);
			}

			// 设置头像
			HeadImageUtil.getInstance(GroupInfoListActivtiy.this).setAvatarResourceAppendUrl(holder.ivIcon,info.getUserName(),2,60,60);
			holder.tvName.setText(info.getNickName());
			return convertView;
		}
	}


	class ViewHolder {
		TextView tvName;
		TextView tvDept;
		TextView tvWord;
		ImageView ivIcon;
		ImageView cbCheck;
	}


	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if(response == null){
			showNoNetWork();
			return;
		}

		if (taskId.equals(IMAPI.TASKID.SESSION)) {
			GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
			if (pojo.isResultSuccess()) {
				pojo.getData().setMemberJson(TTJSONUtil.convertObjToJson(pojo.getData().getMemberList()));
				mGroupInfo = pojo.getData();
				GroupInfoDBService.getIntance().put(mGroupInfo);
				List<GroupInfo> memberList = mGroupInfo.getMemberList();
				// 判断是否是群主
				boolean b = isTheFirstCaptain(memberList);
				if(b){
					memberList.add(0,mAllInfo);
				}
				if(mGroupList != null){
					mGroupList.clear();
				}else {
					mGroupList = new ArrayList<>();
				}
				mGroupList.addAll(memberList);
				mAdapter.changeData(memberList);
			}
		}
	}

	@Override
	public void onBackPressed() {
		if(mEtSearch.getVisibility() == View.VISIBLE){
			mEtSearch.setVisibility(View.GONE);
			mIvSearch.setVisibility(View.VISIBLE);
		}else{
			super.onBackPressed();
		}

	}

	class Watcher implements TextWatcher{

		private String str = "";
		Runnable r = new Runnable() {

			@Override
			public void run() {
				handleSearch(str);
			}
		};

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			str = s.toString().trim();
			mHandler.removeCallbacks(r);
			mHandler.postDelayed(r, 500);
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	/**
	 * 处理搜索
	 * @param str
	 */
	private void handleSearch(String str){
		if(str != null && !str.equals("")){
			List<GroupInfo> list = new ArrayList<GroupInfo>();
			String text = str.toLowerCase();
			for(int i = 0; i < mGroupList.size(); i++){
				GroupInfo info = mGroupList.get(i);
				if(info.getNickName().toLowerCase().contains(text)){
					list.add(info);
				}
			}
			mAdapter.changeData(list);
		}else {
			mAdapter.changeData(mGroupList);
		}



	}

}
