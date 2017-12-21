package com.ecjia.util.common;

import android.content.Context;
import android.widget.Toast;

/**
 * 打印工具类
 *
 * @author wanglei
 */
public class T {

    private static Toast toast;

    public static void show(Context activity, CharSequence message) {
        if (activity == null) return;
        if (toast == null) {
            toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void show(Context activity, int message) {
        if (activity == null) return;
        if (toast == null) {
            toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
