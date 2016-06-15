package com.mintcode.launchr.photo.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.photo.activity.ImageFloder;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;


public class PhotoActivity extends BaseActivity implements OnClickListener {

    private ImageView mIvBack;

    private TextView mTvNumber;

    private static final int SCAN_SUCCESS_CODE = 0x11;


//	private ProgressDialog mProgressDialog;

    /**
     * 存放扫描拿到的所有图片文件夹
     */
    private List<ImageFloder> mFloaderList = new ArrayList<ImageFloder>();

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;

    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 存放所有图片的集合
     */
    private List<String> mShowList;

    /**
     * 所有图片
     */
    private TextView mTvAll;

    /**
     * 预览
     */
    private TextView mTvPreview;

    /**
     * 图片列表
     */
    private GridView mGvList;


    private PhotoHandler mHandler;


    private PhotoAdapter mPhotoAdapter;

    private int mScreenHeight;
    private PopupWindow mPopupWindow;
    /**
     * 最大选择数
     */
    public static int MAXIMUM = 9;
    /**
     * 设置选择照片方式标记
     */
    public static final String SET_SELECT_IMAGE_TYPE = "set_select_image_type";
    /**
     * 选择的照片数组标记
     */
    public static final String SELECT_IMAGE_LIST = "select_image_list";

    /**
     * 选择照片方式枚举
     */
    public enum SELECT_TYPE {
        SINGLE_SELECT,
        MUTIL_SELECT
    }

    /**
     * 选择照片方式
     */
    private int SELECT_IMAGE_TYPE = SELECT_TYPE.MUTIL_SELECT.ordinal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        initView();
    }


    private void initView() {
        mTvAll = (TextView) findViewById(R.id.id_choose_dir);
        mTvPreview = (TextView) findViewById(R.id.tv_preview);
        mGvList = (GridView) findViewById(R.id.gv_photo);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mIvBack.setOnClickListener(this);

        mHandler = new PhotoHandler();


        mTvAll.setOnClickListener(this);
        mTvPreview.setOnClickListener(this);
        SELECT_IMAGE_TYPE = getIntent().getIntExtra(SET_SELECT_IMAGE_TYPE, SELECT_TYPE.MUTIL_SELECT.ordinal());
        getImagesFromExternalStorage();
    }

    /**
     * 实例化数据
     */
    private void initData() {

        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示所有图片
        ImageFloder floder = mFloaderList.get(0);
        List<String> allList = floder.getAllPath();

        if (allList == null) {
            mShowList = Arrays.asList(new File(floder.getDir()).list());
            mPhotoAdapter = new PhotoAdapter(this, mShowList, mImgDir.getAbsolutePath(), false);
        } else {

            mPhotoAdapter = new PhotoAdapter(this, allList, mImgDir.getAbsolutePath(), true);
        }
        mGvList.setAdapter(mPhotoAdapter);


    }


    @Override
    public void onClick(View v) {
        if (v == mTvAll) {
            popupWindow();
        } else if (v == mTvPreview) {
            if (mPhotoAdapter != null && mPhotoAdapter.getSelectImage() != null && mPhotoAdapter.getSelectImage().size() > 0) {
                List<String> list = mPhotoAdapter.getSelectImage();
                Intent intent = new Intent();
                intent.putExtra(SELECT_IMAGE_LIST, (Serializable) list);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                toast(getString(R.string.please_choose_photo));
            }
        } else if (v == mIvBack) {
            finish();
        }

    }

    private void popupWindow() {
        mPopupWindow.showAsDropDown(mTvAll, 0, 0);
        // 设置背景变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 从sd卡扫描图片
     */
    private void getImagesFromExternalStorage() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        ScanThread scanThread = new ScanThread();
        scanThread.start();
    }


    String filterPath = Environment.getExternalStorageDirectory() + "/launchr/send/";

    class ScanThread extends Thread {


        @Override
        public void run() {

            String firstImage = null;

            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver resolver = PhotoActivity.this.getContentResolver();

            // 从ContentResolver查询图片
            Cursor cursor = resolver.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?"
                    , new String[]{"image/jpeg", "image/jpg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

            while (cursor.moveToNext()) {
                // 获取图片路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                if (firstImage == null) {
                    firstImage = path;
                }

                // 获取该图片的父路径
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }

                String parentFilePath = parentFile.getAbsolutePath();
                ImageFloder imageFloader = new ImageFloder();
                if (mDirPaths.contains(parentFilePath)) {
                    continue;
                } else {
                    if (!filterPath.contains(parentFilePath)) {
                        mDirPaths.add(parentFilePath);
                        imageFloader = new ImageFloder();
                        imageFloader.setDir(parentFilePath);
                        imageFloader.setFirstImagePath(path);
                    } else {
                        continue;
                    }

                }

                FilenameFilter filter = new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                            return true;
                        }
                        return false;
                    }
                };

                int picSize = 0;
                if (parentFile.list(filter) != null) {
                    picSize = parentFile.list(filter).length;
                }

                // 拿到存放的图片的文件夹
                imageFloader.setCount(picSize);
                mFloaderList.add(imageFloader);

                //
                if (picSize > mPicsSize) {
                    mPicsSize = picSize;
                    mImgDir = parentFile;
                }

            }

            cursor.close();

            // 扫描完成，释放
            mDirPaths = null;

            // 组装所有图片文件夹
            ImageFloder floder = new ImageFloder();
            List<String> fList = new ArrayList<String>();

            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                        return true;
                    }
                    return false;
                }
            };
            for (int i = 0; i < mFloaderList.size(); i++) {
                ImageFloder f = mFloaderList.get(i);
                File file = new File(f.getDir());

                if (file.list(filter) != null) {
                    List<String> tempList = Arrays.asList(file.list(filter));
                    for (int j = 0; j < tempList.size(); j++) {
                        String str = f.getDir() + "/" + tempList.get(j);
                        fList.add(str);
                    }
                }

            }
//			Arrays.so
            CompratorByLastModified compratorByLastModified = new CompratorByLastModified();
            String[] stringArray = fList.toArray(new String[fList.size()]);
            Arrays.sort(stringArray, compratorByLastModified);
            List<String> sortList = Arrays.asList(stringArray);


            floder.setAllPath(sortList);
            // 防止相册为空，没有照片发生崩溃问题
            if (!fList.isEmpty()) {
                floder.setFirstImagePath(fList.get(0));
            } else {
                floder.setFirstImagePath("");
            }

            floder.setCount(fList.size());
            floder.setName("/所有图片");
            mFloaderList.add(0, floder);
            // 通知扫描完成
            mHandler.sendEmptyMessage(SCAN_SUCCESS_CODE);
        }


    }

    static class CompratorByLastModified implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            File f1 = new File(lhs);
            File f2 = new File(rhs);

            long diff = f2.lastModified() - f1.lastModified();
            if (diff > 0) {
                return 1;
            } else if (diff == 0) {
                return 0;
            } else {
                return -1;
            }
//			return 0;
        }
    }

    private class PhotoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == SCAN_SUCCESS_CODE) {

                // 为view绑定数据
                initData();
                // 初始化文件夹popupWindow
                mPopupWindow = getListPopupWindow();
            }

        }

    }

    private class PhotoAdapter extends BaseAdapter {
        /**
         * 存放选择的图片路径
         */
        public List<String> mSelectImageList = new LinkedList<String>();

        public List<String> mList;


        private String path;

        private Context context;

        private LayoutInflater inflater;
        private boolean isAll = false;

        public PhotoAdapter(Context context, List<String> list, String path, boolean isAll) {
            if (list != null) {
                mList = list;
            } else {
                mList = new ArrayList<String>();
            }
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.path = path;

            this.isAll = isAll;
        }

        public void changeData(List<String> list, String p, boolean isAll) {
            if (list != null) {
                mList = list;
                path = p;
                this.isAll = isAll;
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item, null);
                holder = new ViewHodler();
                holder.ibCheck = (ImageButton) convertView.findViewById(R.id.id_item_select);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.id_item_image);
                convertView.setTag(holder);

            } else {
                holder = (ViewHodler) convertView.getTag();
            }

            final String url;

            if (isAll) {
                url = mList.get(position);
            } else {
                url = path + "/" + mList.get(position);
            }

            File file = new File(url);
            Glide.with(context).load(file).into(holder.ivPhoto);
            final ImageView ivPhoto = holder.ivPhoto;
            final ImageButton ibCheck = holder.ibCheck;
            holder.ivPhoto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 已经选择过该图片
                    if (mSelectImageList.contains(url)) {
                        mSelectImageList.remove(url);
                        ibCheck.setImageResource(R.drawable.icon_unchecked);
                        ivPhoto.setColorFilter(null);
                    } else {
                        if (mSelectImageList.size() < MAXIMUM) {
                            mSelectImageList.add(url);
                            ibCheck.setImageResource(R.drawable.icon_blue_checked);
//                            if (SELECT_IMAGE_TYPE == SELECT_TYPE.SINGLE_SELECT.ordinal()) {
//                                Intent intent = new Intent();
//                                intent.putExtra(SELECT_IMAGE_LIST, (Serializable) mSelectImageList);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
                            ivPhoto.setColorFilter(Color.parseColor("#77000000"));
                        } else {
                            Toast.makeText(getApplicationContext(), "最多只能选" + MAXIMUM + "张", Toast.LENGTH_SHORT).show();
                        }
                    }

                    mTvNumber.setText(mSelectImageList.size() + "/" + MAXIMUM);
                }
            });

            if (mSelectImageList.contains(url)) {
                ibCheck.setImageResource(R.drawable.icon_green_checked);
                ivPhoto.setColorFilter(Color.parseColor("#77000000"));
            } else {
                ibCheck.setImageResource(R.drawable.icon_unchecked);
                ivPhoto.setColorFilter(null);
            }


            return convertView;
        }


        public List<String> getSelectImage() {
            return mSelectImageList;
        }

    }

    class ViewHodler {
        ImageView ivPhoto;
        ImageButton ibCheck;
        TextView tvName;
        TextView tvCount;
    }

    private void selectFloder(ImageFloder floder) {

        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        };


        List<String> allList = floder.getAllPath();
        if (allList == null) {
            mImgDir = new File(floder.getDir());
            mShowList = Arrays.asList(mImgDir.list(filter));
            mPhotoAdapter.changeData(mShowList, mImgDir.getAbsolutePath(), false);
        } else if (mImgDir != null) {
            mPhotoAdapter.changeData(allList, mImgDir.getAbsolutePath(), true);
        }


        mPopupWindow.dismiss();
    }

    private PopupWindow getListPopupWindow() {
        if (mPopupWindow == null) {

            View v = getLayoutInflater().inflate(R.layout.list_dir_pop_view, null);
            ListView lv = (ListView) v.findViewById(R.id.lv_dir);
            PopupWindow popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7), true);
            mPopupWindow = popupWindow;
            ListAdapter adapter = new ListAdapter();
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ImageFloder floder = (ImageFloder) parent.getAdapter().getItem(position);

                    if (floder != null) {
                        selectFloder(floder);
                    }
                }
            });

            popupWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    // 设置背景颜色回复正常
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().setAttributes(lp);
                }
            });
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchInterceptor(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            return popupWindow;
        }
        return mPopupWindow;
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFloaderList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFloaderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_floder, null);
                holder = new ViewHodler();
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_floder);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHodler) convertView.getTag();
            }

            // 设置数据
            ImageFloder floder = (ImageFloder) getItem(position);


            File file = new File(floder.getFirstImagePath());


            Glide.with(PhotoActivity.this).load(file).into(holder.ivPhoto);

            String name = floder.getName().substring(1);
            holder.tvName.setText(name);
            holder.tvCount.setText(floder.getCount() + "");

            return convertView;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MAXIMUM = 9;
    }
}
