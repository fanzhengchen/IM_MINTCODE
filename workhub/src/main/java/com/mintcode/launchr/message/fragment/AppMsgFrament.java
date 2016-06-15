package com.mintcode.launchr.message.fragment;


import java.util.List;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.cache.CacheManager;
import com.mintcode.chat.entity.Info;
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
import com.mintcode.launchr.pojo.entity.MessageEventTaskEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

/**
 * @author StephenHe 2015/10/10 消息记录-》应用
 */
public class AppMsgFrament extends BaseFragment implements OnItemClickListener {
    private View view;

    private LayoutInflater inflater;

    /**
     * 应用列表适配器
     */
    private AppAdapter mAppAdapter;

    /**
     * 应用列表
     */
    private ListView mLvMessage;

    /**
     * 应用消息
     */
    private List<MessageItem> mMessageList;



    public AppMsgFrament() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_record_app,
                container, false);
        initData();
        initView();
        return view;
    }

    public void initData() {
        mMessageList = MessageDBService.getInstance().searchMsg(loginName, toLoginName, 0, 50, Command.EVENT, "");
        // 判断数据是否为空
        if (mMessageList == null || mMessageList.isEmpty()) {
            MessageRecordActivity activity = (MessageRecordActivity) getActivity();
            activity.setAppNoData();
        }
    }

    public void initView() {
        inflater = LayoutInflater.from(getActivity());
        mLvMessage = (ListView) view.findViewById(R.id.lv_message_record_app);
        mAppAdapter = new AppAdapter();
        mLvMessage.setAdapter(mAppAdapter);
        mLvMessage.setOnItemClickListener(this);
    }

    private class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            MessageItem messageItem = mMessageList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_message_record_app, null);
                holder.mIvRead = (ImageView) convertView.findViewById(R.id.iv_message_record_app_read);
                holder.mIvHeader = (ImageView) convertView.findViewById(R.id.iv_message_recod_app_header);
                holder.mTvName = (TextView) convertView.findViewById(R.id.tv_message_record_app_name);
                holder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_message_record_app_title);
                holder.mIvFinalTime = (ImageView) convertView.findViewById(R.id.iv_message_record_app_finaltime);
                holder.mTvFinalTime = (TextView) convertView.findViewById(R.id.tv_message_record_app_finaltime);
                holder.mTvType = (TextView) convertView.findViewById(R.id.tv_message_record_app_type);
                holder.mTvTime = (TextView) convertView.findViewById(R.id.iv_message_record_app_time);
                holder.mTvAddress = (TextView) convertView.findViewById(R.id.tv_message_record_app_address);
                holder.mIvAddress = (ImageView) convertView.findViewById(R.id.iv_message_record_app_address);
                holder.mIvRightSpeach = (ImageView) convertView.findViewById(R.id.iv_message_record_app_speach);
                holder.mIvRightPaper = (ImageView) convertView.findViewById(R.id.iv_message_record_paper);
                holder.mIvRightAddress = (ImageView) convertView.findViewById(R.id.iv_message_record_app_right_address);
                holder.mTvFinish = (TextView) convertView.findViewById(R.id.tv_message_record_app_finish);
                holder.mIvFinish = (ImageView) convertView.findViewById(R.id.iv_message_record_app_finish);
                holder.mTvFinalTime = (TextView) convertView.findViewById(R.id.tv_message_record_app_finaltime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            showHolderMessage(holder, position, messageItem);
            return convertView;
        }

        /**
         * 显示头像
         */
        public void setHeaderImage(ImageView mIvAvatar, String mUserId) {
            HeadImageUtil.getInstance(getActivity()).setAvatarResourceAppendUrl(mIvAvatar,mUserId,0,60,60);
        }

        public void showHolderMessage(ViewHolder holder, int position, MessageItem item) {
            // 表情转码
            MessageEventTaskEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventTaskEntity>() {
            });
            MessageEventTaskEntity.EventTaskCard eventCard;
            MessageEventTaskEntity.MessageEventTaskBackupEntity eventEntity2 = null;
            if (eventEntity == null) {
                eventEntity2 = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventTaskEntity.MessageEventTaskBackupEntity>() {
                });
                eventCard = eventEntity2.getMsgInfo();
            } else {
                String jsonStr = TTJSONUtil.convertObjToJson(eventEntity.getMsgInfo());
                eventCard = TTJSONUtil.convertJsonToCommonObj(jsonStr, MessageEventTaskEntity.EventTaskCard.class);
            }

            if (eventCard == null) {
                return;
            }

            String myInfoStr = item.getInfo();
            Info myInfo = null;
            if (myInfoStr != null) {
                myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr, Info.class);
            }

            // 是否已读
            if (item.getIsRead() != 1) {
                holder.mIvRead.setImageBitmap(null);
            }

            // 名字
            if (myInfo != null) {
                holder.mTvName.setText(myInfo.getNickName());
                setHeaderImage(holder.mIvHeader, myInfo.getUserName());
            }

            // 标题
            if (eventEntity != null) {
                holder.mTvTitle.setText(eventEntity.getMsgTitle());
            } else if (eventEntity2 != null) {
                holder.mTvTitle.setText(eventEntity2.getMsgTitle());
            }
            if (eventCard != null) {
                holder.mTvTitle.setText(eventCard.getTitle());
            }
            // 创建时间
            holder.mTvTime.setText(TimeFormatUtil.getTimeForSearch(item.getCreateDate(), getActivity()));
            TimeFormatUtil.setAppTaskTime(getActivity(),holder.mTvFinalTime,holder.mIvFinalTime,eventCard.getEnd());
            // 会议地点
            if (eventEntity != null && "MEETING".equals(eventEntity.getMsgAppType())) {
                holder.mIvAddress.setVisibility(View.VISIBLE);
                holder.mTvAddress.setVisibility(View.VISIBLE);
//				holder.mTvAddress.setText(eventCard.getRoomName());
            } else if (eventEntity2 != null && "MEETING".equals(eventEntity2.getMsgAppType())) {
                holder.mIvAddress.setVisibility(View.VISIBLE);
                holder.mTvAddress.setVisibility(View.VISIBLE);
//				holder.mTvAddress.setText(eventCard.getRoomName());
            }

            // 完成
            if (eventCard != null && eventEntity2 != null && "TASK".equals(eventEntity2.getMsgAppType())) {
				holder.mTvFinish.setText(eventCard.getStateName());
				holder.mTvFinish.setVisibility(View.VISIBLE);
				holder.mIvFinish.setVisibility(View.VISIBLE);
            }else{
                holder.mTvFinish.setVisibility(View.GONE);
                holder.mIvFinish.setVisibility(View.GONE);
            }

            //评论
            //holder.mIvRightSpeach.setVisibility(View.VISIBLE);

            // 事件地址
//			if("ORDER".equals(eventEntity.getMsgAppType())){
//				holder.mIvRightAddress.setVisibility(View.VISIBLE);
//			}

            // 消息头像
//            if (mMessageList.get(position).getCmd() == MessageItem.TYPE_SEND) {
//                setHeaderImage(holder.mIvHeader, loginName);
//            } else {
//                setHeaderImage(holder.mIvHeader, item.getToLoginName());
//            }

            // 显示消息类型
            if (eventEntity != null) {
                if ("TASK".equals(eventEntity.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_task));
                } else if ("MEETING".equals(eventEntity.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_meetting));
                } else if ("APPROVE".equals(eventEntity.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_apply));
                } else if ("".equals(eventEntity.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_task));
                }
            } else if (eventEntity2 != null) {
                if ("TASK".equals(eventEntity2.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_task));
                } else if ("MEETING".equals(eventEntity2.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_meetting));
                } else if ("APPROVE".equals(eventEntity2.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_apply));
                } else if ("".equals(eventEntity2.getMsgAppType())) {
                    holder.mTvType.setText(getActivity().getString(R.string.app_task));
                }
            }
        }

        private class ViewHolder {
            public ImageView mIvRead;
            public ImageView mIvHeader;
            public TextView mTvName;
            public TextView mTvTitle;
            public ImageView mIvFinalTime;
            public TextView mTvFinalTime;
            public TextView mTvFinish;
            public ImageView mIvFinish;
            public TextView mTvType;
            public TextView mTvTime;
            public TextView mTvAddress;
            public ImageView mIvAddress;
            public ImageView mIvRightSpeach;
            public ImageView mIvRightAddress;
            public ImageView mIvRightPaper;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        MessageItem messageItem = mMessageList.get(position);
        long msgId = messageItem.getMsgId();
        String from = messageItem.getFromLoginName();
        String to = messageItem.getToLoginName();
        int cmd = messageItem.getCmd();
        String nickName;
        if (cmd == MessageItem.TYPE_RECV) {
            nickName = messageItem.getNickName();
        } else {
            nickName = messageItem.getToNickName();
        }
        Intent intent = new Intent(getActivity(), SearchResultActiviy.class);
        intent.putExtra("nickName", nickName);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("start", msgId);
        startActivity(intent);
    }
}
