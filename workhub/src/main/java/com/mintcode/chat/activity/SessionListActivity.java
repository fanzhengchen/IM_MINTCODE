package com.mintcode.chat.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.chat.image.MutiTaskUpLoad;
import com.mintcode.im.Command;
import com.mintcode.im.IMManager;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.MsgListenerManager;
import com.mintcode.im.listener.OnIMMessageListener;
import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TimeFormatUtil;

public class SessionListActivity extends Activity implements
		OnIMMessageListener, OnItemClickListener {

	private ListView mListView;
	private SessionAdapter mAdapter;
	private LayoutInflater mInflater;
	private List<SessionItem> mSessionItems;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=SessionListActivity.this;

		setContentView(R.layout.activity_session_list);
		mInflater = this.getLayoutInflater();
		mListView = (ListView) findViewById(R.id.lv_section);
		MsgListenerManager.getInstance().setMsgListener(this);
		mSessionItems = IMManager.getInstance().getSession();
		mAdapter = new SessionAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		findViewById(R.id.img_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onStatusChanged(int result, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(List<MessageItem> messages, int msgCount) {
		for (MessageItem item : messages) {
			// 只需要处理附件
			if ((Command.IMAGE).equals(item.getType())) {
				MutiTaskUpLoad.getInstance().sendMsgToDownload(item, this,
						null);
			} else if ((Command.AUDIO).equals(item.getType())) {
				Log.i("msg", "AUDIO  OnMessage:" + item.toString());
				MutiTaskUpLoad.getInstance().sendMsgToDownload(item, this,
						null);
			}
		}
		
	}

	@Override
	public void onSession(List<SessionItem> sections) {
		mSessionItems.clear();
		mSessionItems.addAll(IMManager.getInstance().getSession());
		mAdapter.notifyDataSetChanged();
	}
	
	class SessionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mSessionItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			SectionViewHolder holder = null;
			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.item_session, null);
				holder = new SectionViewHolder();
				holder.ivAvatar = (ImageView) convertView
						.findViewById(R.id.message_head_iv);
				holder.tvNickName = (TextView) convertView
						.findViewById(R.id.message_name);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.message_content);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.message_time);
				holder.tvUnreadNum = (TextView) convertView
						.findViewById(R.id.message_unread_num);
				convertView.setTag(holder);
			} else {
				holder = (SectionViewHolder) convertView.getTag();
			}
			SessionItem SessionItem = mSessionItems.get(position);
			

			holder.ivAvatar.setImageResource(R.drawable.icon_default);

			holder.tvNickName.setText(SessionItem.getNickName());
			holder.tvContent.setText(SessionItem.getContent());
			int num = SessionItem.getUnread();
			holder.tvUnreadNum
					.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
			holder.tvUnreadNum.setText(SessionItem.getUnread() + "");
			holder.tvTime.setText(TimeFormatUtil.formatNowTime(SessionItem.getTime()));

			return convertView;
		}
		
		class SectionViewHolder {
			ImageView ivAvatar;
			TextView tvNickName;
			TextView tvContent;
			TextView tvTime;
			TextView tvUnreadNum;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		SessionItem SessionItem = mSessionItems.get(position);
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("section", SessionItem);
		startActivity(intent);
		IMManager.getInstance().readMessageByUid(SessionItem.getUserName());
	}
}
