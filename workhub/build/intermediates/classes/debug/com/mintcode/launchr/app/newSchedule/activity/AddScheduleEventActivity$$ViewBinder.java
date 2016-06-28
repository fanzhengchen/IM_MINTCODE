// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.newSchedule.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddScheduleEventActivity$$ViewBinder<T extends com.mintcode.launchr.app.newSchedule.activity.AddScheduleEventActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427955, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131427955, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131427971, "field 'mTvAddPlace' and method 'addPlaceXY'");
    target.mTvAddPlace = finder.castView(view, 2131427971, "field 'mTvAddPlace'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addPlaceXY();
        }
      });
    view = finder.findRequiredView(source, 2131427717, "field 'mLlMap'");
    target.mLlMap = finder.castView(view, 2131427717, "field 'mLlMap'");
    view = finder.findRequiredView(source, 2131427975, "field 'mCbSetImportant'");
    target.mCbSetImportant = finder.castView(view, 2131427975, "field 'mCbSetImportant'");
    view = finder.findRequiredView(source, 2131427962, "field 'mLlAlternateTime1'");
    target.mLlAlternateTime1 = finder.castView(view, 2131427962, "field 'mLlAlternateTime1'");
    view = finder.findRequiredView(source, 2131427964, "field 'mLlAlternateTime2'");
    target.mLlAlternateTime2 = finder.castView(view, 2131427964, "field 'mLlAlternateTime2'");
    view = finder.findRequiredView(source, 2131427966, "field 'mLlAlternateTime3'");
    target.mLlAlternateTime3 = finder.castView(view, 2131427966, "field 'mLlAlternateTime3'");
    view = finder.findRequiredView(source, 2131427963, "field 'mTvAlternateTime1'");
    target.mTvAlternateTime1 = finder.castView(view, 2131427963, "field 'mTvAlternateTime1'");
    view = finder.findRequiredView(source, 2131427965, "field 'mTvAlternateTime2'");
    target.mTvAlternateTime2 = finder.castView(view, 2131427965, "field 'mTvAlternateTime2'");
    view = finder.findRequiredView(source, 2131427967, "field 'mTvAlternateTime3'");
    target.mTvAlternateTime3 = finder.castView(view, 2131427967, "field 'mTvAlternateTime3'");
    view = finder.findRequiredView(source, 2131427958, "field 'mEdtTitle'");
    target.mEdtTitle = finder.castView(view, 2131427958, "field 'mEdtTitle'");
    view = finder.findRequiredView(source, 2131427970, "field 'mTvPlace'");
    target.mTvPlace = finder.castView(view, 2131427970, "field 'mTvPlace'");
    view = finder.findRequiredView(source, 2131427974, "field 'mTvRepeat'");
    target.mTvRepeat = finder.castView(view, 2131427974, "field 'mTvRepeat'");
    view = finder.findRequiredView(source, 2131427977, "field 'mTvNotification'");
    target.mTvNotification = finder.castView(view, 2131427977, "field 'mTvNotification'");
    view = finder.findRequiredView(source, 2131427978, "field 'mEdtRemark'");
    target.mEdtRemark = finder.castView(view, 2131427978, "field 'mEdtRemark'");
    view = finder.findRequiredView(source, 2131427824, "field 'mCbOnlyMeSee'");
    target.mCbOnlyMeSee = finder.castView(view, 2131427824, "field 'mCbOnlyMeSee'");
    view = finder.findRequiredView(source, 2131427972, "field 'LLReapet'");
    target.LLReapet = finder.castView(view, 2131427972, "field 'LLReapet'");
    view = finder.findRequiredView(source, 2131427956, "method 'saveEvent'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveEvent();
        }
      });
    view = finder.findRequiredView(source, 2131427954, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131427959, "method 'setDayTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setDayTime();
        }
      });
    view = finder.findRequiredView(source, 2131427960, "method 'addTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addTime();
        }
      });
    view = finder.findRequiredView(source, 2131427973, "method 'setRepeat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setRepeat();
        }
      });
    view = finder.findRequiredView(source, 2131427976, "method 'setNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNotification();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTvTitle = null;
    target.mTvAddPlace = null;
    target.mLlMap = null;
    target.mCbSetImportant = null;
    target.mLlAlternateTime1 = null;
    target.mLlAlternateTime2 = null;
    target.mLlAlternateTime3 = null;
    target.mTvAlternateTime1 = null;
    target.mTvAlternateTime2 = null;
    target.mTvAlternateTime3 = null;
    target.mEdtTitle = null;
    target.mTvPlace = null;
    target.mTvRepeat = null;
    target.mTvNotification = null;
    target.mEdtRemark = null;
    target.mCbOnlyMeSee = null;
    target.LLReapet = null;
  }
}
