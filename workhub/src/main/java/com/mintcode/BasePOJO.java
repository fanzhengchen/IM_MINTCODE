package com.mintcode;

import java.io.Serializable;

import android.util.SparseArray;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mintcode.chat.image.AttachDetail;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.launchr.util.TTJSONUtil;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "action")
@JsonSubTypes({@JsonSubTypes.Type(value = AttachDetail.class),
        @JsonSubTypes.Type(value = GroupInfoPOJO.class),
        @JsonSubTypes.Type(value = CreateGroupPOJO.class),
        @JsonSubTypes.Type(value = DelGroupUserPOJO.class),
        @JsonSubTypes.Type(value = AddGroupUserPOJO.class),
        @JsonSubTypes.Type(value = ReadSessionPOJO.class),
        @JsonSubTypes.Type(value = UpdateGroupNamePOJO.class),
        @JsonSubTypes.Type(value = OfflineMsgPOJO.class),
        @JsonSubTypes.Type(value = LoginPOJO.class),
        @JsonSubTypes.Type(value = UnReadSessionPOJO.class),
        @JsonSubTypes.Type(value = HistoryMessagePOJO.class)})

public class BasePOJO implements Serializable {

    private static SparseArray<String> NextActionMap = new SparseArray<String>();

    public static String getActionMessage(int code) {
        return NextActionMap.get(code);
    }

    private static SparseArray<String> SuccessMap = new SparseArray<String>();

    public static String getSuccessMessage(int code) {
        return SuccessMap.get(code);
    }

    private static SparseArray<String> TryAgainMap = new SparseArray<String>();

    public static String getTryAgainMessage(int code) {
        return TryAgainMap.get(code);
    }

    private static SparseArray<String> LogoutMap = new SparseArray<String>();

    public static String getLogoutMessage(int code) {
        return LogoutMap.get(code);
    }

    private static SparseArray<String> PleasePay = new SparseArray<String>();

    public static String getPayMessage(int code) {
        return PleasePay.get(code);
    }

    static {
        NextActionMap.put(1000, "请绑定手机号");
    }

    static {
        SuccessMap.put(2000, "操作成功");
        SuccessMap.put(2100, "医发送验证码");
        SuccessMap.put(2200, "审核待反馈");
    }

    static {
        TryAgainMap.put(3000, "请重试");
        TryAgainMap.put(3100, "手机号格式不正确");
        TryAgainMap.put(3101, "发送验证码失败");
        TryAgainMap.put(3102, "验证码无效");
    }

    static {
        LogoutMap.put(4000, "请重新登录");
        LogoutMap.put(4100, "Token过期");
    }

    static {
        PleasePay.put(5000, "请付费");
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String action;

    private int code;

    private String message;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResultSuccess() {
        return code / 1000 == 2;
    }

    public boolean isLogout() {
        return code / 1000 == 4;
    }

    public String getDetailMsg(int code) {
        String error = "";
        int type = code / 1000;
        switch (type) {
            case 1:
                error = getActionMessage(code);
                break;
            case 2:
                error = getSuccessMessage(code);
                break;
            case 3:
                error = getTryAgainMessage(code);
                break;
            case 4:
                error = getLogoutMessage(code);
                break;
            case 5:
                error = getPayMessage(code);
                break;
            default:
                break;
        }
        return error;
    }

    public String toJson() {
        return TTJSONUtil.convertObjToJson(this);
    }
}
