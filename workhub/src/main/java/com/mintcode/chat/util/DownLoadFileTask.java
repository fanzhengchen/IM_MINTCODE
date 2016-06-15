package com.mintcode.chat.util;

import com.mintcode.cache.CacheUtil;
import com.mintcode.cache.MD5;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.TTJSONUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class DownLoadFileTask extends AsyncTask<Object, Integer, String> {

	private MessageItem item;
	private Context context;
	private Handler mHandler;
	private String url;
	private String SERVER_PATH;
	private String fileName;
	
	public DownLoadFileTask(Context context, MessageItem item,
			Handler handler){
		this.item = item;
		this.mHandler = handler;
		this.context = context;
		SERVER_PATH = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr";
	}
	
	@Override
	protected String doInBackground(Object... params) {
		AttachItem attach = TTJSONUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
		if(attach != null){
			String str;
			if (attach.getFileUrl() != null) {
				str = attach.getFileUrl();
			} else {
				str = attach.getThumbnail();
			}
			url = SERVER_PATH + str;
			
			HeaderParam mHeaderParam = LauchrConst.getHeader(context);
			String mUserName = mHeaderParam.getLoginName();
				
			//得到后缀名,文件名为url经过MD5加密
			fileName = attach.getFileName();
			String afterName = "." + fileName.substring(fileName.lastIndexOf(".") + 1);
			
			if(CacheUtil.downloadFile(mUserName, MD5.getMD5Str(url) + afterName, url)){
				item.setFileName(MD5.getMD5Str(url) + afterName);
				return attach.getFileName();
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);		
		if(mHandler != null){
			if(result == null){
				Message msg = Message.obtain();
				msg.what = 0x0002;
				mHandler.sendMessage(msg);
			}else{
				Message msg = Message.obtain();
				msg.what = 0x0001;
				mHandler.sendMessage(msg);
			}
		}
	}

}
