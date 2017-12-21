package com.ecjia.consts;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConst {

    //errorcode类-----------------------------------------------------------
    public final static int UnexistInformation = 13;//不存在的信息
    public final static int InvalidParameter = 101;//错误的参数提交

    //eventbus 通知 tag
    public static final int PROMOTES=100;//修改价格促销时间开始
    public static final int PROMOTEE=101;//修改价格促销时间结束

    public static final int DISCOUNTS=110;//修改优惠活动时间开始
    public static final int DISCOUNTE=111;//修改优惠活动时间结束

    public static final int AREA=88;//选择所在地

    public static final int CATEGORYT=50;//分类名字
    public static final int SCATEGORYT=52;//分类名字
    public static final int NEWTITLE=51;//分类标题


    //首页监听-------------------------------------------------------

    private static RegisterApp registerApp;

    public static void registerApp(RegisterApp register) {
        registerApp = register;
    }

    public static interface RegisterApp {
        public void onRegisterResponse(boolean success);
    }

    // 获取SDK的版本
    public static Integer getSDKVERSION() {
        return Integer.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    // 获取Resources
    public static Resources getResources(Context context) {
        return context.getResources();
    }

}
