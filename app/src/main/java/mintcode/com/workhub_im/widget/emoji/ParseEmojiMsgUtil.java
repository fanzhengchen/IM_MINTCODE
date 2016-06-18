package mintcode.com.workhub_im.widget.emoji;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mintcode.com.workhub_im.util.DensityUtil;


public class ParseEmojiMsgUtil {
    private static final String TAG = ParseEmojiMsgUtil.class.getSimpleName();
    private static final String REGEX_STR = "\\[e\\](.*?)\\[/e\\]";

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     */
    public static SpannableString dealExpression(Context context, SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
//            String tmp = key.substring(key.indexOf(']') + 1, key.lastIndexOf('['));
            int resId = RM.getRId(context, "R.drawable." + "emoji_"
                    + key.substring(key.indexOf("]") + 1, key.lastIndexOf("[")));

            if (resId != 0) {
                Drawable drawable = context.getResources().getDrawable(resId);
                int x = DensityUtil.dip2px(15);
                drawable.setBounds(0,0,x,x);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(drawable, key);

                int end = matcher.start() + key.length();
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
        return spannableString;
    }



    /**
     * @param context
     * @param str
     * @return
     * @desc <pre>
     * 解析字符串中的表情字符串替换成表情图片
     * </pre>
     * @author Weiliang Hu
     * @date 2013-12-17
     */
    public static SpannableString getExpressionString(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(REGEX_STR, Pattern.CASE_INSENSITIVE);
        try {
            spannableString = dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return spannableString;
    }

    /**
     * @param cs
     * @param mContext
     * @return
     * @desc <pre>表情解析,转成unicode字符</pre>
     * @author Weiliang Hu
     * @date 2013-12-17
     */
    public static String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
//		String[] sp = ssb.getSpans(0, cs.length(), String.class);
//		int x = sp.length;
//		SpannableString s = getExpressionString(mContext, ssb.toString());

        for (int i = 0; i < spans.length; i++) {
            ImageSpan span = spans[i];
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            if (c.contains("[")) {
//				String d = convertUnicode(c);
                ssb.replace(a, b, convertUnicode(c));
            }
        }
        ssb.clearSpans();
        return ssb.toString();
    }




    /**
     * 因为草稿中包含了两种格式的图文混排编码
     */
    public static String convertDraToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            ImageSpan span = spans[i];
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            if (c.contains("[e]")) {
                String emojiStr = ssb.subSequence(a,b).toString();
                if(emojiStr.equals(c)){
                    ssb.replace(a, b, convertDraUnicode(c));
                }else{
                    ssb.replace(a, b,"");
                }
            } else if (c.contains("[")) {
                String emojiStr = ssb.subSequence(a,b).toString();
                if(emojiStr.equals(c)){
                    ssb.replace(a, b, convertUnicode(c));
                }else{
                    ssb.replace(a, b,"");
                }
            }
        }
        SpannableStringBuilder s = ssb;
        return ssb.toString();
    }

    private static String convertDraUnicode(String emo) {
        emo = emo.substring(3, emo.length() - 4);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
//        for (int i = 0; i < char0.length; i++) {
//            emoji[i] = char0[i];
//        }
        System.arraycopy(char0, 0, emoji, 0, char0.length);
        System.arraycopy(char1, 0, emoji, char0.length, char1.length);
//        for (int i = char0.length; i < emoji.length; i++) {
//            emoji[i] = char1[i - char0.length];
//        }
        return new String(emoji);
    }

    private static String convertUnicode(String emo) {
        emo = emo.substring(1, emo.length() - 1);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
//        for (int i = 0; i < char0.length; i++) {
//            emoji[i] = char0[i];
//        }
        System.arraycopy(char0, 0, emoji, 0, char0.length);
        System.arraycopy(char1, 0, emoji, char0.length, char1.length);
//        for (int i = char0.length; i < emoji.length; i++) {
//            emoji[i] = char1[i - char0.length];
//        }
        return new String(emoji);
    }
}
