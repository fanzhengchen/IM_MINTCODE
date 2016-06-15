package com.mintcode.launchr.message.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.chat.entity.Info;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.Command;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.IMConst;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.FileDetailActivity;
import com.mintcode.launchr.message.activity.MessageRecordActivity;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.TimeFormatUtil;

public class FileMsgFragment extends BaseFragment implements OnItemClickListener{

	private View view;
	
	private LayoutInflater inflater;
	/** 消息列表*/
	private ListView mLvFile;
	/** 消息列表适配器*/
	private FileAdapter mAdapterFile;
	/** 消息*/
	private List<MessageItem> mListMessage;
	
	private Context mContext;;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message_record_file, container, false);
		initData();
		initView();
		return view;
	}
	
	public void initData(){
		mContext = getActivity();
		mListMessage = MessageDBService.getInstance().searchMsg(loginName, toLoginName, 0, 100, Command.FILE, "");
		// 判断数据是否为空
		if(mListMessage == null || mListMessage.isEmpty()){
			MessageRecordActivity activity = (MessageRecordActivity) getActivity();
			activity.setFileNoData();
		}
	}
	
	public void initView(){
		inflater = LayoutInflater.from(getActivity());
		
		mAdapterFile = new FileAdapter();
		mLvFile = (ListView) view.findViewById(R.id.lv_message_record_file);
		mLvFile.setAdapter(mAdapterFile);
		mLvFile.setOnItemClickListener(this);
	}
	
	private class FileAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListMessage.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListMessage.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_message_record_file, null);
				holder.mIvFile = (ImageView) convertView.findViewById(R.id.iv_message_record_file_headr);
				holder.mTvFileName = (TextView) convertView.findViewById(R.id.tv_message_record_file_filename);
				holder.mTvTime = (TextView) convertView.findViewById(R.id.tv_message_record_file_time);
				holder.mTvSize = (TextView) convertView.findViewById(R.id.tv_message_record_file_size);
				holder.mTvName = (TextView) convertView.findViewById(R.id.tv_message_record_file_name);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			setFileMessage(holder, mListMessage.get(position));
			
			return convertView;
		}
	}
	
	public class ViewHolder{
		public ImageView mIvFile;
		public TextView mTvFileName;
		public TextView mTvSize;
		public TextView mTvTime;
		public TextView mTvName;
	}
	
	private void setFileMessage(ViewHolder holder, MessageItem item){
		AttachItem attach = JsonUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
		if(attach == null){
			return;
		}
		
		holder.mTvSize.setText(IMConst.FormetFileSize(attach.getFileSize()));
		if (item.getCmd() == MessageItem.TYPE_RECV) {
			String myInfoStr = item.getInfo();
			Info myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr,
					Info.class);
			holder.mTvName.setText(myInfo.getNickName());
		} else {
			HeaderParam mHeaderParam = LauchrConst.getHeader(mContext);
			holder.mTvName.setText(mHeaderParam.getUserName());
		}
		holder.mTvTime.setText(TimeFormatUtil.formatNowTime(item.getCreateDate()));
		holder.mTvFileName.setText(attach.getFileName());
		holder.mIvFile.setImageResource(IMConst.getFileIcon(attach.getFileName()));
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		MessageItem messageItem = mListMessage.get(position);
//		int msgId = messageItem.getId();
//		String from = messageItem.getFromLoginName();
//		String to = messageItem.getToLoginName();
//		int cmd = messageItem.getCmd();
//		String nickName;
//		if (cmd == MessageItem.TYPE_RECV) {
//			nickName = messageItem.getNickName();
//		}else{
//			nickName = messageItem.getToNickName();
//		}
//		Intent intent = new Intent(mContext, SearchResultActiviy.class);
//		intent.putExtra("nickName", nickName);
//		intent.putExtra("from", from);
//		intent.putExtra("to", to);
//		intent.putExtra("start", msgId);
//		startActivity(intent);
		
		Intent intent = new Intent(mContext, FileDetailActivity.class);
		intent.putExtra("fileMessage", mListMessage.get(position));
		mContext.startActivity(intent);
	}
}

