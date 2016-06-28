// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.newTask.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FinalTimeDialog$$ViewBinder<T extends com.mintcode.launchr.app.newTask.view.FinalTimeDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428357, "field 'mTimer'");
    target.mTimer = finder.castView(view, 2131428357, "field 'mTimer'");
    view = finder.findRequiredView(source, 2131428299, "field 'mCbAllDay'");
    target.mCbAllDay = finder.castView(view, 2131428299, "field 'mCbAllDay'");
    view = finder.findRequiredView(source, 2131427543, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131427543, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131428355, "method 'cancelDismiss'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelDismiss();
        }
      });
    view = finder.findRequiredView(source, 2131428356, "method 'saveTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveTime();
        }
      });
    view = finder.findRequiredView(source, 2131428358, "method 'nullTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.nullTime();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTimer = null;
    target.mCbAllDay = null;
    target.mTvTitle = null;
  }
}
