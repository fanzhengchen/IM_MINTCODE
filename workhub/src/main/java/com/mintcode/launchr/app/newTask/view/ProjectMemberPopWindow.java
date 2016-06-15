package com.mintcode.launchr.app.newTask.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.TTDensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/25.
 */
public class ProjectMemberPopWindow extends PopupWindow implements View.OnClickListener {


    private Context mContext;
    private View mContentView;
    private LayoutInflater mInfalter;

    private LinearLayout mLlMemberList;

    private List<TaskAddProjectEntity.TaskAddProjectMembersEntity> mPersonList = new ArrayList<>();

    public ProjectMemberPopWindow(Context context){
        this.mContext = context;
        initviews();
        setWindow();
    }

    private void initviews() {

        LayoutInflater inflater = mInfalter = LayoutInflater.from(mContext);
        mContentView = inflater.inflate(R.layout.popwindow_project_member, null);
        mLlMemberList = (LinearLayout)mContentView.findViewById(R.id.ll_project_member);
    }

    private void setWindow() {
        // 设置PopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置PopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.popupAnimation);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
//		 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

     public void showProjectMembers(List<TaskAddProjectEntity.TaskAddProjectMembersEntity> list){
         mPersonList = list;
        for(int i =0 ;i< mPersonList.size();i++ ){
            TaskAddProjectEntity.TaskAddProjectMembersEntity entity = mPersonList.get(i);
            String showId = entity.getMemberName();
            String name = entity.getMemberTrueName();
            addPerson(showId,name);
        }

    }

    private void addPerson(String showid,String name){
        // 头像
        ImageView iv = new ImageView(mContext);
        String companyCode = LauchrConst.header.getCompanyCode();
        String mHeadPicUrl = LauchrConst.ATTACH_PATH +"/Base-Module/Annex/Avatar?width=80&height=80&companyCode="
                + companyCode + "&userName=" + showid;
        setImageResuces(iv, mHeadPicUrl);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ivParams.gravity = Gravity.CENTER_VERTICAL;
        int x = TTDensityUtil.dip2px(mContext,40);
        ivParams.height = x;
        ivParams.width = x;
        // 名字
        TextView tv = new TextView(mContext);
        tv.setTextColor(Color.BLACK);
        int sp = TTDensityUtil.px2sp(mContext,30);
        tv.setTextSize(sp);
        tv.setSingleLine();
        tv.setText(name);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.width = x;

        LinearLayout linear = new LinearLayout(mContext);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(iv, ivParams);
        linear.addView(tv,tvParams);
        LinearLayout.LayoutParams linearParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        int padding = TTDensityUtil.dip2px(mContext,15);
        int paddingtop = TTDensityUtil.dip2px(mContext,10);
        linear.setPadding(padding,paddingtop,padding,paddingtop);

        mLlMemberList.addView(linear,linearParam);

    }
    public void setImageResuces(ImageView iv, String url){
        // 处理圆形图片
            RequestManager man = Glide.with(mContext);
            GlideRoundTransform g =  new GlideRoundTransform(mContext,3);
            man.load(url).transform(g).placeholder(R.drawable.icon_head_default).crossFade().into(iv);
    }
    @Override
    public void onClick(View v) {

    }
}
