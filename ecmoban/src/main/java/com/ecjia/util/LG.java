package com.ecjia.util;

import com.ecjia.consts.AndroidManager;

/**
 * Created by Adam on 2014/12/24.
 */
public class LG {
//    protected static final String TAG = "EC模板堂";
    /**
     * 是否开启debug
     */
//    public static boolean isDebug = true;

    public LG() {
    }

    public static void v(String msg) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.v(AndroidManager.APPNAME, buildMessage(msg));
    }

    public static void v(String msg, Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.v(AndroidManager.APPNAME, buildMessage(msg), thr);
    }

    public static void d(String msg) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.d(AndroidManager.APPNAME, buildMessage(msg));
    }

    public static void d(String msg, Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.d(AndroidManager.APPNAME, buildMessage(msg), thr);
    }

    public static void i(String msg) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.i(AndroidManager.APPNAME,buildMessage(msg));
    }

    public static void i(String msg, Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.i(AndroidManager.APPNAME,buildMessage(msg), thr);
    }

    public static void e(String msg) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.e(AndroidManager.APPNAME, buildMessage(msg));
    }

    public static void w(String msg) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.w(AndroidManager.APPNAME, buildMessage(msg));
    }

    public static void w(String msg, Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.w(AndroidManager.APPNAME, buildMessage(msg), thr);
    }

    public static void w(Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.w(AndroidManager.APPNAME, buildMessage(""), thr);
    }

    public static void e(String msg, Throwable thr) {
        if (AndroidManager.ISDEBUG)
            android.util.Log.e(AndroidManager.APPNAME,buildMessage(msg), thr);
    }

    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace()
                .getStackTrace()[2];

        return new StringBuilder().append(caller.getClassName()).append(".")
                .append(caller.getMethodName()).append("(): ").append(msg)
                .toString();
    }
}
