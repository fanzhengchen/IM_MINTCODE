package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.AttachmentApi;
import com.mintcode.launchr.app.newApproval.Entity.FormMutilDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormSingleDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.MessageFormData;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormViewUtil {

    /** 单行文本输入视图*/
    public static final String SINGLE_TEXT_INPUT = "TextInput";
    /** 多行文本输入视图*/
    public static final String MUTIL_TEXT_INPUT = "TextArea";
    /** 多行文本输入视图*/
    public static final String SINGLE_SELECT_INPUT = "RadioButton";
    /** 多行文本输入视图*/
    public static final String MUTIL_SELECT_INPUT = "CheckBox";
    /** 时间输入视图*/
    public static final String TIME_INPUT = "Time";
    /** 审批期限视图*/
    public static final String APPROVE_LIMIT_INPUT = "ApprovePeriod";
    /** 附件视图*/
    public static final String FILE_INPUT = "File";
    /** 审批者视图*/
    public static final String APPROVE_PERSON_INPUT = "ApprovePerson";
    /** 抄送者视图*/
    public static final String CC_PERSON_INPUT = "CCPerson";
    /** 无空值*/
    public static boolean BOOL_NO_NULL = true;
    /** 有文件*/
    public static boolean BOOL_NO_FILE = true;


    /** 设置显示人员选择值*/
    public static void setPersonText(List<UserDetailEntity> userList,HashMap<String,View> map ,String selectId){
        String name = "";
        List<String> strName = new ArrayList<>();
        if(userList != null && !userList.isEmpty()){

            name += userList.get(0).getTrueName() ;
            strName.add(name);
            for (int i = 1; i < userList.size(); i++) {
                name = name + "、" + userList.get(i).getTrueName();
                strName.add(userList.get(i).getTrueName());
            }
        }
        if(!strName.isEmpty()){
            View personLinearLayout =  map.get(selectId);
            if(personLinearLayout instanceof PersonLinearLayoutView){
                ((PersonLinearLayoutView) personLinearLayout).setUserText(name);
            }
        }
    }
    /** 设置显示多选选择值*/
    public static void setSelectText(List<FormCheckBoxEntity> datas,HashMap<String,View> map ,String selectId){
        String name = "";
        if(datas != null && !datas.isEmpty()){

            name += datas.get(0).getValue() ;
            for (int i = 1; i < datas.size(); i++) {
                name = name + "、" + datas.get(i).getValue();
            }
        }
            View selectLinearLayout =  map.get(selectId);
            if(selectLinearLayout instanceof SelectLinearLayoutView){
                ((SelectLinearLayoutView) selectLinearLayout).setSelectedText(name);
                ((SelectLinearLayoutView) selectLinearLayout).setSeletedData(datas);
            }
    }
    /** StringList转String*/
    public static String getStrListToStr(List<String> strList){
        String str = "";
        if(strList != null && !strList.isEmpty()){
            for(String text : strList){
                str += (text + "、");
            }
            str = str.substring(0,str.length()-1);
        }
        return str;
    }
    /** */
    public static String getJsonListToString(String json){
        String str = "";
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i =0;i < jsonArray.length();i++){
                String temp = jsonArray.getString(i);
                str += (temp + "、");
            }
            str = str.substring(0,str.length()-1);
            return str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }




    public static  void createFormContent(Context context,ViewGroup view,HashMap<String,String> mJsonHashMap,List<MessageFormData> forms) {
        for(MessageFormData form : forms){
            String type = form.getInputType();
            String key = form.getKey();
            String json = null;
            if(mJsonHashMap.get(key) != null){
                json = mJsonHashMap.get(key);
            }
            if(type.equals(FormViewUtil.SINGLE_TEXT_INPUT)){ //单行输入框
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }else if(type.equals(FormViewUtil.MUTIL_TEXT_INPUT)){//多行输入框
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }else if(type.equals(FormViewUtil.TIME_INPUT)){//时间
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }else if(type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)){//审批期限
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }else if(type.equals(FormViewUtil.SINGLE_SELECT_INPUT)){//单选框
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }else if(type.equals(FormViewUtil.MUTIL_SELECT_INPUT)){//多选框
                MessageTextLayoutView textView = new MessageTextLayoutView(context,form,json,0,10);
                addViewIntoForm(view,textView);
            }
        }
    }

    public static void addViewIntoForm(ViewGroup viewGroup,View view){
        viewGroup.addView(view);
    }

    /**  隐藏软键盘 */
    public static void hideSoftInputWindow(Context context ,View v){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /** 创建分割线*/
    public static  ImageView createLineView(Context context){
        ImageView line = new ImageView(context);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        line.setLayoutParams(imageParam);
        line.setBackgroundColor(context.getResources().getColor(R.color.grey_launchr));
        return line;
    }

    /** 表单数据集合*/
    public static HashMap<String,String> getFormDataJson(String json){
        HashMap<String,String> formDataMap = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject temp = (JSONObject)jsonArray.get(i);
                if(temp != null){
                    String key = temp.getString("key");
                    String value = temp.optString("value");
                    formDataMap.put(key, value);
                }
            }
            return formDataMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 表单控件集合*/
    public static List<formData> getFormList(String json){
        List<formData> forms = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i =0;i < jsonArray.length();i++){
                JSONObject temp = (JSONObject)jsonArray.get(i);
                formData form = TTJSONUtil.convertJsonToCommonObj(temp.toString(), formData.class);
                forms.add(form);
            }
            return forms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void setSingeTimeDisplayFormat(boolean state,long time1,TextView tvTime){
        SimpleDateFormat OneDayFormat;
        if(state == true){
            OneDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
        }else{
            OneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        }
        String oneday = OneDayFormat.format(time1);
        tvTime.setText(oneday);
    }
    public static void setSingeTimeDisplayFormat(long time1,TextView tvTime){
        SimpleDateFormat OneDayFormat;
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(time1);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int mint = time.get(Calendar.MINUTE);
        if(hour == 0 && mint == 0){
            OneDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
        }else{
            OneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        }
        String oneday = OneDayFormat.format(time1);
        tvTime.setText(oneday);
    }
    /** 获取时间显示*/
    public static void setTimeDisplayFormat(boolean state,long time1,long time2,TextView tvTime){
        //获取开始月日和结束月日
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(time1);
        int bYear = beginTime.get(Calendar.YEAR);
        int bDayofYear = beginTime.get(Calendar.DAY_OF_YEAR);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(time2);
        int eYear = endTime.get(Calendar.YEAR);
        int eDayofYear = endTime.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat AllDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
        SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());
        if(state == true ){
            if(bYear == eYear && bDayofYear == eDayofYear){
                String sBeginTime  = AllDayFormat.format(time1);
                tvTime.setText(sBeginTime);
            }else{
                String sBeginTime  = AllDayFormat.format(time1);
                String sOverTime = AllDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }
        }else{
            //设置时间显示格式 一天以上 或 一天
            if(bYear == eYear && bDayofYear == eDayofYear){
                String sBeginTime  = OverOneDayFormat.format(time1);
                String sOverTime = OneDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }else{
                String sBeginTime  = OverOneDayFormat.format(time1);
                String sOverTime  = OverOneDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }
        }
    }

    public static void setTimeDisplayFormat(long time1,long time2,TextView tvTime){

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(time1);
        int bYear = beginTime.get(Calendar.YEAR);
        int bDayofYear = beginTime.get(Calendar.DAY_OF_YEAR);
        int hour = beginTime.get(Calendar.HOUR_OF_DAY);
        int mint = beginTime.get(Calendar.MINUTE);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(time2);
        int eYear = endTime.get(Calendar.YEAR);
        int eDayofYear = endTime.get(Calendar.DAY_OF_YEAR);
        int ehour = endTime.get(Calendar.HOUR_OF_DAY);
        int emint = endTime.get(Calendar.MINUTE);
        //获取开始月日和结束月日
        SimpleDateFormat AllDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
        SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());
        if(hour == 0 && mint == 0 && ehour == 0 && emint == 0){
            if(bYear == eYear && bDayofYear == eDayofYear){
                String sBeginTime  = AllDayFormat.format(time1);
                tvTime.setText(sBeginTime);
            }else{
                String sBeginTime  = AllDayFormat.format(time1);
                String sOverTime = AllDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }
        }else{
            //设置时间显示格式 一天以上 或 一天
            if(bYear == eYear && bDayofYear == eDayofYear){
                String sBeginTime  = OverOneDayFormat.format(time1);
                String sOverTime = OneDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }else{
                String sBeginTime  = OverOneDayFormat.format(time1);
                String sOverTime  = OverOneDayFormat.format(time2);
                tvTime.setText(sBeginTime + "~" + sOverTime);
            }
        }
    }

    /** 获取用户真实姓名*/
    public static String getUserTrueNameText(List<UserDetailEntity> userList){
        StringBuilder build = new StringBuilder();
        UserDetailEntity en;
        for (int i = 0; i < userList.size() - 1;i++) {
            en = userList.get(i);
            build.append(en.getTrueName()).append("●");
        }
        en = userList.get(userList.size() - 1);
        build.append(en.getTrueName());
        return build.toString();
    }
    /** 获取用户Id*/
    public static String getUserNameText(List<UserDetailEntity> userList){
        StringBuilder build = new StringBuilder();
        UserDetailEntity en;
        for (int i = 0; i < userList.size() - 1;i++) {
            en = userList.get(i);
            build.append(en.getName()).append("●");
        }
        en = userList.get(userList.size() - 1);
        build.append(en.getName());
        return build.toString();
    }
    /** 获取审批状态*/
    public static int getApprovalStatus(String text) {
        if (text.equals("APPROVE")) {
            return 1;
        }
        if (text.equals("WAITING")) {
            return 4;
        }
        if (text.equals("IN_PROGRESS")) {
            return 3;
        }
        if (text.equals("DENY")) {
            return 0;
        }
        if (text.equals("CALL_BACK")) {
            return 2;
        }
        return 0;
    }

    public static void mustInputToast(Context context ,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
