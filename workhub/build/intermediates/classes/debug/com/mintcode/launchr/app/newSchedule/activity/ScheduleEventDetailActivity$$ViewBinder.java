// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.newSchedule.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ScheduleEventDetailActivity$$ViewBinder<T extends com.mintcode.launchr.app.newSchedule.activity.ScheduleEventDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428000, "field 'mRbAlternate1'");
    target.mRbAlternate1 = finder.castView(view, 2131428000, "field 'mRbAlternate1'");
    view = finder.findRequiredView(source, 2131428004, "field 'mRbAlternate2'");
    target.mRbAlternate2 = finder.castView(view, 2131428004, "field 'mRbAlternate2'");
    view = finder.findRequiredView(source, 2131428008, "field 'mRbAlternate3'");
    target.mRbAlternate3 = finder.castView(view, 2131428008, "field 'mRbAlternate3'");
    view = finder.findRequiredView(source, 2131427994, "field 'mIvImprotantCircle'");
    target.mIvImprotantCircle = finder.castView(view, 2131427994, "field 'mIvImprotantCircle'");
    view = finder.findRequiredView(source, 2131427995, "field 'mTvImprotantText'");
    target.mTvImprotantText = finder.castView(view, 2131427995, "field 'mTvImprotantText'");
    view = finder.findRequiredView(source, 2131427993, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131427993, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131427999, "field 'mTvTime1'");
    target.mTvTime1 = finder.castView(view, 2131427999, "field 'mTvTime1'");
    view = finder.findRequiredView(source, 2131428003, "field 'mTvTime2'");
    target.mTvTime2 = finder.castView(view, 2131428003, "field 'mTvTime2'");
    view = finder.findRequiredView(source, 2131428007, "field 'mTvTime3'");
    target.mTvTime3 = finder.castView(view, 2131428007, "field 'mTvTime3'");
    view = finder.findRequiredView(source, 2131427997, "field 'mRlTime1' and method 'selectTime1'");
    target.mRlTime1 = finder.castView(view, 2131427997, "field 'mRlTime1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectTime1();
        }
      });
    view = finder.findRequiredView(source, 2131428001, "field 'mRlTime2' and method 'selectTime2'");
    target.mRlTime2 = finder.castView(view, 2131428001, "field 'mRlTime2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectTime2();
        }
      });
    view = finder.findRequiredView(source, 2131428005, "field 'mRlTime3' and method 'selectTime3'");
    target.mRlTime3 = finder.castView(view, 2131428005, "field 'mRlTime3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectTime3();
        }
      });
    view = finder.findRequiredView(source, 2131428012, "field 'mTvPlace'");
    target.mTvPlace = finder.castView(view, 2131428012, "field 'mTvPlace'");
    view = finder.findRequiredView(source, 2131428011, "field 'mTvProvience'");
    target.mTvProvience = finder.castView(view, 2131428011, "field 'mTvProvience'");
    view = finder.findRequiredView(source, 2131427717, "field 'mLlMap'");
    target.mLlMap = finder.castView(view, 2131427717, "field 'mLlMap'");
    view = finder.findRequiredView(source, 2131428014, "field 'mTvRemind'");
    target.mTvRemind = finder.castView(view, 2131428014, "field 'mTvRemind'");
    view = finder.findRequiredView(source, 2131427731, "field 'mCommentView'");
    target.mCommentView = finder.castView(view, 2131427731, "field 'mCommentView'");
    view = finder.findRequiredView(source, 2131427730, "field 'mCommentListView'");
    target.mCommentListView = finder.castView(view, 2131427730, "field 'mCommentListView'");
    view = finder.findRequiredView(source, 2131428015, "field 'mLinRemark'");
    target.mLinRemark = finder.castView(view, 2131428015, "field 'mLinRemark'");
    view = finder.findRequiredView(source, 2131428016, "field 'mTvRemark'");
    target.mTvRemark = finder.castView(view, 2131428016, "field 'mTvRemark'");
    view = finder.findRequiredView(source, 2131427485, "field 'mIvMoreHandle' and method 'doMoreHandle'");
    target.mIvMoreHandle = finder.castView(view, 2131427485, "field 'mIvMoreHandle'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.doMoreHandle();
        }
      });
    view = finder.findRequiredView(source, 2131428009, "method 'copyTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.copyTime();
        }
      });
    view = finder.findRequiredView(source, 2131427991, "method 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mRbAlternate1 = null;
    target.mRbAlternate2 = null;
    target.mRbAlternate3 = null;
    target.mIvImprotantCircle = null;
    target.mTvImprotantText = null;
    target.mTvTitle = null;
    target.mTvTime1 = null;
    target.mTvTime2 = null;
    target.mTvTime3 = null;
    target.mRlTime1 = null;
    target.mRlTime2 = null;
    target.mRlTime3 = null;
    target.mTvPlace = null;
    target.mTvProvience = null;
    target.mLlMap = null;
    target.mTvRemind = null;
    target.mCommentView = null;
    target.mCommentListView = null;
    target.mLinRemark = null;
    target.mTvRemark = null;
    target.mIvMoreHandle = null;
  }
}
