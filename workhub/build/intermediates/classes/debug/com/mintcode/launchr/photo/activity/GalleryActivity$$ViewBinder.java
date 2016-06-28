// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.photo.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GalleryActivity$$ViewBinder<T extends com.mintcode.launchr.photo.activity.GalleryActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427900, "field 'mIvBack' and method 'onBack'");
    target.mIvBack = finder.castView(view, 2131427900, "field 'mIvBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mIvBack = null;
  }
}
