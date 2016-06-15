package com.mintcode.launchr.app.meeting.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mintcode.launchr.R;

/**
 * Created by StephenHe on 2015/12/24.
 */
public class HandleMeetingPopWindow extends PopupWindow {

    private View mContentView;

    public HandleMeetingPopWindow(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.popwindow_handle_meeting,null);
        setWindow();
    }

    private void setWindow() {
        // 设置PopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

}