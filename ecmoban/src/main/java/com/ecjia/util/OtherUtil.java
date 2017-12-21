package com.ecjia.util;

/**
 * Created by Administrator on 2015/7/29.
 */
public class OtherUtil {

    /**
     * 判断输入是否是数字
     */
    public static boolean isNumber(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
