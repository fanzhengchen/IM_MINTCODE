package com.mintcode.launchr.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Created by StephenHe on 2016/2/23.
 * 文字高亮等样式的重载类
 */
public class RoundBackgroundColorSpan extends ReplacementSpan{
    private int bgColor;
    private int textColor;

    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return ((int)paint.measureText(text, start, end));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int color1 = paint.getColor();
        paint.setColor(this.bgColor);
        canvas.drawRoundRect(new RectF(x, top+1, x + ((int) paint.measureText(text, start, end)), bottom-1), 5, 5, paint);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x-5, y, paint);
        paint.setColor(color1);
    }

}
