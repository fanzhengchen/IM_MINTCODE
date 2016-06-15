package com.mintcode.launchr.app.meeting.adapter;

import com.mintcode.launchr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter{


	private String mStr = "";
	private String[] mData;
	private LayoutInflater mInflater;
	public ItemAdapter(Context context,String[] data){
		this.mData=data;
		mInflater=LayoutInflater.from(context);
	}
	public void setStr(String str){
		mStr = str;
	}
	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		return mData[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView =  mInflater.inflate(R.layout.remind_list_item, parent,false);


		TextView text = (TextView)convertView.findViewById(R.id.tv_item_name);
		ImageView box = (ImageView)convertView.findViewById(R.id.cb_check);
		text.setText(mData[position]);
		if(mStr != null && mStr.equals(mData[position])){
			box.setImageResource(R.drawable.icon_blue_checked);
		}else{
			box.setImageResource(R.drawable.icon_unchecked);
		}

		return convertView;
	}

}

















