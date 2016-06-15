package com.mintcode.launchr.app.newApproval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.ApprovalTypePOJO;
import com.mintcode.launchr.pojo.entity.ApprovalTypeEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class ApprovalTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    private ListView mLvApprovalType;
    private ApprovalTypaAdapter mAdapter;
    private List<ApprovalTypeEntity> mData;

    private LayoutInflater mInflater;

    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_type);

        initView();
    }

    private void initView(){
        mInflater = LayoutInflater.from(ApprovalTypeActivity.this);
        mIvBack = (ImageView) findViewById(R.id.iv_appect_detail_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mData = new ArrayList<ApprovalTypeEntity>();
        mLvApprovalType = (android.widget.ListView) findViewById(R.id.lv_approval_type);
        mAdapter = new ApprovalTypaAdapter();
        mLvApprovalType.setAdapter(mAdapter);
        mLvApprovalType.setOnItemClickListener(this);

        showLoading();
        ApproveAPI.getInstance().getApproveTypeList(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLvApprovalType){
            Intent intent = new Intent();
            intent.putExtra(ApprovalMainActivity.APPROVEL_TYPE, mData.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            return;
        }

        dismissLoading();
         if(taskId.equals(ApproveAPI.TaskId.TASK_URL_GET_APPROVE_TYPE_V2)){//获取审批类型 新接口
            ApprovalTypePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApprovalTypePOJO.class);
            if(pojo == null){
                return;
            }else if(pojo.getBody().getResponse().getData() == null){
                return;
            }else{
                mData = pojo.getBody().getResponse().getData();

                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ApprovalTypaAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_approval_type, null);
                holder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvTypeName.setText(mData.get(position).getT_NAME());
            return convertView;
        }
    }

    private class ViewHolder{
        TextView tvTypeName;
    }
}
