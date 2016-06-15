package com.mintcode.chat.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.ResultActionEntity;
import com.mintcode.chat.image.AttachDetail;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.upload_download.UserHeadFileDetail;
import com.mintcode.launchr.util.TTJSONUtil;

import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * 上传附件工具类
 *
 * @author KevinQiao
 *
 *
 */
public class UploadMsgPicPartUtil {
	public final static String TAG = "UploadUtil";

	public static interface WriteCallback {
		void write(OutputStream out) throws IOException;
	}

	public static byte[] getWrappedBuffer(String text) {
		byte[] bytes = null;
		try {
			String json = new String(text.getBytes(), "UTF-8");
			bytes = json.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int len = bytes.length;
		byte[] buffer = new byte[len + 4];
		buffer[0] = (byte) (len & 0xFF);
		buffer[1] = (byte) ((len >>> 8) & 0xFF);
		buffer[2] = (byte) ((len >>> 16) & 0xFF);
		buffer[3] = (byte) ((len >>> 24) & 0xFF);
		System.arraycopy(bytes, 0, buffer, 4, len);
		return buffer;


	}




	/**
	 * 上传图片 聊天
	 * @param
	 * @param
	 * @param file
	 * @return
	 */
	//TODO 头像分包上传
	public static String uploadPic(String url,AttachDetail detail,  final File file,Handler handler,MessageItem item) {
		detail.setSrcOffset(0);
//		detail.setFileUrl("");
		detail.setFileStatus(0);
		ResultActionEntity entity = uploadPart(url, file, detail,handler,item);
//		String s = uploadPart(url, detail, null,file);
		String result = TTJSONUtil.convertObjToJson(entity);
		return result;
	}


	public static ResultActionEntity uploadPart(String urlSpec,File file, AttachDetail detail,Handler handler,MessageItem item){
		long filePosition = detail.getSrcOffset();

		byte[] tmp = getByteByPosition(file, 0);
		ResultActionEntity pojo = null;
		while(tmp.length > 0){
			Log.i("infoss" , "===11==");
			pojo = null;
			if(tmp.length < BUFFER_SIZE){
				//TODO:进入这里表明这是最后一段数据流！！！！@Robin
				detail.setFileStatus(1);
			}
			//TODO:
			try {
				System.out.println("tmp.length=" + tmp.length);
				Log.i("upload", detail.toJson() + "---");
				pojo = uploadPart(urlSpec, detail, tmp);
				if(pojo == null){
					return null;
				}
				int resCode = pojo.getCode();
				if(resCode == 2000){
					long offset = pojo.getFileSize();
					String filePath = pojo.getFileUrl();
					detail.setSrcOffset((int) offset);
					detail.setFileUrl(filePath);
				}
				filePosition += tmp.length;
				tmp = getByteByPosition(file, filePosition);

				// 发送进度消息
				Message msg = Message.obtain();
				msg.what = ChatActivity.HandleMsgType.TYPE_FILE_UPLOAD;
				msg.obj = item;
				long percent = (pojo.getFileSize() * 100) / file.length();
				item.setPercent(percent + "");
				handler.sendMessage(msg);

				String result = TTJSONUtil.convertObjToJson(pojo);
				Log.i("upload", "开始//" + result + "---");
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		
		return pojo;
	}
	/**
	 * 上传头像
	 * @param urlSpec
	 * @param file
	 * @param uploadBlock
	 * @return
	 */
	public static String uploadUserHeadPart(String urlSpec,File file, UserHeadFileDetail uploadBlock){
		long filePosition = uploadBlock.getSrcOffset();
		byte[] tmp = getByteByPosition(file, 0);
		ResultActionEntity pojo = null;
		while(tmp.length > 0){
			if(tmp.length < BUFFER_SIZE){
				//TODO:进入这里表明这是最后一段数据流！！！！@Robin
				uploadBlock.setFileStatus(1);
			}
			//TODO:
			try {
				System.out.println("tmp.length="+tmp.length);
				pojo = uploadHeadPart(urlSpec, uploadBlock, tmp);
				if(pojo == null){
					return null;
				}
				int resCode = pojo.getCode();
				if(resCode == 2000){
					long offset = pojo.getFileSize();
					String filePath = pojo.getFileUrl();
					uploadBlock.setSrcOffset(offset);
					uploadBlock.setFilePath(filePath);
				}
				filePosition += tmp.length;
				tmp = getByteByPosition(file, filePosition);
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		
		return pojo.getFileUrl();
	}
	public static int BUFFER_SIZE = 100*1024;
	
	private static byte[] getByteByPosition(File file,long pos){
		byte[] tmp = null;
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			long fileSize = raf.length();
			raf.seek(pos);
			long position = channel.position();
			int bufferSize = (int) ((fileSize - position < BUFFER_SIZE) ? (fileSize - position) : BUFFER_SIZE);
			tmp = new byte[bufferSize];
			raf.read(tmp);
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tmp;
	}
	
	private static ResultActionEntity uploadPart(String urlSpec, final AttachDetail detail,
			final byte[] block) {
		ResultActionEntity pojo = null;
		pojo = upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				String jsonStr = detail.toJson();
				Log.i("infoss",  "-------" +  "====" + jsonStr);
//				System.out.println(jsonStr);
				out.write(getWrappedBuffer(jsonStr));
				out.write(block);

			}
		});
		
		return pojo;
	}
	
	private static ResultActionEntity uploadHeadPart(String urlSpec, final UserHeadFileDetail uploadBlock,
			final byte[] block) {
		ResultActionEntity pojo = upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				String jsonStr = uploadBlock.toJson();
				System.out.println(jsonStr);
				out.write(getWrappedBuffer(jsonStr));
				out.write(block);
			}
		});
		
		return pojo;
	}


	public static ResultActionEntity upload(String urlSpec, WriteCallback callback) {
		try {

			URL url = new URL(urlSpec);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.addRequestProperty("Accept-Charset", "UTF-8");
			connection.addRequestProperty("Content-Type","application/json");
			connection.addRequestProperty("Accept","application/json");
//
//
//			URL url = new URL(urlSpec);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setRequestMethod("POST");
////			connection.setRequestProperty("Charset", "UTF-8");
//			connection.setRequestProperty("Accept",
//					"application/json;charset=UTF-8");
//			connection.addRequestProperty("Content-Type", null);
			connection.setDoOutput(true);

			OutputStream httpOutputStream = connection.getOutputStream();
			
			callback.write(httpOutputStream);
			
			httpOutputStream.flush();
			httpOutputStream.close();
			//
//			HttpPost post = new HttpPost(urlSpec);
//			post.setHeader("Content-Type", null);
//			
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			callback.write(out);
//			out.flush();
//			out.close();
//			ByteArrayEntity entity = new ByteArrayEntity(out.toByteArray());
//			post.setEntity(entity);
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			InputStream in = response.getEntity().getContent();
			//

			InputStream in = connection.getInputStream();
//<<<<<<< .mine
////			FileInputStream fi = new 
////			in.available()
//			
////			byte[] temp = new byte[in.available()];
//			
//			// available 不适合作为数组的初始长度获取
//			byte[] temp = new byte[1024 * 2];
//			in.read(temp);
//			String result = new String(temp).trim();
//=======
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String tempbf;
			StringBuffer html = new StringBuffer();
			while ((tempbf = reader.readLine()) != null) {
				html.append(tempbf);
			}
			
//			byte[] temp = new byte[in.available()];
//			in.read(temp);
			String result = html.toString().trim();
//			Log.i("infossresult",  "====执行了===" + result);
//			Log.i("infoss", LauchrConst.header.getAuthToken() + "====执行了===" + result);
			in.close();
			ResultActionEntity pojo = TTJSONUtil.convertJsonToCommonObj(result,
					ResultActionEntity.class);
			return pojo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static String upload2(String urlSpec, WriteCallback callback) {
		if (urlSpec == null || callback == null) {
			return null;
		}

		boolean success = true;

		String result = null;

		try {
			URL url = new URL(urlSpec);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("accept",
					"application/json;charset=UTF-8");
			connection.setRequestProperty("content-type",
					"application/json;charset=UTF-8");
			connection.setDoOutput(true);
			OutputStream httpOutputStream = connection.getOutputStream();

			callback.write(httpOutputStream);

			httpOutputStream.flush();
			httpOutputStream.close();

			// TODO
			byte[] temp = new byte[1024];
			int status = connection.getResponseCode();
			InputStream in;
			if (status >= HttpStatus.SC_BAD_REQUEST)
				in = connection.getErrorStream();
			else
				in = connection.getInputStream();
			in.read(temp);
			result = new String(temp);
			Log.e("result", result + "");
			in.close();
			success = success && result.contains("2000");
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

		return result;
	}
}
