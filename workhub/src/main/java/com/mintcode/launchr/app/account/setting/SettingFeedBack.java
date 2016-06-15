package com.mintcode.launchr.app.account.setting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/*
 * create by Stephen He 2015/9/10
 */
public class SettingFeedBack extends BaseActivity {
	// 返回
	private ImageView mIvBack;

	// 标题
	private EditText mEdTitle;

	// 内容
	private EditText mEdContent;

	// 发送
	private TextView mTvSend;

	// 记录
	private TextView mTvRecord;

	// 反馈记录列表
	private ListView mLvFeedRecord;
	private FeedBackAdapter adapter;
	private List<FeedEntity> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_feed_back);
		
		initView();
		
		initData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_feed_back);
		
		mEdTitle = (EditText) findViewById(R.id.et_feed_title);
		mEdContent = (EditText) findViewById(R.id.et_feed_content);
		
		mTvSend = (TextView) findViewById(R.id.tv_feed_send);
		mTvRecord = (TextView) findViewById(R.id.tv_feed_record);
		
		mLvFeedRecord = (ListView) findViewById(R.id.lv_feed_back_recod);
		
		mIvBack.setOnClickListener(this);
		mTvSend.setOnClickListener(this);
	}

	public void initData() {
		list = new ArrayList<FeedEntity>();
		FeedEntity feed = new FeedEntity();
		feed.setName("我:");
		feed.setTitle("上传头像时无法保存");
		feed.setContent("修改头像上传图片后又变成原来的头像");
		feed.setTime("8/21");
		list.add(feed);

		feed = new FeedEntity();
		feed.setName("客服:");
		feed.setTitle("试着刷新一下网页或者推出重新登录一下就有自己的图标了;要不就是你没有上传成功(其中包括:你没有提交,或者图像不符合要求)");
		feed.setContent("");
		feed.setTime("8/21");
		list.add(feed);

		adapter = new FeedBackAdapter();
		mLvFeedRecord.setAdapter(adapter);

		if (list.size() <= 0) {
			mTvRecord.setText("没有反馈记录!");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			// 返回操作
			finish();
		} else if (v == mTvSend) {
			// 发送操作
			sendFeedBack();
		}
	}

	// 发送反馈
	public void sendFeedBack() {
		if (mEdTitle.getText().length() <= 0) {
			toast("标题不能为空");
			return;
		}
		if (mEdContent.getText().length() <= 0) {
			toast("内容不能为空");
			return;
		}
		
		// 对象FeedEntity位置不对，应该改掉
	}

	private class FeedBackAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Viewholder holder = null;
			if (convertView == null) {
				holder = new Viewholder();
				convertView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.activity_setting_feed_back_item, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.tv_feed_item_name);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_feed_item_title);
				holder.content = (TextView) convertView
						.findViewById(R.id.tv_feed_item_content);
				holder.time = (TextView) convertView
						.findViewById(R.id.tv_feed_item_time);
				convertView.setTag(holder);
			} else {
				holder = (Viewholder) convertView.getTag();
			}
			holder.name.setText(list.get(position).getName());
			holder.title.setText(list.get(position).getTitle());
			holder.content.setText(list.get(position).getContent());
			holder.time.setText(list.get(position).getTime());
			return convertView;
		}
	}

	private class Viewholder {
		public TextView name;
		public TextView title;
		public TextView content;
		public TextView time;
	}

	private class FeedEntity {
		public String name;
		public String title;
		public String content;
		public String time;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}
}
