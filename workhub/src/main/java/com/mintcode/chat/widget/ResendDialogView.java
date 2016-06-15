package com.mintcode.chat.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;

public class ResendDialogView extends PopupWindow {
    private TextView mTvCancel;

    private TextView mTvOk;

    private TextView mTvTitle;

    private TextView mTvContent;

    private View mViewContent;
    private Context context;

    public ResendDialogView(Context context, OnClickListener listener) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mViewContent = inflater.inflate(R.layout.dialog_confirm, null);
        mTvCancel = (TextView) mViewContent.findViewById(R.id.tv_cancel);
        mTvOk = (TextView) mViewContent.findViewById(R.id.tv_ok);
        mTvTitle = (TextView) mViewContent.findViewById(R.id.tv_title);
        mTvContent = (TextView) mViewContent.findViewById(R.id.tv_content);
        mTvCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvTitle.setVisibility(View.GONE);
        mTvOk.setOnClickListener(listener);
        mTvContent.setTextSize(16);
        mTvContent.setText("重新发送消息？");

        this.setContentView(mViewContent);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        mViewContent.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                View view = mViewContent.findViewById(R.id.pop_layout);
                int top = view.getTop();
                int bottom = view.getBottom();
                int left = view.getLeft();
                int right = view.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top || y > bottom || x < left || x > right) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
