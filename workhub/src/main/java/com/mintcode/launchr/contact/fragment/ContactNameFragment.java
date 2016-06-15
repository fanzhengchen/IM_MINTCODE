package com.mintcode.launchr.contact.fragment;

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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.titans.widget.sortindex.PinyinComparator;
import com.mintcode.titans.widget.sortindex.SideBar;
import com.mintcode.titans.widget.sortindex.SideBar.OnTouchingLetterChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录
 * @author KevinQiao
 *
 */
public class ContactNameFragment extends BaseFragment implements OnItemClickListener{

	public static final String SORT_CHAR_KEY = "letters";

	public static final String CONTACT_ENTITY_KEY = "contact_entity_key";
	
	/** 字母索引 */
	private SideBar mSideBar;
	
	/** 大写字母*/
	private TextView mTvIndex;
	
	private View mView;
	
	/** listview */
	private ListView mLvName;
	
	/** 数据集 */
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	
	/** 适配器 */
	private ContactNameAdapter mContactNameAdapter;
	
	/** 根据拼音来排列ListView里面的数据类 */
	private PinyinComparator pinyinComparator;
	
	private boolean mIsFirst = false;
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//			
		mView = inflater.inflate(R.layout.fragment_contact_name, null);
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
		return mView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
		initView();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	}
	
	
	
	private void initView(){
		pinyinComparator = new PinyinComparator();
		
		mLvName = (ListView) mView.findViewById(R.id.lv_name);
		mSideBar = (SideBar) mView.findViewById(R.id.sidrbar);
		mTvIndex = (TextView) mView.findViewById(R.id.dialog);
		mSideBar.setTextView(mTvIndex);
		
		
		mContactNameAdapter = new ContactNameAdapter();
		mLvName.setAdapter(mContactNameAdapter);
		
		// 设置右侧触摸监听
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mContactNameAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mLvName.setSelection(position);
				}

			}
		});
		
		mLvName.setOnItemClickListener(this);
		notifyAdater();
	}

	private void initData(){
//		mList = new ArrayList<UserDetailEntity>();
		
//		ContactBroadcastReceiver receiver = new ContactBroadcastReceiver();
//		mReceiver = new ContactBroadcastReceiver();
	}
	
	
	
	
	public void sendBroadcast(){
		Intent intent = new Intent(ContactFragment.CONTACT_ACTION);
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, Object> map = (Map<String, Object>) parent.getAdapter().getItem(position);
		UserDetailEntity entity = (UserDetailEntity)map.get(CONTACT_ENTITY_KEY);
		// 多选模式
		if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE) {
//			holder.cbCheck.setVisibility(View.VISIBLE);
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
//			intent.putExtra(LauchrConst.KEY_USERENTITY, entity);
			intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getShowId());
			startActivity(intent);
		}

		// TODO Auto-generated method stub

		mContactNameAdapter.notifyDataSetChanged();
	}


	class ContactNameAdapter extends BaseAdapter implements SectionIndexer{

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
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				convertView.setTag(holder);

			}else{
				holder = (ViewHolder) convertView.getTag();
			}

			// 设置数据
			Map<String, Object> map = mList.get(position);//.get(CONTACT_ENTITY_KEY);
			UserDetailEntity entity = (UserDetailEntity) map.get(CONTACT_ENTITY_KEY);
			String sortChar = (String) map.get(SORT_CHAR_KEY);




			// 设置姓名
			holder.tvName.setText(entity.getTrueName());
			// 设置部门
			holder.tvDept.setText(entity.getdName());


			// 设置索引字母显示排序
			int ascII = getSectionForPosition(position);

			if (position == getPositionForSection(ascII)) {
				holder.tvWord.setVisibility(View.VISIBLE);
				holder.tvWord.setText(sortChar);

			}else{
				holder.tvWord.setVisibility(View.GONE);
			}


			// 设置头像
			String companyCode = LauchrConst.header.getCompanyCode();

			Log.i("infos",LauchrConst.header.getCompanyCode() + "--创造---");
			String mHeadPicUrl = LauchrConst.ATTACH_PATH +"/Base-Module/Annex/Avatar?width=80&height=80&companyCode="
					 + companyCode + "&userName=" + entity.getShowId();
			setImageResuces(holder.ivIcon, mHeadPicUrl);
//			if (position % 3 == 0) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/df431690-b13c-4ab4-990b-fc811e679432.jpg";
//				setImageResuces(holder.ivIcon, url);
//			}else if (position % 3 == 1) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/da219741-9840-438e-b298-a90d7ff5ff5d.jpg";
//				setImageResuces(holder.ivIcon, url);
//			}else if (position % 3 == 2) {
//				String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/5aa47427-b792-4cad-ae7d-0eaf4ea3a5a8.jpg";
//				setImageResuces(holder.ivIcon, url);
//			}

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

//			Log.i("mes", ContactFragment.mSelectState + "-");
//			if (isChecked) {
//				hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
//			} else {
//				hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
//			}
			return convertView;
		}

		@Override
		public Object[] getSections() {
			return null;
		}

		@Override
		public int getPositionForSection(int sectionIndex) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mList.get(i).get(SORT_CHAR_KEY).toString();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == sectionIndex) {
					return i;
				}
			}

			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			int index = mList.get(position).get(SORT_CHAR_KEY).toString().charAt(0);
			return index;
		}



	}

	class ViewHolder {
		TextView tvName;
		TextView tvDept;
		TextView tvWord;
		ImageView ivIcon;
		ImageView cbCheck;
	}





	/**
	 * 更新数据适配器
	 */
	public void notifyAdater(){
		if (mContactNameAdapter != null) {
			List<Map<String, Object>> list = sortData();
			if(list != null){
				mList.clear();
//			mList.addAll(ContactFragment.mDataList);
				mList.addAll(list);
			}

			mContactNameAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 排序
	 * @return
	 */
	private List<Map<String, Object>> sortData(){
		if(ContactFragment.mDataList == null){
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String> barList = new ArrayList<String>();
		for (UserDetailEntity entity : ContactFragment.mDataList) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 获取第一个字母
			String sortChar = entity.getTrueNameC();
			String sortJpChar = entity.getHira();
			if (sortJpChar != null && !sortJpChar.equals("")) {
				map.put(SORT_CHAR_KEY, sortJpChar);
//				removeSideBar(sortJpChar);
				if (!barList.contains(sortJpChar)) {
					barList.add(sortJpChar);
				}
			}else{
				map.put(SORT_CHAR_KEY, sortChar);
				if (!barList.contains(sortChar)) {
					barList.add(sortChar);
				}
			}

			map.put(CONTACT_ENTITY_KEY, entity);

			list.add(map);
		}


		if (!ContactFragment.mDataList.isEmpty()) {
			List<String> l = new ArrayList<String>();
			for (String s : SideBar.mBList) {
				if (!barList.contains(s)) {
					l.add(s);
				}
			}

			SideBar.mBList.removeAll(l);
		}

		Collections.sort(list, pinyinComparator);
		mSideBar.postInvalidate();

		// 重新对包括日本在内排序
		List<Map<String, Object>> l = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < SideBar.mBList.size(); i++) {
			String b = SideBar.mBList.get(i);

			//
			for (int j = 0; j < ContactFragment.mDataList.size(); j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserDetailEntity entity = ContactFragment.mDataList.get(j);
				// 获取第一个字母
				String sortChar = entity.getTrueNameC();
				String sortJpChar = entity.getHira();
				if (sortJpChar != null && !sortJpChar.equals("")) {
					if (b.equals(sortJpChar)) {
						map.put(SORT_CHAR_KEY, sortJpChar);
						map.put(CONTACT_ENTITY_KEY, entity);
						l.add(map);
					}
					
				}else{
					if (b.equals(sortChar)) {
						map.put(SORT_CHAR_KEY, sortChar);
						map.put(CONTACT_ENTITY_KEY, entity);
						l.add(map);
					}
				}
				
			}
			
		}
		
		return l;
	}
	
	
	public void setImageResuces(ImageView iv, String url){
		// 处理圆形图片
		if (isAdded()) {
			RequestManager man = Glide.with(this);
			GlideRoundTransform g =  new GlideRoundTransform(getActivity(),3);
			man.load(url).transform(g).placeholder(R.drawable.icon_head_default).crossFade().into(iv);
		}
	}
	
	public void notifyAdapter(){
		if (mContactNameAdapter != null) {
			mContactNameAdapter.notifyDataSetChanged();
		}
	}
}