package com.mintcode.chat;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.chat.widget.DirectImageView;
import com.mintcode.chat.widget.MaskImage;
import com.mintcode.launchr.R;

import butterknife.Bind;

public class ChatViewHolder {


	@Nullable
	@Bind(R.id.sound_image)
	public TextView textSoundView;

	@Nullable
	@Bind(R.id.time)
	public TextView time;

	@Nullable
	@Bind(R.id.text)
	public TextView textView;

	@Nullable
	@Bind(R.id.tv_chat_name)
	public TextView tvName;

	@Nullable
	@Bind(R.id.iv_head_icon)
	public ImageView ivAvatar;// 头像
	/**
	 * 进度条 显示是否发送成功
	 */
	@Nullable
	@Bind(R.id.sending_bar)
	public ProgressBar progressBarSending;
	// 图片
//	public MaskImage imageView;//

	public DirectImageView imageView;

	// 语音
	@Nullable
	@Bind(R.id.sound_time)
	public TextView tvSoundTime;

	// 是否已读
	@Nullable
	@Bind(R.id.iv_read_mark)
	public ImageView ivReadMark ;
	
	//发送失败标志
	@Nullable
	@Bind(R.id.failed)
	public ImageView ivFailed;
	
	//小视频标志
	@Nullable
	@Bind(R.id.img_vidio)
	public ImageView ivVidio;
	
	//已读标志
	@Nullable
	@Bind(R.id.iv_read)
	public TextView ivRead;
	
	//文件名
	@Nullable
	@Bind(R.id.tv_file_name)
	public TextView tvFileName;
	//文件大小
	@Nullable
	@Bind(R.id.tv_file_size)
	public TextView tvFileSize;
	//文件图标
	@Nullable
	@Bind(R.id.iv_file_image)
	public ImageView ivFileIcon;

	@Nullable
	@Bind(R.id.rel_file)
	public RelativeLayout relFileLayout;
	
	public RelativeLayout relEndTime;
	public RelativeLayout relDeadTime;

	/** 上传进度百分比  */
	@Nullable
	@Bind(R.id.tv_percent)
	public TextView tvPercent;

	/**
	 * 标记重点的星星
	 */
	@Nullable
	@Bind(R.id.mark_point)
	public ImageView ivMarkPoint;
	public ViewGroup container;
	public TextView event_end;
	public TextView event_project_name;
	public TextView event_priority;
	public TextView event_state_name;
	public TextView event_app_name;
	public TextView event_place;
	public TextView event_deadline;
	public TextView event_remark;
	public TextView event_progress;
	public Button event_join;
	public Button event_refuse;
	public Button event_give_back;
	public Button event_reject;
	public Button event_ok;
	public ViewGroup event_layout_bottom_buttons;
	public ViewGroup event_layout_end;
	public ViewGroup form_layout;

	/** 转发消息*/
	@Nullable
	@Bind(R.id.cb_merge_msg)
	public CheckBox mergeMsg;
}
