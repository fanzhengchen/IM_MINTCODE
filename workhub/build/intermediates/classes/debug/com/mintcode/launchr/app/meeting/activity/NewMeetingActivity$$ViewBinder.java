// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.meeting.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewMeetingActivity$$ViewBinder<T extends com.mintcode.launchr.app.meeting.activity.NewMeetingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427809, "field 'mEtTitle'");
    target.mEtTitle = finder.castView(view, 2131427809, "field 'mEtTitle'");
    view = finder.findRequiredView(source, 2131427813, "field 'mTvHaveToName'");
    target.mTvHaveToName = finder.castView(view, 2131427813, "field 'mTvHaveToName'");
    view = finder.findRequiredView(source, 2131427816, "field 'mTvChooseName'");
    target.mTvChooseName = finder.castView(view, 2131427816, "field 'mTvChooseName'");
    view = finder.findRequiredView(source, 2131427821, "field 'mTvRepeatName'");
    target.mTvRepeatName = finder.castView(view, 2131427821, "field 'mTvRepeatName'");
    view = finder.findRequiredView(source, 2131427823, "field 'mTvNoticeName'");
    target.mTvNoticeName = finder.castView(view, 2131427823, "field 'mTvNoticeName'");
    view = finder.findRequiredView(source, 2131427824, "field 'mCbSee'");
    target.mCbSee = finder.castView(view, 2131427824, "field 'mCbSee'");
    view = finder.findRequiredView(source, 2131427825, "field 'mEtRemark'");
    target.mEtRemark = finder.castView(view, 2131427825, "field 'mEtRemark'");
    view = finder.findRequiredView(source, 2131427717, "field 'mLlMap'");
    target.mLlMap = finder.castView(view, 2131427717, "field 'mLlMap'");
    view = finder.findRequiredView(source, 2131427818, "field 'mTvChooseTime'");
    target.mTvChooseTime = finder.castView(view, 2131427818, "field 'mTvChooseTime'");
    view = finder.findRequiredView(source, 2131427819, "field 'mTvChooseRoom'");
    target.mTvChooseRoom = finder.castView(view, 2131427819, "field 'mTvChooseRoom'");
    view = finder.findRequiredView(source, 2131427810, "method 'selectHaveToPerson'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectHaveToPerson();
        }
      });
    view = finder.findRequiredView(source, 2131427814, "method 'selectChosePerson'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectChosePerson();
        }
      });
    view = finder.findRequiredView(source, 2131427820, "method 'repeatChose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.repeatChose();
        }
      });
    view = finder.findRequiredView(source, 2131427822, "method 'noticeChose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.noticeChose();
        }
      });
    view = finder.findRequiredView(source, 2131427436, "method 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
    view = finder.findRequiredView(source, 2131427437, "method 'saveMeeting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveMeeting();
        }
      });
    view = finder.findRequiredView(source, 2131427817, "method 'showTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showTime();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mEtTitle = null;
    target.mTvHaveToName = null;
    target.mTvChooseName = null;
    target.mTvRepeatName = null;
    target.mTvNoticeName = null;
    target.mCbSee = null;
    target.mEtRemark = null;
    target.mLlMap = null;
    target.mTvChooseTime = null;
    target.mTvChooseRoom = null;
  }
}
