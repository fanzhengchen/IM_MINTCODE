package mintcode.com.workhub_im.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.mintcode.imkit.util.TTDensityUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;

/**
 * Created by mark on 16-6-15.
 */
public class HeadImageUtil {
    private static HeadImageUtil mInstance = null;

    /**
     * 所有的数据都是通过{@link SharedPreferences} 来存在内部存储中的。
     */
    private SharedPreferences mSharedPreferences;

    /**
     * 用来改变{@link SharedPreferences} 中所存的值。
     */
    private SharedPreferences.Editor mEditor;

    private Context mContext = null;

    private HeadImageUtil(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(
                "headview", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    public static HeadImageUtil getInstance() {
        if (mInstance == null) {
            mInstance = new HeadImageUtil(App.getGlobalContext());
        }
        return mInstance;
    }


    /**
     * 保存key
     *
     * @param key
     * @param value
     */
    public void saveValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 传入KEY，得到对应的数据。
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        String retValue = mSharedPreferences.getString(key, "");
        return retValue;
    }

    public void removeValue(String key) {
        mEditor.putString(key, null);
        mEditor.commit();
    }

    /**
     * 传入KEY，得到对应的数据。
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        boolean retValue = mSharedPreferences.getBoolean(key, false);
        return retValue;
    }

    /**
     * 存储boolean value
     *
     * @param key
     * @param value
     */
    public void saveBooleanValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * 储存int value
     *
     * @param key
     * @param value
     */
    public void saveIntValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * 储存long value
     *
     * @param key
     * @param value
     */
    public void saveLongValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 获取long value
     *
     * @return
     */
    public long getLogVaule(String key) {
        long retValue = mSharedPreferences.getLong(key, -1);
        return retValue;
    }

    /**
     * 获取int value
     *
     * @return
     */
    public int getIntVaule(String key) {
        int retValue = mSharedPreferences.getInt(key, -1);
        return retValue;
    }


    /**
     * 置空shareperfence
     */
    public void deleteFile() {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * 保存当前用户头像的时间戳
     */
    public void savaAvatarLongTime(String strUser, long value) {
        String companyCode = UserPrefer.getCompanyCode();
        String key = companyCode + "_" + strUser;
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 获取当时保存用户头像的时间戳
     */
    public long getAvatarLongTime(String strUser) {
        String companyCode = UserPrefer.getCompanyCode();
        String key = companyCode + "_" + strUser;
        long retValue = mSharedPreferences.getLong(key, -1);
        return retValue;
    }

    /**
     * 重新获取最新的用户头像
     */
    public void restartSetAvatarResource(ImageView iv, String strUser, String avatarUrl, int radis, int width, int height) {
        long currentTime = System.currentTimeMillis();
        savaAvatarLongTime(strUser, currentTime);
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + avatarUrl)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&t=").append(currentTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).placeholder(R.drawable.icon_default).into(iv);
        } else {
            man.load(builder.toString()).into(iv);
        }
    }

    /**
     * 重新获取最新的用户头像
     */
    public void restartSetUserAvatarResource(ImageView iv, String strUser, String avatarUrl, int radis, int width, int height) {
        long currentTime = System.currentTimeMillis();
        savaAvatarLongTime(strUser, currentTime);
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + avatarUrl)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&t=").append(currentTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).into(iv);
        } else {
            man.load(builder.toString()).into(iv);
        }
    }

    /**
     * 根据字段URL拼接宽高获取用户头像
     */
    public void setAvatarResource(ImageView iv, String strUser, String avatarUrl, int radis, int width, int height) {
        long avatarTime = getAvatarLongTime(strUser);
        if (avatarTime < 0) {
            avatarTime = System.currentTimeMillis();
            savaAvatarLongTime(strUser, avatarTime);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + avatarUrl)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&t=").append(avatarTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).placeholder(R.drawable.icon_default).into(iv);
        } else {
            man.load(builder.toString()).placeholder(R.drawable.icon_default).into(iv);
        }
    }

    /**
     * 根据用户ID获取好友或同事用户头像
     */
    public void setAvatarResourceWithUserId(ImageView iv, String strUser, int radis, int width, int height) {
//        String friendAvtarUrl = getValue(strUser);
//        if (!TextUtils.isEmpty(friendAvtarUrl)) {
//            setAvatarResource(iv, strUser, friendAvtarUrl, radis, width, height);
//            return;
//        }
//        friendAvtarUrl = FriendDBService.getInstance().getFriendAvtarUrl(strUser);
//        if (TextUtils.isEmpty(friendAvtarUrl)) {
            setAvatarResourceAppendUrl(iv, strUser, radis, width, height);
//        } else {
//            setAvatarResource(iv, strUser, friendAvtarUrl, radis, width, height);
//        }
    }

    /**
     * 根据用户ID重新获取好友或同事用户头像
     */
//    public void restartSetAvatarResourceWithUserId(ImageView iv, String strUser, int radis, int width, int height) {
//        String friendAvtarUrl = FriendDBService.getInstance().getFriendAvtarUrl(strUser);
//        if (TextUtils.isEmpty(friendAvtarUrl)) {
//            restartSetUserAvatarAppendUrl(iv, strUser, radis, width, height);
//        } else {
//            restartSetUserAvatarResource(iv, strUser, friendAvtarUrl, radis, width, height);
//        }
//    }

    /**
     * 拼接URL获取头像
     */
    public void setAvatarResourceAppendUrl(ImageView iv, String strUser, int radis, int width, int height) {
        long avatarTime = getAvatarLongTime(strUser);
        String companyCode = UserPrefer.getCompanyCode();
        if (avatarTime < 0) {
            avatarTime = System.currentTimeMillis();
            savaAvatarLongTime(strUser, avatarTime);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + "/Base-Module/Annex/Avatar?")
                .append("companyCode=").append(companyCode)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&userName=").append(strUser)
                .append("&t=").append(avatarTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).placeholder(R.drawable.icon_default).into(iv);
        } else {
            man.load(builder.toString()).placeholder(R.drawable.icon_default).into(iv);
        }
    }

    /**
     * 重新拼接URL获取头像
     */
    public void restartSetAvatarResourceAppendUrl(ImageView iv, String strUser, int radis, int width, int height) {
        String companyCode = UserPrefer.getCompanyCode();
        long avatarTime = System.currentTimeMillis();
        savaAvatarLongTime(strUser, avatarTime);
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + "/Base-Module/Annex/Avatar?")
                .append("companyCode=").append(companyCode)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&userName=").append(strUser)
                .append("&t=").append(avatarTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).placeholder(R.drawable.icon_default).into(iv);
        } else {
            man.load(builder.toString()).placeholder(R.drawable.icon_default).into(iv);
        }
    }

    /**
     * 重新拼接URL获取用户大头像
     */
    public void restartSetUserAvatarAppendUrl(ImageView iv, String strUser, int radis, int width, int height) {
        String companyCode = UserPrefer.getCompanyCode();
        long avatarTime = System.currentTimeMillis();
        savaAvatarLongTime(strUser, avatarTime);
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + "/Base-Module/Annex/Avatar?")
                .append("companyCode=").append(companyCode)
                .append("&width=").append(width)
                .append("&height=").append(height)
                .append("&userName=").append(strUser)
                .append("&t=").append(avatarTime);
        RequestManager man = Glide.with(mContext);
        if (radis > 0) {
            GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
            man.load(builder.toString()).transform(g).into(iv);
        } else {
            man.load(builder.toString()).into(iv);
        }
    }

    /**
     * 根据url加载图片
     */
    public void setImageResuces(ImageView iv, String url) {
        RequestManager man = Glide.with(mContext);
        man.load(url).placeholder(R.drawable.im_default_image).into(iv);
    }

    /**
     * 设置聊天图片
     *
     * @param iv
     * @param url
     */
    public void setChatImage(ImageView iv, String url, int placeholder, int width, int height) {
        final String username = UserPrefer.getImUsername();
        GlideUrl glideUrl = new GlideUrl(url) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap map = new HashMap();
                map.put("Cookie", "AppName=launchr;UserName=" + username);
                return map;
            }
        };
        int maxWidth = TTDensityUtil.dip2px(mContext, 140);
        int maxHeight = TTDensityUtil.dip2px(mContext, 260);
        if (width <= 0) {
            width = 1;
        }
        int h = maxWidth * height / width;

        if (h > maxHeight) {
            h = maxHeight;
        }
        ViewGroup.LayoutParams p = iv.getLayoutParams();
        p.height = h;
        p.width = maxWidth;
        iv.setLayoutParams(p);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mContext).load(glideUrl).into(iv);
    }

    /**
     * 设置聊天图片
     *
     * @param iv
     * @param url
     */
    public void setChatImageFile(ImageView iv, String url, int width, int height) {
        int maxWidth = TTDensityUtil.dip2px(mContext, 140);
        int maxHeight = TTDensityUtil.dip2px(mContext, 260);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//        GlideUrl glideUrl = new GlideUrl(url) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap map = new HashMap();
//                map.put("Cookie", "AppName=launchr;UserName=" + UserPrefer.getImUsername());
//                return map;
//            }
//        };
//        if (width <= 0) {
//            width = 1;
//        }
        int h = maxWidth * height / width;

        if (h > maxHeight) {
            h = maxHeight;
        }
        ViewGroup.LayoutParams p = iv.getLayoutParams();
        p.height = h;
        p.width = maxWidth;
        iv.setLayoutParams(p);
        Glide.with(mContext).load(url).into(iv);
    }

    /**
     * 加载应用图标
     */
    public void setChatAppIcon(ImageView iv, String appFileName, int radis, int width, int height) {
        StringBuilder builder = new StringBuilder();
        builder.append(AppConsts.ATTACH_PATH + "/Base-Module/Annex/AppIcon?annexType=APPIcon&width=")
                .append(width).append("&height=")
                .append(height).append("&fileName=")
                .append(appFileName);
        // 处理圆形图片
        RequestManager man = Glide.with(mContext);
        GlideRoundTransform g = new GlideRoundTransform(mContext, radis);
        man.load(builder.toString()).transform(g).into(iv);
    }


    public static void setImageResuces(Context context, ImageView iv, File file) {
        // 处理圆形图片
        RequestManager man = Glide.with(context);
        man.load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.im_default_image).into(iv);
    }

//    public static void openLargeImage(final Context context, final ImageView iv, String url, final String path) {
//        RequestQueue mQueue = Volley.newRequestQueue(context);
//        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
//
//            @Override
//            public void onResponse(Bitmap response) {
//                boolean success = BitmapUtil.saveBitmap(response, context, path, 100);
//                if (success) {
//                    File file = new File(path);
//                    setImageResuces(context, iv, file);
//                } else {
//                    BaseActivity a = (BaseActivity) context;
//                    a.toast("error save bitmap");
//                }
//            }
//        };
//
//        Response.ErrorListener error = new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        };
//        String random = "&t=" + System.currentTimeMillis();
//        ImageRequest request = new ImageRequest(url + random, listener, 0, 0, Bitmap.Config.RGB_565, error);
//
//        mQueue.add(request);
//        mQueue.start();
//    }

}
