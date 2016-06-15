package com.mintcode.launchr.app.newSchedule.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newSchedule.util.CreateCalendar;
import com.mintcode.launchr.app.newSchedule.util.ScheduleEventUtil;
import com.mintcode.launchr.app.newSchedule.adapter.MonthAdapter;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TTDensityUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/4.
 */
public class ScheduleEventView extends View {


    private Context mContext;

    /** 格子数组 将View分成4x7的布局*/
    private boolean[][] mGrids = new boolean[7][4];
    /** 画布宽度 屏幕高度*/
    private int mWidth;
    /** 画布高度*/
    private int mHeight;
    /** 标记宽度*/
    private int mTagWidth;
    /** 单个格子的宽度*/
    private int mGridWidth;
    /** 单个格子的高度*/
    private int mGridHeight;
    /** 事件之间间距宽度*/
    private int mSpaceWidth;
    /** 边框宽度*/
    private int mLineWidth;

    /** 文字大小*/
    private int mTextSize;
    /** 画笔*/
    private Paint mPaint;
    /** 画布*/
    private Canvas mCanvas;

    /** 日期工具类 */
    private CreateCalendar mCreateCalendar;
    /** 私人日程锁图标*/
    private Bitmap mLockBitmap;
    /** 日程锁图标宽*/
    private int mIntBitmapWith;
    /** 日程锁图标高*/
    private int mIntBitmapHeight;
    private int COLOR_BLUE_BACKGROUND;
    private int COLOR_GREEN_BACKGROUND;
    private int COLOR_RED_FESTIVAL;
    private int COLOR_BULE_MEETING;
    private int COLOR_GREEN_EVENT;
    private int COLOR_RED_THIN_FESTIVAL;
    private int COLOR_BULE_THIN_MEETING;
    private int COLOR_GREEN_THIN_EVENT;
    private int COLOR_BLACK;
    private int COLOR_WHITE;

    private List<EventEntity> mEvetSets;
    private int [] mDays;
    private int mYear;
    private int mMonth;

    public ScheduleEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mCreateCalendar = new CreateCalendar();
        initView();
    }

    public ScheduleEventView(Context context,int year,int month,int[] days,MonthAdapter adapter) {
        super(context);
        mContext = context;
        mDays = days;
        mYear = year;
        mMonth = month;
        initView();
    }

    private void initView(){
        mLockBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icon_private_event_white);
        mIntBitmapWith = mLockBitmap.getWidth();
        mIntBitmapHeight = mLockBitmap.getHeight();
        COLOR_BLUE_BACKGROUND = mContext.getResources().getColor(R.color.background_blue_color);
        COLOR_GREEN_BACKGROUND = mContext.getResources().getColor(R.color.background_green_color);
        COLOR_RED_FESTIVAL = mContext.getResources().getColor(R.color.red_launchr);
        COLOR_BULE_MEETING = mContext.getResources().getColor(R.color.blue_launchr);
        COLOR_GREEN_EVENT = mContext.getResources().getColor(R.color.green_launchr);
        COLOR_RED_THIN_FESTIVAL = mContext.getResources().getColor(R.color.thin_red);
        COLOR_BULE_THIN_MEETING = mContext.getResources().getColor(R.color.thin_blue);
        COLOR_GREEN_THIN_EVENT  = mContext.getResources().getColor(R.color.thin_green);
        COLOR_BLACK = Color.BLACK;
        COLOR_WHITE = Color.WHITE;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // 边框宽度
        mLineWidth = TTDensityUtil.dip2px(mContext,1);
        // 宽度设置与屏幕一样宽
        mWidth = dm.widthPixels;
        // 高度设置
//        mHeight = TTDensityUtil.dip2px(mContext,50);
//        mHeight = dm.heightPixels - TTDensityUtil.dip2px(mContext,75);
        // 单个格子的高度设置
        mGridHeight = mHeight / 4;
        // 单个格子的宽度设置
        mGridWidth = mWidth / 7;
        // 标签高度设置
        mTagWidth = TTDensityUtil.dip2px(mContext,2);
        mTextSize = TTDensityUtil.sp2px(mContext,10);
        // 间距大小设置
        mSpaceWidth = TTDensityUtil.dip2px(mContext,4);

    }

    public void setEventData(int year,int month,int[] days,List<EventEntity> entities,int height){
        mEvetSets = entities;
        mDays = days;
        mYear = year;
        mMonth = month;
        mHeight = height - TTDensityUtil.dip2px(mContext,20);
        initView();
        postInvalidate();
    }

    public void setEventData(int year,int month,int[] days,MonthAdapter adapter){
//        mEvetSets = entities;
        mDays = days;
        mYear = year;
        mMonth = month;
        initView();
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        mGrids = new boolean[7][4];
        mPaint = new Paint();
        mCanvas = canvas;
            if(mEvetSets != null){
                setEveryDayEvents();
            }
    }


    /** 绘制每一天的事件集合*/
    private void setEveryDayEvents(){

        for(int day = 0;day<mDays.length;day++){
            boolean soMore = false;
            int mIntMoreEvent =0;
            Calendar calendar = Calendar.getInstance();
            for(EventEntity entity : mEvetSets){
                long startTime = entity.getStartTime();
                long endTime = entity.getEndTime() - 1000;
                String eventEndTime =  ScheduleEventUtil.getAllTimeKey(calendar, endTime);
                String eventStartTime =  ScheduleEventUtil.getAllTimeKey(calendar, startTime);
                String timeKey = ScheduleEventUtil.getKey(mYear, mMonth, mDays[day]);
                if(MonthWeekFristDaySpecialEventDeal(calendar,eventStartTime,eventEndTime,mYear, mMonth, mDays[day],day)  || timeKey.equals(eventStartTime)) { //WeekFristDaySpecialEventDeal(calendar,eventStartTime,eventEndTime,mYear, mMonth, mDays[day],day) ||
                    if(!soMore){//未超出范围显示事件
                        boolean [] grids = mGrids[day];
                        int is = 0;
                        for(int i = 0;i<grids.length - 1;i++){
                            if(grids[i] == true){
                                is++;
                            }
                        }
                        if(is >= grids.length - 1){
                            soMore = true;
                            mIntMoreEvent ++;
                        }else{
                            //绘制事件
                            drawAtOneDayEvents(calendar, timeKey, eventEndTime,day,entity);
                        }
                    }else{//计算超出事件数量
                        mIntMoreEvent ++;
                    }
                }
            }
            if(mIntMoreEvent > 0){ //不显示的事件数量
                drawMoreEvent(day + 1,"+" + mIntMoreEvent);
            }
        }
    }
    /** 处理特殊的跨月周事件*/
    private boolean MonthWeekFristDaySpecialEventDeal(Calendar calendar,String eventStartTime,String eventEndTime,int year,int month,int day,int tag){

        if(day == 1){
            long dayTime = ScheduleEventUtil.getLongKey(year,month,day);
            long sTime = dayTime - Integer.valueOf(eventStartTime);
            long eTime = dayTime - Integer.valueOf(eventEndTime);
            if(sTime > 0 && eTime <= 0) {
                calendar.set(year,month - 1,day);
                return true;
            }
        }else{
            if(tag != 0 || day == 0 ){
                return false;
            }
            long dayTime = ScheduleEventUtil.getLongKey(year,month,day);
            long sTime = dayTime - Integer.valueOf(eventStartTime);
            long eTime = dayTime - Integer.valueOf(eventEndTime);
            if(sTime > 0 && eTime <= 0) {
                calendar.set(year,month - 1,day);
                return true;
            }
        }
         return false;
    }

    /** 一个事件的内容*/
    private void drawAtOneDayEvents(Calendar calendar ,String eventStartTime,String eventEndTime,int day,EventEntity entity) {

                if(eventStartTime.equals(eventEndTime)){// 一天事件
                    String content = entity.getTitle();
                    int isImprotant = entity.getIsImportant();
                    String type = entity.getType();
                    int isAllDay = entity.getIsAllDay();
                    int isVisible = entity.getIsVisible();
                    drawEventInCanvas(isVisible,day,1,type,isImprotant,isAllDay, content);
                }else{// 多天事件
                    int gridNum = 1;
                    boolean IsLeap = mCreateCalendar.JudgeLeapYear(mYear);
                    int daysOfMonth  = mCreateCalendar.MonthDays(IsLeap, mMonth);
                    do{
                        gridNum ++;
                        int dayNum = calendar.get(Calendar.DAY_OF_MONTH) + 1;
                        calendar.set(Calendar.DAY_OF_MONTH ,dayNum);
                        eventStartTime = ScheduleEventUtil.getAllTimeKey(calendar);
                    }while (!eventStartTime.equals(eventEndTime));
                    String content = entity.getTitle();
                    int isImprotant = entity.getIsImportant();
                    int isVisible = entity.getIsVisible();
                    String type = entity.getType();
                    int isAllDay = entity.getIsAllDay();
                    if((day + gridNum) >= mDays.length){
                        gridNum = mDays.length - day;
                        if((mDays[day] + gridNum) > daysOfMonth){
                            gridNum = daysOfMonth - mDays[day] + 1;
                        }
                    }
                    drawEventInCanvas(isVisible,day, gridNum, type, isImprotant,isAllDay, content);
                }
    }

    /** 选择绘制的事件类型*/
    private void selectEventType(int visible,int start,int y,int end,String type, int isImprotant,int isAllDay,String content) {
        if (type.equals("meeting")) { // 会议事件 蓝
            drawMeeting(visible,start, y, end, isImprotant,isAllDay, content);
        } else if (type.equals("event")) {// 事件 绿色
            drawEvent(visible,start, y, end, isImprotant,isAllDay, content);
        } else if (type.equals("statutory_festival") || type.equals("company_festival")) {// 假期 红色
            drawFestival(start,y,end,content);
        }else if(type.equals("event_sure")){ // 未选时间事件
            drawUnSelectEvent(visible,start, y, end, isImprotant,isAllDay, content);
        }
    }
    /** 绘制事件到视图中*/
    private void drawEventInCanvas(int visible,int start,int end,String type, int isImprotant,int isAllDay, String content) {

        boolean[] gridY = mGrids[start];
        for(int i = 0;i < gridY.length;i++){
            if(!(gridY[i] == true)) {
                selectEventType(visible,start + 1,i + 1,end,type,isImprotant,isAllDay,content);
                for(int j = 0;j < end;j++){//填充被占用的grid位置
                    if(start + j < 7){
                        mGrids[start + j][i] = true;
                    }
                }
                gridY[i] = true;
                i = gridY.length;
            }
        }

    }


    /** 绘制节日 起始x坐标 起始y坐标，所占格子数量grid，显示的文字text*/
    private void drawFestival(int X,int Y,int grid,String text){

        int startX = (X -1) * mGridWidth;
        int startY = (Y -1) * mGridHeight;

        mPaint.setColor(COLOR_RED_FESTIVAL);// 设置背景颜色红色
        mPaint.setStyle(Paint.Style.FILL);//设置填满
        mCanvas.drawRect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight, mPaint);// 长方形


        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeWidth((float)mLineWidth);
        mPaint.setColor(COLOR_RED_THIN_FESTIVAL);// 设置淡红边框
        Rect targetRect = new Rect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight);
        mCanvas.drawRect(targetRect, mPaint);

        // 居中绘制文字
        mPaint.setColor(COLOR_WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        mPaint.setTextAlign(Paint.Align.LEFT);
        float strWidth = mPaint.measureText(text);
        float singleWidth = strWidth / text.length();
        int GridWith = mGridWidth * grid - mTagWidth * 4;
        if(strWidth >= GridWith){// 判断文字是否超出显示范围
            int num = 0;
            if(GridWith %singleWidth >= 0.5){
                num = (int)(GridWith /singleWidth) - 1;
            }else{
                num = (int)(GridWith /singleWidth);
            }
            text = text.substring(0,num);
        }
        mCanvas.drawText(text,targetRect.left + mTagWidth * 2 , baseline, mPaint);

    }
    /** 绘制会议 起始x坐标 起始y坐标，所占格子数量grid，是否至急improtant，显示的文字text*/
    private void drawMeeting(int visible,int X,int Y,int grid,int improtant,int isAllDay,String text){

        int startX = (X -1) * mGridWidth;
        int startY = (Y -1) * mGridHeight;
        if(isAllDay == 1){//全天事件填充事件背景色
            mPaint.setColor(COLOR_BLUE_BACKGROUND);// 设置背景颜色
            mPaint.setStyle(Paint.Style.FILL);//设置填满
            mCanvas.drawRect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight, mPaint);// 长方形
        }
        //非全天事件不填充背景色描边显示
        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeWidth((float)mLineWidth);
        mPaint.setColor(COLOR_BULE_THIN_MEETING);// 设置淡蓝边框
        Rect targetRect = new Rect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight);
        mCanvas.drawRect(targetRect, mPaint);
        if(improtant == 1){
            mPaint.setColor(COLOR_RED_FESTIVAL);//绘制标签
        }else{
            mPaint.setColor(COLOR_BULE_MEETING);//绘制标签
        }
        drawEventTagIcon(visible,COLOR_BLACK,startX,startY,targetRect,grid,text);
//

    }
    /** 绘制日程事件 起始x坐标 起始y坐标，所占格子数量grid，是否全天isAllDay，是否至急improtant，显示的文字text*/
    private void drawEvent(int visible,int X,int Y,int grid,int improtant,int isAllDay,String text){

        int startX = (X -1) * mGridWidth;
        int startY = (Y -1) * mGridHeight;
        if(isAllDay == 1){
            mPaint.setColor(COLOR_GREEN_BACKGROUND);// 设置背景颜色
            mPaint.setStyle(Paint.Style.FILL);//设置填满
            mCanvas.drawRect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight, mPaint);// 长方形
        }
        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeWidth((float)mLineWidth);
        mPaint.setColor(COLOR_GREEN_THIN_EVENT);// 设置淡蓝边框
        Rect targetRect = new Rect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight);
        mCanvas.drawRect(targetRect, mPaint);
        //设置绘制标签颜色
        if(improtant == 1){
            mPaint.setColor(COLOR_RED_FESTIVAL);
        } else {
            mPaint.setColor(COLOR_GREEN_EVENT);
        }
        drawEventTagIcon(visible,COLOR_BLACK,startX,startY,targetRect,grid,text);
    }

    /** 绘制未选时间日程事件 起始x坐标 起始y坐标，所占格子数量grid，是否至急improtant，显示的文字text*/
    private void drawUnSelectEvent(int visible,int X,int Y,int grid,int improtant,int isAllDay,String text){

        int startX = (X -1) * mGridWidth;
        int startY = (Y -1) * mGridHeight;


        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeWidth((float) mLineWidth);
        mPaint.setColor(COLOR_GREEN_THIN_EVENT);// 设置绿色边框
        PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);// 虚线边框
        mPaint.setPathEffect(effects);
        Rect targetRect = new Rect(startX, mSpaceWidth + startY, startX + mGridWidth * grid, startY + mGridHeight);
        mCanvas.drawRect(targetRect, mPaint);
        mPaint.setPathEffect(null);
        if(improtant == 1){
            mPaint.setColor(COLOR_RED_FESTIVAL);//绘制标签
        }else{
            mPaint.setColor(COLOR_GREEN_EVENT);
        }
        drawEventTagIcon(visible,COLOR_BLACK,startX,startY,targetRect,grid,text);
    }

    /** 绘制不能再显示的事件数量 所在X坐标*/
    private void drawMoreEvent(int X,String text){
        int startX = (X -1) * mGridWidth;
        int startY = 3 * mGridHeight;

        mPaint.setColor(COLOR_BULE_MEETING); //绘制文字
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth((float) 1.0);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);

        float strWidth = mPaint.measureText(text);
        int GridWith = mGridWidth - mTagWidth * 2;
        if(strWidth >= GridWith){// 判断文字是否超出显示范围
            text = "+N";
        }
        mCanvas.drawText(text,startX + mTagWidth * 2,mSpaceWidth + startY + mGridHeight / 2,mPaint);
    }

    /** 绘制文字内容*/
    private void drawEventContentText(int paintColor,Rect targetRect,int grid,int tagWith,String text){
        // 居中绘制文字
        mPaint.setColor(paintColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        mPaint.setTextAlign(Paint.Align.LEFT);
        float strWidth = mPaint.measureText(text);
        float singleWidth = strWidth / text.length();
        int GridWith = mGridWidth * grid - tagWith;
        if(strWidth >= GridWith){// 判断文字是否超出显示范围
            int num = 0;
            if(GridWith %singleWidth >= 0.5){
                num = (int)(GridWith /singleWidth) - 1;
            }else{
                num = (int)(GridWith /singleWidth);
            }
            text = text.substring(0,num);
        }
        mCanvas.drawText(text, targetRect.left + tagWith, baseline, mPaint);
    }
    /** 绘制事件的图标显示*/
    private void drawEventTagIcon(int isVisible,int textColor,int startX,int startY,Rect targetRect,int grid,String text){
        if(isVisible == 0){
            mPaint.setStyle(Paint.Style.FILL);
            //绘制直线
            mCanvas.drawRect(startX, mSpaceWidth + startY, startX + mIntBitmapWith * 2, startY + mGridHeight, mPaint);
            mCanvas.drawBitmap(mLockBitmap,startX + mIntBitmapWith/2,mSpaceWidth + startY + mIntBitmapHeight /6,mPaint);
            // 居中绘制文字
            drawEventContentText(textColor,targetRect,grid,mIntBitmapWith * 2 + mTagWidth * 2,text);
        }else{
            //设置线宽
            mPaint.setStrokeWidth((float) mTagWidth);
            //绘制直线
            mCanvas.drawLine(startX, mSpaceWidth + startY,startX,startY + mGridHeight, mPaint);
            // 居中绘制文字
            drawEventContentText(textColor,targetRect,grid,mTagWidth * 4,text);
        }
    }
}
