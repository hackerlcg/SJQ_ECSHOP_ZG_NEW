package com.ecjia.hamster.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.ecjia.component.network.model.LoginModel;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.DBManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.DBUSER;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.DeviceInfoUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import gear.yc.com.gearlibrary.utils.ToastUtil;

public class StartActivity extends Activity implements HttpResponse {
    private Context context;
    private SharedPreferences remlock;
    public PushAgent mPushAgent;

    private SharedPreferences remdname;
    private SharedPreferences.Editor editor;
    private LoginModel loginModel;
    private String name;
    private String pwd;
    private String api;
    private Resources res;
    private DBManager database;
    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View startView = View.inflate(this, R.layout.splash, null);
        setContentView(startView);
        context = this;
        res = getResources();
        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();
        remdname = getSharedPreferences("userInfo", 0);
        remlock = getSharedPreferences("LockInfo", 0);

        editor = remdname.edit();


        //渐变
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        startView.setAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initPermission();
            }
        });

    }

    private void initData() {
        DeviceInfoUtil.CreateDevice(this);
        //是否支持推送
        if (AndroidManager.SUPPORT_PUSH) {
            //主要功能：推送的相关设置
            mPushAgent = PushAgent.getInstance(this);
            mPushAgent.onAppStart();
            //开启推送
            mPushAgent.enable();
            //正式发布应用时，请务必将本开关关闭，避免影响用户正常使用APP
            mPushAgent.setDebugMode(AndroidManager.PUSH_DEBUG);
            //通知栏多条消息消息显示
            mPushAgent.setMergeNotificaiton(false);
        }
    }

    private void initPermission() {
        //请求权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        initData();
//                        redirectto();
                        login();
                    } else {
                        ToastUtil.getInstance().makeShortToast(this, "程序没有被授权无法运行.");
                        finish();
                    }
                });
    }

    //默认登陆
    private void login() {
        loginModel = new LoginModel(StartActivity.this);
        loginModel.addResponseListener(this);
        boolean lockstate = remlock.getBoolean("lockState", false);
        if (lockstate) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        } else {
            name = remdname.getString("uname", "");
            api = remdname.getString("shopapi", "");
            pwd = remdname.getString("password", "");
            if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(name)) {
                loginModel.login(name, pwd, api);
            }else{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        }
    }

    private void redirectto() {
        if (AndroidManager.SUPPORT_GALLEY) {
            Intent intent = new Intent(this, GalleryImageActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.SIGNIN)) {
            String invalid = res.getString(R.string.login_invalid);
            String welcome = res.getString(R.string.login_welcome);
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(StartActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                boolean distinct = database.isDistinct(db, api);
                if (distinct) {
                    database.updateDefault(db, api);
                } else {
                    DBUSER dbuser = new DBUSER(name, pwd, api, 1, 0);
                    database.insertData(db, dbuser);
                }
                if (AndroidManager.SUPPORT_GALLEY) {
                    Intent intent = new Intent(this, GalleryImageActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent0 = new Intent(StartActivity.this, ECJiaMainActivity.class);
                    intent0.putExtra("login", true);
                    boolean lockstate = remlock.getBoolean("lockState", false);
                    if (lockstate) {
                        intent0.putExtra("lockclear", true);
                    }
                    setResult(Activity.RESULT_OK, intent0);
                    startActivity(intent0);
                }
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } else {
//                ToastView toast = new ToastView(StartActivity.this, invalid);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
