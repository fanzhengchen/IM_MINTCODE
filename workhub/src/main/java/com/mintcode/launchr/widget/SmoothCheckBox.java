package com.mintcode.launchr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;

public class SmoothCheckBox extends RelativeLayout implements OnClickListener {

	private boolean isChecked;
	private View mViewBall;
	private int width;
	private OnCheckChangedListener listener;

	public SmoothCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		mViewBall = new ImageView(context);
		mViewBall.setBackgroundResource(R.drawable.cb_ball);
		setBackgroundResource(R.drawable.cb_not_selected);
		addView(mViewBall);
		setOnClickListener(this);
		width = dp2px(context, 19);
	}

	public boolean isChecked() {
		return isChecked;
	}
	
	public static int dp2px(Context context, double dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		if (!isChecked) {
			setBackgroundResource(R.drawable.cb_not_selected);
		} else {
			scrollTo(-width, 0);
			setBackgroundResource(R.drawable.cb_selected);
		}
	}

	@Override
	public void onClick(View v) {
		mViewBall.post(new SmoothRollRunnable());
	}

	class SmoothRollRunnable implements Runnable {

		private int mDeltaValue;
		private long mStartTime;
		private int duration = 500;
		private Interpolator mInterpolator;

		public SmoothRollRunnable() {
			mInterpolator = new CycleInterpolator(0.25f);
		}

		@Override
		public void run() {
			if (mDeltaValue == 0) {
				if (listener != null) {
//					listener.onMoveStart();
				}
				mDeltaValue++;
				mStartTime = System.currentTimeMillis();
			} else {
				if (listener != null) {
//					listener.onMove();
				}
				setClickable(false);
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
						/ duration;
				mDeltaValue = Math.round(width
						* mInterpolator
								.getInterpolation(normalizedTime / 1000f));
				scrollTo(isChecked ? mDeltaValue - width : -mDeltaValue, 0);
				if (mDeltaValue > width / 2) {
					if (isChecked) {
						setBackgroundResource(R.drawable.cb_not_selected);
					} else {
						setBackgroundResource(R.drawable.cb_selected);
					}
				}
			}
			if (mDeltaValue < width) {
				mViewBall.postDelayed(this, 10);
			} else {
				if (listener != null) {
//					listener.onMoveEnd();
					listener.oncheckChange(SmoothCheckBox.this,!isChecked);
				}
				isChecked = !isChecked;
				setClickable(true);
			}
		}
	}

	public interface OnCheckChangedListener {
		void oncheckChange(View v,boolean b);
	}

	public void setOnCheckChangedListener(OnCheckChangedListener listener) {
		this.listener = listener;
	}

}
