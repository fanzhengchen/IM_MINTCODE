// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.message.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PersonSettingActivity$$ViewBinder<T extends com.mintcode.launchr.message.activity.PersonSettingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427648, "field 'mcbDND'");
    target.mcbDND = finder.castView(view, 2131427648, "field 'mcbDND'");
    view = finder.findRequiredView(source, 2131427629, "field 'mTvName'");
    target.mTvName = finder.castView(view, 2131427629, "field 'mTvName'");
    view = finder.findRequiredView(source, 2131427856, "field 'mIvAvatar'");
    target.mIvAvatar = finder.castView(view, 2131427856, "field 'mIvAvatar'");
    view = finder.findRequiredView(source, 2131427857, "field 'mTvDept'");
    target.mTvDept = finder.castView(view, 2131427857, "field 'mTvDept'");
    view = finder.findRequiredView(source, 2131427527, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131427649, "method 'clearHistory'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clearHistory();
        }
      });
    view = finder.findRequiredView(source, 2131427858, "method 'createGroup'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.createGroup();
        }
      });
    view = finder.findRequiredView(source, 2131427855, "method 'userDetail'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.userDetail();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mcbDND = null;
    target.mTvName = null;
    target.mIvAvatar = null;
    target.mTvDept = null;
  }
}
