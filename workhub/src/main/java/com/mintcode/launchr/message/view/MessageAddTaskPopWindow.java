package com.mintcode.launchr.message.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.im.Command;
import com.mintcode.im.IMManager;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.activity.NewTaskActivity;
import com.mintcode.launchr.app.newTask.activity.ProjectListActivity;
import com.mintcode.launchr.app.newTask.view.FinalTimeDialog;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.message.activity.ChooseMemberActivity;
import com.mintcode.launchr.pojo.NewTaskPOJO;
import com.mintcode.launchr.pojo.entity.MessageEventTaskEntity;
import com.mintcode.launchr.pojo.entity.NewTaskEntity;
import com.mintcode.launchr.pojo.entity.TaskDetailEntity;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessageAddTaskPopWindow extends PopupWindow implements
         View.OnClickListener,FinalTimeDialog.OnGetTimeListener {
//    private static final String APP_SHOW_ID = "PWP56jQLLjFEZXLe";
    private static final String APP_SHOW_ID = Const.SHOWID_TASK;

    private View mContentView;

    private LayoutInflater mInflater;

    private Context mContext;

    /** 时间控件 */
//    private Timer mTimer;

    /** 截止时间 */
//    private Calendar finalTime;

    /** 全天 */
//    private CheckBox mCbAllDay;

    /** 更多选项 */
    private TextView mTvMoreItem;

    /** 项目 */
    private RelativeLayout mRelProject;
    private TextView mTvProjectName;
    private TaskProjectListEntity mProjectEntity;

    /** 参与者 */
    private RelativeLayout mRelEnter;
    private TextView mTvMemberName;
    private String mUserName;
    private String mUserId;

    /** 标题 */
    private EditText mEditTitle;

    /** 确定 */
    private TextView mIvOk;
    /** 取消*/
    private TextView mTvCancel;

    //开始时间
    private RelativeLayout mRelStartTime;
    private TextView mTvStartTime;
    private Long mLStartTime;
    private int mStartTimeAllDay = 1;

    // 截止时间
    private RelativeLayout mRlFinalTime;
    private TextView mTvFinalTime;
    private Long mLFinalTime;
    private int mFinalTimeAllDay = 1;

    private FinalTimeDialog mTimeDialog;

    public MessageAddTaskPopWindow(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.popwindow_add_task, null);

        setWindow();
        initView();
    }

    private void setWindow() {
        this.setContentView(mContentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable color = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(color);
    }

    private void initView() {
//        mTimer = (Timer) mContentView.findViewById(R.id.add_task_timer);
//        mCbAllDay = (CheckBox) mContentView.findViewById(R.id.cb_is_allday);
        mTvMoreItem = (TextView) mContentView.findViewById(R.id.tv_more);
        mRelEnter = (RelativeLayout) mContentView.findViewById(R.id.rel_enter);
        mRelProject = (RelativeLayout) mContentView.findViewById(R.id.rel_project);
        mEditTitle = (EditText) mContentView.findViewById(R.id.edit_title);
        mIvOk = (TextView) mContentView.findViewById(R.id.dialog_ok);
        mTvCancel = (TextView) mContentView.findViewById(R.id.dialog_cancel);
        mTvProjectName = (TextView) mContentView.findViewById(R.id.tv_project_name);
        mTvMemberName = (TextView) mContentView.findViewById(R.id.tv_member_name);
        mRelStartTime = (RelativeLayout) mContentView.findViewById(R.id.new_task_starttime);
        mRlFinalTime = (RelativeLayout) mContentView.findViewById(R.id.new_task_finaltime);
        mTvStartTime = (TextView) mContentView.findViewById(R.id.new_task_starttime_text);
        mTvFinalTime = (TextView) mContentView.findViewById(R.id.new_task_finaltime_text);

//        mCbAllDay.setOnCheckedChangeListener(this);
        mTvMoreItem.setOnClickListener(this);
        mRelEnter.setOnClickListener(this);
        mRelProject.setOnClickListener(this);
        mIvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mRelStartTime.setOnClickListener(this);
        mRlFinalTime.setOnClickListener(this);
//        mTimer.setOnTimerListener(this);
//        mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
//        mTimer.setConflictViewDisplay(false);
//
//        finalTime = Calendar.getInstance();

        //  初始化开始、结束时间
        Calendar c = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        b.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        mLStartTime = b.getTimeInMillis();
        mLFinalTime = null;

        mUserName = AppUtil.getInstance(mContext).getUserName();
        mUserId = AppUtil.getInstance(mContext).getLoginName();
        mTvMemberName.setText(mUserName);
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(isChecked){
//            mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
//        }else{
//            mTimer.setStyle(Timer.SINGLE_TIME_DATE);
//        }
//    }
//
//    @Override
//    public void OnTimeChangeListenner(View view, int year, int month, int day,
//                                      int hour, int minute, int type, boolean isEnd) {
//        finalTime = Calendar.getInstance();
//        finalTime.set(year, month - 1, day, hour, minute, 0);
//    }

    @Override
    public void onClick(View v) {
        if (v == mTvMoreItem) {
            doMoreChoose();
        } else if (v == mRelEnter) {
            doMemberChoose();
        } else if (v == mRelProject) {
            doProjectChoose();
        } else if (v == mIvOk) {
            sendMessage();
        }else if(v == mRelStartTime){
            // 开始时间
            chooseStartTime();
        }else if(v == mRlFinalTime){
            // 截止时间
            chooseFinalTime();
        }else if(v == mTvCancel){
            dismiss();
        }
    }

    // 选择开始时间
    public void chooseStartTime() {
        initTimeDialog(mContext.getString(R.string.choose_start_time));
        mTimeDialog.setStartTime(mLStartTime);
        if(mStartTimeAllDay == 1){
            mTimeDialog.setAllDay(true);
        }else{
            mTimeDialog.setAllDay(false);
        }
//        final FinalTimeDialog finalTime = new FinalTimeDialog(mContext, mContext.getString(R.string.choose_start_time));
//        finalTime.show();
//        if(mLStartTime  != null){
//            finalTime.setStartTime(mLStartTime);
//        }
//        Window window = finalTime.getWindow();
//        final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v == finalOk) {
//                    mLStartTime = finalTime.getTime().getTimeInMillis();
////                    showFinalTime(finalTime.getTime(), mTvStartTime);
//                    setTime(mStartTimeAllDay, mLStartTime, mLFinalTime);
//                    finalTime.dismiss();
//                }
//            }
//        };
//        finalOk.setOnClickListener(listener);
//        final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//        finalSetting.setText(mContext.getString(R.string.some_day));
//        finalSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTvStartTime.setText(mContext.getString(R.string.some_day));
//                mLStartTime = 0l;
//                finalTime.dismiss();
//            }
//        });
//        final CheckBox startAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//        if(mStartTimeAllDay == 1){
//            startAllDay.setChecked(true);
//            finalTime.setAllDay(true);
//        }else{
//            startAllDay.setChecked(false);
//            finalTime.setAllDay(false);
//        }
//        startAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    finalTime.setAllDay(isChecked);
//                    mStartTimeAllDay= 1;
//                } else {
//                    finalTime.setAllDay(isChecked);
//                    mStartTimeAllDay = 0;
//                }
//            }
//        });
    }

    // 选择截止时间
    public void chooseFinalTime() {

        initTimeDialog(mContext.getString(R.string.choose_stop_time));
        mTimeDialog.setEndTime(mLFinalTime);
        if(mFinalTimeAllDay == 1){
            mTimeDialog.setAllDay(true);
        }else{
            mTimeDialog.setAllDay(false);
        }
//        final FinalTimeDialog finalTime = new FinalTimeDialog(mContext, mContext.getString(R.string.choose_stop_time));
//        finalTime.show();
//        if(mLFinalTime != null  && mLFinalTime > 0){
//            finalTime.setEndTime(mLFinalTime);
//        }
//
//        Window window = finalTime.getWindow();
//        final TextView finalOk = (TextView) window.findViewById(R.id.dialog_finaltime_ok);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v == finalOk) {
//                    mLFinalTime = finalTime.getTime().getTimeInMillis();
//                    if(mLFinalTime >= mLStartTime ){
////                        showFinalTime(finalTime.getTime(), mTvFinalTimeText);
//                        setTime(mFinalTimeAllDay,mLStartTime,mLFinalTime);
//                        finalTime.dismiss();
//                    }else{
//                        Toast.makeText(mContext, mContext.getString(R.string.end_time_must_not_be_later_than_begin_time), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        };
//        finalOk.setOnClickListener(listener);
//        final TextView finalSetting = (TextView) window.findViewById(R.id.tv_one_day);
//        finalSetting.setText(mContext.getString(R.string.no_set_time));
//        finalSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTvFinalTime.setText(mContext.getString(R.string.no_set_time));
//                mLFinalTime = null;
//                finalTime.dismiss();
//            }
//        });
//        final CheckBox finalAllDay = (CheckBox) window.findViewById(R.id.cb_is_allday);
//        if(mFinalTimeAllDay == 1){
//            finalAllDay.setChecked(true);
//            finalTime.setAllDay(true);
//        }else{
//            finalAllDay.setChecked(false);
//            finalTime.setAllDay(false);
//        }
//        finalAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    finalTime.setAllDay(isChecked);
//                    mFinalTimeAllDay = 1;
//                }else{
//                    finalTime.setAllDay(isChecked);
//                    mFinalTimeAllDay = 0;
//                }
//            }
//        });
    }

    // 显示时间
    public void showFinalTime(Calendar calendar, TextView textView) {
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);

        if (h == 0 && mm == 0) {
            textView.setText(m + "/" + d);
        }else{
            textView.setText(m + "/" + d + " " + h + ":" + mm);
        }
    }

    public void setTime(int allDay,long startTime,long finaTime){

        SimpleDateFormat dateFormat;
        mLStartTime = startTime;
        mLFinalTime = finaTime;
        if (allDay == 1) {
            dateFormat = new SimpleDateFormat("MM/dd");
            if(mLFinalTime != null){
                mTvFinalTime.setText(dateFormat.format(mLFinalTime));
            }else{
                mTvFinalTime.setText(mContext.getString(R.string.no_set_time));
            }
            if(mLStartTime != null){
                mTvStartTime.setText(dateFormat.format(mLStartTime));
            }else{
                mTvStartTime.setText(mContext.getString(R.string.no_set_time));
            }
        }else{
            dateFormat = new SimpleDateFormat("MM/dd HH:mm");
            if(mLFinalTime != null){
                mTvFinalTime.setText(dateFormat.format(mLFinalTime));
            }else{
                mTvFinalTime.setText(mContext.getString(R.string.no_set_time));
            }
            if(mLStartTime != null){
                mTvStartTime.setText(dateFormat.format(mLStartTime));
            }else{
                mTvStartTime.setText(mContext.getString(R.string.no_set_time));
            }
        }
    }

    /** 发送消息 */
    public void sendMessage() {
        if (mEditTitle.getText().length() <= 0) {
            Toast.makeText(mContext, mContext.getString(R.string.calendar_title_toast), Toast.LENGTH_SHORT).show();
            return;
        }

//        if (mProjectEntity == null) {
//            Toast.makeText(mContext, mContext.getString(R.string.message_add_task_project), Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (mUserName == null) {
            Toast.makeText(mContext, mContext.getString(R.string.meeting_enter_people), Toast.LENGTH_SHORT).show();
            return;
        }

        if(mTvFinalTime.getText().toString().equals(mContext.getString(R.string.no_set_time))){
            mLFinalTime = 0L;
        }

        if(mProjectEntity != null){
            TaskApi.getInstance().newTask(addTaskResult, mProjectEntity.getShowId(), mEditTitle.getText().toString(), null, mLStartTime, mLFinalTime,
                    mStartTimeAllDay, mFinalTimeAllDay, mUserId, mUserName, TaskUtil.NONE_PRIORITY, 0, null, null);
        }else{
            TaskApi.getInstance().newTask(addTaskResult, null, mEditTitle.getText().toString(), null, mLStartTime, mLFinalTime,
                    mStartTimeAllDay, mFinalTimeAllDay, mUserId, mUserName, TaskUtil.NONE_PRIORITY, 0, null,  null);
        }

        dismiss();
    }

    private com.mintcode.launchrnetwork.OnResponseListener addTaskResult = new com.mintcode.launchrnetwork.OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            // TODO Auto-generated method stub
            if (response == null) {
                return;
            } else if (taskId.equals(TaskApi.TaskId.TASK_URL_ADD_TASK)){
                // 判断是否是新建任务返回
                handleAddTaskResult(response);
            }
        }

        @Override
        public boolean isDisable() {
            // TODO Auto-generated method stub
            return false;
        }
    };

    /**
     * 处理新建任务返回
     * @param response
     */
    private void handleAddTaskResult(Object response){
        NewTaskPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NewTaskPOJO.class);
        if (pojo == null) {
            return;
        }else if(pojo.isResultSuccess() == false){
            return;
        }else{
            Toast.makeText(mContext, mContext.getString(R.string.msg_change__schedule_success), Toast.LENGTH_SHORT).show();
//            NewTaskEntity entity = pojo.getBody().getResponse().getData();
//            if (entity != null) {
//                sendMessageToIM(entity);
//            }
        }

    }

    private void sendMessageToIM(NewTaskEntity entity){
        MessageEventTaskEntity.EventTaskCard taskCard = new MessageEventTaskEntity.EventTaskCard();
        taskCard.setTitle(entity.gettTitle());
        taskCard.setEnd(entity.gettEndTime());
        if(mProjectEntity!=null && mProjectEntity.getName() != null){
            taskCard.setProjectName(mProjectEntity.getName());
        }
        taskCard.setId(entity.getShowId());
        taskCard.setLevel(entity.gettLevel());
        taskCard.setStateName(entity.getsType());
        taskCard.setPriority(entity.gettPriority());
//		String msgInfo = TTJSONUtil.convertObjToJson(taskCard);

        MessageEventTaskEntity.MessageEventTaskBackupEntity task = new MessageEventTaskEntity.MessageEventTaskBackupEntity();
        task.setMsgTitle(entity.gettTitle());
        task.setMsgContent("");
        task.setMsgAppShowID(APP_SHOW_ID);
        task.setMsgAppType("TASK");
        task.setMsgInfo(taskCard);
        task.setMsgType(1);


        //((ChatActivity) mContext).sendTaskMessage(task, isInGroup, mUserId, mUserName);

        String mUid = AppUtil.getInstance(mContext).getLoginName();
        String nickName = AppUtil.getInstance(mContext).getUserName();
        MessageItem item = new MessageItem();
        item.setCmd(MessageItem.TYPE_SEND);
        item.setContent(TTJSONUtil.convertObjToJson(task));
        item.setType(Command.EVENT);
        item.setSent(Command.STATE_SEND);
        item.setClientMsgId(System.currentTimeMillis());
        item.setCreateDate(System.currentTimeMillis());
        item.setFromLoginName(mUid);
        item.setToLoginName(mUserId);
        item.setToNickName(mUserName);
        item.setNickName(nickName);
        item.setMsgId(System.currentTimeMillis());

        IMManager.getInstance().sendMessageItem(item);
        Toast.makeText(mContext, mContext.getString(R.string.msg_change__schedule_success), Toast.LENGTH_SHORT).show();
    }

    /** 更多选项 */
    private void doMoreChoose() {
        List<String> enterName = new ArrayList<String>();
        List<String> enterId = new ArrayList<String>();
        enterName.add(mUserName);
        enterId.add(mUserId);

        TaskDetailEntity entity = new TaskDetailEntity();
        entity.settTitle(mEditTitle.getText().toString());
        if(mProjectEntity != null){
            entity.setpShowId(mProjectEntity.getShowId());
            entity.setpName(mProjectEntity.getName());
        }
        entity.settStartTime(mLStartTime);
        entity.settEndTime(mLFinalTime);
        entity.settUser(enterId.get(0));
        entity.settUserName(enterName.get(0));

        Bundle bundle = new Bundle();
        bundle.putSerializable("add_task", entity);
        Intent intent = new Intent(mContext, NewTaskActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        dismiss();
    }

    /** 选择项目 */
    private void doProjectChoose() {
        Intent intent = new Intent(mContext, ProjectListActivity.class);
        ((BaseActivity) mContext).startActivityForResult(intent, 1008);
    }

    /** 选择参与人员 */
    private void doMemberChoose() {
        Intent intent = new Intent(mContext, ChooseMemberActivity.class);
        ((BaseActivity) mContext).startActivityForResult(intent, 1009);
    }

    /** 项目选择结果 */
    public void setProjectResult(TaskProjectListEntity entity) {
        if (entity != null) {
            mProjectEntity = entity;
            mTvProjectName.setText(entity.getName());
        }
    }

    /** 参与人员选择结果 */
    public void setMemberResult(List<String> userId, List<String> userName) {
        if(userId != null && userName != null){
            mUserId = userId.get(0);
            mUserName = userName.get(0);
            mTvMemberName.setText(mUserName);
        }
    }


    private void initTimeDialog(String title){
        if(mTimeDialog == null){
            mTimeDialog = new FinalTimeDialog(mContext,title);
            mTimeDialog.setGetTimeListener(this);
            mTimeDialog.show();
        }else{
            mTimeDialog.show();
            mTimeDialog.setCustomTitle(title);
        }
    }

    @Override
    public void getLongTime(int timeFlag, Long time, String strTime, int allDay) {
        if(timeFlag == FinalTimeDialog.START_TIME){
            mLStartTime = time;
            mTvStartTime.setText(strTime);
            mStartTimeAllDay = allDay;
        }else if(timeFlag == FinalTimeDialog.OVER_TIME){
            mLFinalTime = time;
            mTvFinalTime.setText(strTime);
            mFinalTimeAllDay = allDay;
        }
    }
}
