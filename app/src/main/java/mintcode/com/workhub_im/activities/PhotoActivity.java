package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.adapter.PhotoAdapter;
import mintcode.com.workhub_im.util.ImageFolder;

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


/**
 * Created by mark on 16-6-22.
 */
public class PhotoActivity extends Activity implements OnClickListener {

    private static final int SPAN = 3;
    @BindView(R.id.photo_recycler_view)
    RecyclerView recyclerView;

    private ImageView mIvBack;

    private TextView mTvNumber;

    List<String> fList = new ArrayList<String>();

    private static final int SCAN_SUCCESS_CODE = 0x11;


//	private ProgressDialog mProgressDialog;

    private PhotoAdapter adapter;
    /**
     * 存放扫描拿到的所有图片文件夹
     */
    private List<ImageFolder> mFloaderList = new ArrayList<ImageFolder>();

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
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {


        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN);

        adapter = new PhotoAdapter(fList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
        ImageFolder folder = mFloaderList.get(0);
        List<String> allList = folder.getAllPath();


    }


    @Override
    public void onClick(View v) {

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
                ImageFolder imageFloader = new ImageFolder();
                if (mDirPaths.contains(parentFilePath)) {
                    continue;
                } else {
                    if (!filterPath.contains(parentFilePath)) {
                        mDirPaths.add(parentFilePath);
                        imageFloader = new ImageFolder();
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
            ImageFolder folder = new ImageFolder();


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
                ImageFolder f = mFloaderList.get(i);
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
            String[] stringArray = fList.toArray(new String[fList.size()]);
            List<String> sortList = Arrays.asList(stringArray);


            folder.setAllPath(sortList);
            // 防止相册为空，没有照片发生崩溃问题
            if (!fList.isEmpty()) {
                folder.setFirstImagePath(fList.get(0));
            } else {
                folder.setFirstImagePath("");
            }

            folder.setCount(fList.size());
            folder.setName("/所有图片");
            mFloaderList.add(0, folder);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

            // 通知扫描完成
        }


    }


}
