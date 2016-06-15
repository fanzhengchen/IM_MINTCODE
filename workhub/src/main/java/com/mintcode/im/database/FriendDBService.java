package com.mintcode.im.database;

import android.content.ContentValues;
import android.text.TextUtils;

import com.mintcode.launchr.pojo.entity.FriendEntity;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Objects;

/**
 * Created by JulyYu on 2016/4/5.
 */
public class FriendDBService {

    private static FriendDBService sInstance;

    public synchronized static FriendDBService getInstance(){
        if (sInstance == null) {
            sInstance = new FriendDBService();
        }
        return sInstance;
    }
    /** 获取好友*/
    public List<FriendEntity> getFriendList(String userName){
        List<FriendEntity> result = DataSupport.where("userName = ? ",userName).find(FriendEntity.class);
        return result;
    }

    public String getFriendAvtarUrl(String relationName){
        List<FriendEntity> result = DataSupport.where("relationName = ? ",relationName).find(FriendEntity.class);
        if(result != null && result.size() > 0){
            return result.get(0).getRelationAvatar();
        }
        return "";
    }
    public FriendEntity getFriendInfo(String relationName){
        List<FriendEntity> result = DataSupport.where("relationName = ? ",relationName).find(FriendEntity.class);
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        return null;
    }
    /** 插入好友列表*/
    public void insertFriendList(List<FriendEntity> entities){
        if(entities != null && !entities.isEmpty()){
            DataSupport.saveAll(entities);
        }
    }
    /** 更新好友的备注*/
    public void updateFriendRemark(String userName,String relationName,String remark){
        String whereSql = "(userName = '" + userName
                + "' and relationName = '" + relationName + "') ";
        ContentValues values = new ContentValues();
        values.put("remark", remark);
        DataSupport.updateAll(FriendEntity.class, values, whereSql);
    }
    /** 删除好友*/
    public void delectFriend(String userName,String relationName){
        String whereSql = "(userName = '" + userName
                + "' and relationName = '" + relationName + "')";
        DataSupport.deleteAll(FriendEntity.class,whereSql);
    }

    /** 增加一个好友*/
    public void insertFriend(FriendEntity entity){
        entity.save();
        entity.saveThrows();
    }
}
