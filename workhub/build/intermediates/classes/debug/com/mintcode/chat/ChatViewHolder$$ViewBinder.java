// Generated code from Butter Knife. Do not modify!
package com.mintcode.chat;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatViewHolder$$ViewBinder<T extends com.mintcode.chat.ChatViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131428807, null);
    target.textSoundView = finder.castView(view, 2131428807, "field 'textSoundView'");
    view = finder.findOptionalView(source, 2131428773, null);
    target.time = finder.castView(view, 2131428773, "field 'time'");
    view = finder.findOptionalView(source, 2131428777, null);
    target.textView = finder.castView(view, 2131428777, "field 'textView'");
    view = finder.findOptionalView(source, 2131428775, null);
    target.tvName = finder.castView(view, 2131428775, "field 'tvName'");
    view = finder.findOptionalView(source, 2131428774, null);
    target.ivAvatar = finder.castView(view, 2131428774, "field 'ivAvatar'");
    view = finder.findOptionalView(source, 2131428770, null);
    target.progressBarSending = finder.castView(view, 2131428770, "field 'progressBarSending'");
    view = finder.findOptionalView(source, 2131428808, null);
    target.tvSoundTime = finder.castView(view, 2131428808, "field 'tvSoundTime'");
    view = finder.findOptionalView(source, 2131428809, null);
    target.ivReadMark = finder.castView(view, 2131428809, "field 'ivReadMark'");
    view = finder.findOptionalView(source, 2131428769, null);
    target.ivFailed = finder.castView(view, 2131428769, "field 'ivFailed'");
    view = finder.findOptionalView(source, 2131428812, null);
    target.ivVidio = finder.castView(view, 2131428812, "field 'ivVidio'");
    view = finder.findOptionalView(source, 2131428795, null);
    target.ivRead = finder.castView(view, 2131428795, "field 'ivRead'");
    view = finder.findOptionalView(source, 2131427607, null);
    target.tvFileName = finder.castView(view, 2131427607, "field 'tvFileName'");
    view = finder.findOptionalView(source, 2131428647, null);
    target.tvFileSize = finder.castView(view, 2131428647, "field 'tvFileSize'");
    view = finder.findOptionalView(source, 2131428646, null);
    target.ivFileIcon = finder.castView(view, 2131428646, "field 'ivFileIcon'");
    view = finder.findOptionalView(source, 2131428811, null);
    target.relFileLayout = finder.castView(view, 2131428811, "field 'relFileLayout'");
    view = finder.findOptionalView(source, 2131428817, null);
    target.tvPercent = finder.castView(view, 2131428817, "field 'tvPercent'");
    view = finder.findOptionalView(source, 2131428810, null);
    target.ivMarkPoint = finder.castView(view, 2131428810, "field 'ivMarkPoint'");
    view = finder.findOptionalView(source, 2131428803, null);
    target.mergeMsg = finder.castView(view, 2131428803, "field 'mergeMsg'");
  }

  @Override public void unbind(T target) {
    target.textSoundView = null;
    target.time = null;
    target.textView = null;
    target.tvName = null;
    target.ivAvatar = null;
    target.progressBarSending = null;
    target.tvSoundTime = null;
    target.ivReadMark = null;
    target.ivFailed = null;
    target.ivVidio = null;
    target.ivRead = null;
    target.tvFileName = null;
    target.tvFileSize = null;
    target.ivFileIcon = null;
    target.relFileLayout = null;
    target.tvPercent = null;
    target.ivMarkPoint = null;
    target.mergeMsg = null;
  }
}
