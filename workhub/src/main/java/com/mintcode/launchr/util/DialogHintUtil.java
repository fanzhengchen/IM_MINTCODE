package com.mintcode.launchr.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.CommentsListView;
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleEventDetailActivity;
import com.mintcode.launchr.app.newTask.activity.TaskDetailActivity;

/**
 * Created by JulyYu on 2016/4/21.
 */
public class DialogHintUtil {


    public static void selectStandbyTime(final Context context, final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getText(R.string.sbumit_choose_time));
        builder.setPositiveButton(context.getText(R.string.is_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(context instanceof ScheduleEventDetailActivity){
                    ((ScheduleEventDetailActivity)context).selectWaitTime();
                }
            }
        });

        builder.setNegativeButton(context.getText(R.string.is_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((RadioButton)view).setChecked(false);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void delectComment(final Context context, final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getText(R.string.decide_to_delect));
        builder.setPositiveButton(context.getText(R.string.is_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(view instanceof CommentsListView){
                    ((CommentsListView)view).delectComment();
                }
            }
        });

        builder.setNegativeButton(context.getText(R.string.is_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public static void delectEvent(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getText(R.string.decide_to_delect));
        builder.setPositiveButton(context.getText(R.string.is_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(context instanceof ApproveDetailActivity){
                    ((ApproveDetailActivity)context).delect();
                }else if(context instanceof ScheduleEventDetailActivity){
                    ((ScheduleEventDetailActivity)context).delSchedule(0);
                }else if(context instanceof TaskDetailActivity){
                    ((TaskDetailActivity)context).deleteTask();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(context.getText(R.string.is_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

}
