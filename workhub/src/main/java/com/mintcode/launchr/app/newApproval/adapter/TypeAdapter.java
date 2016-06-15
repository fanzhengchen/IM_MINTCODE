package com.mintcode.launchr.app.newApproval.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;

public  class TypeAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private LinearLayout llBackground;
	
	private TextView tvView;
	
	private List<String> mListType;
	
	private String mSelectText;
	
	
	public TypeAdapter(Context context,List<String> type){
		
		this.mContext = context;
		this.mListType = type;
	}
	
	@Override
	public int getCount() {
		
		return mListType.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mListType.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_type_select, null);
		if(convertView != null){
//			llBackground = (LinearLayout)convertView.findViewById(R.id.ll_item_type);
			tvView = (TextView)convertView.findViewById(R.id.tv_item_type);
			tvView.setText(mListType.get(position));
			if(mListType.get(position).equals(mSelectText)){
//				llBackground.setBackgroundColor(mContext.getResources().getColor(R.color.bule_simple));
				tvView.setTextColor(mContext.getResources().getColor(R.color.blue_launchr));
			}
		}
		return convertView;
	}
	
	public void setSelectText(String text){
		mSelectText = text;
	}
	public String getSelectText(){
		return mSelectText;
	}
}
