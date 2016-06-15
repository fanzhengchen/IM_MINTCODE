package com.mintcode.launchr.message.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mintcode.chat.ChatListAdapter;
import com.mintcode.chat.audio.AudioPlayingAnimation;
import com.mintcode.chat.util.AudioRecordPlayUtil;
import com.mintcode.chat.widget.ChatListView;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

public class SearchResultActiviy extends BaseActivity implements
		OnLoadMoreListener, OnRefreshListener<ListView> {
	
	private ImageView mIvBack;
	private TextView mTvName;
	private ChatListView mListView;
	private List<MessageItem> mListData;
	private ChatListAdapter mChatListAdapter;
	
	
	private String mFrom;
	private String mTo;
	private String mNickName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		mIvBack = (ImageView) findViewById(R.id.img_back);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		mNickName = getIntent().getStringExtra("nickName");
		mFrom = getIntent().getStringExtra("from");
		mTo = getIntent().getStringExtra("to");
		id = getIntent().getLongExtra("start", 0);
		mTvName.setText(mNickName);
		
		initListView();
	}

	
	private void initListView() {
		AudioRecordPlayUtil playUtil = new AudioRecordPlayUtil(mOnAudioInfoListener, this, null);
		mListView = (ChatListView) findViewById(R.id.chat_list);
		mListData = MessageDBService.getInstance().getHistoryMsgList(mFrom, mTo, id, 0);
		mChatListAdapter = new ChatListAdapter(this, mListData, playUtil, null, false, null, mTo);
		mListView.setAdapter(mChatListAdapter);
		mListView.setScrollToBottom(false);
		mListView.getRefreshableView().setSelection(0);

		if (countNext > mListData.size()) {
			mListView.setStateNoMoreData();
		}

		setResult(RESULT_OK);

		mListView.setMode(PullToRefreshBase.Mode.BOTH);
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			// 下拉
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				final List<MessageItem> list = MessageDBService.getInstance().getLastTenMsgList(mFrom, mTo, id, countLast);
				Log.e("--onPullDownToRefresh--", list.size() + "");
				countLast = countLast + STEP;
				if (list.size() <= 0) {
					mListView.onRefreshComplete();
					mListView.hideHeaderView();
					return;
				}

				mListData.addAll(0, list);
				mListTopSelectionIndex = list.size();
				mChatListAdapter.notifyDataSetChanged();

				mListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mListView.onRefreshComplete();
						mListView.getRefreshableView().setSelection(list.size()-1);
						if (list.size() < STEP) {
							mListView.hideHeaderView();
						}
					}
				}, 000);
			}

			// 上拉
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				long msgId = mListData.get(mListData.size()-1).getMsgId();
				final List<MessageItem> list = MessageDBService.getInstance().getNextTenMsgList(mFrom, mTo, msgId, 0);
				Log.e("--onPullUpToRefresh--", list.size() + "");
				countNext = countNext + STEP;
				if(list.size() <= 1){
					mListView.onRefreshComplete();
					mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
					return;
				}

				list.remove(0);

				mListData.addAll(mListData.size(), list);
				mChatListAdapter.notifyDataSetChanged();

				mListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mListView.onRefreshComplete();
						mListView.getRefreshableView().setSelection(mListData.size()-list.size()-1);
						if (list.size() < STEP-1) {
							mListView.hideHeaderView();
						}
					}
				}, 000);
			}
		});
	}
	private AudioRecordPlayUtil.OnInfoListener mOnAudioInfoListener = new AudioRecordPlayUtil.OnInfoListener() {

		@Override
		public void onVolume(int volume) {
		}

		@Override
		public void onMaxFileSize() {
		}

		@Override
		public void onMaxDuration() {
		}

		@Override
		public void onPlayCompletion() {
			AudioPlayingAnimation.stop();
		}
	};


	private long id = 0;
	private int countNext = 10;
	private int countLast = 0;
	private final static int STEP = 10;
	private int mListTopSelectionIndex = 0;
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onLoadMore() {
		List<MessageItem> list = MessageDBService.getInstance().getNextTenMsgList(mFrom, mTo, id, countNext);
		mListData.addAll(mListData.size(), list);
		mChatListAdapter.notifyDataSetChanged();
		countNext = countNext + STEP;
		if (list.size() < STEP) {
			mListView.setStateNoMoreData();
		}
	}
}
