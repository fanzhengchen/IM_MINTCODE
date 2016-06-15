package com.mintcode.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * LruCache的封装类。 类整体用单例模型。
 * 
 * @author RobinLin
 * 
 */
public class MImageCache {
	private static MImageCache sMImageCache;
	private Context mContext;

	/**
	 * 尝试获得可以缓存的路径。
	 * 
	 * @param context
	 * @return
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

	public MImageCache(Context context) {
		mContext = context;
		mCacheDir = context.getExternalCacheDir();
		if (mCacheDir == null) {
			mCacheDir = Environment.getExternalStorageDirectory();
		}
		if (mCacheDir == null) {
			mCacheDir = context.getCacheDir();
		}
		mCacheDir.mkdirs();
	}

	// public MImageCache(String dirPath) {
	// mCacheDir = new File(dirPath);
	// mCacheDir.mkdirs();
	// }

	public static MImageCache getInstance(Context context) {
		if (sMImageCache == null) {
			sMImageCache = new MImageCache(context);
		}
		return sMImageCache;
	}

	// public static MImageCache getInstance(String dirPath) {
	// if (sMImageCache == null) {
	// sMImageCache = new MImageCache(dirPath);
	// }
	// return sMImageCache;
	// }

	// 开辟8M硬缓存空间
	private final int hardCachedSize = 8 * 1024 * 1024;
	// hard cache
	private final LruCache<String, Bitmap> mLruCacheBitmapCache = new LruCache<String, Bitmap>(
			hardCachedSize) {
		@Override
		public int sizeOf(String key, Bitmap value) {
			return value.getRowBytes() * value.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			Log.v("tag", "hard cache is full , push to soft cache");
			// 硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
			mSoftBitmapCache.put(key, new SoftReference<Bitmap>(oldValue));
		}
	};

	// 软引用
	private static final int SOFT_CACHE_CAPACITY = 40;
	private final LinkedHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
			SOFT_CACHE_CAPACITY, 0.75f, true) {
		private static final long serialVersionUID = -8063297216284380955L;

		@Override
		public SoftReference<Bitmap> put(String key, SoftReference<Bitmap> value) {
			return super.put(key, value);
		}

		@Override
		protected boolean removeEldestEntry(
				Entry<String, SoftReference<Bitmap>> eldest) {
			if (size() > SOFT_CACHE_CAPACITY) {
				Log.v("tag", "Soft Reference limit , purge one");
				return true;
			}
			return false;
		}
	};

	// 缓存bitmap
	private boolean putBitmapToLruCache(String key, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (mLruCacheBitmapCache) {
				mLruCacheBitmapCache.put(key, bitmap);
			}
			return true;
		}
		return false;
	}

	public Bitmap getBitmapAlbum(String url) {
		String key = MD5.getMD5Str(url);
		String ablum_key = "album_" + key;
		Bitmap bitmap = getBitmap(ablum_key, false);
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getBitmap(key, false);
		if (bitmap == null) {
			return null;
		}
		Bitmap ablum_bitmap = getAblum(bitmap);
		// bitmap.recycle();
		putBitmapToLruCache(ablum_key, ablum_bitmap);
		// if(bitmap == null){
		// bitmap = getBitmap(key, false);
		// if(bitmap != null){
		// putAblumToExtStorage(key, bitmap);
		// }
		// }
		return ablum_bitmap;
	}

	/**
	 * 根据传入的Key先后从硬缓存、软引用缓存和外部存储中读取对应的bitmap
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmap(String url, boolean needMD5) {
		String key;
		if (needMD5) {
			key = MD5.getMD5Str(url);
		} else {
			key = url;
		}
		Bitmap bitmap = null;
		synchronized (mLruCacheBitmapCache) {
			if (key != null) {
				bitmap = mLruCacheBitmapCache.get(key);
				if (bitmap != null) {
					Log.i("", "get bitmap from mLruCacheBitmapCache");
					return bitmap;
				}
			}
		}
		// 硬引用缓存区间中读取失败，从软引用缓存区间读取
		synchronized (mSoftBitmapCache) {
			SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(key);
			if (bitmapReference != null) {
				bitmap = bitmapReference.get();
				if (bitmap != null) {
					Log.i("", "get bitmap from mSoftBitmapCache");
					return bitmap;
				} else {
					Log.v("tag", "soft reference 已经被回收");
					mSoftBitmapCache.remove(key);
				}
			}
		}
		// 继续尝试从外部存储中读取图片。。。
		synchronized (mFileCache) {
			bitmap = getBitmapFromExtStorageAndSaveToCache(key);
			if (bitmap != null) {
				Log.i("", "get bitmap from ExtStorage");
				return bitmap;
			}
		}
		Log.i("", "get bitmap null");
		return null;
	}

	// TODO:缓存文件路径
	private File mCacheDir;
	private static final int MAX_CACHE_SIZE = 500 * 1024 * 1024; // 20M
	private static int progress;
	private final LruCache<String, Long> mFileCache = new LruCache<String, Long>(
			MAX_CACHE_SIZE) {
		@Override
		public int sizeOf(String key, Long value) {
			return value.intValue();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key, Long oldValue,
				Long newValue) {
			File file = null;
			try {
				file = getFile(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (file != null)
				file.delete();
		}
	};

	public String putStreamToExtStorage(String url) {
		String key = MD5.getMD5Str(url);
		String fileName = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			File file = getFile(key);
			if (!file.exists()) {
				file.createNewFile();
			}
			int c;
			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(file);
			byte[] b = new byte[100];
			while ((c = in.read(b)) != -1) {
				out.write(b, 0, c);
			}
			conn.getContentLength();
			out.flush();
			in.close();
			out.close();

			fileName = file.getAbsolutePath();
			Log.i("infos", "fileName:" + fileName);
//			Bitmap bitmap = BitmapFactory.decodeStream(
//					new FileInputStream(file), null, sBitmapOptions);
			Bitmap bitmap = BitmapFactory.decodeFile(fileName);
			putAblumToExtStorage(key, bitmap);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}

	/**
	 * 缓存bitmap到外部存储,可以给图片下载类使用！！
	 * 
	 * @param key
	 *            文件名
	 * @param bitmap
	 *            图片
	 * @return 是否成功写入
	 */
	public boolean putBitmapToExtStorage(String url, Bitmap bitmap) {
		String key = MD5.getMD5Str(url);
		File file;
		try {

			file = getFile(key);

			// 讲bitmap存到外部存储里去
			FileOutputStream fos = getOutputStream(file);
			boolean saved = bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

			// 检查是否正确写入外部存储
			if (saved) {
				putAblumToExtStorage(key, bitmap);
				synchronized (mFileCache) {
					mFileCache.put(key, getFile(key).length());
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean putAblumToExtStorage(String key, Bitmap bitmap) {
		File file;
		try {
			key = "ablum_" + key;
			file = new File(mCacheDir, key);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// if (!file.exists()) {
			// file.mkdirs();
			// }
			if (!file.exists()) {
				file.createNewFile();
			}
			Bitmap ablum_bitmap = getAblum(bitmap);
			// bitmap.recycle();
			// 讲bitmap存到外部存储里去
			FileOutputStream fos = new FileOutputStream(file);
			boolean saved = ablum_bitmap
					.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

			// 检查是否正确写入外部存储
			if (saved) {
				synchronized (mFileCache) {
					mFileCache.put(key, getFile(key).length());
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private Bitmap getAblum(Bitmap bitmap) {
		// int HEIGHT = TTDensityUtil.dip2px(mContext, 100);
		int width = (int) (mContext.getResources().getDisplayMetrics().density * 100);
		int height = bitmap.getHeight() * width / bitmap.getWidth();
		// int h = 150;
		// if(h <= HEIGHT){
		// float scale = h / HEIGHT;
		// int ws = (int) (bitmap.getWidth() / scale);
		try {
			return Bitmap.createScaledBitmap(bitmap, width, height, false);
		} catch (Exception e) {
			return bitmap;
		}
		// }
		// return bitmap;
	}

	private File getFile(String fileName) throws FileNotFoundException {
		File file = new File(mCacheDir, fileName);
		if (file.isDirectory()) {
			throw new FileNotFoundException("有同名文件夹");
		}
		return file;
	}

	// 根据key获取OutputStream
	private FileOutputStream getOutputStream(File file) {
		if (mCacheDir == null)
			return null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fos;
	}

	// 获取bitmap
	private static BitmapFactory.Options sBitmapOptions;
	static {
		sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inPurgeable = true; // bitmap can be purged to disk
	}

	private Bitmap getBitmapFromExtStorageAndSaveToCache(String key) {
		File bitmapFile;
		try {
			makeRootDirectory(mCacheDir.getAbsolutePath());
			bitmapFile = new File(mCacheDir, key);

			if (bitmapFile != null) {
				FileInputStream is = new FileInputStream(bitmapFile);
				Bitmap bitmap = BitmapFactory.decodeStream(is, null,
						sBitmapOptions);
				if (bitmap != null) {
					// 重新将其缓存至硬引用中
					// putBitmapToLruCache(key, bitmap);
				}
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}
}
