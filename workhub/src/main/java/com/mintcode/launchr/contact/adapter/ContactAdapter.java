package com.mintcode.launchr.contact.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.GlideRoundTransform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter{

	public static final String MAP_KEY = "map_key";
	
	public static final int NO_STATE = 1;
	
	public static final int SELECT_STATE = 2;
	
	private int mState = NO_STATE;
	
	private List<ContactEntity> mList;
	
	private LayoutInflater mInflat;
	
	private Context mContext;
	
	private List<Boolean> mPositionList;
	
	private List<UserDetailEntity> mUserList;
	
	
	
	public ContactAdapter(Context context,List<UserDetailEntity> list){
		getList();
		mInflat = LayoutInflater.from(context);
		mContext = context;
		
		if (list != null) {
			this.mUserList = list;
		}else{
			mUserList = new ArrayList<UserDetailEntity>();
		}
		
	}
	
	public void changeData(List<UserDetailEntity> list){
		this.mUserList.clear();
		if (list != null) {
			this.mUserList.addAll(list);
		}
	}
	
	
	public void changeSelectState(int state, List<Boolean> list){
		
		if (state == SELECT_STATE) {
			if (list != null) {
				mPositionList = list;
			}else{
				mPositionList = new ArrayList<Boolean>();
				for (int i = 0; i < mUserList.size(); i++) {
					mPositionList.add(false);
				}
			}
			
			mState = SELECT_STATE;
			notifyDataSetChanged();
		}else if (state == NO_STATE) {
			mState = NO_STATE;
			notifyDataSetChanged();
		}
		
	}
	
	public void setState(int position){
		if (mPositionList != null) {
			Log.i("mes", mPositionList.get(position) + "===" +mPositionList.size() );
			if (position < mPositionList.size()) {
				boolean b = mPositionList.get(position) ;
				if (b) {
					b = false;
					mPositionList.add(position, b);
					mPositionList.remove(position + 1);
				}else{
					b = true;
					mPositionList.add(position, b);
					mPositionList.remove(position + 1);
				}
			}
			Log.i("mes", mPositionList.get(position) + "==="+mPositionList.size() );
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return mUserList.size();
	}
	
	//
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
		ViewHold hold = null;
		if (convertView == null) {
			convertView = mInflat.inflate(R.layout.item_contact, null);
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
		RequestManager man = Glide.with(mContext);
		GlideRoundTransform g =  new GlideRoundTransform(mContext,18);
		man.load(url).transform(g).into(hold.ivIcon);
		
		UserDetailEntity entity = mUserList.get(position);
		
		if (mState == SELECT_STATE) {
			hold.cbCheck.setVisibility(View.VISIBLE);
			boolean isChecked = mPositionList.get(position);
			if (isChecked) {
				hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
			}else{
				hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
			}
		}else{
			hold.cbCheck.setVisibility(View.GONE);
		}
		
		
		
		hold.tvName.setText(entity.getTrueName());
		
		return convertView;
	}
	

	class ViewHold{
		TextView tvName;
		ImageView ivIcon;
		ImageView cbCheck;
	}
	
	
	public class ContactEntity{
		public String name;
		public String url;
		
	}
//	http://121.40.30.204:10050/attachment/headPicAttachment/80x80/7ae0c298-4e51-49c1-a2e6-31509069dd32.jpg alvin
//		http://121.40.30.204:10050/attachment/headPicAttachment/80x80/aaeac452-070c-4ade-8cce-281ac7d54a19.jpg alex
//		http://121.40.30.204:10050/attachment/headPicAttachment/80x80/6508ae69-7b15-4ae9-bfdc-16b8b3cce7f8.jpg
	
	String path = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80";
	
	private void getList(){
		mList = new ArrayList<ContactEntity>();
		ContactEntity e1 = new ContactEntity();
		e1.name = "Alvin";
		e1.url = path + "/7ae0c298-4e51-49c1-a2e6-31509069dd32.jpg";
		mList.add(e1);
		
		ContactEntity e2 = new ContactEntity();
		e2.name = "Alex";
		e2.url = path + "/aaeac452-070c-4ade-8cce-281ac7d54a19.jpg";
		mList.add(e2);
		
		ContactEntity e3 = new ContactEntity();
		e3.name = "Ano";
		
		e3.url = path + "/6508ae69-7b15-4ae9-bfdc-16b8b3cce7f8.jpg";
		mList.add(e3);
		
	}
	
}
