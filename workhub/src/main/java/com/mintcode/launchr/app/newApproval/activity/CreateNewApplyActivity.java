package com.mintcode.launchr.app.newApproval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.WorkFlowAPI;
import com.mintcode.launchr.app.newApproval.view.BaseLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.EditTextLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.FileLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.app.newApproval.view.PersonLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.SelectLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.TimeLinearLayoutView;
import com.mintcode.launchr.app.newApproval.view.UrgencyLayoutView;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.FormPOJO;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.WorkFlowPOJO;
import com.mintcode.launchr.pojo.entity.ApprovalTypeEntity;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;
import com.mintcode.launchr.pojo.entity.FormEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.SelectPicTypeWindowView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JulyYu on 2016/4/12.
 */
public class CreateNewApplyActivity extends BaseActivity implements ContactFragment.OnSelectContactListner {


    /**
     * 获取审批类型标记
     */
    public static final String APPLY_TYPE = "create_new_apply_activity_type";
    /**
     * 新建的审批类型
     */
    private ApprovalTypeEntity mTypeEntity;

    private FragmentManager mFragManger;
    /**
     * 部门人员选择页面
     */
    private ContactFragment mContactFragment;
    /**
     * 返回
     */
    private TextView mTvBack;
    /**
     * 新建
     */
    private TextView mTvCreate;
    /**
     * 审批类型标题
     */
    private TextView mTvTitle;
    /**
     * 基本表单数据的布局
     */
    private LinearLayout mLinearFormContent;
    /**
     * 人员放置的布局
     */
    private LinearLayout mLinearPersonContent;
    /**
     * 其他内容放置的布局
     */
    private LinearLayout mLinearOthetContent;
    /**
     * 是否紧急的View组件
     */
    private UrgencyLayoutView mUrgencyView;
    /**
     * 附件上传的view组件
     */
    private FileLinearLayoutView mFileLinear;

    /**
     * 必须参加会议的类型
     */
    public static final int PROMISE_TYPE = 1;
    /**
     * 非必须参加会议类型
     */
    public static final int CC_TYPE = 2;
    /**
     * 人员选择类型
     */
    private int mSelectUserType;
    /**
     * 审批人员
     */
    private List<UserDetailEntity> mPromiseList = new ArrayList<>();
    /**
     * 抄送人员
     */
    private List<UserDetailEntity> mCcList = new ArrayList<>();

    /**
     * 组件数量
     */
    private int mIntViewAccount = 0;
    /**
     * 被选择组件的ID记录
     */
    private String mStrSelectId;
    /**
     * 组件集合
     */
    private HashMap<String, View> mFormViewMap;

    /**
     * 拍照
     */
    public static final int TAKE_PICTURE = 0x000001;
    /**
     * 预览照片
     */
    public static final int PREVIEW_PIC = 0x000002;
    /**
     * 打开相册选择照片
     */
    public static final int GET_ALBUM = 0x000003;
    /**
     * 单多选选择
     */
    private static final int SELECT_BOX = 0x000004;
    /**
     * 照片地址
     */
    private List<AttachmentListEntity> mPicUrl = new ArrayList<>();


    private String mStrFormJsonData;
    private String mStrMessageFormJson;
    private String mStrTitle;
    private String mStrApprove;
    private String mStrApproveName;
    private String mStrCc;
    private String mStrCcName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_approval_apply);
        initView();
        initData();
        MixPanelUtil.sendEvent(this, MixPanelUtil.APP_APPROVE_NEW_EVENT);
    }

    private void initData() {
        mFormViewMap = new HashMap<>();
        Intent i = getIntent();
        mTypeEntity = (ApprovalTypeEntity) i.getSerializableExtra(APPLY_TYPE);
        if (mTypeEntity != null) {
            showLoading();
            String formId = mTypeEntity.getFORM_ID();
            String title = mTypeEntity.getT_NAME();
            mTvTitle.setText(title);
            WorkFlowAPI.getInstance().getForm(this, formId);
        }
    }

    private void initView() {

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvBack = (TextView) findViewById(R.id.tv_apply_canel);
        mTvCreate = (TextView) findViewById(R.id.tv_apply_send);
        mLinearFormContent = (LinearLayout) findViewById(R.id.ll_apply_form);
        mLinearPersonContent = (LinearLayout) findViewById(R.id.ll_person_layout);
        mLinearOthetContent = (LinearLayout) findViewById(R.id.ll_other_layout);

        mContactFragment = new ContactFragment();
        mContactFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE, null);
        mContactFragment.setOnSelectContactListner(this);
        mFragManger = getSupportFragmentManager();
        mFragManger.beginTransaction().add(R.id.ll_contact, mContactFragment).hide(mContactFragment).commit();

        mTvBack.setOnClickListener(this);
        mTvCreate.setOnClickListener(this);
    }


    /**
     * 隐藏最后期限
     */
    public void setLimitTime(boolean bool) {
        int vibilable = (bool == true ? View.GONE : View.VISIBLE);
        Iterator iter = mFormViewMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            View view = (View) entry.getValue();
            if (view instanceof TimeLinearLayoutView) {
                formData formData = ((BaseLinearLayoutView) view).getmFormData();
                if (formData.getInputType().equals(FormViewUtil.APPROVE_LIMIT_INPUT)) {
                    view.setVisibility(vibilable);
                }
            }
        }
    }

    /**
     * 画线
     */
    private ImageView createLineView() {
        ImageView line = new ImageView(this);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        line.setLayoutParams(imageParam);
        line.setBackgroundColor(getResources().getColor(R.color.grey_launchr));
        return line;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mTvBack) {
            MixPanelUtil.sendEvent(this, MixPanelUtil.APP_APPROVE_CANCEL_EVENT);
            this.finish();
        } else if (v == mTvCreate) {
            MixPanelUtil.sendEvent(this, MixPanelUtil.APP_APPROVE_SAVE_EVENT);
            getFormJsonData();
        }
    }

    /**
     * 新建审批
     */
    public void createNewApprove() {
//        if (TextUtils.equals(mStrApproveName, mStrCcName)) {
//            toast(getString(R.string.approval_not_agree));
//            return;
//        }

        String showId = mTypeEntity.getSHOW_ID();
        String workId = mTypeEntity.getWORK_FLOW_ID();
        int isUrgency = 0;
        if (mUrgencyView != null) {
            isUrgency = mUrgencyView.getIsUrgency();
        }
        showLoading();
        ApproveAPI.getInstance().newApproveV2(this, mStrTitle, showId, mStrApprove, mStrApproveName, mStrCc, mStrCcName,
                workId, mStrMessageFormJson, mStrFormJsonData, isUrgency, null);
    }

    /**
     * 上传照片
     */
    public void createNewApprove(List<String> images) {

        mStrFormJsonData = "[" + mStrFormJsonData.substring(0, mStrFormJsonData.length() - 1) + "]";
        Log.i("mStrFormJsonData", mStrFormJsonData);
        String showId = mTypeEntity.getSHOW_ID();
        String workId = mTypeEntity.getWORK_FLOW_ID();
        int isUrgency = 0;
        if (mUrgencyView != null) {
            isUrgency = mUrgencyView.getIsUrgency();
        }
        showLoading();
        ApproveAPI.getInstance().newApproveV2(this, mStrTitle, showId, mStrApprove, mStrApproveName, mStrCc, mStrCcName,
                workId, mStrMessageFormJson, mStrFormJsonData, isUrgency, images);
    }

    /**
     * 组装所有表单成Json
     */
    private void getFormJsonData() {
        mStrFormJsonData = "";
        Iterator iter = mFormViewMap.entrySet().iterator();
        FormViewUtil.BOOL_NO_NULL = true;
        while (iter.hasNext() && FormViewUtil.BOOL_NO_NULL) {
            Map.Entry entry = (Map.Entry) iter.next();
            View view = (View) entry.getValue();
            getEveryFormJsonData(view);
        }
        if (mFileLinear != null) {
            mFileLinear.upLoadingImage();
        }
        if (FormViewUtil.BOOL_NO_NULL && FormViewUtil.BOOL_NO_FILE) {
            mStrFormJsonData = "[" + mStrFormJsonData.substring(0, mStrFormJsonData.length() - 1) + "]";
            Log.i("mStrFormJsonData", mStrFormJsonData);
            createNewApprove();
        }
    }

    /**
     * 获取每个表单的json数据
     */
    private void getEveryFormJsonData(View view) {
        String json = null;
        if (view instanceof EditTextLinearLayoutView) { // 输入框
            json = ((EditTextLinearLayoutView) view).getJsonString();
            boolean boolFirstEdit = ((EditTextLinearLayoutView) view).ismBoolFirstEdit();
            if (boolFirstEdit) {
                mStrTitle = ((EditTextLinearLayoutView) view).getText().toString();
            }
        } else if (view instanceof PersonLinearLayoutView) {// 人员选择
            getApplyPersonInfo(view);
        } else if (view instanceof SelectLinearLayoutView) { // 单选多选框
            json = ((SelectLinearLayoutView) view).getJsonString();
        } else if (view instanceof TimeLinearLayoutView) { // 时间选择
            json = ((TimeLinearLayoutView) view).getJsonString();
        } else if (view instanceof FileLinearLayoutView) { // 文件选择
            mFileLinear = (FileLinearLayoutView) view;
        }
        if (json != null && !"".equals(json)) {
            mStrFormJsonData += json + ",";
        }
    }

    /**
     * 获取人员信息
     */
    private void getApplyPersonInfo(View view) {
        formData data = ((PersonLinearLayoutView) view).getUserInfo();
        String type = data.getInputType();
        boolean require = data.isRequired();
        if (type.equals(FormViewUtil.APPROVE_PERSON_INPUT)) {
            if (mPromiseList != null && mPromiseList.size() > 0) {
                mStrApprove = FormViewUtil.getUserNameText(mPromiseList);
                mStrApproveName = FormViewUtil.getUserTrueNameText(mPromiseList);
            } else if (require) {
                FormViewUtil.BOOL_NO_NULL = false;
                toast(getResources().getString(R.string.apply_promise_toast));
            }
        } else if (type.equals(FormViewUtil.CC_PERSON_INPUT)) {
            if (mCcList != null && mCcList.size() > 0) {
                mStrCc = FormViewUtil.getUserNameText(mCcList);
                mStrCcName = FormViewUtil.getUserTrueNameText(mCcList);
            } else if (require) {
                FormViewUtil.BOOL_NO_NULL = false;
                toast(getResources().getString(R.string.apply_promise_toast));
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean hidden = mContactFragment.isHidden();
        if (hidden) {
            setResult(RESULT_CANCELED, null);
            this.finish();
        } else {
            mFragManger.beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (response == null) {
            return;
        }
        if (taskId.equals(WorkFlowAPI.TaskId.TASK_URL_GET_WORK_FLOW)) {

        } else if (taskId.equals(WorkFlowAPI.TaskId.TASK_URL_GET_FORM)) {//获取表单信息
            FormPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), FormPOJO.class);
            FormDataHandle(pojo);
//            String workflowId = mTypeEntity.getWORK_FLOW_ID();
//            if(workflowId != null && !"".equals(workflowId)){
//                WorkFlowAPI.getInstance().getWorkPerson(this, workflowId);
//            }
        } else if (taskId.equals(ApproveAPI.TaskId.TASK_URL_NEW_APPROVE_V2)) {//新建审批成功
            NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
            NormalPOJOHandle(pojo);
        } else if (taskId.equals(WorkFlowAPI.TaskId.TASK_URL_GET_WORK_FLOW)) {//
            WorkFlowPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), WorkFlowPOJO.class);
        } else if (taskId.equals(WorkFlowAPI.TaskId.TASK_URL_GET_WORK_FLOW_BY_TRIGGER)) {

        }
    }

    private void NormalPOJOHandle(NormalPOJO pojo) {
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                toast(getString(R.string.apply_addnew_toast));
                setResult(RESULT_OK);
                this.finish();
            } else {
                toast(pojo.getBody().getResponse().getReason());
            }
        }
    }

    private void FormDataHandle(FormPOJO pojo) {
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                FormEntity entity = pojo.getBody().getResponse().getData();
                List<formData> forms = entity.getForm();
                createFormContent(forms);
            } else {
                toast(pojo.getBody().getResponse().getReason());
            }
        }
    }

    private void addViewIntoForm(View view, String type) {
        if (type.equals(FormViewUtil.APPROVE_PERSON_INPUT)) {
            mLinearPersonContent.addView(view, 0);
            mLinearPersonContent.addView(createLineView(), 0);
        } else if (type.equals(FormViewUtil.CC_PERSON_INPUT)) {
            mLinearPersonContent.addView(view);
            mLinearPersonContent.addView(createLineView());
        } else if (type.equals(FormViewUtil.FILE_INPUT)) {
            mLinearOthetContent.addView(view, 0);
            mLinearOthetContent.addView(createLineView(), 1);
        } else {
            mLinearFormContent.addView(view);
            mLinearFormContent.addView(createLineView());
        }
        mIntViewAccount++;
        view.setTag(mIntViewAccount + type);
        mFormViewMap.put((String) view.getTag(), view);
    }

    private void createFormContent(List<formData> forms) {
        boolean boolTitle = true;
        boolean boolFirstSingleEdit = true;
        boolean boolFirstMutilEdit = true;
        boolean boolFirstTime = true;
        boolean boolFirstLimit = true;
        boolean boolFirstSingleSelect = true;
        boolean boolFirstMutilSelect = true;
        mStrMessageFormJson = "";
        for (formData form : forms) {
            String type = form.getInputType();
            if (type.equals(FormViewUtil.SINGLE_TEXT_INPUT)) { //单行输入框
                EditTextLinearLayoutView editView = new EditTextLinearLayoutView(this, form);
                if (boolTitle) {
                    editView.setmBoolFirstEdit(true);
                    boolTitle = false;
                    addViewIntoForm(editView, FormViewUtil.SINGLE_TEXT_INPUT);
                } else if (boolFirstSingleEdit) {//第一个单行输入框
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstSingleEdit = false;
                    if (form.getLabelText().equals(getString(R.string.accpect_cost))) {//金额
                        addViewIntoForm(editView, FormViewUtil.SINGLE_TEXT_INPUT);
                    } else {
                        addViewIntoForm(editView, FormViewUtil.SINGLE_TEXT_INPUT);
                    }
                } else if (form.getLabelText().equals(getString(R.string.remark))) {//备注
                    mLinearOthetContent.addView(editView);
                    mLinearOthetContent.addView(createLineView());
                    editView.setTag(mIntViewAccount + type);
                    mFormViewMap.put((String) editView.getTag(), editView);
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                } else if (form.getLabelText().equals(getString(R.string.accpect_cost))) {//金额
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    addViewIntoForm(editView, FormViewUtil.SINGLE_TEXT_INPUT);
                } else {
                    addViewIntoForm(editView, FormViewUtil.SINGLE_TEXT_INPUT);
                }

            } else if (type.equals(FormViewUtil.MUTIL_TEXT_INPUT)) {//多行输入框
                EditTextLinearLayoutView editView = new EditTextLinearLayoutView(this, form);
                if (boolTitle) {
                    editView.setmBoolFirstEdit(true);
                    boolTitle = false;
                    addViewIntoForm(editView, FormViewUtil.MUTIL_TEXT_INPUT);
                } else if (boolFirstMutilEdit) {//第一个多行输入框
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstMutilEdit = false;
                    if (form.getLabelText().equals(getString(R.string.remark))) {//备注
                        mLinearOthetContent.addView(editView);
                        mLinearOthetContent.addView(createLineView());
                        editView.setTag(mIntViewAccount + type);
                        mFormViewMap.put((String) editView.getTag(), editView);
                    } else {
                        addViewIntoForm(editView, FormViewUtil.MUTIL_TEXT_INPUT);
                    }
                } else if (form.getLabelText().equals(getString(R.string.accpect_cost))) {//金额
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    addViewIntoForm(editView, FormViewUtil.MUTIL_TEXT_INPUT);
                } else if (form.getLabelText().equals(getString(R.string.remark))) {//备注
                    mLinearOthetContent.addView(editView);
                    mLinearOthetContent.addView(createLineView());
                    editView.setTag(mIntViewAccount + type);
                    mFormViewMap.put((String) editView.getTag(), editView);
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                } else {
                    addViewIntoForm(editView, FormViewUtil.MUTIL_TEXT_INPUT);
                }
            } else if (type.equals(FormViewUtil.TIME_INPUT)) {//时间
                if (boolFirstTime) {//第一个时间
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstTime = false;
                }
                TimeLinearLayoutView timeView = new TimeLinearLayoutView(this, form);
                addViewIntoForm(timeView, FormViewUtil.TIME_INPUT);
            } else if (type.equals(FormViewUtil.APPROVE_PERSON_INPUT)) {//审批者
                mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                PersonLinearLayoutView personView = new PersonLinearLayoutView(this, form);
                addViewIntoForm(personView, FormViewUtil.APPROVE_PERSON_INPUT);
            } else if (type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)) {//审批期限
                if (boolFirstLimit) {
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstLimit = false;
                }
                TimeLinearLayoutView timeView = new TimeLinearLayoutView(this, form);
                addViewIntoForm(timeView, FormViewUtil.APPROVE_LIMIT_INPUT);
            } else if (type.equals(FormViewUtil.CC_PERSON_INPUT)) {//抄送者
                mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                PersonLinearLayoutView personView = new PersonLinearLayoutView(this, form);
                addViewIntoForm(personView, FormViewUtil.CC_PERSON_INPUT);
            } else if (type.equals(FormViewUtil.FILE_INPUT)) {//附件
                FileLinearLayoutView fileView = new FileLinearLayoutView(this, form);
                addViewIntoForm(fileView, FormViewUtil.FILE_INPUT);
            } else if (type.equals(FormViewUtil.SINGLE_SELECT_INPUT)) {//单选框
                if (boolFirstSingleSelect) {
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstSingleSelect = false;
                }
                SelectLinearLayoutView selectView = new SelectLinearLayoutView(this, form);
                addViewIntoForm(selectView, FormViewUtil.SINGLE_SELECT_INPUT);
            } else if (type.equals(FormViewUtil.MUTIL_SELECT_INPUT)) {//多选框
                if (boolFirstMutilSelect) {
                    mStrMessageFormJson += TTJSONUtil.convertObjToJson(form) + ",";
                    boolFirstMutilSelect = false;
                }
                SelectLinearLayoutView selectView = new SelectLinearLayoutView(this, form);
                addViewIntoForm(selectView, FormViewUtil.MUTIL_SELECT_INPUT);
            }
        }
        if (mTypeEntity.getSHOW_ID().equals("vEyVJ7K29qcovp3p") || mTypeEntity.getSHOW_ID().equals("BB1xoKW53kCPW7OP")) {
            mUrgencyView = new UrgencyLayoutView(this, null);
            mLinearFormContent.addView(mUrgencyView);
            mLinearFormContent.addView(createLineView());
        }
        if (mStrMessageFormJson.length() > 0) {
            mStrMessageFormJson = "[" + (mStrMessageFormJson.substring(0, mStrMessageFormJson.length() - 1)) + "]";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE:
                takePictureResult(resultCode, data);
                break;
            case PREVIEW_PIC:
                View FileDelectLinearLayout = mFormViewMap.get(mStrSelectId);
                if (FileDelectLinearLayout instanceof FileLinearLayoutView) {
                    if (data != null) {
                        List<String> pic = data.getStringArrayListExtra(GalleryActivity.KEY_PHOTO_URL);
                        ((FileLinearLayoutView) FileDelectLinearLayout).setImageStrList(pic);
                        ((FileLinearLayoutView) FileDelectLinearLayout).updateAdapter();
                    } else {
                        List<String> noNull = new ArrayList<>();
                        ((FileLinearLayoutView) FileDelectLinearLayout).setImageStrList(noNull);
                        ((FileLinearLayoutView) FileDelectLinearLayout).updateAdapter();
                    }
                }
                break;
            case GET_ALBUM:
                List<String> list = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
                if (list != null) {
                    View FileLinearLayout = mFormViewMap.get(mStrSelectId);
                    if (FileLinearLayout instanceof FileLinearLayoutView) {
                        ((FileLinearLayoutView) FileLinearLayout).setImageStrList(list);
                        ((FileLinearLayoutView) FileLinearLayout).updateAdapter();
                    }
                }
                break;
            case SELECT_BOX:
                selectBoxResult(resultCode, data);
        }
    }

    private void selectBoxResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            List<FormCheckBoxEntity> datas = (List<FormCheckBoxEntity>) data.getSerializableExtra(CheckBoxFormActivity.SELECT_RESULT);
            if (datas != null) {
                FormViewUtil.setSelectText(datas, mFormViewMap, mStrSelectId);
            }
        }

    }

    /**
     * 表单数据选择页
     */
    public void selectFormBox(View view, formData type, List<FormCheckBoxEntity> seleted) {
        mStrSelectId = (String) view.getTag();
        Intent select = new Intent(this, CheckBoxFormActivity.class);
        select.putExtra(CheckBoxFormActivity.SELECT_TYPE, type);
        if (select != null && seleted.size() > 0) {
            select.putExtra(CheckBoxFormActivity.SELECTED_DATA, (Serializable) seleted);
        }
        startActivityForResult(select, SELECT_BOX);
    }

    /**
     * 拍照返回
     */
    private void takePictureResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (!"".equals(SelectPicTypeWindowView.mStrPicPath)) {
                View FileLinearLayout = mFormViewMap.get(mStrSelectId);
                if (FileLinearLayout instanceof FileLinearLayoutView) {
                    ((FileLinearLayoutView) FileLinearLayout).addImageStr(SelectPicTypeWindowView.mStrPicPath);
                    ((FileLinearLayoutView) FileLinearLayout).updateAdapter();
                }
            } else {
                toast(getString(R.string.photo_unget_photo));
            }
        }
    }

    /**
     * 添加照片
     */
    public void AddImage(View view) {
        mStrSelectId = (String) view.getTag();
        SelectPicTypeWindowView popWindow = new SelectPicTypeWindowView(this);
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 选取联系人操作
     */
    public void accessUser(int type, String id) {
        mSelectUserType = type;
        mStrSelectId = id;
        mFragManger.beginTransaction().show(mContactFragment).commit();
        mContactFragment.startReciver(this);
        if (type == PROMISE_TYPE) {
            mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, mPromiseList);

        } else if (type == CC_TYPE) {
            mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, mCcList);
        }
    }

    @Override
    public void onSelectContact(List<UserDetailEntity> userList) {
        if (mSelectUserType == PROMISE_TYPE) {
            selectPromiseUser(userList);
        } else if (mSelectUserType == CC_TYPE) {
            selectCCUser(userList);
        }
        mSelectUserType = 0;
    }

    /**
     * 显示必须要参加的人操作
     */
    private void selectPromiseUser(List<UserDetailEntity> userList) {
        if (userList != null) {

            mPromiseList.clear();
            mPromiseList.addAll(userList);
            // 显示操作
            FormViewUtil.setPersonText(mPromiseList, mFormViewMap, mStrSelectId);
        }
    }

    /**
     * 显示要参加的人操作
     */
    private void selectCCUser(List<UserDetailEntity> userList) {
        if (userList != null) {
            mCcList.clear();
            mCcList.addAll(userList);
            // 显示要参加的人的姓名
            FormViewUtil.setPersonText(mCcList, mFormViewMap, mStrSelectId);
        }
    }
}
