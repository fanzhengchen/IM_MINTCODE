package com.mintcode.launchr.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.view.FlowViewGroup;

import java.util.List;

public class CollapsibleTextView extends LinearLayout implements
        android.view.View.OnClickListener {

    /**
     * 默认的最大显示行数
     */
    private static final int DEFAULT_MAX_LINES = 2;
    /**
     * 整体的状态参数，0代表msg的行数没有超过默认行数，msg全部显示； 1代表msg的行数超过默认行数，msg全部显示，文字后有“收起”；
     * 2代表msg的行数超过默认行数，msg处于折叠状态，文字后有“显示更多”。
     */
    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_PACKUP = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;

    private int mMaxLines = DEFAULT_MAX_LINES;
    private TextView mMsg;
    private FlowViewGroup mFlowView;
    // 标记，是否处于折叠显示状态
    private boolean flag;
    private Context mContext;
    private List<String> mName;

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setOrientation(VERTICAL);
        View view = inflate(context, R.layout.view_more_textview, this);
        mMsg = (TextView) view.findViewById(R.id.collapsible_msg);
        int textSize = TTDensityUtil.sp2px(mContext, 15);
        mMsg.setTextColor(mContext.getResources().getColor(R.color.black));
        mMsg.setTextSize(15);
        mFlowView = new FlowViewGroup(mContext);
        ViewGroup.LayoutParams flow = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mFlowView, flow);
        mMsg.setOnClickListener(this);
//        mIsShow = (TextView) view.findViewById(R.id.collapsible_isShow);
//        mIsShow.setOnClickListener(this);
    }

    /**
     * 设置显示的文字
     *
     * @param charSequence
     *            显示的信息文字
     * @param bufferType
     *            TextView中android:bufferType属性的作用指定getText()方式取得的文本类别。
     *            选项editable
     *            类似于StringBuilder可追加字符，也就是说getText后可调用append方法设置文本内容。 spannable
     *            则可在给定的字符区域使用样式。
     */
    public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        mMsg.setText(charSequence, bufferType);
        flag = false;
        requestLayout();
    }

//    public final void setText(CharSequence charSequence) {
//        mMsg.setText(charSequence, TextView.BufferType.NORMAL);
//        mState = COLLAPSIBLE_STATE_SPREAD;
//        flag = true;
//        requestLayout();
//    }

    /**
     * 设置显示的文字，默认bufferType为NORMAL
     *
     * @param charSequence
     *            显示的信息文字
     */
    public final void setText(CharSequence charSequence,List<String> name) {
        setText(charSequence, TextView.BufferType.NORMAL);
        mName = name;
        flag = true;
    }

    public final void setText(List<String> name) {
//        setText(charSequence, TextView.BufferType.NORMAL);
        mMsg.setVisibility(View.GONE);
        mName = name;
        setFlowText(mName);
        flag = false;

    }

    public String getText(){
        return mMsg.getText().toString().trim();
    }

    /**
     * 设置显示最多的行数
     *
     * @param num
     *            显示的行数
     */
    public final void setMaxLines(int num) {
        this.mMaxLines = num;
    }

    @Override
    public void onClick(View v) {
        Layout l = mMsg.getLayout();
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0) {
                if (l.getEllipsisCount(lines - 1) > 0 && flag) {
                        mMsg.setVisibility(View.GONE);
                        setFlowText(mName);
                }else{
                    flag = false;
                }
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 一行一行的动态添加文字
//        if (!flag) {
//            flag = true;
//            if (mMsg.getLineCount() <= mMaxLines) {
//                mState = COLLAPSIBLE_STATE_NONE;
////                mIsShow.setVisibility(View.GONE);
//                mMsg.setMaxLines(mMaxLines + 1);
//            }else{
//                mState = COLLAPSIBLE_STATE_SPREAD;
//            }
//            else {
//                post(new InnerRunnable());
//            }
//        }
    }

    // 名字流式布局
    private FlowViewGroup showFlowTextLayout(List<String> name){
        FlowViewGroup flowView = new FlowViewGroup(mContext);
        int textSize = TTDensityUtil.sp2px(mContext, 15);
        for(int i = 0;i < name.size();i++){
            TextView textView = new TextView(mContext);
            textView.setTextSize(textSize);
            textView.setTextColor(Color.BLACK);
            if( i == name.size() - 1){
                textView.setText(name.get(i));
            }else{
                textView.setText(name.get(i)+"、");
            }

            flowView.addView(textView);
        }
        return flowView;
    }

    private void setFlowText(List<String> name){
        mFlowView.removeAllViews();
        int textSize = TTDensityUtil.sp2px(mContext, 15);
        for(int i = 0;i<name.size();i++){
            TextView textView = new TextView(mContext);
            textView.setTextSize(15);
            textView.setTextColor(Color.BLACK);
            if( i == name.size() - 1){
                textView.setText(name.get(i));
            }else{
                textView.setText(name.get(i)+"、");
            }
            mFlowView.addView(textView);
        }
    }
}