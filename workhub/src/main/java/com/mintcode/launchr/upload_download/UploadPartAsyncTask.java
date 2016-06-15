package com.mintcode.launchr.upload_download;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.upload_download.UplaodResponsePOJO.Body.Response.Data;
import com.mintcode.launchr.upload_download.UploadFileTask.UploadOverLister;
import com.mintcode.launchr.util.Const;

public class UploadPartAsyncTask extends AsyncTask<Void, Integer, Data> {

	String mShowId;
	private String SERVER_PATH = LauchrConst.SERVER_PATH
			+ "/Base-Module/Annex/Mobile";
	private String USER_SERVER_PATH = LauchrConst.SERVER_PATH
			+ "/Base-Module/Annex/Avatar";
	private UploadOverLister listener;

	public static final int TYPE_TASK = 0;
	public static final int TYPE_APPROVE = 1;
	public static final int TYPE_SCHEDULE = 2;

	public static final  String SHOWID_TASK = Const.SHOWID_TASK;
	public static final String SHOWID_APPROVE = Const.SHOWID_APPROVE;
	public static final  String SHOW_SCHEDULE = Const.SHOW_SCHEDULE;
	
	public static final  String USER_HEAD = "userhead";

	private String TYPE = "";
	private File file;
	private FileDetail fileDetail = new FileDetail();
	private UserHeadFileDetail mUserHeadFileDetail = new UserHeadFileDetail();

	public UploadPartAsyncTask(Context context, String path,UploadOverLister listener) {
		this.listener = listener;
		file = new File(path);
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		fileDetail.setFileName(fileName);
		mUserHeadFileDetail.setFileName(fileName);
	}

	public UploadPartAsyncTask(Context context ,File file,String fileName,UploadOverLister listener){
		this.listener = listener;
		this.file = file;
		fileDetail.setFileName(fileName);
		mUserHeadFileDetail.setFileName(fileName);
	}

	public void setType(int type) {
		switch (type) {
		case TYPE_TASK:
			TYPE = SHOWID_TASK;
			break;
		case TYPE_APPROVE:
			TYPE = SHOWID_APPROVE;
			break;
		case TYPE_SCHEDULE:
			TYPE = SHOW_SCHEDULE;
			break;
		case 3:
			TYPE = USER_HEAD;
			break;
		default:
			
			break;
		}
	}

	@Override
	protected Data doInBackground(Void... arg0) {
		if(TYPE.equals(USER_HEAD)){
			Data json = UploadPartUtil.uploadUserHeadPart(USER_SERVER_PATH, file, mUserHeadFileDetail);
			return json;
		}else{
			fileDetail.setAppShowID(TYPE);
			Data json = UploadPartUtil.uploadPart(SERVER_PATH, file, fileDetail);
			return json;
		}
		
		
	}

	@Override
	protected void onPostExecute(Data json) {
		super.onPostExecute(json);
		if (listener == null) {
			return;
		}
		if (json != null) {
			listener.uploadOver(json.getShowID(),json.getPath());
		} else {
			listener.uploadError("error");
		}
	}

}
