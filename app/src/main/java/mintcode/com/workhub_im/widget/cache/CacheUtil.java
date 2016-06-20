package mintcode.com.workhub_im.widget.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.mintcode.cache.DiskLruCache.Snapshot;
import com.mintcode.launchr.consts.LauchrConst;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CacheUtil {

	/**
	 * 尝试获得可以缓存的路径。
	 */
	public static File getAvailableDir(Context context) {
		File file = null;
		if (file == null) {
			file = context.getExternalCacheDir();
		}
		if (file == null) {
			file = Environment.getExternalStorageDirectory();
		}
		if (file == null) {
			file = context.getCacheDir();
		}
		return file;
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 根据传进来的宽高压缩图片
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	/**
	 * 图像压缩
	 * 
	 */

	// Path&&URl
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
			int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	
	/*
	 * 将某个输入流存储进diskCache
	 */
	public static void addFileToDiskCache(InputStream inputStream, String key, DiskLruCache diskLruCache) {
		if (inputStream == null || key == null)
			return;
		final String diskLruCacheKey = MD5.getMD5Str(key);
		// 查找key对应的缓存
		Snapshot snapShot = null;
		DiskLruCache.Editor editor = null;

		BufferedOutputStream bufferedOutputStream = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			snapShot = diskLruCache.get(diskLruCacheKey);
			if (snapShot == null) {
				editor = diskLruCache.edit(diskLruCacheKey);
				if (editor != null) {
					OutputStream outputStream = editor.newOutputStream(0);
					bufferedInputStream = new BufferedInputStream(inputStream,
							8 * 1024);
					bufferedOutputStream = new BufferedOutputStream(
							outputStream, 8 * 1024);
					int b;
					while ((b = bufferedInputStream.read()) != -1) {
						bufferedOutputStream.write(b);
					}
					editor.commit();
				}
			} 
		} catch (IOException e) {
			if (editor != null)
				try {
					editor.abort();
				} catch (Exception e2) {
				}
		}

		try {
			if (bufferedInputStream != null)
				bufferedInputStream.close();
			if (bufferedOutputStream != null)
				bufferedOutputStream.close();
		} catch (Exception e) {
		}
	}
	
	public static Bitmap download(String userName, String url ,DiskLruCache diskLruCache){
		String key = MD5.getMD5Str(url);
		FileInputStream fileInputStream = null;
		FileDescriptor fileDescriptor = null;
		Bitmap bitmap = null;
		try {
			Snapshot snapshot = diskLruCache.get(key);
			if (snapshot == null) {
				DiskLruCache.Editor editor = diskLruCache.edit(key);
				OutputStream outputStream = editor.newOutputStream(0);
				if (downloadUrlToStream(userName, url, outputStream)) {
					editor.commit();
				} else {
					editor.abort();
				}
				// 缓存被写入后，再次查找key对应的缓存
				snapshot = diskLruCache.get(key);
			}
			if (snapshot != null) {
				fileInputStream = (FileInputStream) snapshot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			bitmap = null;

			if (fileDescriptor != null) {
				System.out.println(url);
				BitmapFactory.Options op = new BitmapFactory.Options();
				op.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fileDescriptor, null, op);
				op.inSampleSize = CacheUtil.calculateInSampleSize(op, CacheManager.WIDTH, CacheManager.HEIGHT);
				op.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,
						null, op);
			}			
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileDescriptor != null && fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	
	private static boolean downloadUrlToStream(String userName, String imgUrl, OutputStream outputStream) {
		String urlPath = imgUrl;
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlPath);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Cookie", "AppName=launchr;UserName=" + userName);
			in = new BufferedInputStream(urlConnection.getInputStream(),
					8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
			}
		}
		return false;
	}
	
	public static boolean downloadFile(String userName, String fileName, String fileUrl) {
		String path = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path = LauchrConst.DOWNLOAD_FILE_PATH_SDCARD;
		}else{
			path = LauchrConst.DOWNLOAD_FILE_PATH_DATA;
		}
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
		HttpURLConnection urlConnection = null;
		InputStream in = null;
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(path + fileName);
			final URL url = new URL(fileUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Cookie", "AppName=launchr;UserName=" + userName);
			in = urlConnection.getInputStream();
			int b;
			while ((b = in.read()) != -1) {
				outputStream.write(b);
			}
			return true;
		} catch (final IOException e) {
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if(in != null){
					in.close();
				}
			} catch (final IOException e) {
			}
		}
		return false;
	}
	
	public static boolean downloadAudio(String userName, String fileName, String fileUrl) {
		String path = null;
		path = LauchrConst.DOWNLOAD_AUDIO_PATH_SDCARD;
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
		HttpURLConnection urlConnection = null;
		InputStream in = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			outputStream = new FileOutputStream(path + fileName);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			final URL url = new URL(fileUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Cookie", "AppName=launchr;UserName=" + userName + ";Version=Android");
			in = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int b;
			while ((b = in.read(buffer)) != -1) {
				bufferedOutputStream.write(buffer, 0, b);
			}
			bufferedOutputStream.flush();
			return true;
		} catch (final IOException e) {
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if(bufferedOutputStream != null){
					bufferedOutputStream.close();
				}
				if(in != null){
					in.close();
				}
			} catch (final IOException e) {
			}
		}
		return false;
	}
}
