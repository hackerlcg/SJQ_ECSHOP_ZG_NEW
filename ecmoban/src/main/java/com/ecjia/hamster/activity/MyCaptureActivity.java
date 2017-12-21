package com.ecjia.hamster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecjia.component.view.MyDialog;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.google.zxing.Result;
import com.umeng.message.PushAgent;

import cn.itguy.zxingportrait.CaptureActivity;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;

public class MyCaptureActivity extends CaptureActivity {
    MyDialog dialog;
    public static int S;

    private int startType = 0;

    private String fromActivity="";

    String tName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        tName = intent.getStringExtra("tName");
        PushAgent.getInstance(this).onAppStart();
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        capture_topview.setBackgroundResource(R.color.public_theme_color_normal);
        S = dm.widthPixels;
        homeBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCaptureActivity.this, ECJiaMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }


    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {

        // 震动
        beepManager.playBeepSoundAndVibrate();
        final String getresult = rawResult.getText();//扫描的字符串
        String resultstring = getresult.toLowerCase();//转小写
        checkUrl(resultstring, getresult);
        // 添加这句话可持续扫描
        restartPreviewAfterDelay(5000L);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                LG.i("back键 l ");
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                this.finish();
                break;
            case KeyEvent.KEYCODE_HOME:
                LG.i("home键 l ");
                if (startType == 0) {
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    this.finish();
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                LG.i("menu键 l ");
                if (startType == 0) {
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    this.finish();
                }
                break;
        }
        return false;
    }

    public void checkUrl(String checkurl, String url) {
            gotoWebView(url);
    }


    public void gotoWebView(final String url) {
        fromActivity= getIntent().getStringExtra("from");
        if("delivergoodsactivity".equals(fromActivity) ){
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            resultIntent.putExtra("result",url);
            setResult(RESULT_OK, resultIntent);
            finish();
//            ToastUtil.getInstance().makeLongToast(MyCaptureActivity.this,url);
        }else if (url.indexOf("http") == 0 || url.indexOf("https") == 0) {
                    Intent intent = new Intent(MyCaptureActivity.this, WebViewActivity.class);
                    intent.putExtra("webUrl", url);
                    intent.putExtra("webtitle", "扫码登录");
                    MyCaptureActivity.this.startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startType = getIntent().getIntExtra("startType", 0);
        LG.i(startType + "===========");
        if (startType == 1) {
            btn_back.setVisibility(View.VISIBLE);
            homeBack.setVisibility(View.GONE);
        } else {
            EventBus.getDefault().post(new MyEvent("not_from_widget"));
            btn_back.setVisibility(View.GONE);
            homeBack.setVisibility(View.VISIBLE);
        }
    }


    public void onEvent(MyEvent event) {
        if ("not_from_widget".equals(event.getMsg())) {
            if (startType == 1) {
                this.finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mHomeKeyEventReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    if (startType == 0) {
                        MyCaptureActivity.this.finish();
                    }
                    //表示按了home键,程序到了后台
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };
    private void addShortCut(String tName, String typeEnable, int drawable) {
        // 安装的Intent
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, tName);
        // 快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.putExtra("tName", tName);
        shortcutIntent.setClassName(getPackageName(), "com.ecjia.hamster.activity.StartActivity");
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, drawable);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        // 发送广播
        sendBroadcast(shortcut);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putBoolean(typeEnable, false);
        editor1.commit();
    }

}
