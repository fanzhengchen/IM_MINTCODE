package mintcode.com.workhub_im.widget.cache;

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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author sid'pc 图片缓存管理类 sid 2015-8-6整理
 */
@SuppressLint("NewApi")
public class CacheManager {
    public static int WIDTH = 216;
    public static int HEIGHT = 320;

    private Context context;
    private static CacheManager sInstance;

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
     * 通过key值查找保存本地文件的地址
     */
    public String getFilePath(String key) {
        String path = null;
        String md5Key = MD5.getMD5Str(key);
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(md5Key);
            if (snapShot != null) {
                path = mCacheDir + md5Key;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }




    public void loadBitmap(String url) {
//        HeaderParam mHeaderParam = LauchrConst.getHeader(context);
//        String mUserName = mHeaderParam.getLoginName();
//        final String md5Key = MD5.getMD5Str(url);
//        Bitmap bitmap = mMemoryLruCache.get(url);
//        if (bitmap == null) {
//            DownLoadTask task = new DownLoadTask(mDiskLruCache, mUserName) {
//                @Override
//                protected void onPostExecute(Bitmap result) {
//                    super.onPostExecute(result);
//                    mMemoryLruCache.put(md5Key, result);
//                }
//            };
//            mDownLoadTasks.add(task);
//            task.execute(url);
//        }
    }



    public void ClearDiskLruCache() {
        try {
            DiskLruCache.deleteContents(CacheUtil.getAvailableDir(context));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
