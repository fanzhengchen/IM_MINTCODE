// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectPicTypeWindowView$$ViewBinder<T extends com.mintcode.launchr.view.SelectPicTypeWindowView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428864, "field 'mTvCamerSelect' and method 'openCamer'");
    target.mTvCamerSelect = finder.castView(view, 2131428864, "field 'mTvCamerSelect'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openCamer();
        }
      });
    view = finder.findRequiredView(source, 2131428866, "field 'mTvAblumSelect' and method 'openAblum'");
    target.mTvAblumSelect = finder.castView(view, 2131428866, "field 'mTvAblumSelect'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openAblum();
        }
      });
    view = finder.findRequiredView(source, 2131428861, "method 'closeWindow'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.closeWindow();
        }
      });
    view = finder.findRequiredView(source, 2131428863, "method 'closeWindow'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.closeWindow();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTvCamerSelect = null;
    target.mTvAblumSelect = null;
  }
}
