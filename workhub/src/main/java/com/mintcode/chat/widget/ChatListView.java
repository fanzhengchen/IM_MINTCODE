package com.mintcode.chat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;



public class ChatListView extends PullToRefreshListView {
	
	private boolean scrollToBottom = true;
	

	public boolean isScrollToBottom() {
		return scrollToBottom;
	}

	public void setScrollToBottom(boolean scrollToBottom) {
		this.scrollToBottom = scrollToBottom;
	}

	public static interface OnActionDownListener {
		void onActionDown(ChatListView view);
	}

	private OnActionDownListener mOnActionDownListener;

	public ChatListView(Context context) {
		super(context);
		init();
	}

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnScrollListener(mOnScrollListener);
	}

	public void scrollToBottom(boolean smooth) {
		ListView listView = getRefreshableView();
		// listView.setSelection(listView.getAdapter().getCount()-1);
		if (smooth) {
			listView.smoothScrollToPosition(listView.getCount() - 1);
		} else {
			if (listView.getAdapter() != null) {
				listView.setSelection(listView.getAdapter().getCount());
			}
		}
	}

	// 得到可见界面的最后一条view的位置
	public int getlastVisiblePosition() {

		ListView listView = getRefreshableView();
		return listView.getLastVisiblePosition();
	}

	public int getFirstVisiblePosition(){
		ListView listView = getRefreshableView();
		return listView.getFirstVisiblePosition();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (mOnActionDownListener != null) {
				mOnActionDownListener.onActionDown(this);
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void setOnActionDownListener(OnActionDownListener l) {
		mOnActionDownListener = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (!scrollToBottom) {
			return;
		}
		postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollToBottom(false);
			}
		}, 20);
		
	}

	OnScrollListener mOnScrollListener = new OnScrollListener() {
		int status = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if(visibleItemCount+firstVisibleItem >= totalItemCount - 0){
				Log.d("robintmp", "scroll-bottom-down");
				if(status != AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL){
					Log.d("robintmp", "change to mode always scroll");
					ChatListView.this.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					status = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL;
				}
			}else{
				if(status != AbsListView.TRANSCRIPT_MODE_NORMAL){
					Log.d("robintmp", "change to mode TRANSCRIPT_MODE_NORMAL");
					ChatListView.this.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
					status = AbsListView.TRANSCRIPT_MODE_NORMAL;
				}
			}
        }
    };
}
