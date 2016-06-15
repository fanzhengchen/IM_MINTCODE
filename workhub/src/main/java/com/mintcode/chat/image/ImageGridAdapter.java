package com.mintcode.chat.image;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.RM;
import com.mintcode.chat.image.BitmapCache.ImageCallback;
import com.mintcode.launchr.R;

public class ImageGridAdapter extends BaseAdapter {

    private TextCallback textcallback = null;
    final String TAG = getClass().getSimpleName();
    Activity act;
    List<ImageItem> dataList;
    Map<String, String> map = new HashMap<String, String>();
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    BitmapCache cache;
    private Handler mHandler;
    private int selectTotal = 0;
    ImageCallback callback = new ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public static interface TextCallback {
        public void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
        this.act = act;
        dataList = list;
        cache = new BitmapCache();
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();

            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.isselected);
            holder.text = (TextView) convertView
                    .findViewById(R.id.item_image_grid_text);
            // 设置没有选中状态
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem item = dataList.get(position);

        holder.selected.setImageResource(R.drawable.picture_unselected);

        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
                callback);
        Log.i("infos", "=========" + item.thumbnailPath + ",======"
                + item.imagePath);

        holder.selected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 已选择该图片
                if (mSelectedImage.contains(item.imagePath)) {
                    mSelectedImage.remove(item.imagePath);
                    holder.selected

                            .setImageResource(R.drawable.picture_unselected);
                    holder.iv.setColorFilter(null);

                } else {// 未选择该图
                    if (selectTotal < 3) {
                        mSelectedImage.add(item.imagePath);
                        holder.selected.setImageResource(R.drawable.pictures_selected);
                        holder.iv.setColorFilter(Color.parseColor("#77000000"));
                    }
                }
                selectTotal = mSelectedImage.size();
                if ((selectTotal) <= 3) {
                    item.isSelected = !item.isSelected;
                    if (item.isSelected) {
                        // holder.selected
                        // .setImageResource(R.drawable.bg_selected);
                        // holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);

                    } else if (!item.isSelected) {
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                    }
                } else if (selectTotal > 3) {
                    if (item.isSelected == true) {
                        item.isSelected = !item.isSelected;
                        holder.selected
                                .setImageResource(R.drawable.picture_unselected);

                    } else {
                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    }
                }
                if (selectTotal == 0) {
                    Message message = Message.obtain(mHandler, 1);
                    message.sendToTarget();
                } else {
                    Message message = Message.obtain(mHandler, 2);
                    message.sendToTarget();
                }

                // Message message = Message.obtain(mHandler,
                // SelectPhotoActivity.MSG_IMG_TOTAL);
                // message.sendToTarget();

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(item.imagePath)) {
            holder.selected.setImageResource(R.drawable.pictures_selected);
            holder.iv.setColorFilter(Color.parseColor("#77000000"));
        }

        holder.iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = dataList.get(position).imagePath;

            }

        });

        return convertView;
    }
}
