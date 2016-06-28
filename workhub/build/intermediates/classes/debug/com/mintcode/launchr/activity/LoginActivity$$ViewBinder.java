// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.mintcode.launchr.activity.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427679, "method 'showPassword'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showPassword();
        }
      });
    view = finder.findRequiredView(source, 2131427681, "method 'accessLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.accessLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427682, "method 'tryApplyUse'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.tryApplyUse();
        }
      });
    view = finder.findRequiredView(source, 2131427683, "method 'forgetPwd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.forgetPwd();
        }
      });
  }

  @Override public void unbind(T target) {
  }
}
