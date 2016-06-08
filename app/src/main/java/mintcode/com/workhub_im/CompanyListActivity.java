package mintcode.com.workhub_im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.adapter.CompanyListAdapter;
import mintcode.com.workhub_im.pojo.LoginResponse;

/**
 * Created by mark on 16-6-8.
 */
public class CompanyListActivity extends Activity {


    @BindView(R.id.company_list)
    RecyclerView recyclerView;

    private CompanyListAdapter adapter;
    private List<LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        ButterKnife.bind(this);
        Intent dataIntent = getIntent();
        String listStr = dataIntent.getStringExtra("CompanyList");
        list = JSON.parseArray(listStr, LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean.class);
        adapter = new CompanyListAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
