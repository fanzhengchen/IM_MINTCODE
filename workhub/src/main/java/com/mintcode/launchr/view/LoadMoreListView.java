package com.mintcode.launchr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class LoadMoreListView extends ListView implements OnScrollListener{

	private OnScrollListener mOnScrollListener;
	private OnLoadMoreListener mOnLoadMoreListener;
	
	private int mCurrentScrollState;
	private boolean mNoMore = false;
	private boolean mIsLoadingMore = false;
	
	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(this);
	}
	
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 */
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}

	private boolean isLastItemAboveBottom() {
		int lastViewBottom = getChildAt(getChildCount() - 1).getBottom();
		int listEnd = getHeight() - getPaddingBottom();
		return lastViewBottom <= listEnd;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

		if (mOnLoadMoreListener != null && !mNoMore) {
			boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount) || (visibleItemCount == totalItemCount);
			if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE && isLastItemAboveBottom()) {
				onLoadMore();
			}
		}
		
	}
	
	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			setStateLoading();
			mOnLoadMoreListener.onLoadMore();
		}
	}
	
	public void setStateLoading() {
		mIsLoadingMore = true;
		mNoMore = false;
	}
	
	public void setStateLoadComplete() {
		mIsLoadingMore = false;
	}
	
	public void setStateNoMoreData() {
		mIsLoadingMore = false;
		mNoMore = true;
	}
}
