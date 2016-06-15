package com.mintcode.launchr.util;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JulyYu on 2016/5/23.
 */
public class MixPanelUtil  {
    //正式TOKEN  4e1265a0caa761695c68db1cf95345e2
    //测试TOKEN  5ddabf66d799830aebc2fa0742f73a38
    private static final String MIX_TOKEN = "4e1265a0caa761695c68db1cf95345e2";

    public static MixpanelAPI sInstance;

    public static final String SIGN_EVENT = "signEvent";
    public static final String ABOUT_EVENT = "aboutEvent";
    public static final String LOGIN_EVENT = "loginEvent";
    public static final String LOGOUT_EVENT = "logoutEvent";
    public static final String HOME_TASK_EVENT = "homeTaskEvent";
    public static final String HOME_CALENDAR_EVENT = "homeCalendarEvent";
    public static final String HOME_APPROVE_EVENT = "homeApproveEvent";
    public static final String CONTACT_EVENT = "contactEvent";
    public static final String APPCENTER_EVENT = "appCenterEvent";
    public static final String APPCENTER_TASK_EVENT = "appCenterTaskEvent";
    public static final String APPCENTER_CALENDAR_EVENT = "appCenterCalendarEvent";
    public static final String APPCENTER_APPROVE_EVENT = "appCenterApproveEvent";
    public static  final String APP_TASK_NEW_EVENT = "appTaskNewEvent";
    public static  final String APP_TASK_SAVE_EVENT = "appTaskSaveEvent";
    public static  final String APP_TASK_CANCEL_EVENT = "appTaskCancelEvent";
    public static  final String APP_APPROVE_NEW_EVENT = "appApproveNewEvent";
    public static  final String APP_APPROVE_SAVE_EVENT = "appApproveSaveEvent";
    public static  final String APP_APPROVE_CANCEL_EVENT = "appApproveCancelEvent";
    public static  final String APP_CALENDAR_NEW_EVENT = "appCalendarNewEvent";
    public static  final String APP_CALENDAR_SAVE_EVENT = "appCalendarSaveEvent";
    public static  final String APP_CALENDAR_CANCEL_EVENT = "appCalendarCancelEvent";


    public static MixpanelAPI getInstance(Context context){
        if(sInstance == null){
            sInstance =  MixpanelAPI.getInstance(context,MIX_TOKEN);
        }
        sInstance.flush();
        return sInstance;
    }
    /** 发送事件*/
    public static void sendEvent(Context context,String event){
        if(event.equals(SIGN_EVENT) || event.equals(LOGIN_EVENT)
                ||event.equals(LOGOUT_EVENT)){
            getInstance(context).track(event);
        }
//        getInstance(context).track(event);
    }
    /** 注册*/
    public static void signRecord(Context context,String mail){
        JSONObject mailObject = new JSONObject();
        try {
            mailObject.put("mail", mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getInstance(context).track(SIGN_EVENT,mailObject);
    }
    public static void flushEvent(Context context){
        getInstance(context).flush();
    }

//    /** 关于*/
//    public void aboutRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 登录*/
//    public void loginRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 登出*/
//    public void exitRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 首页任务*/
//    public void homeTaskRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 首页日程*/
//    public void homeCalendarRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 首页审批*/
//    public void homeApproveRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 通讯录*/
//    public void contactRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 应用中心*/
//    public void appCenterRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 应用中心任务*/
//    public void appCenterTaskRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 应用中心日程*/
//    public void appCenterCalendarRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 应用中心审批*/
//    public void appCenterApproveRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 新建任务*/
//    public void taskNewRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 保存任务*/
//    public void taskSaveRecord(Context context){
//        sendEvent(context,SIGN_EVENT);
//    }
//    /** 取消任务*/
//    public void taskCancelRecord(Context context){
//        getInstance(context).track("appTaskCancelEvent");
//    }
//    /** 新建审批*/
//    public void approveNewRecord(Context context){
//        getInstance(context).track("appApproveNewEvent");
//    }
//    /** 保存审批*/
//    public void approvSaveRecord(Context context){
//        getInstance(context).track("appApproveSaveEvent");
//    }
//    /** 取消审批*/
//    public void approvCancelRecord(Context context){
//        getInstance(context).track("appApproveCancelEvent");
//    }
//    /** 新建日程*/
//    public void calendarNewRecord(Context context){
//        getInstance(context).track("appCalendarNewEvent");
//    }
//    /** 保存日程*/
//    public void calendarSaveRecord(Context context){
//        getInstance(context).track("appCalendarSaveEvent");
//    }
//    /** 取消日程*/
//    public void calendarCancelRecord(Context context){
//        getInstance(context).track("appCalendarCancelEvent");
//    }

}
