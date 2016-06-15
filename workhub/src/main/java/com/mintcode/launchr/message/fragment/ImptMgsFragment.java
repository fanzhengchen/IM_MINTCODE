package com.mintcode.launchr.message.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout.LayoutParams;

import com.mintcode.cache.CacheManager;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.im.Command;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.JsonUtil;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.MessageRecordActivity;
import com.mintcode.launchr.message.activity.SearchResultActiviy;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.view.MTContainerPopWindow;

public class ImptMgsFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener{
	private static final int ENTER_SEARCH_RESULT = 1000;
	
	private ListView mLv;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	/** 重点msg **/
	private List<MessageItem> mMarkMsgs;
	private MarkMsgAdapter mAdapter;

	private RelativeLayout mRelBottom;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater=inflater;
		if(mLv==null){
			mLv=new ListView(mContext);
			mLv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		ViewGroup parent=(ViewGroup) mLv.getParent();
		if(parent!=null){
			parent.removeView(mLv);
		}
		return mLv;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter=new MarkMsgAdapter();
		mMarkMsgs=MessageDBService.getInstance().searchMarkMsg(loginName,toLoginName,0, 20, "");

		// 判断数据是否为空
		if(mMarkMsgs == null || mMarkMsgs.isEmpty()){
			MessageRecordActivity activity = (MessageRecordActivity) getActivity();
			activity.setMarkPointNoData();
		}

		mLv.setAdapter(mAdapter);
		mLv.setOnItemClickListener(this);
		mLv.setOnItemLongClickListener(this);
		mRelBottom = (RelativeLayout) getActivity().findViewById(R.id.rel_message_record_bottom);
	}
	private class MarkMsgAdapter extends BaseAdapter{

		@Override
		public int getCount() { //通知消息数改变
			return mMarkMsgs.size();
		}

		@Override
		public Object getItem(int position) {
			return mMarkMsgs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.item_mark, parent, false);
				holder = new ViewHolder();
				holder.ivAvatar = (ImageView) convertView
						.findViewById(R.id.message_head_iv);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.message_content);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.message_name);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.message_time);
				holder.ivIsRead = (ImageView) convertView.findViewById(R.id.iv_message_record_app_read);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MessageItem messageItem = mMarkMsgs.get(position);
			if (messageItem.getCmd() == MessageItem.TYPE_RECV) {
				String myInfoStr = messageItem.getInfo();
				Info myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr,
						Info.class);
				holder.tvName.setText(myInfo.getNickName());
			} else {
				HeaderParam mHeaderParam = LauchrConst.getHeader(mContext);
				holder.tvName.setText(mHeaderParam.getUserName());
			}
			if(messageItem.getType().equals(Command.TEXT)){
				String content=messageItem.getContent();
				holder.tvContent.setText(content);
			}else if(messageItem.getType().equals(Command.FILE)){
				holder.tvContent.setText(mContext.getString(R.string.im_session_file));
			}else if(messageItem.getType().equals(Command.IMAGE)){
				holder.tvContent.setText(mContext.getString(R.string.im_session_image));
			}else if(messageItem.getType().equals(Command.AUDIO)){
				holder.tvContent.setText(mContext.getString(R.string.im_session_audio));
			}else if(messageItem.getType().equals(Command.MERGE)){
				MergeEntity.MergeCard entity = TTJSONUtil.convertJsonToCommonObj(messageItem.getContent(), MergeEntity.MergeCard.class);
				holder.tvContent.setText(entity.getTitle());
			}
			
//			if(messageItem.getIsRead() == 1){
//				holder.ivIsRead.setVisibility(View.GONE);
//			}

			if(messageItem.getCmd() == MessageItem.TYPE_SEND){
				setHeaderImage(holder.ivAvatar, loginName);
			}else{
				String info = messageItem.getInfo();
				Info userInfo = com.mintcode.chat.util.JsonUtil.convertJsonToCommonObj(info, Info.class);
				setHeaderImage(holder.ivAvatar, userInfo.getUserName());
			}
			String time = TimeFormatUtil.getTimeForSearch(messageItem.getClientMsgId(),mContext);
			holder.tvTime.setText(time);
			return convertView;
		}
		class ViewHolder{
			ImageView ivAvatar;
			ImageView ivIsRead;
			TextView tvName;
			TextView tvContent;
			TextView tvTime;
		}
	} 
	
	/** 显示头像*/
	public void setHeaderImage(ImageView mIvAvatar, String mUserId){
		HeadImageUtil.getInstance(getActivity()).setAvatarResourceAppendUrl(mIvAvatar,mUserId,0,60,60);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MessageItem messageItem = mMarkMsgs.get(position);
		long msgId = messageItem.getMsgId();
		String from = messageItem.getFromLoginName();
		String to = messageItem.getToLoginName();
		int cmd = messageItem.getCmd();
		String nickName;
		if (cmd == MessageItem.TYPE_RECV) {
			nickName = messageItem.getNickName();
		}else{
			nickName = messageItem.getToNickName();
		}
		Intent intent = new Intent(mContext,SearchResultActiviy.class);
		intent.putExtra("nickName", nickName);
		intent.putExtra("from", from);
		intent.putExtra("to", to);
		intent.putExtra("start", msgId);
		getActivity().startActivityForResult(intent, ENTER_SEARCH_RESULT);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		final MessageItem item = mMarkMsgs.get(position);
		final MTContainerPopWindow mContainerPopWindow = new MTContainerPopWindow(mContext);
		int rname = item.getIsMarkPoint() == 0 ? R.string.message_mark_point
				: R.string.message_unmark_point;
		if(Command.TEXT.equals(item.getType())){
			mContainerPopWindow.addCopyTextView(mContext.getString(R.string.calendar_copy), new OnClickListener() {
				@Override
				public void onClick(View v) {
					ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("simple text", item.getContent());
					clipboard.setPrimaryClip(clip);
					mContainerPopWindow.dismiss();
				}
			});
		}
		mContainerPopWindow.addTextView(rname, new OnClickListener() {

			@Override
			public void onClick(View v) {
				int markPoint = item.getIsMarkPoint() == 0 ? 1 : 0;
				item.setIsMarkPoint(markPoint);
				item.save();
				mMarkMsgs.remove(item);
				mAdapter.notifyDataSetChanged();
				mContainerPopWindow.dismiss();
			}
		});
		mContainerPopWindow.show(mRelBottom);
		return true;
	}
	
}














