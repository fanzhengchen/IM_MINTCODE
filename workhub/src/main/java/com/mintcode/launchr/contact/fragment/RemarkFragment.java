package com.mintcode.launchr.contact.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.contact.activity.FriendVerifyActivity;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class RemarkFragment  extends BaseFragment{

    private View mView;
    private EditText mEdtRemark;
    private TextView mTvSave;
    private ImageView mIvBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_remark,null);
        initView();
        return mView;
    }

    private void initView(){

        mEdtRemark = (EditText)mView.findViewById(R.id.edt_remark);
        mIvBack = (ImageView)mView.findViewById(R.id.iv_back);
        mTvSave = (TextView)mView.findViewById(R.id.tv_save);

        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            getActivity().onBackPressed();
        }else if(v == mTvSave){
            ((FriendVerifyActivity)getActivity()).getRemark();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mEdtRemark != null){
            mEdtRemark.setText("");
        }
    }

    public String getRemarkText() {
        String remarkText = mEdtRemark.getText().toString();
        return remarkText;
    }

    public void setRemarkText(String remark) {
        if(mEdtRemark != null){
            mEdtRemark.setText(remark);
        }
    }
}
