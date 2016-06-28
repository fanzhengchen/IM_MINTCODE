// Generated code from Butter Knife. Do not modify!
package com.mintcode.launchr.app.meeting.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MeetingDetailActivity$$ViewBinder<T extends com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427710, "field 'mMeetingTitile'");
    target.mMeetingTitile = finder.castView(view, 2131427710, "field 'mMeetingTitile'");
    view = finder.findRequiredView(source, 2131427711, "field 'mMeetingTime'");
    target.mMeetingTime = finder.castView(view, 2131427711, "field 'mMeetingTime'");
    view = finder.findRequiredView(source, 2131427712, "field 'mMeetingRoom'");
    target.mMeetingRoom = finder.castView(view, 2131427712, "field 'mMeetingRoom'");
    view = finder.findRequiredView(source, 2131427717, "field 'mLlMap'");
    target.mLlMap = finder.castView(view, 2131427717, "field 'mLlMap'");
    view = finder.findRequiredView(source, 2131427721, "field 'mTvAttendPeople'");
    target.mTvAttendPeople = finder.castView(view, 2131427721, "field 'mTvAttendPeople'");
    view = finder.findRequiredView(source, 2131427723, "field 'mTvChosePeople'");
    target.mTvChosePeople = finder.castView(view, 2131427723, "field 'mTvChosePeople'");
    view = finder.findRequiredView(source, 2131427725, "field 'mTvRepeat'");
    target.mTvRepeat = finder.castView(view, 2131427725, "field 'mTvRepeat'");
    view = finder.findRequiredView(source, 2131427727, "field 'mTvNotifaction'");
    target.mTvNotifaction = finder.castView(view, 2131427727, "field 'mTvNotifaction'");
    view = finder.findRequiredView(source, 2131427729, "field 'mTvMark'");
    target.mTvMark = finder.castView(view, 2131427729, "field 'mTvMark'");
    view = finder.findRequiredView(source, 2131427722, "field 'mLlToAttend'");
    target.mLlToAttend = finder.castView(view, 2131427722, "field 'mLlToAttend'");
    view = finder.findRequiredView(source, 2131427724, "field 'mLlRepeact'");
    target.mLlRepeact = finder.castView(view, 2131427724, "field 'mLlRepeact'");
    view = finder.findRequiredView(source, 2131427726, "field 'mLlNofication'");
    target.mLlNofication = finder.castView(view, 2131427726, "field 'mLlNofication'");
    view = finder.findRequiredView(source, 2131427728, "field 'mLlRemark'");
    target.mLlRemark = finder.castView(view, 2131427728, "field 'mLlRemark'");
    view = finder.findRequiredView(source, 2131427731, "field 'mCommentsView'");
    target.mCommentsView = finder.castView(view, 2131427731, "field 'mCommentsView'");
    view = finder.findRequiredView(source, 2131427730, "field 'mCommentsListView'");
    target.mCommentsListView = finder.castView(view, 2131427730, "field 'mCommentsListView'");
    view = finder.findRequiredView(source, 2131427485, "field 'mIvMoreHandle' and method 'doMoreHandle'");
    target.mIvMoreHandle = finder.castView(view, 2131427485, "field 'mIvMoreHandle'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.doMoreHandle();
        }
      });
    view = finder.findRequiredView(source, 2131427713, "field 'mLinEnter'");
    target.mLinEnter = finder.castView(view, 2131427713, "field 'mLinEnter'");
    view = finder.findRequiredView(source, 2131427707, "method 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
    view = finder.findRequiredView(source, 2131427714, "method 'attendMeeting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.attendMeeting();
        }
      });
    view = finder.findRequiredView(source, 2131427715, "method 'unAttendMeeting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.unAttendMeeting();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mMeetingTitile = null;
    target.mMeetingTime = null;
    target.mMeetingRoom = null;
    target.mLlMap = null;
    target.mTvAttendPeople = null;
    target.mTvChosePeople = null;
    target.mTvRepeat = null;
    target.mTvNotifaction = null;
    target.mTvMark = null;
    target.mLlToAttend = null;
    target.mLlRepeact = null;
    target.mLlNofication = null;
    target.mLlRemark = null;
    target.mCommentsView = null;
    target.mCommentsListView = null;
    target.mIvMoreHandle = null;
    target.mLinEnter = null;
  }
}
