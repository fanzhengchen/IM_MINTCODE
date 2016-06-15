package com.mintcode.launchr.message.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.DataPOJO;
import com.mintcode.RM;
import com.mintcode.chat.ChatViewHolder;
import com.mintcode.chat.image.MutiSoundUpload;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.chat.widget.ChatListView;
import com.mintcode.im.IMManager;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.OnIMMessageListener;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.ValidateListPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ValidateEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.RemarkDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class VerifyFriendMessageActivity extends BaseActivity implements AdapterView.OnItemClickListener,OnIMMessageListener {


    /** 未读数量 */
    private TextView mTvUnReadNum;
    private ImageView mIvBack;
    private TextView mTvTitle;
    /** 聊天列表 */
    private ChatListView mListView;
    private VerifyMessageAdapter mListAdapter;

    private List<ValidateEntity> mEntityList;

    private IMManager manager;

    /** 同意*/
    public final static int DEAL_AGRESS = 2;
    /** 拒绝*/
    public final static int DEAL_REFUSE = 3;
    /** 忽略*/
    public final static int DEAL_IGNORE = 4;

    public static String mTo = "";
    public static String toNickName;
    private int MSG = 0x01;
    private int mUpdateTime = 2000;

    /**  主要判断接收到的消息是否需要刷新页面*/
    private boolean mBoolUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_message);
        initView();
        initData();
    }


    private void initView(){
        mTvUnReadNum = (TextView) findViewById(R.id.tv_unread_num);
        mIvBack = (ImageView) findViewById(R.id.img_back);
        mListView = (ChatListView) findViewById(R.id.chat_list);

        mIvBack.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mListAdapter = new VerifyMessageAdapter(this);
        mListView.setAdapter(mListAdapter);

        manager = IMManager.getInstance();
        manager.setOnIMMessageListener(this);
    }

    private void initData() {
        // 从会话列表进入，获取信息
        SessionItem section = (SessionItem) getIntent().getParcelableExtra(
                "section");
        if (section != null) {
            toNickName = section.getNickName();
            mTo = section.getOppositeName();

        }

        mUnReadNumHandler.sendEmptyMessage(MSG);
        MessageNotifyManager.cancelNotification();
        MutiSoundUpload.getInstance();
    }


    private void setUnReadNum() {
        List<SessionItem> mSessionItems = IMManager.getInstance().getSession();
        int num = 0;
        for (SessionItem item
                : mSessionItems) {
            if (toNickName != null && !toNickName.equals(item.getNickName())) {
                // 屏蔽群消息标志设置为10000，判断是否是真实的未读消息数
                if (item.getUnread() != 10000) {
                    num += item.getUnread();
                }
            }
        }
        if (num == 0) {
            mTvUnReadNum.setText("");
        } else {
            String strNum;
            if (num > 99) {

                strNum = "( 99+ )";

            } else {
                strNum = "( " + num + " )";
            }
            mTvUnReadNum.setText(strNum);
        }
    }

    private Handler mUnReadNumHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG) {
                setUnReadNum();
                mUnReadNumHandler.sendEmptyMessageDelayed(MSG, mUpdateTime);
            }
        }
    };

    public void updataList(){
        String userName = AppUtil.getInstance(this).getShowId();
        showLoading();
        IMNewApi.getInstance().getValidataList(this,userName,20,-1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = AppUtil.getInstance(this).getShowId();
        showLoading();
        IMNewApi.getInstance().getValidataList(this,userName,20,-1);
    }

    @Override
    public void onBackPressed() {
        manager.readMessageByUid(mTo);
        this.finish();
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if(response == null){
            return;
        }
        if(taskId.equals(IMNewApi.TaskId.GET_VALIDATE_LISTTASK)){
            ValidateListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),ValidateListPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    mEntityList = pojo.getData();
                    mListAdapter.setData(mEntityList);
                    mBoolUpdate = false;
                }
            }
        }else if(taskId.equals(IMNewApi.TaskId.TASK_DISPOSE_VALIDATA)){
            DataPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DataPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    String userName = AppUtil.getInstance(this).getShowId();
                    showLoading();
                    IMNewApi.getInstance().getValidataList(this,userName,20,-1);
                }else{
                    toast(pojo.getMessage());
                }
            }
        }
    }

    @Override
    public void onStatusChanged(int result, String msg) {

    }

    @Override
    public void onMessage(List<MessageItem> items, int msgCount) {

        if (items == null) {
            items = new ArrayList<MessageItem>();
        }

        for(MessageItem item : items){
            String name = item.getFromLoginName();
            if(name.contains(Const.SHOW_FRIEND)){
                mBoolUpdate = true;
            }
        }
        if(mBoolUpdate){
            String userName = AppUtil.getInstance(this).getShowId();
            IMNewApi.getInstance().getValidataList(this,userName,20,-1);
        }
    }

    @Override
    public void onSession(List<SessionItem> sections) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTo = "";
        toNickName = "";
        manager.removeOnIMMessageListener(this);
    }

    class VerifyMessageAdapter extends BaseAdapter{

        private List<ValidateEntity> mData;
        private Context mContext;
        private LayoutInflater inflater;

        public VerifyMessageAdapter(Context context){
            mContext = context;
        }

        public void setData(List<ValidateEntity> data){
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData == null?0:mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData == null?null:mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(mContext);
            ChatViewHolder holder = null;
            if(convertView == null){
                holder = new ChatViewHolder();
                convertView = setupEventFriendVerifyViews(holder);
            }else{
                holder = (ChatViewHolder)convertView.getTag();
            }
             position = mData.size() - position - 1;
            final ValidateEntity entity = mData.get(position);
            int validateState = entity.getValidateState();
            holder.tvName.setText(entity.getFromNickName());
            // 验证信息
            String content = entity.getContent();
            if(content != null && !"".equals(content)){
                holder.event_remark.setText(entity.getContent());
                holder.event_layout_end.setVisibility(View.VISIBLE);
            }else{
                holder.event_layout_end.setVisibility(View.INVISIBLE);
            }
            HeadImageUtil.getInstance(mContext).setAvatarResource(holder.ivAvatar,entity.getFromNickName(),entity.getFromAvatar(), 2, 60, 60);
            if(validateState == 1){
                holder.event_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemarkDialog dilaog = new RemarkDialog(mContext,entity,DEAL_AGRESS);
                        dilaog.show();
                    }
                });
                holder.event_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refuseVerifyRequst(entity);
                    }
                });
                holder.event_layout_bottom_buttons.setVisibility(View.VISIBLE);
                holder.ivFileIcon.setVisibility(View.GONE);
            }else{
                holder.event_layout_bottom_buttons.setVisibility(View.GONE);
                holder.ivFileIcon.setVisibility(View.VISIBLE);
                if(validateState == 2){
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_agree),
                            R.drawable.icon_chat_card_accept, R.color.card_accept));
                }else if(validateState == 3){
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_refuse),
                            R.drawable.icon_chat_card_refuse, R.color.card_refuse));
                }
            }


            return convertView ;
        }

        private View setupEventFriendVerifyViews(ChatViewHolder holder){
            View convertView = inflater.inflate(R.layout.listitem_chat_friend_verify, null);
            holder.tvName = (TextView)convertView.findViewById(R.id.tv_chat_name);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
            holder.textView = (TextView) convertView.findViewById(R.id.title);
            holder.event_reject = (Button)convertView.findViewById(R.id.reject);
            holder.event_ok = (Button)convertView.findViewById(R.id.ok);
            holder.event_remark = (TextView)convertView.findViewById(R.id.remark);
            holder.event_layout_end = (LinearLayout)convertView.findViewById(R.id.ll_message);
            holder.ivFileIcon = (ImageView) convertView.findViewById(R.id.iv_accept);
            holder.event_layout_bottom_buttons = (ViewGroup)convertView.findViewById(R.id.layout_bottom_buttons);
            convertView.setTag(holder);
            return convertView;
        }
    }

    private void refuseVerifyRequst(ValidateEntity mEntity) {

        String userName = mEntity.getUserName();
        String fromNickname = mEntity.getFromNickName();
        String from = mEntity.getFrom();
        String fromVatar = mEntity.getFromAvatar();
        String to = mEntity.getTo();
        String reamrk = "";
        String content = "";
        int relationId = 167;
        int validate = DEAL_REFUSE;
        IMNewApi.getInstance().dealRelationValidata(this,userName,fromNickname,from,fromVatar,
                to,reamrk,content,relationId,validate);
    }

    public void updateList(){

        String userName = AppUtil.getInstance(this).getShowId();
        IMNewApi.getInstance().getValidataList(this,userName,20,-1);
    }

}
