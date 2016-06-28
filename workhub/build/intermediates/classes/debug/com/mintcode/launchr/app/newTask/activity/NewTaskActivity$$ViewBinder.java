// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.newTask.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewTaskActivity$$ViewBinder<T extends com.mintcode.launchr.app.newTask.activity.NewTaskActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428192, "field 'mTaskContent'");
    target.mTaskContent = finder.castView(view, 2131428192, "field 'mTaskContent'");
  }

  @Override public void unbind(T target) {
    target.mTaskContent = null;
  }
}
