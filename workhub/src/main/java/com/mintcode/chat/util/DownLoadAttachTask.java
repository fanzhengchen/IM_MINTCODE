package com.mintcode.chat.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mintcode.cache.CacheManager;
import com.mintcode.cache.CacheUtil;
import com.mintcode.cache.MD5;
import com.mintcode.chat.activity.ChatActivity.HandleMsgType;
import com.mintcode.chat.audio.AudioItem;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.im.Command;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 聊天中附件下载
 * 
 * @author ChristLu
 * 
 */
public class DownLoadAttachTask extends AsyncTask<Object, Integer, String> {

	private MessageItem item;
	private Context context;
	private Handler mHandler;
	private String url;
	private boolean isImage = false;
	private boolean isVideo = false;
	private boolean isOriginalImage = false;
	private String SERVER_PATH;

	public DownLoadAttachTask(Context context, MessageItem item,
			Handler handler, boolean isOriginalImage) {
		this.item = item;
		this.mHandler = handler;
		

		this.context = context;
		this.isOriginalImage = isOriginalImage;
		if (Command.IMAGE.equals(item.getType())) {
			isImage = true;
		}
		if (Command.VIDEO.equals(item.getType())) {
			isVideo = true;
		}
		SERVER_PATH = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr";
	}

	@Override
	protected String doInBackground(Object... params) {
		String fileName = null;
		CacheManager cache = CacheManager.getInstance(context);
		String content = item.getContent();
		if (isImage || isVideo) {
			AttachItem attach = TTJSONUtil.convertJsonToCommonObj(content,
					AttachItem.class);
			if (isOriginalImage) {
				url = SERVER_PATH + attach.getFileUrl();
			} else {
				String str;
				if (attach.getThumbnail() == null) {
					str = attach.getFileUrl();
				} else {
					str = attach.getThumbnail();
				}
				url = SERVER_PATH + str;
			}
			
			cache.putStreamToExtStorage(url);		
			fileName = cache.getFilePath(url);
			item.setFileName(url);
		} else {
			// 语音
			AudioItem audio = TTJSONUtil.convertJsonToCommonObj(content, AudioItem.class);
			url = SERVER_PATH + audio.getFileUrl();
			item.setTimeText(audio.getAudioLength() + "");
			
			HeaderParam mHeaderParam = LauchrConst.getHeader(context);
			String mUserName = mHeaderParam.getLoginName();
			
			if(CacheUtil.downloadAudio(mUserName, MD5.getMD5Str(url) + IMConst.AUDIO_MEDIA_TYPE, url)){
				item.setFileName(MD5.getMD5Str(url) + IMConst.AUDIO_MEDIA_TYPE);
				return audio.getFileUrl();
			}else{
				return null;
			}
		}
		return fileName;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			if (mHandler != null) {
				Message msg = Message.obtain();
				msg.obj = item;
				if (isImage) {
					msg.what = HandleMsgType.TYPE_REVIMAGE;
				} else if (isVideo) {
					msg.what = HandleMsgType.TYPE_REVVIDEO;
				} else{
					msg.what = HandleMsgType.TYPE_REVAUDIO;
				}
				mHandler.sendMessage(msg);
			}
		}
	}

}
