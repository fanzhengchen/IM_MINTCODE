package com.mintcode.chat.audio;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.mintcode.RM;
import com.mintcode.chat.ChatViewHolder;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.chat.ChatViewHolder;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.R;

/**
 * 语音播放动画
 *
 * @author ChristLu
 */
public class AudioPlayingAnimation {
    static ChatViewHolder sViewHolder;
    static MessageItem sMessageItem;
    static Drawable[] sOrginalDrawables;

    private static long sPlayMsgId = -1;

    public static void play(ChatViewHolder vh, MessageItem mi) {
        sViewHolder = vh;
        sMessageItem = mi;
        sPlayMsgId = mi.getMsgId();
        sOrginalDrawables = vh.textSoundView.getCompoundDrawables();
//        Context context = vh.textSoundView.getContext();
        if (mi.getCmd() == MessageItem.TYPE_RECV) {
            AnimationDrawable ad = (AnimationDrawable) ContextCompat.getDrawable(
                    vh.textSoundView.getContext(), R.drawable.anim_playing_left);
            ad.setBounds(0, 0, ad.getIntrinsicWidth(), ad.getIntrinsicHeight());
            vh.textSoundView.setCompoundDrawables(ad, null, null, null);
            ad.start();
        } else if (mi.getCmd() == MessageItem.TYPE_SEND) {
            AnimationDrawable ad = (AnimationDrawable) ContextCompat.getDrawable(
                    vh.textSoundView.getContext(), R.drawable.anim_playing_right);
            ad.setBounds(0, 0, ad.getIntrinsicWidth(), ad.getIntrinsicHeight());
            vh.textSoundView.setCompoundDrawables(null, null, ad, null);
            ad.start();
        }
    }

    public static void stop() {
        if (sViewHolder != null && sMessageItem != null) {
            final Drawable[] d = sOrginalDrawables;
            sViewHolder.textSoundView.setCompoundDrawables(d[0], d[1], d[2], d[3]);
            sViewHolder = null;
            sMessageItem = null;
            sPlayMsgId = -1;
            sOrginalDrawables = null;
        }
    }
}
