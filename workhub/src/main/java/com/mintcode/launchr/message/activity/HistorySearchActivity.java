package com.mintcode.launchr.message.activity;

/**
 * Created by JulyYu on 2016/4/28.
 */

import com.bumptech.glide.Glide;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.base.BaseActivity;
        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;

        import android.content.ClipData;
        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.text.Editable;
        import android.text.SpannableStringBuilder;
        import android.text.Spanned;
        import android.text.TextWatcher;
        import android.text.style.BackgroundColorSpan;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.AdapterView.OnItemLongClickListener;
        import android.widget.BaseAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.mintcode.chat.activity.ChatActivity;
        import com.mintcode.chat.entity.Info;
        import com.mintcode.chat.entity.User;
        import com.mintcode.chat.user.GroupInfo;
        import com.mintcode.chat.user.GroupInfoDBService;
        import com.mintcode.im.Command;
        import com.mintcode.im.database.MessageDBService;
        import com.mintcode.im.entity.MessageItem;
        import com.mintcode.im.util.JsonUtil;
        import com.mintcode.launchr.R;
        import com.mintcode.launchr.api.UserApi;
        import com.mintcode.launchr.consts.LauchrConst;
        import com.mintcode.launchr.pojo.UserListPOJO;
        import com.mintcode.launchr.pojo.body.HeaderParam;
        import com.mintcode.launchr.pojo.entity.UserDetailEntity;
        import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.view.MTContainerPopWindow;
        import com.mintcode.launchr.view.NoscrollListView;

public class HistorySearchActivity extends BaseActivity implements TextWatcher,
        OnItemClickListener, OnItemLongClickListener{
    private static final int ENTER_SEARCH_RESULT = 1000;

    private EditText mEdtSearch;
    private TextView mTvCancel;
    private NoscrollListView mListView;
    private String mSearchWord;
    private final static int SEARCH_MSG = 0;
    private String loginName;
    private String toLoginName;

    private TextView mTvNoMessage;

    private RelativeLayout mRelBottom;
    private RelativeLayout mRelContent;
    private Context mContext;

    private SearchResultAdapter mAdaperResult;

    private int mHighlightColor;

    /** 联系人*/
    private NoscrollListView mLvFiends;
    private FriendAdapter mFriendAdapter;
    public static List<UserDetailEntity> mFriendData;
    /** 查看更多联系人*/
    private TextView mTvMoreFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search);
        loginName = getIntent().getStringExtra("loginName");
        toLoginName = getIntent().getStringExtra("toLoginName");
        mEdtSearch = (EditText) findViewById(R.id.edt_search);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mListView = (NoscrollListView) findViewById(R.id.listView1);
        mRelContent = (RelativeLayout) findViewById(R.id.rel_chat_search_content);
        mTvNoMessage = (TextView) findViewById(R.id.tv_message_history_search);
        mRelBottom = (RelativeLayout) findViewById(R.id.rel_chat_search_bottom);
        mEdtSearch.addTextChangedListener(this);
        mTvCancel.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        mHighlightColor = getResources().getColor(R.color.highlight_text_color);

        mContext = getBaseContext();

        mAdaperResult = new SearchResultAdapter();
        mListView.setAdapter(mAdaperResult);

        mTvMoreFriends = (TextView)findViewById(R.id.tv_more_friends);
        mTvMoreFriends.setOnClickListener(this);
        /** 搜索得到的联系人*/
        mLvFiends = (NoscrollListView) findViewById(R.id.lv_friend);
        mFriendData = new ArrayList<>();
        mFriendAdapter = new FriendAdapter();
        mLvFiends.setAdapter(mFriendAdapter);
        mLvFiends.setOnItemClickListener(this);

        if(toLoginName!=null && loginName!=null){
            mTvMoreFriends.setVisibility(View.GONE);
            mLvFiends.setVisibility(View.GONE);
        }

        this.setResult(RESULT_OK);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_more_friends:
                Intent intent = new Intent(HistorySearchActivity.this, LookMoreFriendsActivity.class);
                intent.putExtra("key_word", mSearchWord);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private List<MessageItem> mSearchMsg;
    Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (loginName == null || toLoginName == null) {
                mSearchMsg = MessageDBService.getInstance().searchMsg(0, 20, Command.TEXT, mSearchWord);
            } else {
                mSearchMsg = MessageDBService.getInstance().searchMsg(
                        loginName, toLoginName, 0, 20, Command.TEXT, mSearchWord);
            }

            // 全局搜索
            if(loginName==null || toLoginName==null){
                UserApi.getInstance().getSearchUserList(HistorySearchActivity.this, mSearchWord);
            }else{
                // 聊天搜索
                showSearchResult();
            }
        };
    };

    @Override
    public void afterTextChanged(Editable s) {
        mRelContent.setVisibility(View.GONE);
        mSearchWord = mEdtSearch.getText().toString().trim();
        if(mSearchWord.equals("")){
            mHandler.removeMessages(SEARCH_MSG);
            mSearchMsg.clear();
            mListView.setAdapter(new SearchResultAdapter());
            mTvNoMessage.setVisibility(View.GONE);
            mRelContent.setVisibility(View.VISIBLE);

            mFriendData.clear();
            mLvFiends.setAdapter(new FriendAdapter());
            mTvMoreFriends.setVisibility(View.GONE);
            return;
        }

        mHandler.removeMessages(SEARCH_MSG);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SEARCH_MSG);
            }
        }, 500);
    }

    /** 消息记录*/
    private void showSearchResult(){
        if(mSearchMsg.size() <= 0){
            mTvNoMessage.setVisibility(View.VISIBLE);
        }else if(mSearchMsg.size() > 0){
            mTvNoMessage.setVisibility(View.GONE);
        }

        mListView.setAdapter(new SearchResultAdapter());

        hideSoftInput();
    }

    class SearchResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSearchMsg == null ? 0 : mSearchMsg.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            MessageItem messageItem = mSearchMsg.get(position);
            String myInfoStr = messageItem.getInfo();
            Info myInfo = null;
            if(myInfoStr != null){
                myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr,Info.class);
            }
            if (convertView == null) {
                convertView = LayoutInflater.from(HistorySearchActivity.this)
                        .inflate(R.layout.item_chat_search, parent, false);
                holder = new Holder();
                holder.ivAvatar = (ImageView) convertView
                        .findViewById(R.id.message_head_iv);
                holder.tvContent = (TextView) convertView
                        .findViewById(R.id.message_content);
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.message_name);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.message_time);
                holder.ivIsMark = (ImageView) convertView.findViewById(R.id.message_is_mark);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            if (messageItem.getCmd() == MessageItem.TYPE_RECV) {
                holder.tvName.setText(myInfo.getNickName());
            } else {
                HeaderParam mHeaderParam = LauchrConst.getHeader(HistorySearchActivity.this);
                String mUserName = mHeaderParam.getUserName();
                holder.tvName.setText(mUserName);
            }
            if(messageItem.getType().equals(Command.TEXT)){
                //高亮显示
                String rawStr=messageItem.getContent();
                holder.tvContent.setText(getHighlightText(rawStr));
            }
            String time = TimeFormatUtil.getTimeForSearch(messageItem.getClientMsgId(), HistorySearchActivity.this);
            holder.tvTime.setText(time);

            //头像
            if(Command.REV.equals(messageItem.getType())){
                setHeaderImage(holder.ivAvatar, messageItem.getUserName());
            }else if(myInfo != null){
                setHeaderImage(holder.ivAvatar, myInfo.getUserName());
            }else if(MessageItem.TYPE_SEND == messageItem.getCmd()){
                setHeaderImage(holder.ivAvatar, messageItem.getFromLoginName());
            }else if(MessageItem.TYPE_RECV== messageItem.getCmd()){
                setHeaderImage(holder.ivAvatar, messageItem.getToLoginName());
            }

            //是否为重点
            if(messageItem.getIsMarkPoint() == 1){
                holder.ivIsMark.setVisibility(View.VISIBLE);
            } else {
                holder.ivIsMark.setVisibility(View.GONE);
            }
            return convertView;
        }

        class Holder {
            ImageView ivAvatar;
            TextView tvName;
            TextView tvContent;
            TextView tvTime;
            ImageView ivIsMark;
        }
    }

    /** 显示头像*/
    public void setHeaderImage(ImageView mIvAvatar, String mUserId){
        HeadImageUtil.getInstance(mContext).setAvatarResourceAppendUrl(mIvAvatar,mUserId,0,60,60);
    }

    private SpannableStringBuilder getHighlightText(String context){
        SpannableStringBuilder ssb = new SpannableStringBuilder(context);
        for (int index : getAllIndex(context)) {
            ssb.setSpan(new BackgroundColorSpan(mHighlightColor),
                    index,index+mSearchWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    //高亮字符串的索引
    private List<Integer> getAllIndex(String content) {
        List<Integer> list = new ArrayList<Integer>();
        int start=0;
        int index;
        String contentNoCase=content.toLowerCase();
        String serchWordNoCase=mSearchWord.toLowerCase();
        while ((index=contentNoCase.indexOf(serchWordNoCase,start))>-1) {
            start=index+1;
            list.add(index);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if(parent == mListView){
            MessageItem messageItem = mSearchMsg.get(position);
            long msgId = messageItem.getMsgId();
            String from = messageItem.getFromLoginName();
            String to = messageItem.getToLoginName();
            int cmd = messageItem.getCmd();
            String nickName;
            if(messageItem.getFromLoginName().contains("@ChatRoom") || messageItem.getToLoginName().equals("@ChatRoom")){
                if (cmd == MessageItem.TYPE_RECV && messageItem.getInfo()!=null) {
                    Info info = TTJSONUtil.convertJsonToCommonObj(messageItem.getInfo(), Info.class);
                    nickName = info.getSessionName();
                }else{
                    nickName = messageItem.getToNickName();
                }
            }else{
                if (cmd == MessageItem.TYPE_RECV && messageItem.getInfo()!=null) {
                    Info info = TTJSONUtil.convertJsonToCommonObj(messageItem.getInfo(), Info.class);
                    nickName = info.getNickName();
                }else{
                    nickName = messageItem.getToNickName();
                }
            }
            if(nickName == null){
                GroupInfo groupInfo = GroupInfoDBService.getIntance().getGroupInfo(messageItem.getToLoginName());
                if(groupInfo!=null){
                    nickName = groupInfo.getNickName();
                }
            }
            Intent intent = new Intent(this,SearchResultActiviy.class);
            intent.putExtra("nickName", nickName);
            intent.putExtra("from", from);
            intent.putExtra("to", to);
            intent.putExtra("start", msgId);
            startActivityForResult(intent, ENTER_SEARCH_RESULT);
        }else if(parent == mLvFiends){
            User user = new User(mFriendData.get(position).getTrueName(), mFriendData.get(position).getShowId());
            Intent intent = new Intent(HistorySearchActivity.this, ChatActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK != resultCode){
            return;
        }
        if(requestCode == ENTER_SEARCH_RESULT){
            // 刷新
            refreshListView();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        // TODO Auto-generated method stub
        final MessageItem item = mSearchMsg.get(position);
        final MTContainerPopWindow mContainerPopWindow = new MTContainerPopWindow(mContext);
        int rname = item.getIsMarkPoint() == 0 ? R.string.message_mark_point
                : R.string.message_unmark_point;
        if(Command.TEXT.equals(item.getType())){
            mContainerPopWindow.addCopyTextView(getResources().getString(R.string.calendar_copy), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ClipboardManager clipboard = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", item.getContent());
                    clipboard.setPrimaryClip(clip);
                    mContainerPopWindow.dismiss();
                }
            });
        }
        mContainerPopWindow.addTextView(rname, new OnClickListener() {

            @Override
            public void onClick(View v) {
                int markPoint = item.getIsMarkPoint() == 0 ? 1 : 0;
                item.setIsMarkPoint(markPoint);
                item.save();
                mContainerPopWindow.dismiss();

                refreshListView();
            }
        });
        mContainerPopWindow.show(mRelBottom);
        return true;
    }

    // 刷新列表
    public void refreshListView(){
        mSearchMsg.clear();
        if (loginName == null || toLoginName == null) {
            mSearchMsg = MessageDBService.getInstance().searchMsg(0, 20, Command.TEXT, mSearchWord);
        } else {
            mSearchMsg = MessageDBService.getInstance().searchMsg(
                    loginName, toLoginName, 0, 20, Command.TEXT, mSearchWord);
        }
        mListView.setAdapter(new SearchResultAdapter());
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            showSearchResult();
            return;
        }

        if(taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)){
            UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
            if(pojo == null){
                showSearchResult();
                return;
            }else if(pojo.isResultSuccess() == false){
                showSearchResult();
                return;
            }else if(pojo.getBody().getResponse().getData()!=null){
                mFriendData = pojo.getBody().getResponse().getData();

                mLvFiends.setAdapter(new FriendAdapter());

                if(mFriendData.size() > 5){
                    mTvMoreFriends.setVisibility(View.VISIBLE);
                }else{
                    mTvMoreFriends.setVisibility(View.GONE);
                }

                showSearchResult();
            }
        }
    }

    private class FriendAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mFriendData.size()>5 ? 5 : mFriendData.size();
        }

        @Override
        public Object getItem(int position) {
            return mFriendData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView == null){
                holder = new Holder();
                convertView =  LayoutInflater.from(HistorySearchActivity.this).inflate(R.layout.item_chat_search, parent, false);
                holder.ivAvatar = (ImageView) convertView.findViewById(R.id.message_head_iv);
                holder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
                holder.tvName = (TextView) convertView.findViewById(R.id.message_name);
                holder.tvTime = (TextView) convertView.findViewById(R.id.message_time);
                holder.ivIsMark = (ImageView) convertView.findViewById(R.id.message_is_mark);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            UserDetailEntity entity = mFriendData.get(position);
            holder.tvName.setText(getHighlightText(entity.getTrueName()));
            holder.tvContent.setText(entity.getdName());
            setHeaderImage(holder.ivAvatar, entity.getShowId());
            return convertView;
        }

        class Holder {
            ImageView ivAvatar;
            TextView tvName;
            TextView tvContent;
            TextView tvTime;
            ImageView ivIsMark;
        }
    }

    /** 隐藏输入法*/
    private void hideSoftInput(){
        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(mFriendData!=null && mSearchMsg!=null && (mFriendData.size()+mSearchMsg.size() > 3)){
            inputMethodManager.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), 0);
        }
    }

    /** 打开输入法*/
    private void openSoftInput(){

    }
}

