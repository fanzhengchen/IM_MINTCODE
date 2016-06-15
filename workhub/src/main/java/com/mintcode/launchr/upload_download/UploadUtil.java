package com.mintcode.launchr.upload_download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;

public class UploadUtil {
	public final static String TAG = "UploadUtil";

	public static interface WriteCallback {
		void write(OutputStream out) throws IOException;
	}

	/**
	 * 1格式为byte 
	 * 2 0--3字节：INT类型，表示json用UTF-8编码转成字节数组的长度 
	 * 3 4--n字节：AttachDetail类型，json UTF-8编码 
	 * 4 n+1—结束：附件byte
	 */
	public static byte[] getWrappedBuffer(String text) {
		byte[] bytes = text.getBytes();
		int len = bytes.length;
		byte[] buffer = new byte[len + 4];
		buffer[0] = (byte) (len & 0xFF);
		buffer[1] = (byte) ((len >>> 8) & 0xFF);
		buffer[2] = (byte) ((len >>> 16) & 0xFF);
		buffer[3] = (byte) ((len >>> 24) & 0xFF);
		System.arraycopy(bytes, 0, buffer, 4, len);
		return buffer;
	}

	public static String upload(String urlSpec, final String description,
			final File file) {
		return upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				out.write(getWrappedBuffer(description));
				if (file != null) {
					FileInputStream in = new FileInputStream(file);
					byte[] buffer = new byte[in.available()];

					in.read(buffer);
					in.close();
					out.write(buffer);
				}
			}
		});
	}

	public static String upload(String urlSpec, WriteCallback callback) {
		if (urlSpec == null || callback == null) {
			return null;
		}

		String result = null;

		try {
			URL url = new URL(urlSpec);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream httpOutputStream = connection.getOutputStream();

			callback.write(httpOutputStream);

			httpOutputStream.flush();
			httpOutputStream.close();

			byte[] temp = new byte[1024];
			int status = connection.getResponseCode();
			InputStream in;
			if (status >= HttpStatus.SC_BAD_REQUEST)
				in = connection.getErrorStream();
			else
				in = connection.getInputStream();
			in.read(temp);
			result = new String(temp).trim();
			in.close();
			System.out.println(result);
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
