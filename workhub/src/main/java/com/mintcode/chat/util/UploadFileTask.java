package com.mintcode.chat.util;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mintcode.chat.activity.ChatActivity.HandleMsgType;
import com.mintcode.chat.image.AttachDetail;
import com.mintcode.im.Command;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.util.AppUtil;

/**
 * 
 * @author ChristLu 聊天框上传图片的异步工具方法
 */
public class UploadFileTask extends AsyncTask<Object, Integer, String> {

	private AttachDetail detail;
	private KeyValueDBService mValueDBService;
	private File file;
	private Handler mHandler;
	private MessageItem item;
	private String SERVER_PATH;

	public UploadFileTask(Context context, AttachDetail detail, File file,
			Handler mHandler, MessageItem item) {
		mValueDBService = KeyValueDBService.getInstance();
		this.detail = detail;
		this.file = file;
		this.mHandler = mHandler;
		this.item = item;
		SERVER_PATH = mValueDBService.find(Keys.HTTP_IP)+"/launchr/api/upload";
	}

	@Override
	protected String doInBackground(Object... params) {
//		Log.i("infoss",detail.toJson() + " -----");
//		return UploadUtil.upload(SERVER_PATH, detail.toJson(), file);
		return UploadMsgPicPartUtil.uploadPic(SERVER_PATH, detail, file,mHandler,item);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null && !result.equals("null")) {
			Log.d("upload", result);
			item.setContent(result);
			Message msg = Message.obtain();
			if(item.getType().equals(Command.VIDEO)){
				msg.what = HandleMsgType.TYPE_SEND_VIDEO;
			}else if(item.getType().equals(Command.AUDIO)){
				msg.what = HandleMsgType.TYPE_SEND_AUDIO;
			}else{
				msg.what = HandleMsgType.TYPE_SEND_IMAGE;
			}
			msg.obj = item;
			Log.i("------task-load", "onPostExecute");
			mHandler.sendMessage(msg);
		}else{
			item.setSent(Command.SEND_FAILED);
			Message msg = Message.obtain();
			if(item.getType().equals(Command.VIDEO)){
				msg.what = HandleMsgType.TYPE_SEND_VIDEO;
			}else if(item.getType().equals(Command.AUDIO)){
				msg.what = HandleMsgType.TYPE_SEND_AUDIO;
			}else{
				msg.what = HandleMsgType.TYPE_SEND_IMAGE;
			}
			msg.obj = item;
			mHandler.sendMessage(msg);
		}
	}

}