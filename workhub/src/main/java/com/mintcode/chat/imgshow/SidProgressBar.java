package com.mintcode.chat.imgshow;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.RM;
import com.mintcode.launchr.R;

/**
 * Created by sid'pc on 2015/7/9.
 */
public class SidProgressBar extends LinearLayout implements View.OnTouchListener {

    private ButtonListener mListener;

    private View mStick;
    private TextView mTvRecord;
    private float mWidth;
    private float mBlankWidth;
    private boolean mIsRecording = false;

    public SidProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View content = LayoutInflater.from(context).inflate(R.layout.view_myprogressbar, this);
        mStick = content.findViewById(R.id.stick);
        mTvRecord = (TextView) content.findViewById(R.id.tv_record);
        mTvRecord.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        Log.d("SidProgressBar", "onTouch__>action=" + action);
        if (v == mTvRecord) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mIsRecording = true;
                    if (mListener != null) {
                        mListener.onStartRecode();
                    }
                    mStick.post(new SmoothShortRunnable());
                    break;
                case MotionEvent.ACTION_UP:
                    mIsRecording = false;
                    break;
            }
            return true;
        }
        return false;
    }


    class SmoothShortRunnable implements Runnable {

        private int mDuration = 10000;
        private long mStartTime;
        private Interpolator mInterpolator;

        public SmoothShortRunnable() {
            mInterpolator = new CycleInterpolator(0.25f);
        }

        @Override
        public void run() {
            if (mBlankWidth == 0) {
                mBlankWidth++;
                mStartTime = System.currentTimeMillis();
            } else {
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
                        / mDuration;
                if (normalizedTime > 1000) {
                	mListener.onEndRecord();
					return;
				}
                float interpolation = mInterpolator
                        .getInterpolation(normalizedTime / 1000f);
                mBlankWidth = mWidth * interpolation;
                ViewGroup.LayoutParams params = mStick.getLayoutParams();
                params.width = (int) (mWidth - mBlankWidth);
                mStick.setLayoutParams(params);
            }
            if (mIsRecording && mBlankWidth < mWidth) {
                postDelayed(this, 10);
            } else if (mListener != null) {
                mListener.onEndRecord();
            }
        }
    }


    public interface ButtonListener {
        void onStartRecode();

        void onEndRecord();
    }

    public void setButtonListener(ButtonListener listener) {
        mListener = listener;
    }
}
