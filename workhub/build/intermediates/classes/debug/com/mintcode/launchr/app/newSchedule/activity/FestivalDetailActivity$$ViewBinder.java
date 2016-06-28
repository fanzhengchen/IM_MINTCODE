// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.newSchedule.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FestivalDetailActivity$$ViewBinder<T extends com.mintcode.launchr.app.newSchedule.activity.FestivalDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427604, "field 'mTvName'");
    target.mTvName = finder.castView(view, 2131427604, "field 'mTvName'");
    view = finder.findRequiredView(source, 2131427605, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131427605, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131427527, "method 'back'");
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
    target.mTvName = null;
    target.mTvTime = null;
  }
}
