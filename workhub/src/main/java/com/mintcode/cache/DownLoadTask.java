package com.mintcode.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request.Method;
import com.mintcode.cache.DiskLruCache.Snapshot;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;

public class DownLoadTask extends AsyncTask<String, Void, Bitmap> {

	private DiskLruCache diskLruCache;
	private ProgressBar progressBar;
	private String userName;
	public DownLoadTask(DiskLruCache diskLruCache, ProgressBar progressBar, String userName) {
		this.diskLruCache = diskLruCache;
		this.progressBar=progressBar;
		this.userName = userName;
	}
	public DownLoadTask(DiskLruCache diskLruCache, String userName) {
		this.diskLruCache = diskLruCache;
		this.userName = userName;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		String key = MD5.getMD5Str(url);
		FileInputStream fileInputStream = null;
		FileDescriptor fileDescriptor = null;
		try {
			if(diskLruCache == null){
				return null;
			}
			Snapshot snapshot = diskLruCache.get(key);
			if (snapshot == null) {
				DiskLruCache.Editor editor = diskLruCache.edit(key);
				OutputStream outputStream = editor.newOutputStream(0);
				if (downloadUrlToStream(url, outputStream)) {
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
			Bitmap bitmap = null;

			if (fileDescriptor != null) {
				System.out.println(url);
				BitmapFactory.Options op = new BitmapFactory.Options();
				op.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fileDescriptor, null, op);
				op.inSampleSize = CacheUtil.calculateInSampleSize(op,
						CacheManager.WIDTH, CacheManager.HEIGHT);
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

	private boolean downloadUrlToStream(String imgUrl, OutputStream outputStream) {
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
			int size = urlConnection.getContentLength();
			int percent=0;
			int num=0;
			while ((b = in.read()) != -1) {
				out.write(b);
				num++;
				percent=num*100/size;
				if (progressBar != null) {
					progressBar.setProgress(percent);
				}
			}
			Log.v("down", "done");

			return true;
		} catch (final IOException e) {
			e.printStackTrace();
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

}
