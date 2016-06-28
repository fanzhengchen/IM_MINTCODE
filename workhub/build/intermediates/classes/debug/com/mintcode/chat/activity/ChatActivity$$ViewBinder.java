// Generated code from Butter Knife. Do not modify!
package com.mintcode.chat.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.mintcode.chat.activity.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427524, "field 'mTvMoreCancel' and method 'CancelMore'");
    target.mTvMoreCancel = finder.castView(view, 2131427524, "field 'mTvMoreCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.CancelMore();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTvMoreCancel = null;
  }
}
