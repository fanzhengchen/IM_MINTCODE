package com.mintcode.chat.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;

import android.graphics.Bitmap;
import android.util.Log;

import com.mintcode.launchr.util.AppUtil;

/**
 * 上传附件工具类
 * 
 * @author ChristLu
 * 
 *         2014/10/17 添加了uploadFiles等接口。@RobinLin
 */
public class UploadUtil {
	public final static String TAG = "UploadUtil";

	public static interface WriteCallback {
		void write(OutputStream out) throws IOException;
	}

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

	public static String upload(String urlSpec, String description) {
		return upload(urlSpec, description, "");
	}

	public static String upload(String urlSpec, String description,
			String absoluteFilePath) {
		File file = null;
		if (absoluteFilePath != null && !absoluteFilePath.equals("")) {
			file = new File(absoluteFilePath);
		}
		return upload(urlSpec, description, file);
	}

	/**
	 * 上传多附件接口
	 * 
	 * @param urlSpec
	 *            上传地址
	 * @param description
	 *            json流
	 * @param
	 *
	 * @return
	 */
	// public static boolean upload(String urlSpec, String description,
	// List<String> absoluteFilePaths) {
	// List<File> files = new ArrayList<File>();
	// if (absoluteFilePaths != null) {
	// for (int i = 0; i < absoluteFilePaths.size(); i++) {
	// files.add(new File(absoluteFilePaths.get(i)));
	// }
	// }
	// return uploadFiles(urlSpec, description, files);
	// }

	public static String upload(String urlSpec, final String description,
			final File file) {
		return upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				Log.i("infoss" , description + "===更改以前==");
				out.write(getWrappedBuffer(description));
				if (file != null) {
					FileInputStream in = new FileInputStream(file);
					byte[] buffer = new byte[in.available()]; // TODO: new
					Log.i("infoss",buffer.length + "---");											// byte[1024]
					in.read(buffer);
					in.close();
					out.write(buffer);
				}
			}
		});
	}

	/**
	 * 上传多附件接口
	 * 
	 * @param urlSpec
	 *            上传地址
	 * @param params
	 *            json流
	 * @param files
	 *            文件数组
	 * @return
	 */
	public static int uploadFiles(String urlSpec,
			final Map<String, String> params, final Map<String, File> files) {
		return FileImageUpload.uploadFile(urlSpec, params, files);
		// return upload(urlSpec, new WriteCallback() {
		//
		// @Override
		// public void write(OutputStream out) throws IOException {
		// out.write(getWrappedBuffer(description));
		// if (files != null) {
		// for (int i = 0; i < files.size(); i++) {
		// FileInputStream in = new FileInputStream(files.get(i));
		// byte[] buffer = new byte[in.available()]; // TODO: new
		// // byte[1024]
		// in.read(buffer);
		// in.close();
		// out.write(buffer);
		// }
		// }
		// }
		// });
	}

	public static String uploadFiles(String urlSpec, final String description,
			final List<File> files, int i) {
		return upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				out.write(getWrappedBuffer(description));
				if (files != null) {
						for (int i = 0; i < files.size(); i++) {
							FileInputStream in = new FileInputStream(files.get(i));
							byte[] buffer = new byte[in.available()]; // TODO: new
							// byte[1024]
							in.read(buffer);
							in.close();
							out.write(buffer);
						}
				}
			}
		}, i);
	}

	public static String upload(String urlSpec, String description, Bitmap bmp) {
		return upload(urlSpec, getWrappedBuffer(description), bmp);
	}

	public static String upload(String urlSpec, byte[] buffer, final Bitmap bmp) {
		return upload(urlSpec, buffer, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
		});
	}

	public static String upload(String urlSpec, final byte[] buffer,
			final WriteCallback callback) {
		return upload(urlSpec, new WriteCallback() {

			@Override
			public void write(OutputStream out) throws IOException {
				out.write(buffer);
				callback.write(out);
			}
		});
	}

	public static String upload(String urlSpec, WriteCallback callback) {
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

	public static String upload(String urlSpec, WriteCallback callback, int i) {

		String success = "";

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

			// TODO
			byte[] temp = new byte[1024];
			InputStream in = connection.getInputStream();
			in.read(temp);
			String result = new String(temp);
			in.close();
			int start = result.indexOf("errorMsg") + 11;
			success = result.substring(start, result.trim().length() - 2);
		} catch (Exception e) {
			return "";
		}

		return success;
	}

	public static class FileImageUpload {
		@SuppressWarnings("unused")
		private static final String TAG = "uploadFile";
		private static final int TIME_OUT = 10 * 10000000; // 超时时间
		private static final String CHARSET = "utf-8"; // 设置编码
		public static final boolean SUCCESS = true;
		public static final boolean FAILURE = false;

		/**
		 * * android上传文件到服务器
		 * 
		 * @param file
		 *            需要上传的文件
		 * @param RequestURL
		 *            请求的rul
		 * @return 返回响应的内容
		 */
		public static boolean uploadFile(File file, String RequestURL) {
			String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
			String PREFIX = "--", LINE_END = "\r\n";
			String CONTENT_TYPE = "multipart/form-data"; // 内容类型
			try {
				URL url = new URL(RequestURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(TIME_OUT);
				conn.setConnectTimeout(TIME_OUT);
				conn.setDoInput(true); // 允许输入流
				conn.setDoOutput(true); // 允许输出流
				conn.setUseCaches(false); // 不允许使用缓存
				conn.setRequestMethod("POST"); // 请求方式
				conn.setRequestProperty("Charset", CHARSET);
				// 设置编码
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Content-Type", CONTENT_TYPE
						+ ";boundary=" + BOUNDARY);
				if (file != null) {
					/** * 当文件不为空，把文件包装并且上传 */
					OutputStream outputSteam = conn.getOutputStream();
					DataOutputStream dos = new DataOutputStream(outputSteam);
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);
					/**
					 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					 * filename是文件的名字，包含后缀名的 比如:abc.png
					 */
					sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
							+ file.getName() + "\"" + LINE_END);
					sb.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINE_END);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
					InputStream is = new FileInputStream(file);
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = is.read(bytes)) != -1) {
						dos.write(bytes, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());
					byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
							.getBytes();
					dos.write(end_data);
					dos.flush();
					/**
					 * 获取响应码 200=成功 当响应成功，获取响应的流
					 */
					int status = conn.getResponseCode();
					InputStream in;
					if (status >= HttpStatus.SC_BAD_REQUEST) {
						in = conn.getErrorStream();
						in.close();
						return FAILURE;
					} else {
						in = conn.getInputStream();
					}
					in.read(bytes);
					String result = new String(bytes);
					in.close();
					boolean success = result.contains("2000");
					return success;
					// if(res==200)
					// {
					//
					// return SUCCESS;
					// }
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return FAILURE;
		}

		public static int uploadFile(String actionUrl,
				Map<String, String> params, Map<String, File> files) {

			String BOUNDARY = UUID.randomUUID().toString();
			String PREFIX = "--", LINEND = "\r\n";
			String MULTIPART_FROM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";
			URL uri;
			try {
				uri = new URL(actionUrl);
				HttpURLConnection conn = (HttpURLConnection) uri
						.openConnection();
				conn.setReadTimeout(5 * 1000);
				conn.setDoInput(true);// 允许输入
				conn.setDoOutput(true);// 允许输出
				conn.setUseCaches(false);
				conn.setRequestMethod("POST"); // Post方式
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Charsert", "UTF-8");
				conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
						+ ";boundary=" + BOUNDARY);

				// 首先组拼文本类型的参数
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}

				DataOutputStream outStream = new DataOutputStream(
						conn.getOutputStream());
				outStream.write(sb.toString().getBytes());

				// 发送文件数据
				if (files != null)
					for (Map.Entry<String, File> file : files.entrySet()) {
						StringBuilder sb1 = new StringBuilder();
						sb1.append(PREFIX);
						sb1.append(BOUNDARY);
						sb1.append(LINEND);
						sb1.append("Content-Disposition: form-data; name=\""
								+ file.getKey() + "\"; filename=\""
								+ file.getValue().getName() + "\"" + LINEND);
						sb1.append("Content-Type: application/octet-stream; charset="
								+ CHARSET + LINEND);
						sb1.append(LINEND);
						outStream.write(sb1.toString().getBytes());
						InputStream is = new FileInputStream(file.getValue());
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}

						is.close();
						outStream.write(LINEND.getBytes());
					}

				// 请求结束标志
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
						.getBytes();
				outStream.write(end_data);
				outStream.flush();

				// 得到响应码
				int res = conn.getResponseCode();

				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				InputStream in;
				if (res >= HttpStatus.SC_BAD_REQUEST) {
					in = conn.getErrorStream();
					in.close();
					return -1;
				} else {
					in = conn.getInputStream();
				}
				byte[] bytes = new byte[1024];
				in.read(bytes);
				String result = new String(bytes);
				in.close();
//				boolean success = result.contains("2000");
//				success = result.contains("2200");
				if (result.contains("2200")) {
					return 2200;
				} else if (result.contains("2000")) {
					return 2000;
				}
//				return success;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			return -1;
			// InputStream in = conn.getInputStream();
			// InputStreamReader isReader = new InputStreamReader(in);
			// BufferedReader bufReader = new BufferedReader(isReader);
			// String line = null;
			// String data = "";
			//
			// while ((line = bufReader.readLine()) == null)
			// data += line;
			//
			// if (res == 200) {
			// int ch;
			// StringBuilder sb2 = new StringBuilder();
			// while ((ch = in.read()) != -1) {
			// sb2.append((char) ch);
			// }
			// }
			// outStream.close();
			// conn.disconnect();
			// return in.toString();
		}
	}

}
