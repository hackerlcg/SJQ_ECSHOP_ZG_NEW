package com.ecjia.consts;

import android.content.Context;

public class AndroidManager {

    public static final String CACHEPATH = "sdcard/Android/data/com.ecmoban.android.shopkeeper.sijiqing/cache";

    public static final String APPNAME = "Ecjia掌柜";

    public static final boolean ISDEBUG = true;

    //    public static final String SERVICE_URL="http://app.sjq.cn/";
    public static final String SERVICE_URL = "http://app.topws.cn/";
    public static final String SERVICE_TAG = "?url=";

    //    public static final String APPAPI = "http://app.sjq.cn/?url=";
//    public static final String APPAPI = "http://app.topws.cn/?url=";
    public static final String APPAPI = SERVICE_URL+SERVICE_TAG;

    public static int getSDKCode(Context context) {  //获取sdk的等级
        return android.os.Build.VERSION.SDK_INT;
    }

    //微信appID
    public static final String WXAPPID = "";
    public static final String WXAPPSECRET = "";

    //qq的appID
    public static final String QQAPPID = "";
    public static final String QQAPPSECRET = "";

    /**
     * 支持GalleyImageActivity
     */
    public static final boolean SUPPORT_GALLEY = false;

    public static final boolean SUPPORT_PUSH = false;//支持推送
    public static final boolean PUSH_DEBUG = false;//debug模式推送


}
