package com.mintcode.launchr.contact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mintcode.im.database.FriendDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.adapter.FriendAdapter;
import com.mintcode.launchr.contact.fragment.FriendSearchFragment;
import com.mintcode.RelationGroupPOJO;
import com.mintcode.launchr.pojo.FriendLoadPOJO;
import com.mintcode.launchr.pojo.entity.FriendEntity;
import com.mintcode.launchr.pojo.entity.RelationGroupEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/24.
 */
public class MyFriendActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private ImageView mIvBack;
    private ImageView mIvSearch;
    private ImageView mIvAddFriend;
    private ListView mLvMyFriend;

    private FriendSearchFragment mSearFragment;
    private List<FriendEntity> mFriendList;
    private FriendAdapter mPersonAdapter;

    private String mStrUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        initView();
    }


    private void initView() {

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvAddFriend = (ImageView) findViewById(R.id.iv_add_friend);
        mLvMyFriend = (ListView) findViewById(R.id.lv_friend);
        mPersonAdapter = new FriendAdapter(this);
        mLvMyFriend.setAdapter(mPersonAdapter);

        mIvBack.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mIvAddFriend.setOnClickListener(this);

        mLvMyFriend.setOnItemClickListener(this);
//        mLvMyFriend.setOnItemLongClickListener(this);

    }

    private void initData() {
        mStrUserName = AppUtil.getInstance(this).getShowId();
        List<FriendEntity> entities = FriendDBService.getInstance().getFriendList(mStrUserName);
        if (entities != null && entities.size() > 0) {
            mPersonAdapter.setData(entities);
            long time = System.currentTimeMillis();
            IMNewApi.getInstance().loadRelationInfo(this, mStrUserName, time);
        } else {
            showLoading();
            IMNewApi.getInstance().getFriendList(this, mStrUserName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onBackPressed() {
        if (mSearFragment != null) {
            boolean hidden = mSearFragment.isHidden();
            if (hidden) {
                getSupportFragmentManager().beginTransaction().remove(mSearFragment).commitAllowingStateLoss();
                finish();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mSearFragment).commit();
                findViewById(R.id.fl_search_person).setVisibility(View.GONE);
            }
        } else {
            this.finish();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mIvBack) {
            onBackPressed();
        } else if (v == mIvSearch) {
            searchPerson(true);
        } else if (v == mIvAddFriend) {
            searchPerson(false);
        }
    }

    private void searchPerson(boolean type) {
        if (mSearFragment == null) {
            mSearFragment = new FriendSearchFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_search_person, mSearFragment).show(mSearFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(mSearFragment).commit();
        }
        mSearFragment.setSearchType(type);
        findViewById(R.id.fl_search_person).setVisibility(View.VISIBLE);
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (response == null) {
            return;
        }
        if (taskId.equals(IMNewApi.TaskId.TASK_FRIENDS_LIST)) {
            RelationGroupPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), RelationGroupPOJO.class);
            if (pojo == null) {
                return;
            }
            if (pojo.isResultSuccess()) {
                List<RelationGroupEntity> entityList = pojo.getData();
                if (entityList != null && entityList.size() > 0) {
                    List<FriendEntity> entities = entityList.get(0).getRelationList();
                    if (entities != null) {
                        FriendDBService.getInstance().insertFriendList(entities);
                        mPersonAdapter.setData(entities);
                    }
                }
            } else {
                toast(pojo.getMessage());
            }
        } else if (taskId.equals(IMNewApi.TaskId.TASK_LOAD_RELATION_INFO)) {
            FriendLoadPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), FriendLoadPOJO.class);
            if (pojo == null) {
                return;
            }
            if (pojo.isResultSuccess()) {
                FriendLoadPOJO.LoadRelationLData entityList = pojo.getData();
                List<String> removeList = entityList.getRemoveRelations();
                List<FriendEntity> newFriendList = entityList.getRelations();
                removeFriendData(removeList);
                addFriendData(newFriendList);
                List<FriendEntity> entities = FriendDBService.getInstance().getFriendList(mStrUserName);
                if (entities != null && entities.size() > 0) {
                    mPersonAdapter.setData(entities);
                } else {
                    mPersonAdapter.setData(null);
                }
            } else {
                toast(pojo.getMessage());
            }
        }
    }

    private void removeFriendData(List<String> list) {
        if (list != null && list.size() > 0) {
            for (String name : list) {
                FriendDBService.getInstance().delectFriend(mStrUserName, name);
            }
        }
    }

    private void addFriendData(List<FriendEntity> list) {
        List<FriendEntity> entities = FriendDBService.getInstance().getFriendList(mStrUserName);
        Iterator<FriendEntity> it = list.iterator();
        while (it.hasNext()) {
            for (FriendEntity entity : entities) {
                FriendEntity person = it.next();
                if (entity.getRelationName().equals(person.getRelationName())) {
                    list.remove(person);
                } else {
                    String relationName = person.getUserName();
                    person.setRelationName(relationName);
                    person.setUserName(mStrUserName);
                }
            }
        }
        if (list != null && list.size() > 0) {
            FriendDBService.getInstance().insertFriendList(list);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mLvMyFriend) {
            FriendEntity detail = (FriendEntity) mPersonAdapter.getItem(position);
            Intent friend = new Intent(this, FriendDetailActivity.class);
            friend.putExtra(FriendDetailActivity.KEY_PERSONAL_ID, detail);
            startActivity(friend);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return true;
    }
}
