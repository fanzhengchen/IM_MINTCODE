package com.mintcode.launchr.message.activity;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.cache.MD5;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.util.DownLoadFileTask;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;

public class FileDetailActivity extends BaseActivity {
    private ImageView mIvBack;

    private ImageView mIvFileIcon;

    private TextView mIvFileName;

    private Button mBtnDownload;

    private MessageItem item;

    private AttachItem attach;

    public static final int TYPE_DOWNLOAD_SUCCESS = 0x0001;
    public static final int TYPE_DOWNLOAD_FIELD = 0x0002;

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);

        item = getIntent().getParcelableExtra("fileMessage");

        initView();
    }

    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvFileIcon = (ImageView) findViewById(R.id.iv_file_icon);
        mIvFileName = (TextView) findViewById(R.id.tv_file_name);
        mBtnDownload = (Button) findViewById(R.id.btn_download);

        mIvBack.setOnClickListener(this);
        mBtnDownload.setOnClickListener(this);

        attach = JsonUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
        mIvFileName.setText(attach.getFileName());
        mIvFileIcon.setImageResource(IMConst.getFileIcon(attach.getFileName()));

        String str;
        if (attach.getFileUrl() != null) {
            str = attach.getFileUrl();
        } else {
            str = attach.getThumbnail();
        }
        String url = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr" + str;
        String afterName = "." + attach.getFileName().substring(attach.getFileName().lastIndexOf(".") + 1);

        fileName = MD5.getMD5Str(url) + afterName;
        if (isFileExist(fileName)) {
            mBtnDownload.setText(getString(R.string.open_file));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mIvBack) {
            finish();
        } else if (v == mBtnDownload) {
            if (getResources().getString(R.string.download_file).equals(mBtnDownload.getText().toString())) {
                showLoading();
                downLoadFile(item, FileDetailActivity.this, download);
            } else {
                openFile(fileName);
            }
        }
    }

    // 文件下载
    private void downLoadFile(final MessageItem item, final Context context, final Handler handler) {
        DownLoadFileTask task = new DownLoadFileTask(context, item, handler);
        task.execute();
    }

    private Handler download = new Handler() {
        public void handleMessage(android.os.Message msg) {
            dismissLoading();
            if (msg.what == TYPE_DOWNLOAD_SUCCESS) {
                fileName = item.getFileName();
                mBtnDownload.setText(getResources().getString(R.string.open_file));
                openFile(item.getFileName());
            } else if (msg.what == TYPE_DOWNLOAD_FIELD) {
                toast(getResources().getString(R.string.download_field));
            }
        }

        ;
    };

    private boolean isFileExist(String fileName) {
        File file = new File(LauchrConst.DOWNLOAD_FILE_PATH_SDCARD + fileName);
        if (file.exists()) {
            return true;
        } else {
            File path = new File(LauchrConst.DOWNLOAD_FILE_PATH_DATA + fileName);
            if (path.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 打开文件
     */
    private void openFile(String name) {
        File file = new File(LauchrConst.DOWNLOAD_FILE_PATH_SDCARD + name);
        if (!file.exists()) {
            file = new File(LauchrConst.DOWNLOAD_FILE_PATH_DATA + name);
        }

        if (!file.exists()) {
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        try {
            startActivity(intent);
        } catch (Exception e) {
            toast(getString(R.string.activity_not_cunzai));
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (TextUtils.equals(end, "")) return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".txt", "text/plain"},
            {".xml", "text/plain"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
}
