package com.ecjia.util.common;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erhu on 2016/1/19.
 */
public class StringUtils {
    public static String getLoation(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            int conut = msg.indexOf('.');
            return msg.substring(conut + 1, msg.length());
        } else {
            return "";
        }
    }

    /**
     * 是否包含Emoji表情
     *
     * @param source
     * @return
     */
    public static boolean hasEmoji(String source) {
        if (!TextUtils.isEmpty(source)) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            return emojiMatcher.find();
        }
        return false;
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 字符串是否为非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && str.length() != 0);
    }

    /**
     * 字符串是否为空格串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串是否非空格串
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() != 0);
    }

    /**
     * 将null转换为空串,如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 将null转换为字符串NULL,如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToString(String str) {
        return (str == null ? "NULL" : str);
    }

    public static String getIndexString(String str) {
        if (str.length() <= 5) {
            return str;
        }
        return str.substring(5);
    }

    //字符长度大于多少
    public static  String getLengthMax(String str,int length){
        if(str.length()>length){
          return  str.substring(0,length)+"...";
        }else {
            return str;
        }
    }
}
