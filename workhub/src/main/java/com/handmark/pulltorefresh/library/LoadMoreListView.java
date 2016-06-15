package com.handmark.pulltorefresh.library;

import com.mintcode.RM;
import com.mintcode.launchr.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LoadMoreListView extends ListView implements OnScrollListener {

	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;
	// footer view
	private FrameLayout mFooterView;
	private ProgressBar mProgressBarLoadMore;
	private TextView mTextView;
	
	private OnLoadMoreListener mOnLoadMoreListener;
	private boolean mIsLoadingMore = false;
	private boolean mNoMore = false;
	private int mCurrentScrollState;

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// footer
		mFooterView = (FrameLayout) mInflater.inflate(R.layout.listfooter_load_more,null);
		mProgressBarLoadMore = (ProgressBar) mFooterView
				.findViewById(R.id.load_more_progressBar);
		mTextView = (TextView) mFooterView.findViewById(R.id.load_more_text);
		addFooterView(mFooterView, null, false);

		super.setOnScrollListener(this);
	}
	
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}
	
	public boolean isLastItemAboveBottom() {
		int lastViewBottom = getChildAt(getChildCount() - 1).getBottom();
		int listEnd = getHeight() - getPaddingBottom();
		return lastViewBottom <= listEnd;
	}
	
	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			setStateLoading();
			mOnLoadMoreListener.onLoadMore();
		}
	}
	
	public void setStateLoading() {
		mProgressBarLoadMore.setVisibility(View.VISIBLE);
		mTextView.setText("正在加载");
		mIsLoadingMore = true;
		mNoMore = false;
	}
	
	public void setStateOriginal() {
		mIsLoadingMore = false;
		mTextView.setText("");
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	public void setStateNoMoreData() {
		mIsLoadingMore = false;
		mNoMore = true;
		mTextView.setText("已加载完");
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

}
