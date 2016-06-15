package com.mintcode.launchr.contact.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.im.database.ContactDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.ContactPersonOfDeptActivity;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.pojo.DepartmentPOJO;
import com.mintcode.launchr.pojo.UserUpdatePOJO;
import com.mintcode.launchr.pojo.entity.DepartmentEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserUpdateEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.UserUpdateUtil;

/**
 * 通讯录
 * @author KevinQiao
 *
 */
public class ContactDeptFragment extends BaseFragment implements OnItemClickListener{

	
	/** 根view */
	private View mView;
	
	/** listview 按部门*/
	private ListView mLvDept;
	
	private ListView mLvUser;
	
	/** 适配器 */
	private ContactDeptAdapter mContactDeptAdapter;
	
	private DeptUserAdapter mDeptUserAdapter;
	
	public static List<List<UserDetailEntity>> mDeptList = new ArrayList<List<UserDetailEntity>>();
	
	public static List<UserDetailEntity> mDeptPersonList = new ArrayList<UserDetailEntity>();
	
	public static List<DepartmentEntity> mDataList = new ArrayList<DepartmentEntity>();
	
	public List<List<DepartmentEntity>> mFirstList = new ArrayList<List<DepartmentEntity>>();
	
	public List<DepartmentEntity> mAdapterList = new ArrayList<DepartmentEntity>();

	public List<UserDetailEntity> mUserList = new ArrayList<UserDetailEntity>();
	
	
	private boolean mIsFirst = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_contact_dept, null);
//		if (mView == null) {
//			mIsFirst = true;
//		}else{
//			mIsFirst = false;
//		}
//		
//		ViewGroup parent = (ViewGroup) mView.getParent();
//		if (parent != null) {
//			parent.removeView(mView);
//		}
//			
		return mView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		initData();
	}
	
	
	
	private void initView(){
		mLvDept = (ListView) mView.findViewById(R.id.lv_dept);
		mContactDeptAdapter = new ContactDeptAdapter();
		mLvUser = (ListView) mView.findViewById(R.id.lv_user);
		mDeptUserAdapter = new DeptUserAdapter();
		mLvUser.setAdapter(mDeptUserAdapter);
		
		mLvDept.setAdapter(mContactDeptAdapter);
		mLvDept.setOnItemClickListener(this);
		mLvUser.setOnItemClickListener(this);

	}

	// 首先查找数据库
	public void initData(){
		/** 因为是静态字段，因此重新初始化*/
		mDeptList = new ArrayList<List<UserDetailEntity>>();
		mDeptPersonList = new ArrayList<UserDetailEntity>();
		mDataList = new ArrayList<DepartmentEntity>();

		List<DepartmentEntity> list = ContactDBService.getInstance().getParentDepartment();
		if(list!=null && list.size()>0){
			mDataList.addAll(list);
			notifyAdater();

			String showID = LauchrConst.header.getCompanyShowID();
			UserApi.getInstance().deptLastUpdate(this, showID);
		}else{
			String showID = LauchrConst.header.getCompanyShowID();
			UserApi.getInstance().getComanyDeptList(this,showID);
		}
	}

	public void sendBroadcast(){
		Intent intent = new Intent(ContactFragment.CONTACT_ACTION);
		getActivity().sendBroadcast(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 判断选择状态是否打开
//		List<UserDetailEntity> list = (List<UserDetailEntity>) parent.getAdapter().getItem(position);
//		if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE) {
////			boolean b = ContactFragment.mSelectList.containsAll(list);
////			if (b) {
////				ContactFragment.mSelectList.removeAll(list);
////			}else{
////				ContactFragment.mSelectList.addAll(list);
////			}
////			sendBroadcast();
//			mContactDeptAdapter.notifyDataSetChanged();
//		}else{
//			mDeptPersonList.clear();
//			mDeptPersonList.addAll(list);
			
			if (parent == mLvDept) {
				DepartmentEntity entity =  (DepartmentEntity) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), ContactPersonOfDeptActivity.class);
				intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_ID, entity.getShowId());
				intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_NAME, entity.getdName());

				startActivityForResult(intent, 13);
			}else if (parent == mLvUser) {
				UserDetailEntity entity = (UserDetailEntity) parent.getAdapter().getItem(position);
				// 多选模式
				if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE) {
//					holder.cbCheck.setVisibility(View.VISIBLE);
					boolean b = ContactFragment.mSelectList.contains(entity);
					if (b) {
						ContactFragment.mSelectList.remove(entity);
					}else{
						ContactFragment.mSelectList.add(entity);
					}
					sendBroadcast();
					
				}else 
				// 单选模式
				if (ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE) {
					boolean b = ContactFragment.mSelectList.contains(entity);
					if (b) {
						ContactFragment.mSelectList.remove(entity);
					}else{
						ContactFragment.mSelectList.clear();
						ContactFragment.mSelectList.add(entity);
					}
					sendBroadcast();
				}else{
					Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
//					intent.putExtra(LauchrConst.KEY_USERENTITY, entity);
					intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getShowId());
					startActivity(intent);
				}
				
			}
			
	}
	
	class ContactDeptAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mAdapterList.size();
		}

		@Override
		public Object getItem(int position) {
			return mAdapterList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_dept_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				holder.relDept = (RelativeLayout) convertView.findViewById(R.id.rel_dept);
				holder.tvDeptNum = (TextView) convertView.findViewById(R.id.tv_dept_num);
				holder.tvDeptName = (TextView) convertView.findViewById(R.id.tv_dept_name);
				
				convertView.findViewById(R.id.ll_name).setVisibility(View.GONE);
				holder.relDept.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
//			// 设置数据
			DepartmentEntity entity = mAdapterList.get(position);
			holder.tvDeptName.setText(entity.getdName());
			
//			List<DepartmentEntity> list = mFirstList.get(position);
//			List<UserDetailEntity>l = mDeptList.get(position);
			
			holder.tvDeptNum.setText("");
			
//			List<UserDetailEntity> list = mDeptList.get(position);
//			UserDetailEntity entity = list.get(0);
//			// 设置部门名称
//			if (entity.getD_NAME() != null) {
//				holder.tvDeptName.setText(entity.getD_NAME());
//			}else{
//			}
//			holder.tvDeptNum.setText(list.size() + "");
//			holder.tvDeptName.setText("");
			
			// 判断选人状态是否打开
			// 设置选中状态
			if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE) {
//				holder.cbCheck.setVisibility(View.VISIBLE);
//				boolean b = ContactFragment.mSelectList.containsAll(list);
//				Log.i("infos", b + "===");
//				holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
//				if () {
//					holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
//				}else{
//				}
			}else{
				holder.cbCheck.setVisibility(View.GONE);
			}
			holder.cbCheck.setVisibility(View.GONE);
			
			
			return convertView;
		}

		
		
		
	}
	
	class ViewHolder {
		TextView tvName;
		TextView tvDept;
		TextView tvWord;
		ImageView ivIcon;
		ImageView cbCheck;
		RelativeLayout relDept;
		TextView tvDeptName;
		TextView tvDeptNum;
		
		TextView tvNameOfDept;
	}
	
	class DeptUserAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mUserList.size();
		}

		@Override
		public Object getItem(int position) {
			return mUserList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
  
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				holder.tvNameOfDept = (TextView) convertView.findViewById(R.id.tv_name_of_dept);
				
				holder.tvName.setVisibility(View.GONE);
				holder.tvDept.setVisibility(View.GONE);
				holder.tvWord.setVisibility(View.GONE);
				holder.tvNameOfDept.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// 设置数据
			UserDetailEntity entity = mUserList.get(position);
			
			// 设置姓名
			holder.tvNameOfDept.setText(entity.getTrueName());
			
			
			// 设置头像
//			if (position % 3 == 0) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/df431690-b13c-4ab4-990b-fc811e679432.jpg";
//				setImageResuces(holder.ivIcon, url);
//			}else if (position % 3 == 1) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/da219741-9840-438e-b298-a90d7ff5ff5d.jpg";
//				setImageResuces(holder.ivIcon, url);
//			}else if (position % 3 == 2) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/5aa47427-b792-4cad-ae7d-0eaf4ea3a5a8.jpg";
//			}
			
			// 设置头像
			String companyCode = LauchrConst.header.getCompanyCode();
			String mHeadPicUrl = LauchrConst.ATTACH_PATH +"/Base-Module/Annex/Avatar?width=80&height=80&companyCode=" 
					 + companyCode + "&userName=" + entity.getShowId();
			setImageResuces(holder.ivIcon, mHeadPicUrl);
			
			
			// 设置选中状态
			// 多选模式
			if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE
				|| 	ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE
				|| 	ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE
					) {
				holder.cbCheck.setVisibility(View.VISIBLE);
				boolean b = ContactFragment.mSelectList.contains(entity);
				Log.i("mes", ContactFragment.mSelectList.size() + "======" + b);
				if (b) {
					holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
					Log.i("infos", "======" + b);
				}else{
					holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
				}
			}else{
				holder.cbCheck.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		
	}
	
	
	private void setImageResuces(ImageView iv, String url){
		// 处理圆形图片
		RequestManager man = Glide.with(this);
		GlideRoundTransform g =  new GlideRoundTransform(getActivity(),3);
		man.load(url).transform(g).into(iv);
	}
	
	/**
	 * 更新数据适配器
	 */
	//TODO 无用方法
	public String getddData(){
		List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
		list.addAll(ContactFragment.mDataList);
		mDeptList.clear();
		while (!list.isEmpty()) {
			List<UserDetailEntity> l = new ArrayList<UserDetailEntity>();
			UserDetailEntity  entity = list.remove(0);
			String deptId = entity.getDeptId();
			l.add(entity);
			// 判断是否是为空
			if ((deptId != null) && (!deptId.equals(""))) {
				for (int i = 0; i < list.size(); i++) {
					UserDetailEntity  e = list.get(i);
					String eDeptId = e.getDeptId();
					// 判断是否为空
					if ((eDeptId != null) && (!eDeptId.equals(""))) {
						if (deptId.equals(eDeptId)) {
							l.add(e);
						}
					}
					
				}
			}
			
			mDeptList.add(l);
			// 执行完毕，从原集合移除
//			Log.i("infos", list.size() + "===" + l.size());
			list.removeAll(l);
//			Log.i("infos", list.size() + "----" + l.size());
		}
		if (mContactDeptAdapter != null) {
			mContactDeptAdapter.notifyDataSetChanged();
		}else{
			Log.i("infos",  "==mContactDeptAdapter= null" );
		}
		return "" ;
	}
	
	public void notifyAdater(){
//		mDataList.clear();
		List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
		if(ContactFragment.mDataList != null){
			list.addAll(ContactFragment.mDataList);
		}
		mDeptList.clear();
		mAdapterList.clear();
		mUserList.clear();
		
		// 筛选数据
		List<DepartmentEntity> deptList = new ArrayList<DepartmentEntity>();
		deptList.addAll(mDataList);
		
		for (int i = 0; i < mDataList.size(); i++) {

			DepartmentEntity department = mDataList.get(i);
			String departShowId = department.getShowId();
			if (department.getdLevel().equals("1")) {
				mAdapterList.add(department);
				List<UserDetailEntity> l = new ArrayList<UserDetailEntity>();
				for (int j = 0; j < list.size(); j++) {
					UserDetailEntity userDetail = list.get(j);
					String deptId = userDetail.getDeptId();
					if ((deptId != null) && deptId.equals(departShowId)) {
						l.add(userDetail);
						continue;
					}
				}
				
				mDeptList.add(l);
				if (!l.isEmpty()) {
				}
				
			}
			
		}
		
		// 筛选对应的二级部门
		deptList.removeAll(mAdapterList);
		for (int i = 0; i < mAdapterList.size(); i++) {
			DepartmentEntity department = mAdapterList.get(i);
			String id = department.getShowId();
			List<DepartmentEntity> l = new ArrayList<DepartmentEntity>();
			for (int j = 0; j < deptList.size(); j++) {
				DepartmentEntity d = deptList.get(j);
				String departShowId = d.getdParentShowId();
				if (id.equals(departShowId)) {
					l.add(d);
				}
			}
			
			mFirstList.add(l);
//			Log.i("infos", l.size() + "-----");
		}
		
		// 筛选跟一级部门评级的人员
		if(ContactFragment.mDataList != null)
		for (int i = 0; i < ContactFragment.mDataList.size(); i++) {
			UserDetailEntity entity = ContactFragment.mDataList.get(i);
			if((entity.getDeptId() != null) && (entity.getcShowId() != null) && (entity.getcShowId().equals(entity.getDeptId()))){
				mUserList.add(entity);
			}
			
		}
		
		
		if (mContactDeptAdapter != null) {
			mContactDeptAdapter.notifyDataSetChanged();
		}
		if (mDeptUserAdapter != null) {
			mDeptUserAdapter.notifyDataSetChanged();
		}
	}
	
	
	
	public void notifyAdapter(){
		if (mContactDeptAdapter != null) {
			mContactDeptAdapter.notifyDataSetChanged();
		}
		
		if (mDeptUserAdapter != null) {
			mDeptUserAdapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if (response == null) {
			dismissLoading();
		}else

		// 判断是否是获取部门返回
		if(taskId.equals(UserApi.TaskId.TASK_URL_GET_COMANY_DEPT_LIST)){
			handleDeptResult(response);
			dismissLoading();
		}
		// 部门更新时间戳返回
		else if(taskId.equals(UserApi.TaskId.TASK_URL_DEPT_LAST_UPDATE)){
			handleDeptUpdate(response);
		}

	}

	/** 部门更新时间戳返回*/
	private void handleDeptUpdate(Object response){
		UserUpdatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserUpdatePOJO.class);
		if(pojo == null){
			return;
		}else if(!pojo.isResultSuccess()){
			return;
		}else if(pojo.getBody().getResponse().getData() != null){
			UserUpdateEntity entity = pojo.getBody().getResponse().getData();

			long updateTime = UserUpdateUtil.getInstance(getActivity()).getUpdateTime(entity.getShowId());
			if(entity.getLastUpadteTime() > updateTime){
				UserUpdateUtil.getInstance(getActivity()).saveUpdateTime(entity.getShowId(), entity.getLastUpadteTime());

				String showID = LauchrConst.header.getCompanyShowID();
				UserApi.getInstance().getComanyDeptList(this,showID);
			}else{
				String showID = LauchrConst.header.getCompanyShowID();
				UserApi.getInstance().userLastUpdate(this, showID);
			}
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	private void handleDeptResult(Object response){
		DepartmentPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), DepartmentPOJO.class);
		
		// 判断是否是为空
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				mDataList.clear();
				List<DepartmentEntity> list = pojo.getBody().getResponse().getData();
				if (list != null) {
					ContactDBService.getInstance().deleteParentDepartment(list);

					mDataList.addAll(list);
					notifyAdater();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != getActivity().RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}


	}
}