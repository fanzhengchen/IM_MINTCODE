package com.mintcode.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mintcode.cache.DiskLruCache.Snapshot;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;

/**
 * @author sid'pc 图片缓存管理类 sid 2015-8-6整理
 */
@SuppressLint("NewApi")
public class CacheManager {
    public static int WIDTH = 216;
    public static int HEIGHT = 320;

    private Context context;
    private static CacheManager sInstance;
    private Set<DownLoadTask> mDownLoadTasks = new HashSet<DownLoadTask>();

    private File mCacheDir;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mMemoryLruCache;
    private LinkedHashMap<String, SoftReference<Bitmap>> mSoftLruCache;

    private CacheManager(Context context) {
        this.context = context;
        mCacheDir = CacheUtil.getAvailableDir(context);
        mCacheDir.mkdirs();
        initDiskCache();
        initMemoryCache();
        initSoftCache();
    }

    public static CacheManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CacheManager(context);
        }
        return sInstance;
    }

    /**
     * 设置缓存图片分辨率
     */
    public static CacheManager getInstance(Context context, int width,
                                           int height) {
        WIDTH = width;
        HEIGHT = height;
        return getInstance(context);
    }

    /**
     * 开辟100M作为硬盘存储空间
     */
    private void initDiskCache() {
        try {
            mDiskLruCache = DiskLruCache.open(mCacheDir,
                    CacheUtil.getAppVersion(context), 1, 100 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以程序允许最大内存的8分之一作为缓存大小
     */
    private void initMemoryCache() {
        int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
        mMemoryLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        Bitmap oldValue, Bitmap newValue) {
                // 缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
                Log.v("CacheManage", "hard cache is full , push to soft cache");
                mSoftLruCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
    }

    /**
     * 设置软引用的容量为40个键值
     */
    private void initSoftCache() {
        final int SOFT_CACHE_CAPACITY = 40;
        mSoftLruCache = new LinkedHashMap<String, SoftReference<Bitmap>>(40,
                0.75f, true) {
            private static final long serialVersionUID = -6871516990426264345L;

            @Override
            protected boolean removeEldestEntry(
                    Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_CAPACITY) {
                    Log.v("CacheManage", "Soft Reference limit , purge one");
                    // 将超出40个键值的部分进行删除
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 将缓存记录推回硬盘，在onPause时调用,防止内存中的数据丢失
     */
    public void fluchCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到对应的key值
     */
    public void saveImage(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            return;
        }
        // 将bitmap转为inputstream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CacheUtil.addFileToDiskCache(is, key, mDiskLruCache);
    }

    /**
     * 将path路径的图片压缩并保存到cache
     */
    public void saveImage(String path) {
        Bitmap bitmap = CacheUtil.decodeSampledBitmapFromPath(path, WIDTH, HEIGHT);
        saveImage(path, bitmap);
    }

    /**
     * 通过路径获取压缩图片
     */
    public Bitmap getBitmapByPath(String path) {
        Bitmap bitmap = getBitmapByKey(path);
        if (bitmap == null) {
            bitmap = CacheUtil.decodeSampledBitmapFromPath(path, WIDTH, HEIGHT);
            saveImage(path, bitmap);
        }
        return bitmap;
    }

    /**
     * 通过key值查找保存本地文件的地址
     */
    public String getFilePath(String key) {
        String path = null;
        String md5Key = MD5.getMD5Str(key);
        try {
            Snapshot snapShot = mDiskLruCache.get(md5Key);
            if (snapShot != null) {
                path = mCacheDir + md5Key;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public Bitmap getFileImage(String key) {
        Bitmap bitmap = null;
        String md5Key = MD5.getMD5Str(key);
        try {
            Snapshot snapShot = mDiskLruCache.get(md5Key);
            if (snapShot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
//				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//				if (mMemoryLruCache.get(md5Key) == null) {
//					mMemoryLruCache.put(md5Key, bitmap);
//				}
                if (fileDescriptor != null) {
                    // 将图片进行压缩后取出，防止OM
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inJustDecodeBounds = true;
                    BitmapFactory.decodeFileDescriptor(fileDescriptor, null, op);
                    op.inSampleSize = CacheUtil.calculateInSampleSize(op, WIDTH, HEIGHT);
                    op.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,
                            null, op);
                    if (mMemoryLruCache.get(md5Key) == null) {
                        mMemoryLruCache.put(md5Key, bitmap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 通过key查找Bitmap，如果要查找原图请用getFilePath(String key)
     */
    public synchronized Bitmap getBitmapByKey(String key, int width, int height) {
        String md5Key = MD5.getMD5Str(key);
        Bitmap bitmap = null;
        // 先从内存用中读
        if (md5Key != null && mDiskLruCache != null) {
            bitmap = mMemoryLruCache.get(md5Key);
        } else {
            return null;
        }
        Log.i("CacheManage", "" + md5Key);
        if (bitmap != null) {
            Log.i("CacheManage", "get bitmap from mMemoryLruCache");
            return bitmap;
        }
        // 内存中没有找到，从软引用中读
        SoftReference<Bitmap> reference = mSoftLruCache.get(md5Key);
        if (reference != null) {
            bitmap = reference.get();
            if (bitmap != null) {
                Log.i("CacheManage", "get bitmap from mSoftBitmapCache");
                return bitmap;
            } else {
                Log.v("CacheManage", "soft reference removed");
                mSoftLruCache.remove(md5Key);
            }
        }
        // 软引用依旧没有找到，从硬盘中读
        try {
            Log.d("getBitmapByKey", "软引用依旧没有找到，从硬盘中读");
            Snapshot snapShot = mDiskLruCache.get(md5Key);
            if (snapShot != null) {
                Log.d("getBitmapByKey", "硬盘中找到            " + md5Key);
                FileInputStream fileInputStream = (FileInputStream) snapShot
                        .getInputStream(0);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                if (fileDescriptor != null) {
                    // 将图片进行压缩后取出，防止OM
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inJustDecodeBounds = true;
                    BitmapFactory
                            .decodeFileDescriptor(fileDescriptor, null, op);
                    op.inSampleSize = CacheUtil.calculateInSampleSize(op,
                            width, height);
                    op.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,
                            null, op);

                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.im_default_image);
                    }

                    if (mMemoryLruCache.get(md5Key) == null) {
                        if (bitmap != null) {
                            mMemoryLruCache.put(md5Key, bitmap);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getBitmapByKey(String key) {
        return getBitmapByKey(key, WIDTH, HEIGHT);
    }

    public void loadBitmap(String url) {
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();
        final String md5Key = MD5.getMD5Str(url);
        Bitmap bitmap = mMemoryLruCache.get(url);
        if (bitmap == null) {
            DownLoadTask task = new DownLoadTask(mDiskLruCache, mUserName) {
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    mMemoryLruCache.put(md5Key, result);
                }
            };
            mDownLoadTasks.add(task);
            task.execute(url);
        }
    }

    /**
     * 加载头像
     *
     * @param url
     * @param imageView
     */
    public void loadHeadBitmap(final String url, String headUrl, final ImageView imageView) {
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();
        final String md5Key = MD5.getMD5Str(url);
        DownLoadTask task = new DownLoadTask(mDiskLruCache, mUserName) {
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (result != null && !TextUtils.isEmpty(md5Key)) {
                    Log.i("bitmap", "从服务器上下载到照片");
                    loadBitmap(url, imageView);
                } else {
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_launchr));
                }
            }
        };
        mDownLoadTasks.add(task);
        task.execute(headUrl);
    }

    public void loadBitmap(String url, final ImageView imageView) {
        Bitmap bitmap = getBitmapByKey(url);
        final String md5Key = MD5.getMD5Str(url);
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();
        if (bitmap == null) {
            DownLoadTask task = new DownLoadTask(mDiskLruCache, mUserName) {
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        Log.i("bitmap", "载入头像 从服务器上下载到照片");
                        imageView.setImageBitmap(result);
                        mMemoryLruCache.put(md5Key, result);
                    } else {
                        //TODO 下载失败
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_launchr));
                    }
                }
            };
            mDownLoadTasks.add(task);
            task.execute(url);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void loadBitmapFromDisk(String md5Key, final ImageView imageView) {
        Bitmap bitmap = null;
        try {
            Snapshot snapShot = mDiskLruCache.get(md5Key);
            if (snapShot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapShot
                        .getInputStream(0);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                if (fileDescriptor != null) {
                    // 将图片进行压缩后取出，防止OM
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inJustDecodeBounds = true;
                    BitmapFactory
                            .decodeFileDescriptor(fileDescriptor, null, op);
                    op.inSampleSize = CacheUtil.calculateInSampleSize(op, WIDTH, HEIGHT);
                    op.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,
                            null, op);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_launchr));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadBitmap(String url, final ImageView imageView, int width,
                           int height) {
        Bitmap bitmap = getBitmapByKey(url, width, height);
        final String md5Key = MD5.getMD5Str(url);
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();
        if (bitmap == null) {
            DownLoadTask task = new DownLoadTask(mDiskLruCache, mUserName) {
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        imageView.setImageBitmap(result);
                        mMemoryLruCache.put(md5Key, result);
                    } else {
                        //TODO 下载失败
//						Toast.makeText(context,context.getString(R.string.photo_download_fail_taost), Toast.LENGTH_SHORT).show();
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_launchr));
                    }
                }
            };
            mDownLoadTasks.add(task);
            task.execute(url);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void loadBitmap(final String url, final ImageView imageView, final ProgressBar progressBar, int width,
                           int height) {
        Bitmap bitmap = getBitmapByKey(url, width, height);
        final String md5Key = MD5.getMD5Str(url);
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();
        if (bitmap == null) {
            DownLoadTask task = new DownLoadTask(mDiskLruCache, progressBar, mUserName) {
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null && !TextUtils.isEmpty(md5Key)) {
                        if (imageView.getTag().equals(url)) {
                            imageView.setImageBitmap(result);
                        }
                        Log.v("down", "done2");
                        progressBar.setVisibility(View.GONE);
                        mMemoryLruCache.put(md5Key, result);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        //TODO 下载失败
//						Toast.makeText(context,context.getString(R.string.photo_download_fail_taost), Toast.LENGTH_SHORT).show();
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_launchr));
                    }
                }
            };
            progressBar.setVisibility(View.VISIBLE);
            mDownLoadTasks.add(task);
            task.execute(url);
        } else {
            if (imageView.getTag().equals(url)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap getAblum(Bitmap bitmap) {
        int width = (int) (context.getResources().getDisplayMetrics().density * 100);
        int height = bitmap.getHeight() * width / bitmap.getWidth();
        try {
            return Bitmap.createScaledBitmap(bitmap, width, height, false);
        } catch (Exception e) {
            return bitmap;
        }
    }

    public Bitmap putStreamToExtStorage(String url) {
        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
        String mUserName = mHeaderParam.getLoginName();

        return CacheUtil.download(mUserName, url, mDiskLruCache);
    }

    public void ClearDiskLruCache() {
        try {
            DiskLruCache.deleteContents(CacheUtil.getAvailableDir(context));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 加载本地图片
     */
    public Bitmap getBitmapByRealPath(String path) {
        Bitmap bitmap = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            if (fileInputStream == null) {
                return null;
            }
            FileDescriptor fileDescriptor = fileInputStream.getFD();

            if (fileDescriptor == null) {
                return null;
            }
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, op);
            op.inSampleSize = CacheUtil.calculateInSampleSize(op, 3 * WIDTH, 3 * HEIGHT);
            op.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, op);

            fileInputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;
    }
}
