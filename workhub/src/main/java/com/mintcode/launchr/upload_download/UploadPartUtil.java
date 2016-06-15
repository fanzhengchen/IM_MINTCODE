package com.mintcode.launchr.upload_download;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.mintcode.chat.util.JsonUtil;
import com.mintcode.launchr.upload_download.UplaodResponsePOJO.Body;
import com.mintcode.launchr.upload_download.UplaodResponsePOJO.Body.Response.Data;
import com.mintcode.launchr.upload_download.UplaodResponsePOJO.Header;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 上传附件工具类
 * 
 * @author ChristLu
 * 
 *         2014/10/17 添加了uploadFiles等接口。@RobinLin
 */
public class UploadPartUtil {
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

	public Data uploadPart(String urlSpec,File file){
		FileDetail uploadBlock = new FileDetail();
		return uploadPart(urlSpec, file, uploadBlock);
	}
	
	public static Data uploadPart(String urlSpec,File file, FileDetail uploadBlock){
		long filePosition = uploadBlock.getSrcOffset();
		byte[] tmp = getByteByPosition(file, 0);
		UplaodResponsePOJO pojo = null;
		while(tmp.length > 0){
			if(tmp.length < BUFFER_SIZE){
				//TODO:进入这里表明这是最后一段数据流！！！！@Robin
				uploadBlock.setFileStatus(1);
			}
			//TODO:
			try {
				System.out.println("tmp.length="+tmp.length);
				pojo = uploadPart(urlSpec, uploadBlock, tmp);
				if(pojo == null){
					return null;
				}
				Header header = pojo.getHeader();
				Body body = pojo.getBody();
				int resCode = header.getResCode();
				boolean isSuccess = body.getResponse().isIsSuccess();
				boolean success = (resCode == 8200 && isSuccess);
				if(success){
					long offset = body.getResponse().getData().getSize();
					String filePath = body.getResponse().getData().getPath();
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
		
		return pojo.getBody().getResponse().getData();
	}
	/**
	 * 上传头像
	 * @param urlSpec
	 * @param file
	 * @param uploadBlock
	 * @return
	 */
	public static Data uploadUserHeadPart(String urlSpec,File file, UserHeadFileDetail uploadBlock){
		long filePosition = uploadBlock.getSrcOffset();
		byte[] tmp = getByteByPosition(file, 0);
		UplaodResponsePOJO pojo = null;
		Log.i("finish","===" + tmp.length);
		while(tmp.length > 0){
			if(tmp.length < BUFFER_SIZE){
				//TODO:进入这里表明这是最后一段数据流！！！！@Robin
				uploadBlock.setFileStatus(1);
			}
			//TODO:
			try {
				pojo = uploadHeadPart(urlSpec, uploadBlock, tmp);
				if(pojo == null){
					return null;
				}
				Header header = pojo.getHeader();
				Body body = pojo.getBody();
				int resCode = header.getResCode();
				boolean isSuccess = body.getResponse().isIsSuccess();
				boolean success = (resCode == 8200 && isSuccess);
				if(success){
					long offset = body.getResponse().getData().getSize();
					String filePath = body.getResponse().getData().getPath();
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

//		String s = pojo.getBody().getResponse().getReason();
//		String dd = TTJSONUtil.convertObjToJson(pojo);
//		Log.i("finish",s + "--");
//		Log.i("finish",dd );
		return pojo.getBody().getResponse().getData();
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
	
	private static UplaodResponsePOJO uploadPart(String urlSpec, final FileDetail uploadBlock,
			final byte[] block) {
		UplaodResponsePOJO pojo = upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				String jsonStr = uploadBlock.toJson();
//				System.out.println(jsonStr);
				out.write(getWrappedBuffer(jsonStr));
				out.write(block);
			}
		});
		
		return pojo;
	}
	
	private static UplaodResponsePOJO uploadHeadPart(String urlSpec, final UserHeadFileDetail uploadBlock,
			final byte[] block) {
		UplaodResponsePOJO pojo = upload(urlSpec, new WriteCallback() {

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


	public static UplaodResponsePOJO upload(String urlSpec, WriteCallback callback) {
		try {
			URL url = new URL(urlSpec);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.addRequestProperty("Accept-Charset", "UTF-8"); 
			connection.addRequestProperty("Content-Type", null);
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
			
			in.close();
			UplaodResponsePOJO pojo = TTJSONUtil.convertJsonToCommonObj(result,
					UplaodResponsePOJO.class);
			return pojo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
