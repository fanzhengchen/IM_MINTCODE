package com.mintcode.launchr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mintcode.launchr.util.TTDensityUtil;


public class FlowViewGroup extends LinearLayout {

    private final static String TAG = "LineBreakLayout";

    private int VIEW_MARGIN = 0;


    private int line_height;
    public static final int DEFAULT_HORIZONTAL_SPACING = 5;
    public static final int DEFAULT_VERTICAL_SPACING = 5;

    private int horizontalSpacing = 0;
    private int verticalSpacing = 0;


    public FlowViewGroup(Context context) {
        super(context);
        VIEW_MARGIN = TTDensityUtil.dip2px(context, 10);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(lp);
    }

//	public FlowViewGroup(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		VIEW_MARGIN = TTDensityUtil.dip2px(context, 10);
//	}

    public FlowViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        VIEW_MARGIN = TTDensityUtil.dip2px(context, 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);

        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int line_height = 0;
        // int line_height = 22;

        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED));

                final int childw = child.getMeasuredWidth();
                line_height = Math.max(line_height, child.getMeasuredHeight() + verticalSpacing);

                if (xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += line_height;
                }

                xpos += childw + horizontalSpacing;
            }
        }
        this.line_height = line_height;

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = ypos + line_height;

        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (ypos + line_height < height) {
                height = ypos + line_height;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1); // default of 1px spacing
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();
                // final int childh = 24;
                if (xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += line_height;
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh);
                xpos += childw + horizontalSpacing;
            }
        }
    }
}
