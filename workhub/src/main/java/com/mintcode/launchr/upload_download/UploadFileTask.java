package com.mintcode.launchr.upload_download;

import java.io.File;

import android.os.AsyncTask;
import android.util.Log;

import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.upload_download.UplaodResponsePOJO.Body.Response;
import com.mintcode.launchr.util.TTJSONUtil;

public class UploadFileTask extends AsyncTask<Void, Integer, String> {

	private File file;
	private FileDetail fileDetail;
	private UploadOverLister listener;
	private String SERVER_PATH = LauchrConst.SERVER_PATH
			+ "/Base-Module/Annex/Mobile";

	public UploadFileTask(String fileName, File file, UploadOverLister listener) {
		this.file = file;
		this.listener = listener;
		fileDetail = new FileDetail();
		fileDetail.setFileName(fileName);
	}

	public UploadFileTask(String fileName, String filePath,
			UploadOverLister listener) {
		File file = null;
		if (filePath != null && !filePath.equals("")) {
			file = new File(filePath);
		}
		this.file = file;
		this.listener = listener;
		fileDetail = new FileDetail();
		fileDetail.setFileName(fileName);
	}

	@Override
	protected String doInBackground(Void... params) {
		String json = fileDetail.toJson();
		Log.d("UploadFileTask", "json = " + json);
		return UploadUtil.upload(SERVER_PATH, json, file);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			UplaodResponsePOJO pojo = TTJSONUtil.convertJsonToCommonObj(result,
					UplaodResponsePOJO.class);
			Response response = pojo.getBody().getResponse();
			if (listener == null) {
				return;
			}
			if (response.isIsSuccess()) {
				String path = response.getData().getPath();
				String showid = response.getData().getShowID();
				listener.uploadOver(showid,path);
				Log.d("UploadFileTask", "uplaod success ,url = " + path);
				Log.d("UploadFileTask", "uplaod success ,ShowID = " + showid);
			}else{
				String reason = response.getReason();
				listener.uploadError(reason);
				Log.d("UploadFileTask", "uplaod failed ,reason:" + reason);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface UploadOverLister {
		void uploadOver(String showid, String path);

		void uploadError(String reason);
	}

}
