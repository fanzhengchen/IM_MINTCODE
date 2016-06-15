package com.mintcode.im.database;

import android.database.Cursor;

import com.mintcode.launchr.pojo.entity.DepartmentEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StephenHe on 2016/3/21.
 */
public class ContactDBService {
    private static ContactDBService sInstance;

    public synchronized static ContactDBService getInstance(){
        if (sInstance == null) {
            sInstance = new ContactDBService();
        }
        return sInstance;
    }

    /** 查询根部门*/
    public List<DepartmentEntity> getParentDepartment(){
        List<DepartmentEntity> result = DataSupport.where("dLevel = ?", "1")
                .find(DepartmentEntity.class);
        return result;
    }

    /** 查询子部门*/
    public List<DepartmentEntity> getSubDepartment(String parentShowId){
        List<DepartmentEntity> result = DataSupport.where("dParentShowId = ?", parentShowId)
                .find(DepartmentEntity.class);
        return result;
    }

    /** 查询用户列表*/
    public List<UserDetailEntity> getUserList(String showId){
        List<UserDetailEntity> result = DataSupport.where("deptId = ?", showId).find(UserDetailEntity.class);
        return result;
    }

    /** 更新的时候该部门删除后重新保存*/
    public void deleteParentDepartment(List<DepartmentEntity> list){
        DataSupport.deleteAll(DepartmentEntity.class, "dLevel=?", "1");

        DataSupport.saveAll(list);
    }

    /** 更新的时候该部门删除后重新保存*/
    public void deleteSubDepartment(List<DepartmentEntity> list, String parentShowId){
        DataSupport.deleteAll(DepartmentEntity.class, "dParentShowId=?", parentShowId);

        DataSupport.saveAll(list);
    }

    /** 更新的时候该用户列表删除后重新保存*/
    public void deleteUserList(List<UserDetailEntity> list, String showId){
        DataSupport.deleteAll(UserDetailEntity.class, "deptId=?", showId);

        DataSupport.saveAll(list);
    }

    /** 查询某个人的个人详情*/
    public UserDetailEntity getPersonDetail(String personId){
        List<UserDetailEntity> lists = DataSupport.where("showId=?", personId).find(UserDetailEntity.class);

        if(lists!=null && lists.size()>0){
            return lists.get(0);
        }else{
            return null;
        }
    }

    public  List<UserDetailEntity> searchUserList(List<String> personList){
        String select = "( ";
        for(String str : personList){
            select += ("'"+str + "',");
        }
        select = select.substring(0,select.length()-1);
        select += " )";
        List<UserDetailEntity> lists = new ArrayList<>();
//        lists = DataSupport.where("showId in ",select).find(UserDetailEntity.class);
        Cursor cursor = DataSupport.findBySQL("select * from UserDetailEntity where showId in " + select);
        while(cursor.moveToNext()){
            UserDetailEntity entity = new UserDetailEntity();
            entity.setId(cursor.getInt(0));
            entity.setNumber(cursor.getString(1));
            entity.setLastLoginTime(cursor.getString(2));
            entity.setTrueName(cursor.getString(3));
            entity.setLastLoginToken(cursor.getString(4));
            entity.setMobile(cursor.getString(5));
            entity.setMail(cursor.getString(6));
            entity.setCreateUserName(cursor.getString(7));
            entity.setParentShowId(cursor.getString(8));
            entity.setIsAdmin(cursor.getString(9));
            entity.setCreateUser(cursor.getString(10));
            entity.setTrueNameC(cursor.getString(11));
            entity.setDeptId(cursor.getString(12));
            entity.setdName(cursor.getString(13));
            entity.setJob(cursor.getString(14));
            entity.setStatus(cursor.getString(15));
            entity.setcShowId(cursor.getString(16));
            entity.setTelephone(cursor.getString(17));
            entity.setShowId(cursor.getString(18));
            entity.setCreateTime(cursor.getString(19));
            entity.setPathName(cursor.getString(20));
            entity.setName(cursor.getString(21));
            entity.setHira(cursor.getString(22));
            entity.setOtherField(cursor.getString(23));
            entity.setSort(cursor.getString(24));
            lists.add(entity);
        }
        cursor.close();
        if(lists!=null && lists.size()>0){
            return lists;
        }else{
            return null;
        }

    }

}
