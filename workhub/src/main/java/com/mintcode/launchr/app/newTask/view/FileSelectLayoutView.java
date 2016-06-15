package com.mintcode.launchr.app.newTask.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.view.NoScrollTouchGridView;
import com.mintcode.launchr.app.commonAdapter.SudokuGridAdapter;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.app.newTask.activity.NewTaskActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.upload_download.UploadFileTask;
import com.mintcode.launchr.upload_download.UploadPartAsyncTask;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.view.DialogLoading;
import com.mintcode.launchr.view.WorkHubDialogLoading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/5/6.
 */
public class FileSelectLayoutView extends LinearLayout implements View.OnClickListener, NoScrollTouchGridView.OnTouchBlankPositionListener,AdapterView.OnItemClickListener{

    protected Context mContext;
    /** 标题名*/
    private TextView mTvTitle;
    /** 照片显示视图*/
    private NoScrollTouchGridView mGdImage;
    /**  照片适配器 */
    private SudokuGridAdapter mGridAdapter;
    /** 箭头icon*/
    private ImageView mIvDirect;
    /** 文字*/
    private String mStrTitle;
    /** 加载loading*/
    private DialogLoading mLoading;
    /** workhub加载loading*/
    private WorkHubDialogLoading mWorkHubLoading;
    /** 照片地址*/
    private List<String> mSelectPicUrl = new ArrayList<>();
    /** 上传类型*/
    private int IMAGE_UPDATE_TYPE;

    public FileSelectLayoutView(Context context) {
        super(context);
        mContext = context;
        initView();
    }



    public FileSelectLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public FileSelectLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        this.setOnClickListener(this);
        int LeftRightpadding = TTDensityUtil.dip2px(mContext,15);
        int TopBottomRightpadding = TTDensityUtil.dip2px(mContext,10);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(linearLayout);
        this.setPadding(LeftRightpadding, TopBottomRightpadding, LeftRightpadding, TopBottomRightpadding);

        if(LauchrConst.IS_WORKHUB && mWorkHubLoading==null){
            mWorkHubLoading = WorkHubDialogLoading.creatDialog(mContext);
        }else if (mLoading == null) {
            mLoading = DialogLoading.creatDialog(mContext);
        }

        int with = TTDensityUtil.dip2px(mContext,70);
        LinearLayout.LayoutParams titleLayout = new LinearLayout.LayoutParams(with, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvTitle = new TextView(mContext);
        mTvTitle.setTextSize(15);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvTitle.setText(mStrTitle);
        mTvTitle.setLayoutParams(titleLayout);
        mTvTitle.setText(mContext.getText(R.string.new_task_attach));
        this.addView(mTvTitle);

        LinearLayout.LayoutParams gridViewLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        mGdImage = new NoScrollTouchGridView(mContext);
        mGdImage.setPadding(15, 15, 15, 15);
        mGdImage.setVerticalSpacing(25);
        mGdImage.setNumColumns(3);
        mGdImage.setLayoutParams(gridViewLayout);
        mGridAdapter = new SudokuGridAdapter(mContext);
        mGdImage.setAdapter(mGridAdapter);
        mGdImage.setOnItemClickListener(this);
        mGdImage.setOnTouchBlankPositionListener(this);
        this.addView(mGdImage);

        LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mIvDirect = new ImageView(mContext);
        mIvDirect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_right_on));
        mIvDirect.setLayoutParams(imageLayout);
        this.addView(mIvDirect);
    }


    public void setImageStrList(List<String> list){
        if(list == null){
            mSelectPicUrl = new ArrayList<>();
        }else{
            mSelectPicUrl = list;
        }
        mGridAdapter.setStrPicList(mSelectPicUrl);
        mGridAdapter.notifyDataSetChanged();
    }

    public void addImageStr(String string){
        if(mSelectPicUrl.size() < 9){
            mSelectPicUrl.add(string);
            mGridAdapter.setStrPicList(mSelectPicUrl);
        }
    }
    public void updateAdapter(){
        mGridAdapter.notifyDataSetChanged();
    }


    public void uploadImage(){

                if(mContext instanceof NewTaskActivity){
                    IMAGE_UPDATE_TYPE = UploadPartAsyncTask.TYPE_TASK;
                }

                //如果附件不为空，且无必填未填 上传附件
                final int picSum = mSelectPicUrl.size();
                if(picSum > 0 ){
                    final List<String> picId = new ArrayList<String>();
                    for(int i = 0; i < mSelectPicUrl.size();i++){
                        String imagePath = mSelectPicUrl.get(i);
                        showLoading();
                        UploadPartAsyncTask uploadPartAsyncTask = new UploadPartAsyncTask(mContext,imagePath, new UploadFileTask.UploadOverLister() {
                            @Override
                            public void uploadOver(String showid,String path) {

                                if(!path.equals("")){
                                    picId.add(showid);
                                }
                                if(picId.size() == picSum){
                                    if(mContext instanceof NewTaskActivity){
                                        NewTaskActivity activity = ((NewTaskActivity)mContext);
                                        if(activity != null ){
                                            activity.saveTask(picId);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void uploadError(String reason) {
                                dismissLoading();
                            }
                        });
                        uploadPartAsyncTask.setType(IMAGE_UPDATE_TYPE);
                        uploadPartAsyncTask.execute();
                    }
                }else{//无附件则无附件内容
                    if(mContext instanceof NewTaskActivity){
                        NewTaskActivity activity = ((NewTaskActivity)mContext);
                        if(activity != null ){
                            activity.saveTask(null);
                        }
                    }
         }
    }

    public void uploadEditImage(){

        //如果附件不为空，且无必填未填 上传附件
        final int picSum = mSelectPicUrl.size();
        if(picSum > 0 ){
            final List<String> picId = new ArrayList<String>();
            for(int i = 0; i < mSelectPicUrl.size();i++){
                String imagePath = mSelectPicUrl.get(i);
                showLoading();
                UploadPartAsyncTask uploadPartAsyncTask = new UploadPartAsyncTask(mContext,imagePath, new UploadFileTask.UploadOverLister() {
                    @Override
                    public void uploadOver(String showid,String path) {

                        if(!path.equals("")){
                            picId.add(showid);
                        }
                        if(picId.size() == picSum){
                            if(mContext instanceof NewTaskActivity){
                                NewTaskActivity activity = ((NewTaskActivity)mContext);
                                if(activity != null ){
                                    activity.saveEditor(picId);
                                }
                            }
                        }
                    }
                    @Override
                    public void uploadError(String reason) {
                        dismissLoading();
                    }
                });
                uploadPartAsyncTask.setType(UploadPartAsyncTask.TYPE_APPROVE);
                uploadPartAsyncTask.execute();
            }
        }else{//无附件则无附件内容
            if(mContext instanceof NewTaskActivity){
                NewTaskActivity activity = ((NewTaskActivity)mContext);
                if(activity != null ){
                    activity.saveEditor(null);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mGdImage){
            List<AttachmentListEntity>  picUrl = mGridAdapter.getPicUrl();
            if (picUrl.size() > 0) {//载入照片url地址
                Intent PreView = new Intent(mContext,GalleryActivity.class);
                PreView.putExtra(GalleryActivity.KEY_PHOTO_URL, (Serializable) picUrl);
                PreView.putExtra(GalleryActivity.KEY_TYPE,GalleryActivity.HTTP_URL);
                PreView.putExtra(GalleryActivity.KEY_POSITION, position);
                if(mContext instanceof NewTaskActivity){
                    NewTaskActivity activity = ((NewTaskActivity)mContext);
                    activity.startActivityForResult(PreView,NewTaskActivity.PREVIEW_PIC);
                }
            } else {//载入本地照片地址
                Intent PreView = new Intent(mContext,GalleryActivity.class);
                PreView.putExtra(GalleryActivity.KEY_PHOTO_URL, (Serializable) mSelectPicUrl);
                PreView.putExtra(GalleryActivity.KEY_TYPE,GalleryActivity.STORAGE_URL);
                PreView.putExtra(GalleryActivity.KEY_POSITION, position);
                if(mContext instanceof NewTaskActivity){
                    NewTaskActivity activity = ((NewTaskActivity)mContext);
                    activity.startActivityForResult(PreView,NewTaskActivity.PREVIEW_PIC);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == this){
            FormViewUtil.hideSoftInputWindow(mContext,this);
            if(mContext instanceof NewTaskActivity){
                ((NewTaskActivity)mContext).chooseAttach(this);
            }
        }
    }

    @Override
    public boolean onTouchBlankPosition() {
        if(mContext instanceof NewTaskActivity){
            ((NewTaskActivity)mContext).chooseAttach(this);
        }
        return false;
    }


    public void showLoading(){
        if(LauchrConst.IS_WORKHUB && mWorkHubLoading!=null){
            if (!mWorkHubLoading.isShowing()) {
                mWorkHubLoading.show();
                mWorkHubLoading.start();
            }
        }else if (mLoading != null) {
            if (!mLoading.isShowing()) {
                mLoading.show();
                mLoading.start();
            }
        }
    }
    public void dismissLoading(){
        if(LauchrConst.IS_WORKHUB && mWorkHubLoading!=null){
            if (mWorkHubLoading.isShowing()) {
                mWorkHubLoading.dismiss();
            }
        } else if (mLoading != null) {
            if (mLoading.isShowing()) {
                mLoading.dismiss();
            }
        }
    }
}
