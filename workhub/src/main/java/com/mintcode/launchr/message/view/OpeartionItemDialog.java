package com.mintcode.launchr.message.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.mintcode.launchr.R;

/**
 * Created by StephenHe on 2016/1/20.
 */
public class OpeartionItemDialog extends Dialog{
    private Context mContext;

    public OpeartionItemDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public OpeartionItemDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    protected OpeartionItemDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_opeartion_item);
    }
}
