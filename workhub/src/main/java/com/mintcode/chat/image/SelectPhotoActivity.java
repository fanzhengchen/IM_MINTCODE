package com.mintcode.chat.image;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.RM;
import com.mintcode.chat.image.ImageGridAdapter.TextCallback;
import com.mintcode.launchr.R;

public class SelectPhotoActivity extends Activity implements
        OnClickListener {

    public static final String EXTRA_IMAGE_LIST = "imagelist";
    private List<ImageItem> dataList;
    private GridView gridView;
    private ImageGridAdapter adapter;
    private AlbumHelper helper;
    private TextView mTvComplete;
    private ImageView mTvBack;
    private TextView mIvCancel;
    private TextView mTvSkim;
    private Context context;
    public static final int MSG_IMG_TOTAL = 0X001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Toast.makeText(SelectPhotoActivity.this, getString(R.string.more_9), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MSG_IMG_TOTAL:
                    break;
                default:
                    mTvSkim.setTextColor(getResources().getColor(R.color.white));
                    mTvSkim.setEnabled(true);
                    break;
            }
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SelectPhotoActivity.this;
        setContentView(R.layout.activity_image_grid);

        Bimp.address.clear();

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initView();
        mTvComplete = (TextView) findViewById(R.id.tv_complete);
        mIvCancel = (TextView) findViewById(R.id.cancel);
        mTvBack = (ImageView) findViewById(R.id.iv_back);
        mTvSkim = (TextView) findViewById(R.id.tv_skim);
        mTvSkim.setOnClickListener(this);
        mIvCancel.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mTvComplete.setOnClickListener(this);
        mTvSkim.setTextColor(getResources().getColor(R.color.gray));
        mTvSkim.setEnabled(false);
        mTvComplete.setTextColor(getResources().getColor(R.color.gray));
        mTvComplete.setEnabled(false);
    }

    /**
     * 设置gridView
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(this, dataList, mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new TextCallback() {
            public void onListen(int count) {

                if (count > 0) {
                    mTvComplete.setText(getString(R.string.send) + "(" + count
                            + ")");
                    mTvComplete.setTextColor(getResources().getColor(
                            R.color.white));
                    mTvComplete.setEnabled(true);
                } else {
                    mTvComplete.setText(R.string.send);
                    mTvComplete.setTextColor(getResources().getColor(
                            R.color.gray));
                    mTvComplete.setEnabled(false);
                }
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.notifyDataSetChanged();
            }

        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_complete) {
            setResult(RESULT_OK);
            onBackPressed();
        } else if (id == R.id.cancel) {
            ImageGridAdapter.mSelectedImage.clear();
            onBackPressed();
        } else if (id == R.id.iv_back) {
            onBackPressed();
        } else if (id == R.id.tv_skim) {
            loadingAndSkim();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // BitmapHander.CanAdd = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Method : loading
     * @Description : 将选中图片加载到Bimp.bmp，并预览
     * @data 2014-12-2上午10:01:41
     * @auth : sid'pc
     */
    public void loadingAndSkim() {
        // new Thread(new Runnable() {
        // public void run() {
        // for (int i = 0; i < Bimp.address.size(); i++) {
        // try {
        // String path = Bimp.address.get(i);
        // Bitmap bm = Bimp.revitionImageSize(path);
        // Bimp.bmp.add(bm);
        // } catch (IOException e) {
        //
        // e.printStackTrace();
        // }
        // }
        // Intent intent = new Intent(ReadPhotoActivity.this,
        // PhotoActivity.class);
        // startActivity(intent);
        // }
        // }).start();
    }
}
