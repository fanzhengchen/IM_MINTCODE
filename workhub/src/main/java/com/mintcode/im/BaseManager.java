package com.mintcode.im;

import android.content.Context;

/**
 * Created by KevinQiao on 2016/3/4.
 */
public abstract class BaseManager {

    protected Context context;


    protected  void setContext(Context context){
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        this.context = context;
    }


    public void onStartManger(Context context){
        setContext(context);
        doOnStart();
    }


    /**
     * service开启，初始化manager
     */
    public abstract void doOnStart();

    /**
     * 更新context
     * 重置
     */
    public abstract void reset();

}
