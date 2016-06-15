package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.Entity.FormSingleDataEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class EditTextLinearLayoutView extends EditText {

    private Context mContext;

    private String mMoneyBomal;
    private String mStrTitle;
    private formData mFormData;
    private FormSingleDataEntity mEntity = new FormSingleDataEntity();
    private boolean mBoolFirstEdit = false;

    public EditTextLinearLayoutView(Context context, formData data) {
        super(context);
        mContext = context;
        mFormData = data;
        mStrTitle = mFormData.getPlaceholder();
        mMoneyBomal = mContext.getResources().getString(R.string.money_symbol);
        if (mStrTitle == null || "".equals(mStrTitle)) {
            mStrTitle = mFormData.getLabelText();
        }
        if (mStrTitle.equals(getResources().getString(R.string.accpect_cost))) {
            this.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            this.addTextChangedListener(watcher);
        }
        this.setHint(mStrTitle);
        this.setTextColor(mContext.getResources().getColor(R.color.black));
        this.setMaxEms(120);
        this.setTextSize(15);
        this.setEllipsize(TextUtils.TruncateAt.END);
        this.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        String type = mFormData.getInputType();
        if (FormViewUtil.SINGLE_TEXT_INPUT.equals(type)) {
            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.setLayoutParams(editParams);
            this.setSingleLine();
        } else {
//            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int height = TTDensityUtil.dip2px(mContext, 70);
            int maxHeight = TTDensityUtil.dip2px(mContext, 150);
//            editParams.gravity = Gravity.TOP;
            this.setGravity(Gravity.TOP);
            this.setMinHeight(height);
            this.setMaxHeight(maxHeight);
//            this.setLayoutParams(editParams);
        }
    }


    public EditTextLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getJsonString() {
        String content = this.getText().toString();
        if(mStrTitle.equals(getResources().getString(R.string.accpect_cost))){
            content =  content.substring(1);
            content =  Double.valueOf(content) + "";
        }
        boolean required = mFormData.isRequired();
        if (content != null && !"".equals(content)) {
            mEntity.setKey(mFormData.getKey());
            mEntity.setValue(content);
            return TTJSONUtil.convertObjToJson(mEntity);
        } else if (required) {
            FormViewUtil.BOOL_NO_NULL = false;
            if (mStrTitle.equals(getResources().getString(R.string.accpect_cost))) {
                FormViewUtil.mustInputToast(mContext, mContext.getString(R.string.apply_cost_toast));
            } else if (mBoolFirstEdit) {
                FormViewUtil.mustInputToast(mContext, mContext.getString(R.string.title_empty));
            } else {
                FormViewUtil.mustInputToast(mContext, mContext.getString(R.string.apply_title_toast));
            }
            FormViewUtil.mustInputToast(mContext, mStrTitle);
        }
        return null;
    }

    public boolean ismBoolFirstEdit() {
        return mBoolFirstEdit;
    }

    public void setmBoolFirstEdit(boolean mBoolFirstEdit) {
        this.mBoolFirstEdit = mBoolFirstEdit;
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = EditTextLinearLayoutView.this.getText().toString();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            Editable text = EditTextLinearLayoutView.this.getText();
            EditTextLinearLayoutView.this.setSelection(text.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = EditTextLinearLayoutView.this.getText().toString();

            if (!TextUtils.isEmpty(text) && !text.contains(mMoneyBomal)) {
                EditTextLinearLayoutView.this.setText(mMoneyBomal + text);
            } else if (text.indexOf(mMoneyBomal) > 0) {
                text = text.substring(text.indexOf(mMoneyBomal));
                EditTextLinearLayoutView.this.setText(text);
            }else if(text.contains(mMoneyBomal+".")){
                EditTextLinearLayoutView.this.setText("");
            }
            Editable texts = EditTextLinearLayoutView.this.getText();
            EditTextLinearLayoutView.this.setSelection(texts.length());
        }
    };


}
