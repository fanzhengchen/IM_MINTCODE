package com.mintcode.launchr.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.app.CommentsView;
import com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity;
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;
import com.mintcode.launchr.app.newApproval.activity.CreateNewApplyActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleEventDetailActivity;
import com.mintcode.launchr.app.newTask.activity.NewTaskActivity;
import com.mintcode.launchr.app.newTask.activity.TaskDetailActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.more.MoreFragment;
import com.mintcode.launchr.photo.activity.PhotoActivity;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JulyYu on 2016/5/6.
 */
public class SelectPicTypeWindowView extends PopupWindow {

    private Context mContext;
    private View mContentView;
    @Bind(R.id.tv_popwindow_add_meeting)
    TextView mTvCamerSelect;
    @Bind(R.id.tv_popwindow_add_event)
    TextView mTvAblumSelect;

    public static String mStrPicPath = "";

    public SelectPicTypeWindowView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.popwindow_add_evnet, null);
        ButterKnife.bind(this, mContentView);
        setWindow();
        initView();
    }

    public void initView() {

        mTvCamerSelect.setText(R.string.photo_camer);
        mTvAblumSelect.setText(R.string.photo_ablum);
    }


    private void setWindow() {
        // 设置PopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置PopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.popupAnimation);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    //销毁popwindow
    @OnClick({R.id.iv_popwindow_tran, R.id.iv_popwindow_null})
    void closeWindow() {
        this.dismiss();
    }

    // 拍照
    @OnClick(R.id.tv_popwindow_add_meeting)
    void openCamer() {

        if (mContext.getExternalCacheDir() == null) {
            Toast.makeText(mContext, "外部存储不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (openCameraIntent.resolveActivity(mContext.getPackageManager()) == null) {
            return;
        }
        mStrPicPath = Environment.getExternalStorageDirectory() + "/Pictures/Launchr/IMG_" + System.currentTimeMillis() + ".jpg";
        try {
            File mPhotoFile = new File(mStrPicPath);
            if (!mPhotoFile.getParentFile().exists()) {
                mPhotoFile.getParentFile().mkdirs();
            }
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            if (mContext instanceof CreateNewApplyActivity) {
                ((CreateNewApplyActivity) mContext).startActivityForResult(openCameraIntent, CreateNewApplyActivity.TAKE_PICTURE);
            } else if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).startActivityForResult(openCameraIntent, MoreFragment.TAKE_PICTURE);
            } else if (mContext instanceof NewTaskActivity) {
                ((NewTaskActivity) mContext).startActivityForResult(openCameraIntent, NewTaskActivity.TAKE_PICTURE);
            } else {
                ((BaseActivity) mContext).startActivityForResult(openCameraIntent, CommentsView.TAKE_PICTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dismiss();
    }

    // 相册
    @OnClick(R.id.tv_popwindow_add_event)
    void openAblum() {
        Intent GetAlbum = new Intent(mContext, PhotoActivity.class);
        if (mContext instanceof CreateNewApplyActivity) {
            ((CreateNewApplyActivity) mContext).startActivityForResult(GetAlbum, CreateNewApplyActivity.GET_ALBUM);
        } else if (mContext instanceof MainActivity) {
            PhotoActivity.MAXIMUM = 1;
            GetAlbum.putExtra(PhotoActivity.SET_SELECT_IMAGE_TYPE, PhotoActivity.SELECT_TYPE.SINGLE_SELECT.ordinal());
            ((MainActivity) mContext).startActivityForResult(GetAlbum, MoreFragment.GET_ALBUM);
        } else if (mContext instanceof NewTaskActivity) {
            ((NewTaskActivity) mContext).startActivityForResult(GetAlbum, NewTaskActivity.GET_ALBUM);
        } else {
            PhotoActivity.MAXIMUM = 1;
            GetAlbum.putExtra(PhotoActivity.SET_SELECT_IMAGE_TYPE, PhotoActivity.SELECT_TYPE.SINGLE_SELECT.ordinal());
            ((BaseActivity) mContext).startActivityForResult(GetAlbum, MoreFragment.GET_ALBUM);
        }
        dismiss();
    }

}
