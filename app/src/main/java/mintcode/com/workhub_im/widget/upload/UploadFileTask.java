package mintcode.com.workhub_im.widget.upload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.File;

import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.im.pojo.AttachDetailResponse;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;

/**
 * 
 * @author ChristLu 聊天框上传图片的异步工具方法
 */
public class UploadFileTask extends AsyncTask<Object, Integer, String> {

	private AttachDetailResponse detail;
//	private KeyValueDBService mValueDBService;
	private File file;
	private Handler mHandler;
	private MessageItem item;
	private String SERVER_PATH;

	public UploadFileTask(Context context, AttachDetailResponse detail, File file,
						  Handler mHandler, MessageItem item) {
//		mValueDBService = KeyValueDBService.getInstance();
		this.detail = detail;
		this.file = file;
		this.mHandler = mHandler;
		this.item = item;
		SERVER_PATH = AppConsts.ATTACH_PATH + "/launchr/api/upload";
	}

	@Override
	protected String doInBackground(Object... params) {
//		Log.i("infoss",detail.toJson() + " -----");
//		return UploadUtil.upload(SERVER_PATH, detail.toJson(), file);
		return UploadMsgPartUtil.uploadPic(SERVER_PATH, detail, file,mHandler,item);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null && !result.equals("null")) {
			Log.d("upload", result);
			item.setContent(result);
			Message msg = Message.obtain();
			if(item.getType().equals(Command.VIDEO)){
				msg.what = ChatViewUtil.TYPE_SEND_VIDEO;
			}else if(item.getType().equals(Command.AUDIO)){
				msg.what = ChatViewUtil.TYPE_SEND_AUDIO;
			}else{
				msg.what = ChatViewUtil.TYPE_SEND_IMAGE;
			}
			msg.obj = item;
			Log.i("------task-load", "onPostExecute");
//			mHandler.sendMessage(msg);
		}else{
			item.setSent(Command.SEND_FAILED);
			Message msg = Message.obtain();
			if(item.getType().equals(Command.VIDEO)){
				msg.what = ChatViewUtil.TYPE_SEND_VIDEO;
			}else if(item.getType().equals(Command.AUDIO)){
				msg.what = ChatViewUtil.TYPE_SEND_AUDIO;
			}else{
				msg.what = ChatViewUtil.TYPE_SEND_IMAGE;
			}
			msg.obj = item;
//			mHandler.sendMessage(msg);
		}
	}

}