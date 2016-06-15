package com.mintcode.launchr.message.fragment;

import java.util.List;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.cache.CacheManager;
import com.mintcode.chat.activity.ImgPreviewActivity;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.image.MutiTaskUpLoad;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.Command;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.MessageRecordActivity;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TimeFormatUtil;

/**
 * @author StephenHe 2015/10/10 消息记录-》图片
 *
 */
public class PicMgsFragment extends BaseFragment implements OnItemClickListener{
	private View view;
	
	private LayoutInflater inflater;
	
	/** 消息图片列表适配器*/
	private PictureAdapter mPictureAdapter;
	
	/** 消息*/
	private List<MessageItem> mMessageList;
	
	/** 消息图片列表*/
	private GridView mGvMessage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message_record_pic, container, false);
		
		initData();
		
		initView();
		
		return view;
	}
	
	public void initData(){
		mMessageList = MessageDBService.getInstance().searchMsg(loginName, toLoginName, 0, 20, Command.IMAGE, "");
//		for(int i=0; i<mMessageList.size(); i++){
//			if(mMessageList.get(i).getFileName() == null){
//				mMessageList.remove(i);
//				i -= 1;
//			}
//		}
		// 判断数据是否为空
		if(mMessageList == null || mMessageList.isEmpty()){
			MessageRecordActivity activity = (MessageRecordActivity) getActivity();
			activity.setPicNoData();
		}
	}
	
	public void initView(){
		inflater = LayoutInflater.from(getActivity());
		
		mPictureAdapter = new PictureAdapter();
		mGvMessage = (GridView) view.findViewById(R.id.gv_message_record_pic);
		mGvMessage.setAdapter(mPictureAdapter);
		mGvMessage.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MessageItem messageItem = mMessageList.get(position);
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
		Intent intent = new Intent(getActivity() ,ImgPreviewActivity.class);
		intent.putExtra("item",messageItem);
		startActivity(intent);
	}

	private class PictureAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mMessageList.get(position);
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
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_message_record_pic, null);
				holder.mIvPicture = (ImageView) convertView.findViewById(R.id.iv_message_record_pic);
				holder.mTvName = (TextView) convertView.findViewById(R.id.tv_message_record_pic_name);
				holder.mTvTime = (TextView) convertView.findViewById(R.id.tv_message_record_pic_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			setMessagePicture(holder, mMessageList.get(position));
			
			if (mMessageList.get(position).getCmd() == MessageItem.TYPE_RECV) {
				String myInfoStr = mMessageList.get(position).getInfo();
				Info myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr,
						Info.class);
				holder.mTvName.setText(myInfo.getNickName());
			} else {
				HeaderParam mHeaderParam = LauchrConst.getHeader(getActivity());
				holder.mTvName.setText(mHeaderParam.getUserName());
			}
			
			String time = TimeFormatUtil.getTimeForSearch(mMessageList.get(position).getClientMsgId(),getActivity());
			holder.mTvTime.setText(time);
			return convertView;
		}
		
		private class ViewHolder{
			public ImageView mIvPicture;
			public TextView mTvName;
			public TextView mTvTime;
		}
		
		private void setMessagePicture(ViewHolder holder, MessageItem item){
			String url = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr";
			String key;
			if (MessageItem.TYPE_RECV == item.getCmd()) {
				// 接收
				String content = item.getContent();
				AttachItem attach = JsonUtil.convertJsonToCommonObj(content,
						AttachItem.class);
				CacheManager cache = CacheManager.getInstance(getActivity());
				String str;
				str = attach.getFileUrl();
				key = url + str;
				Bitmap dst = cache.getBitmapByKey(key);
				if (dst != null) {
					holder.mIvPicture.setImageBitmap(cache.getAblum(dst));
				} else {
					str = attach.getThumbnail();
					key = url + str;
					dst = cache.getBitmapByKey(key);
					if (dst != null) {
						holder.mIvPicture.setImageBitmap(cache.getAblum(dst));
					} else {
						holder.mIvPicture.setImageResource(R.drawable.im_default_image);
						MutiTaskUpLoad.getInstance().sendMsgToDownload(item,
								getActivity(), null);
					}
				}
			} else {
				key = item.getFileName();
				CacheManager cache = CacheManager.getInstance(getActivity());
				Bitmap bitmap2 = cache.getBitmapByPath(key);
				holder.mIvPicture.setImageBitmap(bitmap2);
//				if (isGroup) {
//					holder.ivRead.setVisibility(View.GONE);
//				} else {
//					holder.ivRead.setSelected(item.getIsRead() == 1);
//				}
			}
		}
	}
}
