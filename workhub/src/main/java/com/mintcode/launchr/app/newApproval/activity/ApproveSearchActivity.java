package com.mintcode.launchr.app.newApproval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.app.newApproval.ApproveSearchAdapter;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.ApproveListPOJO;
import com.mintcode.launchr.pojo.ApproveSearchPOJO;
import com.mintcode.launchr.pojo.entity.ApproveEntity;
import com.mintcode.launchr.pojo.response.ApproveSearchResponse;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/19.
 */
public class ApproveSearchActivity extends BaseActivity implements
        TextWatcher, ExpandableListView.OnChildClickListener {

    private EditText mEdtSearch;
    private ExpandableListView mListView;
    private ApproveSearchAdapter mAdapter;

    private SearchHandler mHandler = new SearchHandler();
    private final static int SEARCH_MSG = 0;
    private String mSearchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_search);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEdtSearch = (EditText) findViewById(R.id.edt_search);
        mListView = (ExpandableListView) findViewById(R.id.lv_person);
        mEdtSearch.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mSearchWord = mEdtSearch.getText().toString().trim();
        mHandler.removeMessages(SEARCH_MSG);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(SEARCH_MSG);

            }
        }, 500);


    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (taskId.equals(ApproveAPI.TaskId.TASK_URL_APPROVE_LIST_V2)) {
            ApproveListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApproveListPOJO.class);
            ApproveListHandlePOJO(pojo);

        }
    }

    private void ApproveListHandlePOJO(ApproveListPOJO pojo) {
        if(pojo == null){
            return;
        }
        if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }
        if(pojo.getBody().getResponse().getData() == null){
            return;
        }
        List<ApproveEntity> approveEntityList = pojo.getBody().getResponse().getData();
        if(approveEntityList != null){
            if (mAdapter == null) {
                mAdapter = new ApproveSearchAdapter(this, approveEntityList, mSearchWord);
                mListView.setAdapter(mAdapter);
                mListView.expandGroup(0);
                mListView.expandGroup(1);
                mListView.setGroupIndicator(null);
            } else {
                mAdapter.updateData(approveEntityList, mSearchWord);
            }
            mListView.setOnChildClickListener(this);
        }
    }

    class SearchHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!"".equals(mSearchWord)){
                showLoading();
                ApproveAPI.getInstance().getApproveListV2(ApproveSearchActivity.this, "", -1, 200, System.currentTimeMillis());

            }else{
//                mAdapter.clearDate();
//                mAdapter.updateData(mSearchWord);
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Log.i("onChildClick(ExpandableListView " , groupPosition + "---" + childPosition);
        Intent intent = new Intent(this, ApproveDetailActivity.class);
        ApproveEntity approveEntity = (ApproveEntity)mAdapter.getChild(groupPosition, childPosition);
        intent.putExtra(ApproveDetailActivity.APPLY_DETAIL,approveEntity.getSHOW_ID());
        startActivityForResult(intent,2);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == 2){
            mHandler.sendEmptyMessage(SEARCH_MSG);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
