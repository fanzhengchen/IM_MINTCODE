package mintcode.com.workhub_im.view.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.mintcode.imkit.consts.IMConst;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.PhotoActivity;
import mintcode.com.workhub_im.widget.auido.AudioRecordPlayUtil;
import mintcode.com.workhub_im.widget.IMChatManager;
import mintcode.com.workhub_im.widget.emoji.ParseEmojiMsgUtil;

/**
 * Created by JulyYu on 2016/6/8.
 */
public class MsgSendView extends RelativeLayout implements View.OnTouchListener,
        TextWatcher,EmojiView.OnFaceOprateListener,View.OnKeyListener{


    private Context mContext;
    /** 输入框主要组件*/
    @BindView(R.id.ll_input_text_bar)
    protected LinearLayout mLlMainInputBar;
    /** Emoji表情*/
    @BindView(R.id.iv_chat_emoji)
    protected ImageView mIvChatEmoji;
    /** 文本输入框*/
    @BindView(R.id.edt_chat_input)
    protected EditText mEdtSendText;
    /** 语音切换*/
    @BindView(R.id.cb_chat_microphone)
    protected CheckBox mCbMicrophone;
    /** 发送键*/
    @BindView(R.id.iv_text_send)
    protected ImageView mIvSend;
    /** 发送语音*/
    @BindView(R.id.btn_sound)
    protected Button mBtnSound;
    /** 更多操作栏*/
    @BindView(R.id.lin_chat_menu)
    protected LinearLayout mLlChatMenu;
    /** 更多添加操作栏*/
    @BindView(R.id.chat_more_container)
    protected LinearLayout mLlChatMore;
    @BindView(R.id.view_emojilist)
    protected EmojiView mEmojiSelectView;
    /** 输入框操作监听*/
    private OnMsgSendListener mOnMsgSendListener;
    /*** 录音管理工具*/
    private AudioRecordPlayUtil mAudioRecordPlayerUtil;



    /** 切换输入 添加模式*/
    private enum MoreState {
        More, Input
    }
    /** 切换文字 语音模式*/
    private enum InputState {
        Text, Voice
    }
    private MoreState mMoreState = MoreState.Input;
    private InputState mInputState = InputState.Voice;

    public static String mStrPhotoPath;

    public MsgSendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_chat_send,this);
        ButterKnife.bind(this,view);

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
//        int mInputType = mSharedPreferences.getInt(Keys.INPUT_ENTER_TYPE, 0);
//        if (mInputType == Keys.INPUT_ENTER_SEND) {
//            mEdtSendText.setInputType(InputType.TYPE_CLASS_TEXT);
//            mEdtSendText.setImeOptions(EditorInfo.IME_ACTION_SEND);
//        }
        mEdtSendText.addTextChangedListener(this);
        mEdtSendText.setOnKeyListener(this);
        mEmojiSelectView.setFaceOpreateListener(this);
        mBtnSound.setOnTouchListener(this);
        mEdtSendText.setOnTouchListener(this);
        mAudioRecordPlayerUtil = new AudioRecordPlayUtil(null, getContext(), null);
    }
    /** 设置输入内容并设置游标位置*/
    public void setTextContent(SpannableStringBuilder text){
        mEdtSendText.setText(text);
        mEdtSendText.setSelection(mEdtSendText.getText().length());
    }
    /** 设置输入框内容*/
    public void setTextContent(String text){
        mEdtSendText.setText(text);
        mEdtSendText.setSelection(mEdtSendText.getText().length());
    }
    /** 获取输入框Editeable*/
    public Editable getTextContent(){
        Editable editable = mEdtSendText.getText();
        return mEdtSendText.getText();
    }
    /** 是否有输入文字*/
    public boolean isRetianText(){
        return !TextUtils.isEmpty(mEdtSendText.getText().toString());
    }
    public void setVisiableMoreView(boolean visiable){
        int intVisiable = visiable == true ?VISIBLE:GONE;
        int intVisiable2 = visiable == true ?GONE:VISIBLE;
        mLlChatMenu.setVisibility(intVisiable);
        mLlMainInputBar.setVisibility(intVisiable2);
    }
    /** 批量标记重点*/
    @OnClick(R.id.rel_mark_list)
    protected void markImprotant(){
        if(mOnMsgSendListener != null){
            mOnMsgSendListener.markImprotantList();
        }
    }
    /** 转发消息*/
    @OnClick(R.id.rel_forword_list)
    protected void sendforwordList(){
        if(mOnMsgSendListener != null){
            mOnMsgSendListener.sendForwordList();
        }
    }
    /** 发送文本消息*/
    @OnClick(R.id.iv_text_send)
    protected void sendTextMsg(){
        if (mOnMsgSendListener != null) {
            //这里发送的消息可能包含表情符号，所以要进行转码
            String message = ParseEmojiMsgUtil.convertDraToMsg(mEdtSendText.getText(), getContext());
            if (!message.trim().isEmpty()) {
                mOnMsgSendListener.textMsgSend(message);
                mEdtSendText.setText("");
            }
        }
    }
    /** 语音文本切换*/
    @OnClick(R.id.cb_chat_microphone)
    protected void changeMicrophoneMode(){
        if(mLlChatMore.getVisibility() == VISIBLE){
            mLlChatMore.setVisibility(GONE);
        }
        if(mEmojiSelectView.getVisibility() == VISIBLE){
            mEmojiSelectView.setVisibility(GONE);
            mIvChatEmoji.setImageResource(R.drawable.icon_chat_emoji);
        }else{
            mIvChatEmoji.setImageResource(R.drawable.icon_chat_emoji);
        }
        switch (mInputState){
            case Voice:
                mCbMicrophone.setChecked(false);
                mInputState = InputState.Text;
                mBtnSound.setVisibility(GONE);
                mEdtSendText.setVisibility(VISIBLE);
                break;
            case Text:
                mCbMicrophone.setChecked(true);
                mInputState = InputState.Voice;
                mBtnSound.setVisibility(VISIBLE);
                mEdtSendText.setVisibility(GONE);
                break;
            default:
                break;
        }
    }
    /** emoji表情选择*/
    @OnClick(R.id.iv_chat_emoji)
    protected void showEmojiView(){
        if(mLlChatMore.getVisibility() == VISIBLE){
            mLlChatMore.setVisibility(GONE);
            mMoreState = MoreState.Input;
        }
        if(mEmojiSelectView.getVisibility() == VISIBLE){
            mIvChatEmoji.setImageResource(R.drawable.icon_chat_emoji);
            mEmojiSelectView.setVisibility(GONE);
            displayInputMethod(true);
        }else{
            mIvChatEmoji.setImageResource(R.drawable.btn_chat_keyboard_pressed);
            mEmojiSelectView.setVisibility(VISIBLE);
            displayInputMethod(false);
        }
        mInputState = InputState.Text;
        mCbMicrophone.setChecked(false);
        mBtnSound.setVisibility(GONE);
        mEdtSendText.setVisibility(VISIBLE);

    }
    /** 更多添加操作栏的切换*/
    @OnClick(R.id.btn_chat_more)
    protected void ChnageAddMoreMode(){
        if(mEmojiSelectView.getVisibility() == VISIBLE){
            mEmojiSelectView.setVisibility(GONE);
        }
        switch (mMoreState){
            case More:
                mMoreState = MoreState.Input;
                mLlChatMore.setVisibility(GONE);
                break;
            case Input:
                mMoreState = MoreState.More;
                mLlChatMore.setVisibility(VISIBLE);
                displayInputMethod(false);
                break;
        }
    }

    /** 更多添加操作*/
    @OnClick({R.id.tv_chat_add_photo, R.id.tv_chat_camera_photo, R.id.tv_chat_add_task})
    protected void addMoreClick(View view){
        int id = view.getId();
        switch (id){
            //添加照片
            case R.id.tv_chat_add_photo:
                AlbumPhoto();
                break;
            //添加相机照片
            case R.id.tv_chat_camera_photo:
                takePhoto();
                break;
            //添加任务
            case R.id.tv_chat_add_task:
                if(mOnMsgSendListener != null){
                    mOnMsgSendListener.taskMsgSend();
                }
                break;
            default:
                break;
        }
    }
    /** 选择相册照片*/
    private void AlbumPhoto() {
        Intent intent = new Intent(mContext, PhotoActivity.class);
//        mContext.startActivity(intent);
//        ((Activity)mContext).startActivityForResult(intent,);
        ((Activity)mContext).startActivityForResult(intent, IMConst.REQ_SELECT_IMAGE);
    }
    /** 拍照照片*/
    private void takePhoto() {
        if (mContext.getExternalCacheDir() == null) {
            Toast.makeText(mContext, "外部存储不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mStrPhotoPath = Environment.getExternalStorageDirectory() + "/launchr/"
                + System.currentTimeMillis() + ".jpg";
        try {
            File mPhotoFile = new File(mStrPhotoPath);
            if (!mPhotoFile.getParentFile().exists()) {
                mPhotoFile.getParentFile().mkdirs();
            }

            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
//            ((Activity)mContext).startActivityForResult(intent,IMConst.REQ_FROM_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*** 录制声音*/
    private void recordSound() {
        mAudioRecordPlayerUtil.setFileName(UUID.randomUUID().toString()
                + ".amr");
        mAudioRecordPlayerUtil.startRecording();

    }

    /*** 发送语音消息*/
    protected void sendSoundFile() {
        mAudioRecordPlayerUtil.stopRecording();
        if (mAudioRecordPlayerUtil.getRecordMilliseconds() > 300 && mOnMsgSendListener != null) {
            String filePath = mAudioRecordPlayerUtil.getOutputFileName();
            String time = String.valueOf(Math.max(1, mAudioRecordPlayerUtil.getRecordMilliseconds() / 1000));
            mOnMsgSendListener.voiceMsgSend(filePath, time);
        }else{
            mOnMsgSendListener.voiceMsgSend(null,null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == mEdtSendText){
            mLlChatMenu.setVisibility(GONE);
            mEmojiSelectView.setVisibility(GONE);
            mIvChatEmoji.setImageResource(R.drawable.icon_chat_emoji);
            mLlChatMore.setVisibility(GONE);
        }else if(v == mBtnSound){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    recordSound();
                    mBtnSound.setText(mContext.getString(R.string.press_up_speak));
                    if (mOnMsgSendListener != null) {
                        mOnMsgSendListener.recordSound(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    sendSoundFile();
                    mBtnSound.setText(mContext.getString(R.string.press_down_speak));
                    if (mOnMsgSendListener != null) {
                        mOnMsgSendListener.recordSound(false);
                    }
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count == 1) {
            //TODO @人
            String str = s.toString();
            String w = str.substring(str.length() - 1);
                    if (w.equals("@")) {
                        if(mOnMsgSendListener != null){
                            mOnMsgSendListener.atPerson();
                        }
                    }
        }

    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(v == mEdtSendText){
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                // 按下删除键监听
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Editable editable = mEdtSendText.getText();
                    int index = mEdtSendText.getSelectionStart();
                    int len = editable.length();
                    // 判断单光标在否在输入文本的末尾
                    if (index == len) {
                        return  IMChatManager.getInstance().delectAtMessageInfoEntity(editable);
                    } else {
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }
    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().length() > 0) {
            mIvSend.setVisibility(View.VISIBLE);
            mCbMicrophone.setVisibility(View.INVISIBLE);
        } else {
            mIvSend.setVisibility(View.INVISIBLE);
            mCbMicrophone.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFaceSelected(SpannableString spanEmojiStr) {
            if(spanEmojiStr != null){
                mEdtSendText.append(spanEmojiStr);
            }
    }

    @Override
    public void onFaceDeleted() {
        int selection = mEdtSendText.getSelectionStart();
        String text = mEdtSendText.getText().toString();
        if (selection > 0) {
            String text2 = text.substring(selection - 1);
            if ("]".equals(text2)) {
                int start = text.lastIndexOf("[");
                int end = selection;
                mEdtSendText.getText().delete(start, end);
                Editable et = mEdtSendText.getText();
                et.delete(start, end);
                mEdtSendText.invalidate();
                return;
            }
            mEdtSendText.getText().delete(selection - 1, selection);
        }
    }

    public interface OnMsgSendListener{
        void textMsgSend(String msg);
        void voiceMsgSend(String path, String time);
        void taskMsgSend();
        void recordSound(boolean isRecording);
        void atPerson();
        void markImprotantList();
        void sendForwordList();
    }
    public void setOnMsgSendListener(OnMsgSendListener listener){
        mOnMsgSendListener = listener;
    }
    private void displayInputMethod(boolean isOpen) {
        InputMethodManager inputMethodManager = (InputMethodManager) mEdtSendText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isOpen) {
            inputMethodManager.showSoftInput(mEdtSendText, InputMethodManager.SHOW_FORCED);
        } else {
            inputMethodManager.hideSoftInputFromWindow(mEdtSendText.getWindowToken(), 0);
        }
    }
}
