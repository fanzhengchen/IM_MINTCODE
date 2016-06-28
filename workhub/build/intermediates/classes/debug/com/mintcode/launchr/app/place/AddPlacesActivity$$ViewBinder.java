// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.place;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddPlacesActivity$$ViewBinder<T extends com.mintcode.launchr.app.place.AddPlacesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427440, "field 'mEditInput'");
    target.mEditInput = finder.castView(view, 2131427440, "field 'mEditInput'");
    view = finder.findRequiredView(source, 2131427444, "field 'mLvResult'");
    target.mLvResult = finder.castView(view, 2131427444, "field 'mLvResult'");
    view = finder.findRequiredView(source, 2131427441, "method 'clearText'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clearText();
        }
      });
    view = finder.findRequiredView(source, 2131427436, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131427437, "method 'Save'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Save();
        }
      });
    view = finder.findRequiredView(source, 2131427442, "method 'chooseCurrentPlace'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.chooseCurrentPlace();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mEditInput = null;
    target.mLvResult = null;
  }
}
