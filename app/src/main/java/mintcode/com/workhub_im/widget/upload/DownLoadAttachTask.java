package mintcode.com.workhub_im.widget.upload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;

import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.pojo.LoginUserData;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.widget.auido.AudioItem;
import mintcode.com.workhub_im.widget.cache.CacheManager;
import mintcode.com.workhub_im.widget.cache.CacheUtil;
import mintcode.com.workhub_im.widget.cache.MD5;


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
		SERVER_PATH = AppConsts.httpIp + "/launchr";
	}

	@Override
	protected String doInBackground(Object... params) {
		String fileName = null;
		CacheManager cache = CacheManager.getInstance(context);
		String content = item.getContent();
		if (isImage || isVideo) {
//			AttachItem attach = JSON.parseObject(content,AttachItem.class);
//			if (isOriginalImage) {
//				url = SERVER_PATH + attach.getFileUrl();
//			} else {
//				String str;
//				if (attach.getThumbnail() == null) {
//					str = attach.getFileUrl();
//				} else {
//					str = attach.getThumbnail();
//				}
//				url = SERVER_PATH + str;
//			}
//
//			cache.putStreamToExtStorage(url);
//			fileName = cache.getFilePath(url);
//			item.setFileName(url);
		} else {
			// 语音
			AudioItem audio = JSON.parseObject(content,AudioItem.class);
			url = SERVER_PATH + audio.getFileUrl();
			item.setTimeText(audio.getAudioLength() + "");
			
//			HeaderParam mHeaderParam = LauchrConst.getHeader(context);
//			String mUserName = mHeaderParam.getLoginName();
			//TODO  userName
			String name = UserPrefer.getUserName();
			if(CacheUtil.downloadAudio(name, MD5.getMD5Str(url) + ".amr", url)){
				item.setFileName(MD5.getMD5Str(url) + ".amr");
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
					msg.what = ChatViewUtil.TYPE_REVIMAGE;
				} else if (isVideo) {
					msg.what = ChatViewUtil.TYPE_REVVIDEO;
				} else{
					msg.what = ChatViewUtil.TYPE_REVAUDIO;
				}
				mHandler.sendMessage(msg);
			}
		}
	}

}
