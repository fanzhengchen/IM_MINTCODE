// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.meeting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MeetingTimerDialog$$ViewBinder<T extends com.mintcode.launchr.app.meeting.MeetingTimerDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428293, "field 'mTimer'");
    target.mTimer = finder.castView(view, 2131428293, "field 'mTimer'");
    view = finder.findRequiredView(source, 2131428343, "field 'mTvConflict'");
    target.mTvConflict = finder.castView(view, 2131428343, "field 'mTvConflict'");
    view = finder.findRequiredView(source, 2131428349, "field 'mTvRoom'");
    target.mTvRoom = finder.castView(view, 2131428349, "field 'mTvRoom'");
    view = finder.findRequiredView(source, 2131428348, "method 'selectRoom'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectRoom();
        }
      });
    view = finder.findRequiredView(source, 2131428347, "method 'setGetOutAddress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setGetOutAddress();
        }
      });
    view = finder.findRequiredView(source, 2131428346, "method 'setMeetingRoom'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setMeetingRoom();
        }
      });
    view = finder.findRequiredView(source, 2131428344, "method 'seeConfigPersonEvent'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.seeConfigPersonEvent();
        }
      });
    view = finder.findRequiredView(source, 2131427593, "method 'saveTimeSelect'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveTimeSelect();
        }
      });
    view = finder.findRequiredView(source, 2131428341, "method 'cancelSelect'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelSelect();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTimer = null;
    target.mTvConflict = null;
    target.mTvRoom = null;
  }
}
