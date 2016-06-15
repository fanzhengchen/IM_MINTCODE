package com.mintcode.launchr.app.newTask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;

import java.util.List;

/*
 * create by Stephen he 215/9/3
 */
public class AllTaskListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<TaskProjectListEntity> list;

	public AllTaskListAdapter(Context context, List<TaskProjectListEntity> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_task_alltask_list_item, null);
			holder.taskName = (TextView) convertView
					.findViewById(R.id.alltask_list_item_name);
			holder.taskNumber = (TextView) convertView
					.findViewById(R.id.alltask_list_item_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		if (position == 0) {
//			holder.taskName.setTextColor(0x0099ff);
//			holder.taskNumber.setTextColor(0x0099ff);
//		}
		holder.taskName.setText(list.get(position).getName());
		holder.taskNumber.setText(list.get(position).getAllTask() + "");
		return convertView;
	}

	private class ViewHolder {
		public TextView taskName;
		public TextView taskNumber;
	}
}
