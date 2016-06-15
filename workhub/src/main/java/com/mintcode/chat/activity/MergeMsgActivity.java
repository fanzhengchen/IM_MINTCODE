package com.mintcode.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.Command;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.IMConst;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.FileDetailActivity;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StephenHe on 2016/4/1.
 */
public class MergeMsgActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private ImageView mIvBack;

    private String title;
    private TextView mTvTitle;

    private List<MergeEntity> mMergeList;
    private MergeAdapter mMergeAdapter;
    private ListView mLvMerge;

    private String mergeContent;

    private List<MessageItem> mPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_msg);

        initView();
    }

    private void initView(){
        title = getIntent().getStringExtra("merge_title");
        mergeContent = getIntent().getStringExtra("merge_content");
        MergeEntity.MergeCard mergeCard = TTJSONUtil.convertJsonToCommonObj(mergeContent, MergeEntity.MergeCard.class);
        if(mergeCard != null){
            mMergeList = mergeCard.getMsg();
        }else{
            mMergeList = new ArrayList<MergeEntity>();
        }

        // 图片消息
        mPhotoList = new ArrayList<MessageItem>();
        for(int i=0; i<mMergeList.size(); i++){
            if(mMergeList.get(i).getType().equals(Command.IMAGE)){
                mPhotoList.add(changeToMsgItem(mMergeList.get(i)));
            }
        }

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack.setOnClickListener(this);

        mTvTitle.setText(title);

        mLvMerge = (ListView) findViewById(R.id.lv_friend);

        mMergeAdapter = new MergeAdapter();
        mLvMerge.setAdapter(mMergeAdapter);
        mLvMerge.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            finish();
        }
    }

    private class MergeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mMergeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMergeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView == null){
                holder = new Holder();
                convertView =  LayoutInflater.from(MergeMsgActivity.this).inflate(R.layout.item_merge_msg, parent, false);
                holder.ivAvatar = (ImageView) convertView.findViewById(R.id.message_head_iv);
                holder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
                holder.tvName = (TextView) convertView.findViewById(R.id.message_name);
                holder.tvTime = (TextView) convertView.findViewById(R.id.message_time);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
//                holder.lineTop = (View) convertView.findViewById(R.id.line_top);
//                holder.lineLeft = (View) convertView.findViewById(R.id.line_top_left);
                holder.relFile = (RelativeLayout) convertView.findViewById(R.id.rel_file_content);
                holder.ivFile = (ImageView) convertView.findViewById(R.id.iv_file_image);
                holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_file_name);
                holder.tvFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);
                holder.tvAudio = (TextView) convertView.findViewById(R.id.tv_audio);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            MergeEntity entity = mMergeList.get(position);

            Info info = TTJSONUtil.convertJsonToCommonObj(entity.getInfo(), Info.class);
            holder.tvName.setText(info.getNickName());
            holder.tvTime.setText(TimeFormatUtil.getSessionTime(entity.getCreateDate(), MergeMsgActivity.this));


            setAvatar(holder.ivAvatar, info.getUserName());

            if(entity.getType().equals(Command.IMAGE)){
                holder.tvAudio.setVisibility(View.GONE);
                holder.relFile.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);

                holder.tvContent.setText(getString(R.string.message_no_data_pic));

                AttachItem attach = JsonUtil.convertJsonToCommonObj(entity.getContent(), AttachItem.class);
                String url = LauchrConst.httpIp + "/launchr" + attach.getThumbnail();
                HeadImageUtil.getInstance(MergeMsgActivity.this).setChatImage(holder.ivImage,
                        url, R.drawable.im_default_image, attach.getThumbnailWidth(), attach.getThumbnailHeight());
            }else if(entity.getType().equals(Command.FILE)){
                holder.tvAudio.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.relFile.setVisibility(View.GONE);

                AttachItem attach = JsonUtil.convertJsonToCommonObj(entity.getContent(), AttachItem.class);
                holder.tvContent.setText("文件:" + attach.getFileName());
                holder.ivAvatar.setImageResource(IMConst.getFileIcon(attach.getFileName()));
                holder.ivFile.setImageResource(IMConst.getFileIcon(attach.getFileName()));
                holder.tvFileName.setText(attach.getFileName());
                holder.tvFileSize.setText(IMConst.FormetFileSize(attach.getFileSize()));
            }else if(entity.getType().equals(Command.AUDIO)){
                holder.tvAudio.setVisibility(View.GONE);
                holder.relFile.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);

                holder.tvContent.setText(entity.getContent());
            } else{
                holder.tvAudio.setVisibility(View.GONE);
                holder.relFile.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);

                holder.tvContent.setText(entity.getContent());
            }
            return convertView;
        }

        class Holder {
            ImageView ivAvatar;
            TextView tvName;
            TextView tvContent;
            TextView tvTime;
            ImageView ivImage;
//            View lineTop;
//            View lineLeft;
            RelativeLayout relFile;
            ImageView ivFile;
            TextView tvFileName;
            TextView tvFileSize;
            TextView tvAudio;
        }
    }

    /** 显示头像，或图片*/
    private void setAvatar(ImageView ivAvatar, String mUserId){
        HeadImageUtil.getInstance(MergeMsgActivity.this).setAvatarResourceWithUserId(ivAvatar, mUserId, 2, 60, 60);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLvMerge){
            MergeEntity entity = mMergeList.get(position);
            if(entity.getType().equals(Command.IMAGE)){
                // 查看图片
                int location = 0;
                for(int i=0; i<mPhotoList.size(); i++){
                    if(mPhotoList.get(i).getClientMsgId() == mMergeList.get(position).getClientMsgId()){
                        location = i;
                        break;
                    }
                }
                Intent intent = new Intent(MergeMsgActivity.this, PreviewLargeImageActivity.class);
                intent.putExtra("position",location);
                intent.putExtra("item", changeToMsgItem(mMergeList.get(position)));
                intent.putExtra("list", (Serializable) mPhotoList);
                startActivity(intent);
            }else if(entity.getType().equals(Command.FILE)){
                // 查看文件
                Intent intent = new Intent(MergeMsgActivity.this, FileDetailActivity.class);
                intent.putExtra("fileMessage", changeToMsgItem(entity));
                startActivity(intent);
            }else if(entity.getType().equals(Command.AUDIO)){
//                String filePath = item.getFileName();
//                if (filePath != null) {
//                    if (!mAudioRecordPlayUtil.isPlaying()) {
//                        mAudioRecordPlayUtil.setFileName(filePath);
//                        mAudioRecordPlayUtil.startPlaying();
//                        AudioPlayingAnimation.play(holder, item);
//                    } else {
//                        mAudioRecordPlayUtil.stopPlaying();
//                        AudioPlayingAnimation.stop();
//                    }
//                } else {
//                    toast(getString(R.string.msg_not_live));
//                }
            }
        }
    }

    private MessageItem changeToMsgItem(MergeEntity entity){
        MessageItem item = new MessageItem();
        item.setFromLoginName(entity.getFrom());
        item.setClientMsgId(entity.getClientMsgId());
        item.setContent(entity.getContent());
        item.setType(entity.getType());
        item.setInfo(entity.getInfo());
        item.setMsgId(entity.getMsgId());
        item.setCreateDate(entity.getCreateDate());
        item.setModified(entity.getModified());
        return item;
    }

    // 根据语音时长设置语音气泡长短
    private String formatSoundText(String timeText) {
        if ((timeText != null) && !timeText.equals("")) {
            int time = Integer.parseInt(timeText);
            if (time > 30) {
                time = 30;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < time; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
        return "";
    }
}
