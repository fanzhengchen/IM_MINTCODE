package com.mintcode.im.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.mintcode.launchr.pojo.entity.ContactUser;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by KevinQiao on 2016/3/5.
 */
public class ContactUserDB {

    private static ContactUserDB instance = new ContactUserDB();

    public static ContactUserDB getInstacne(){
        return instance;
    }


    public void insert(ContactUser user){
        SQLiteDatabase db = Connector.getDatabase();
        if(!exist(user)){
            user.save();
        }
    }

    public boolean exist(ContactUser user){
        List<ContactUser> list = DataSupport.where("userId = ?",user.getUserId()).find(ContactUser.class);
        return list.size() == 0 ? false : true;
    }


    public void updataTime(ContactUser user){
        updataTime(user.getId(), user.getUpdateTime());
    }

    public void updataTime(int id,String time){
        ContentValues cv = new ContentValues();
        cv.put("updateTime",time);
        DataSupport.update(ContactUser.class, cv, id);
    }

    public void updataTime(String userId,String time){
        ContentValues cv = new ContentValues();
        cv.put("updateTime",time);
        DataSupport.updateAll(ContactUser.class, cv, "userId = ?" , userId);
    }

    public List<ContactUser> getContactUserList(String departId){
        List<ContactUser> list = DataSupport.where("departId = ?",departId).find(ContactUser.class);
        return  list;
    }

}
