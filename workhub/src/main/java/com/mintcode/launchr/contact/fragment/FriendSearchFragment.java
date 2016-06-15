package com.mintcode.launchr.contact.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.PersonPOJO;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.contact.adapter.FriendAdapter;
import com.mintcode.launchr.contact.adapter.PersonAdapter;
import com.mintcode.launchr.pojo.UserIsCompanyPOJO;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.pojo.response.UserIsCompanyResponse;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;


/**
 * Created by JulyYu on 2016/3/24.
 */
public class FriendSearchFragment extends BaseFragment implements TextWatcher,TextView.OnEditorActionListener{

    private View mView;

    private LinearLayout mLlSearchResult;
    private EditText mEdtSearch;
    private TextView mTvCanel;
    private ListView mLvPerson;
    private PersonAdapter mPersoAdapter;

    private SearchHandler mHandler = new SearchHandler();
    private final static int SEARCH_MSG = 0;
    private String mStrSearchKey;

    private List<PersonEntity> mPersonList;

    //手机号搜索
    private LinearLayout mLlSearchNumber;
    private ImageView mIvBack;
    private EditText mEdtNumbser;
    /** true为模糊搜索，false 为精确搜索及手机搜索*/
    private boolean mBoolType = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_friend_search,null);
        initView();

        return mView;
    }


    private void initView(){

        mLlSearchResult = (LinearLayout)mView.findViewById(R.id.ll_search_result);
        mEdtSearch = (EditText)mView.findViewById(R.id.edt_search);
        mTvCanel = (TextView)mView.findViewById(R.id.tv_cancel);
        mLvPerson = (ListView)mView.findViewById(R.id.lv_serach_person);

        mLlSearchNumber = (LinearLayout)mView.findViewById(R.id.ll_add_friend);
        mIvBack = (ImageView)mView.findViewById(R.id.iv_back);
        mEdtNumbser = (EditText)mView.findViewById(R.id.edt_search_mobile);

        mEdtSearch.addTextChangedListener(this);
        mTvCanel.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mEdtNumbser.setOnEditorActionListener(this);
        mEdtNumbser.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mPersoAdapter = new PersonAdapter(getActivity());
        mLvPerson.setAdapter(mPersoAdapter);
        if(mBoolType){ //搜素
            mLlSearchNumber.setVisibility(View.GONE);
            mLlSearchResult.setVisibility(View.VISIBLE);
        }else{ // 手机号搜索
            mLlSearchNumber.setVisibility(View.VISIBLE);
            mLlSearchResult.setVisibility(View.GONE);
        }

    }


    public void setSearchType(boolean  type){
        mBoolType = type;
        if(mLlSearchNumber != null && mLlSearchResult != null){
            if(mBoolType){ //搜素
                mLlSearchNumber.setVisibility(View.GONE);
                mLlSearchResult.setVisibility(View.VISIBLE);
                mEdtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            }else{ // 手机号搜索
                mLlSearchNumber.setVisibility(View.VISIBLE);
                mLlSearchResult.setVisibility(View.GONE);
                mEdtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(mEdtSearch != null){
                mEdtSearch.setText("");
            }
            if(mEdtNumbser != null){
                mEdtNumbser.setText("");
            }
            if(mPersoAdapter != null){
                mPersoAdapter.setData(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mTvCanel || v == mIvBack){
            Activity activity = getActivity();
            if(activity != null){
                activity.onBackPressed();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mStrSearchKey = mEdtSearch.getText().toString().trim();
        mHandler.removeMessages(SEARCH_MSG);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(SEARCH_MSG);

            }
        }, 500);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            mLlSearchNumber.setVisibility(View.GONE);
            mLlSearchResult.setVisibility(View.VISIBLE);
            String key = mEdtNumbser.getText().toString();
            mEdtSearch.setText(key);
            return true;
        }
        return false;
    }

    class SearchHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!"".equals(mStrSearchKey)){
                showLoading();
                String userName = AppUtil.getInstance(getActivity()).getShowId();
                String userToken = AppUtil.getInstance(getActivity()).getIMToken();
                if(mBoolType){
                    IMNewApi.getInstance().fuzzySearchUser(SearchResponse,userName,mStrSearchKey,100);
                }else{
                    IMNewApi.getInstance().searchUser(SearchResponse,userName,mStrSearchKey);
                }
            }
        }
    }



    private OnResponseListener SearchResponse = new  OnResponseListener(){

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            dismissLoading();
            if (response == null){
                return;
            }
            if(taskId.equals(IMNewApi.TaskId.TASK_SEARCH_USER)){
                PersonPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),PersonPOJO.class);
                    if(pojo != null){
                        if(pojo.isResultSuccess()){
                            mPersonList = pojo.getData();
                            if(mPersonList != null && mPersonList.size() > 0){
                                String showId = mPersonList.get(0).getUserName();
                                UserApi.getInstance().getUserIsComanyUser(this, showId);
                            }
                        }
                    }
            }else if(taskId.equals(IMNewApi.TaskId.TASK_FUZZY_SEARCH_USER)){
                PersonPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),PersonPOJO.class);
                if(pojo != null){
                    if(pojo.isResultSuccess()){
                        mPersonList = pojo.getData();
                        if(mPersonList != null){
                            mPersoAdapter.setData(mPersonList,true);
                        }
                    }
                }
            }else if(taskId.equals(UserApi.TaskId.TASK_URL_GET_USER_ISCOMPANY)){
                UserIsCompanyPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),UserIsCompanyPOJO.class);
                 if(pojo != null){
                     if(pojo.isResultSuccess()){
                         UserIsCompanyResponse.ComPanyPersonData  data = pojo.getBody().getResponse().getData();
                         if(data != null){
                             boolean boolIsCompany = data.isExists();
                             if(boolIsCompany){
                                 mPersoAdapter.setData(mPersonList,true);
                             }else{
                                 mPersoAdapter.setData(mPersonList,false);
                             }
                         }
                     }
                 }
            }
        }
        @Override
        public boolean isDisable() {
            return false;
        }
    };

}
