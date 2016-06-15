package com.mintcode.launchr.app.newApproval.activity;

/**
 * Created by JulyYu on 2016/4/13.
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.FormSelectItemAdapter;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;
import com.mintcode.launchr.pojo.entity.formData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 单选和多选表单界面*/
public class CheckBoxFormActivity  extends BaseActivity implements AdapterView.OnItemClickListener{

    private TextView mTvTitle;
    private ImageView mIvBack;
    private TextView mTvConfirm;
    private ListView mLvSelect;

    private FormSelectItemAdapter mAdapter;
    private String mStrType = "";
    private String mStrTitle = "";
    private formData mFormData;
    private List<FormCheckBoxEntity> mSelectedCheckBoxs;
    public static final String SELECT_TYPE ="select_type";
    public static final String SELECTED_DATA ="selected_data";
    public static final String SELECT_RESULT ="select_result";

    /** 多选和单选切换 默认单选*/
    private  boolean MULTI_SELECT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_checkbox);
        initData();
        initView();
    }
    private void initData(){

        mSelectedCheckBoxs = new ArrayList<>();
        mFormData = (formData)getIntent().getSerializableExtra(SELECT_TYPE);

        if(mFormData != null){ //获取选择数据和已选数据
            List<FormCheckBoxEntity> AllSelectData = mFormData.getData();
            mAdapter = new FormSelectItemAdapter(this,AllSelectData);
            mStrType = mFormData.getInputType();
            mStrTitle = mFormData.getLabelText();
            List<FormCheckBoxEntity> selectedData = ( List<FormCheckBoxEntity>)getIntent().getSerializableExtra(SELECTED_DATA);
            if(selectedData != null){
                mSelectedCheckBoxs.clear();
                for(FormCheckBoxEntity data :AllSelectData){
                    for(FormCheckBoxEntity selected:selectedData){
                        if(data.getKey().equals(selected.getKey())){
                            mSelectedCheckBoxs.add(data);
                        }
                    }
                }
                mAdapter.setSelectedData(mSelectedCheckBoxs);
            }
        }
    }
    private void initView(){
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack  = (ImageView) findViewById(R.id.iv_back);
        mLvSelect =(ListView) findViewById(R.id.lv_select);
        mTvConfirm = (TextView)findViewById(R.id.tv_ok);

        if(mStrType.equals(FormViewUtil.SINGLE_SELECT_INPUT)){ //单选模式
            mTvConfirm.setVisibility(View.GONE);
            MULTI_SELECT = false;
        }else if(mStrType.equals(FormViewUtil.MUTIL_SELECT_INPUT)){//多选模式
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirm.setOnClickListener(this);
            MULTI_SELECT = true;
        }
        mTvTitle.setText(mStrTitle);
        mLvSelect.setAdapter(mAdapter);
        mLvSelect.setOnItemClickListener(this);
        mIvBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            setResult(RESULT_CANCELED);
            finish();
        }else if(v == mTvConfirm){
            Intent result = new Intent();
            result.putExtra(SELECT_RESULT,(Serializable)mSelectedCheckBoxs);
            setResult(RESULT_OK,result);
            this.finish();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        FormCheckBoxEntity entity = (FormCheckBoxEntity)mAdapter.getItem(position);
        if(MULTI_SELECT){
            if(mSelectedCheckBoxs.contains(entity)){
                mSelectedCheckBoxs.remove(entity);
            }else{
                mSelectedCheckBoxs.add(entity);
            }
            mAdapter.setSelectedData(mSelectedCheckBoxs);
        }else{
            mSelectedCheckBoxs.clear();
            mSelectedCheckBoxs.add(entity);
            Intent result = new Intent();
            result.putExtra(SELECT_RESULT,(Serializable)mSelectedCheckBoxs);
            setResult(RESULT_OK,result);
            this.finish();
        }
    }
}
