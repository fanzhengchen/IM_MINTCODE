package com.mintcode.im.database;

import com.mintcode.launchr.pojo.entity.ChatGroupEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by JulyYu on 2016/5/5.
 */
public class MyChatGroupDBService {

    private static MyChatGroupDBService sInstance;

    public synchronized static MyChatGroupDBService getInstance(){
        if (sInstance == null) {
            sInstance = new MyChatGroupDBService();
        }
        return sInstance;
    }
    /** 获取群聊列表*/
    public List<ChatGroupEntity> getChatGroupList(){
        List<ChatGroupEntity> result = DataSupport.findAll(ChatGroupEntity.class);
                return result;
    }
    /** 插入群组列表*/
    public void insertChatGroupList(List<ChatGroupEntity> list){
        if(list != null && !list.isEmpty()){
            DataSupport.saveAll(list);
        }
    }

}
