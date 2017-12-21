package com.ecjia.util.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-21.
 */

public class IntentUtils {

    //拨号界面
    public static void IntentCall(final Activity activity, final String phone) {
        DialogUtils.showDialog(activity,"提示", "拨打电话 " + phone, new DialogUtils.ButtonClickListenerAbstract() {
            @Override
            public void positiveButton() {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    T.show(activity, "设备不支持拨打电话");
                }
            }
        });
    }

    //跳转当前app应用信息界面
    public static void IntentAppSetting(Context context, int SETTING_CODE) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        if (context instanceof Activity)
            ((Activity) context).startActivityForResult(intent, SETTING_CODE);
    }

    //跳转当前app应用信息界面
    public static void IntentAppSetting(Fragment fragment, int SETTING_CODE) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", fragment.getActivity().getPackageName(), null);
        intent.setData(uri);
        fragment.startActivityForResult(intent, SETTING_CODE);
    }

    //跳转设置wifi界面
    public static void IntentNetWorkSetting(Activity activity) {
        try {
            Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
            activity.startActivity(wifiSettingsIntent);
        } catch (Exception ignored) {
        }
    }
}
