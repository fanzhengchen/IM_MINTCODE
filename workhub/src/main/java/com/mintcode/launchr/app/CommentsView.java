package com.mintcode.launchr.app;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newSchedule.view.SelectAddEventPopWindowView;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.ApproveAddCommentPOJO;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.upload_download.UploadFileTask;
import com.mintcode.launchr.upload_download.UploadPartAsyncTask;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.SelectPicTypeWindowView;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;


/**
 * Created by JulyYu on 2016/4/22.
 */
public class CommentsView extends LinearLayout implements View.OnClickListener,
        OnResponseListener {

    private Context mContext;
    private ImageView mIvAdd;
    private EditText mEditMessage;
    private ImageView mIvSend;

    private String mStrScheduleType;
    private String mStrAppKey;
    private String mStrShowId;
    private String mStrTitle;
    private int mIntHasComment;
    private List<String> mMessagePerson;
    private List<String> mMessagePersonName;

    private SendMessageListener mListener;
    private String mStrMessageAppType;

    public static final String MEETING = "meeting";
    public static final String EVENT = "event";

    /**
     * 拍照
     */
    public static final int TAKE_PICTURE = 0x000004;
    /**
     * 预览照片
     */
    public static final int PREVIEW_PIC = 0x000002;
    /**
     * 打开相册选择照片
     */
    public static final int GET_ALBUM = 0x000003;

    public CommentsView(Context context) {
        super(context);
        initView();
        mContext = context;
        initView();
    }


    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_send_message, null);
        mIvAdd = (ImageView) view.findViewById(R.id.iv_accpet_detail_add);
        mEditMessage = (EditText) view.findViewById(R.id.edt_approval_sendmessage);
        mIvSend = (ImageView) view.findViewById(R.id.btn_accpect_detail_send);

        mIvAdd.setOnClickListener(this);
        mIvSend.setOnClickListener(this);
        addView(view);
    }


    public void setAppInfo(String appKey, String showId, String title, int hasComment, List<String> person, List<String> personName) {
        mStrAppKey = appKey;
        mStrShowId = showId;
        mStrTitle = title;
        mIntHasComment = hasComment;
        mMessagePerson = person;
        mMessagePersonName = personName;
        if (mStrAppKey.equals(Const.SHOWID_APPROVE)) {
            mStrMessageAppType = "APPROVAL_COMMENT";
        } else if (mStrAppKey.equals(Const.SHOWID_TASK)) {
            mStrMessageAppType = "TASK_COMMENT";
        } else if (mStrAppKey.equals(Const.SHOW_SCHEDULE)) {
            mStrMessageAppType = "MEETING_COMMENT";
        }
    }

    public void setScheduleAppInfo(String appKey, String showId, String title, int hasComment, List<String> person, List<String> personName, String type) {
        mStrAppKey = appKey;
        mStrShowId = showId;
        mStrTitle = title;
        mIntHasComment = hasComment;
        mMessagePerson = person;
        mMessagePersonName = personName;
        if (type.equals(MEETING)) {
            mStrMessageAppType = "MEETING_COMMENT";
        } else if (type.equals(EVENT)) {
            mStrMessageAppType = "EVENT_COMMENT";
        }
    }

    //Upload Images taken by photo
    public void setImageMessage() {

        if (!"".equals(SelectPicTypeWindowView.mStrPicPath)) {
            UploadPartAsyncTask uploadPartAsyncTask = new UploadPartAsyncTask(mContext, SelectPicTypeWindowView.mStrPicPath, new UploadFileTask.UploadOverLister() {

                @Override
                public void uploadOver(String path, String showid) {
                    sendCommentForPic(path, showid);
                }

                @Override
                public void uploadError(String reason) {
                    Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
                }
            });
            uploadPartAsyncTask.setType(UploadPartAsyncTask.TYPE_TASK);
            uploadPartAsyncTask.execute();
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.photo_unget_photo), Toast.LENGTH_SHORT).show();
        }
    }

    // Upload Images which are from ImageGallery
    public void setAublmImageMessage(String imagePath) {
        if (!imagePath.equals("")) {
            UploadPartAsyncTask uploadPartAsyncTask = new UploadPartAsyncTask(mContext, imagePath, new UploadFileTask.UploadOverLister() {
                @Override
                public void uploadOver(String path, String showid) {//TODO
                    sendCommentForPic(path, showid);
                }

                @Override
                public void uploadError(String reason) {
                    Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
                }
            });
            uploadPartAsyncTask.setType(UploadPartAsyncTask.TYPE_TASK);
            uploadPartAsyncTask.execute();
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.phoyo_update_fail), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendCommentForPic(String showid, String picPath) {

        if (!mMessagePerson.isEmpty() && !mMessagePersonName.isEmpty()) {
            ApproveAPI.getInstance().addComment(this, mStrAppKey, mStrShowId, "pic"
                    , 0, showid, picPath, mStrTitle, mStrMessageAppType, mMessagePerson, mMessagePerson);
        }
    }

    @Override
    public void onClick(View v) {
        hideSystemSoftInput();
        if (v == mIvAdd) {
            addFile();
        } else if (v == mIvSend) {
            sendCommentMessage();
        }
    }

    private void sendCommentMessage() {
        String message = mEditMessage.getText().toString();
        if (message != null && message.length() > 0) {
            if (!mMessagePerson.isEmpty() && !mMessagePersonName.isEmpty()) {
                ApproveAPI.getInstance().addComment(this, mStrAppKey, mStrShowId, message,
                        0, null, null, mStrTitle, mStrMessageAppType, mMessagePerson, mMessagePersonName);

            } else {
                Toast.makeText(mContext, mContext.getString(R.string.approval_comment_send_error), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.apply_message_send_toast), Toast.LENGTH_SHORT).show();
        }

    }

    private void addFile() {

        SelectPicTypeWindowView popWindow = new SelectPicTypeWindowView(mContext);
        popWindow.showAtLocation(mEditMessage, Gravity.BOTTOM, 0, 0);
    }


//    @Override
//    public void getSecondOption() {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(mContext instanceof BaseActivity){
//           BaseActivity activity = ((BaseActivity) mContext);
//            if(activity != null){
//                activity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
//            }
//        }
//
//    }
//
//    @Override
//    public void selectFristOption() {
//        Intent GetAlbum = new Intent(mContext,PhotoActivity.class);
//        GetAlbum.putExtra(PhotoActivity.SET_SELECT_IMAGE_TYPE, PhotoActivity.SELECT_TYPE.SINGLE_SELECT.ordinal());
//        if(mContext instanceof BaseActivity){
//            BaseActivity activity = ((BaseActivity) mContext);
//            if(activity != null){
//                activity.startActivityForResult(GetAlbum, GET_ALBUM);
//            }
//        }
//    }

    public void setMessageLisener(SendMessageListener lisenter) {
        mListener = lisenter;
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        if (response == null) {
            return;
        }
        if (taskId.equals(ApproveAPI.TaskId.TASK_URL_ADD_COMMENT)) {
            //处理发送评论返回结果
            ApproveAddCommentPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApproveAddCommentPOJO.class);
            handleApprovalAddComment(pojo);
        } else if (taskId.equals(ApproveAPI.TaskId.TASK_URL_POST_COMMENT_V2) || taskId.equals(TaskApi.TaskId.TASK_URL_UPDATE_TASK_COMMENT)) {
            //有评论接口
            NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
            handleHasComment(pojo);

        }
    }


    private void handleApprovalAddComment(ApproveAddCommentPOJO pojo) {
        if (pojo == null) {

        } else if (pojo.getBody() == null) {
            Toast.makeText(mContext, pojo.getHeader().getReason(), Toast.LENGTH_SHORT).show();
        } else if (!pojo.getBody().getResponse().isIsSuccess()) {
            Toast.makeText(mContext, pojo.getBody().getResponse().getReason(), Toast.LENGTH_SHORT).show();
        } else if (pojo.getBody().getResponse().getData() != null) {
            //获取评论列表
            mEditMessage.setText("");
            hideSystemSoftInput();
            if (mIntHasComment == 0) {
                if (mStrAppKey.equals(Const.SHOWID_TASK)) {
                    TaskApi.getInstance().updateTaskComment(this, mStrShowId, 1);
                } else if (mStrAppKey.equals(Const.SHOWID_APPROVE)) {
                    ApproveAPI.getInstance().postCommentV2(this, mStrShowId, 1);
                } else if (mStrAppKey.equals(Const.SHOW_SCHEDULE)) {

                }
            } else {
                if (mListener != null) {
                    mListener.sendMessage("");
                }
            }
        }
    }

    /**
     * 有评论
     */
    private void handleHasComment(NormalPOJO pojo) {
        if (pojo == null) {

        } else if (pojo.getBody() == null) {
            Toast.makeText(mContext, pojo.getHeader().getReason(), Toast.LENGTH_SHORT).show();
        } else if (!pojo.getBody().getResponse().isIsSuccess()) {
            Toast.makeText(mContext, pojo.getBody().getResponse().getReason(), Toast.LENGTH_SHORT).show();
        } else if (pojo.getBody().getResponse().isIsSuccess()) {
            mIntHasComment = 1;
            mListener.sendMessage("");
        }
    }

    @Override
    public boolean isDisable() {
        return false;
    }

    public interface SendMessageListener {
        void sendMessage(String message);
    }

    // 隐藏键盘
    public void hideSystemSoftInput() {

        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                mEditMessage.getWindowToken(), 0);
    }
}
