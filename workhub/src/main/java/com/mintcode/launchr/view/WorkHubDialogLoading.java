package com.mintcode.launchr.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mintcode.launchr.R;

public class WorkHubDialogLoading extends ProgressDialog {
    private Context context = null;
    private static WorkHubDialogLoading customProgressDialog = null;

    /**
     * 飞机
     */
    private ImageView mIvPlane;

    public WorkHubDialogLoading(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }


    public WorkHubDialogLoading(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 获取loading实例
     *
     * @param context
     * @return
     */
    public static WorkHubDialogLoading creatDialog(Context context) {
        customProgressDialog = new WorkHubDialogLoading(context, R.style.loading);
        return customProgressDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_workhub_loading);
        this.setCanceledOnTouchOutside(false);
        mIvPlane = (ImageView) findViewById(R.id.iv_loading_plane);
    }

    public void start() {
        AnimationDrawable drawable = (AnimationDrawable) mIvPlane
                .getBackground();
        drawable.start();
    }

    Handler handler = new Handler();

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
