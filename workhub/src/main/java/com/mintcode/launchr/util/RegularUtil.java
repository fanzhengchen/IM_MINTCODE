package com.mintcode.launchr.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JulyYu on 2016/4/12.
 */
public class RegularUtil {

    /** 数字 */
    public static boolean isNumeric(String s){
        Pattern pattern =  Pattern.compile(".*?[0-9]+.*?");
        Matcher isNum = pattern.matcher(s);
        if (!isNum.matches()) {
            return true;
        }else{
            return false;
        }
    }
    /** 大写*/
    public static boolean hasBigChar(String s){
        Pattern pattern = Pattern.compile(".*?[A-Z]+.*?");
        Matcher isNum = pattern.matcher(s);
        if (!isNum.matches()) {
            return true;
        }else{
            return false;
        }

    }
    /** 小写*/
    public static boolean hasSmallChar(String s){
        Pattern pattern = Pattern.compile(".*?[a-z]+.*?");
        Matcher isNum = pattern.matcher(s);
        if (!isNum.matches()) {
            return true;
        }else{
            return false;
        }

    }
    /** 特殊字符*/
    public static boolean hasSpecialChar(String s){

        Pattern pattern = Pattern.compile(".*?[^A-Za-z0-9]+.*?");
        Matcher isNum = pattern.matcher(s);
        if (!isNum.matches()) {
            return true;
        }else{
            return false;
        }
    }

    /** 判断手机格式是否正确*/
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher isPhone = p.matcher(mobiles);
        if (!isPhone.matches()) {
            return true;
        }else{
            return false;
        }
    }

    /** 判断email格式是否正确*/
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher isMail = p.matcher(email);
        if (!isMail.matches()) {
            return true;
        }else{
            return false;
        }
    }

    public static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }
    public static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }
    /** 将半角字符转换为全角字符*/
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
