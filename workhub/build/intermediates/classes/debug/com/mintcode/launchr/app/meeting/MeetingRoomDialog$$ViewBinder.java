// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.meeting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MeetingRoomDialog$$ViewBinder<T extends com.mintcode.launchr.app.meeting.MeetingRoomDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428359, "field 'mNpRoom'");
    target.mNpRoom = finder.castView(view, 2131428359, "field 'mNpRoom'");
    view = finder.findRequiredView(source, 2131428360, "method 'selectMeetingRoom'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectMeetingRoom();
        }
      });
    view = finder.findRequiredView(source, 2131428361, "method 'cancelSelect'");
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
    target.mNpRoom = null;
  }
}
