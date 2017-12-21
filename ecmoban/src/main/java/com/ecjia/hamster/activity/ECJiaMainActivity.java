package com.ecjia.hamster.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecjia.component.service.NetworkStateService;
import com.ecjia.component.view.MyDialog;
import com.ecjia.hamster.activity.goods.MyGoodsParentActivity;
import com.ecjia.hamster.fragment.TabsFragment;
import com.ecjia.util.CheckInternet;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.update.UpdateManager;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import com.ecjia.component.view.ToastView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;

import java.util.Locale;

import de.greenrobot.event.EventBus;


public class ECJiaMainActivity extends FragmentActivity {

    private boolean isChange = false;
    private boolean isRefresh = false;
    private SharedPreferences shared, sharedPreference, remlock;
    private String uid;
    private Resources resources;
    private MyDialog myDialog, setDialog;
    private SharedPreferences.Editor editor;
    private boolean devicetoken = false;
    private String msgfrom;
    private String keyword;
    private boolean lockclear = false;
    private String statsfrom;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return false;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //所有新建activity必须有。。。
        PushAgent.getInstance(this).onAppStart();

        resources = getBaseContext().getResources();
        Chooselanguage();
        setContentView(R.layout.main);
        EventBus.getDefault().register(this);
        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        uid = shared.getString("uid", "");
        Intent intent1 = getIntent();
        isChange = intent1.getBooleanExtra("fromchange", false);
        msgfrom = intent1.getStringExtra("msgfrom");
        statsfrom = intent1.getStringExtra("statsfrom");
        keyword = intent1.getStringExtra("keyword");
        lockclear = intent1.getBooleanExtra("lockclear", false);
        if (lockclear) {
            setDialog = new MyDialog(this, resources.getString(R.string.tip), resources.getString(R.string.gestruelock_clear));
            setDialog.show();
            setDialog.positiveText.setText(resources.getString(R.string.set_now));
            setDialog.negative.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDialog.dismiss();

                }
            });
            setDialog.positive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDialog.dismiss();
                    Intent intent = new Intent(ECJiaMainActivity.this, SetLockActivity.class);
                    startActivity(intent);
                }
            });
            new UpdateManager(this).checkUpdate();
        }

        if ("search".equals(msgfrom)) {
            TabsFragment.getInstance().msgFragment(2, keyword);
        } else if ("goods_list".equals(msgfrom)) {
            startActivity(new Intent(ECJiaMainActivity.this, MyGoodsParentActivity.class));
//            TabsFragment.getInstance().msgFragment(4, "");
        } else if ("orders_list".equals(msgfrom) || "orders_list".equals(statsfrom)) {
//            TabsFragment.getInstance().msgFragment(5, "");
            TabsFragment.getInstance().OnTabSelected("tab_four");
        } else {
            TabsFragment.getInstance().OnTabSelected("tab_one");
        }


        if (isChange) {
            TabsFragment.getInstance().toMyFragment(0, "");
        }

        devicetoken = shared.getBoolean("devicetoken", true);
        if (devicetoken) {
            String device_token = UmengRegistrar.getRegistrationId(this);
            LG.e("device_token===" + device_token);
            editor.putBoolean("devicetoken", false);
            editor.commit();
        }

        String no_network = resources.getString(R.string.main_no_network);
        if (!CheckInternet.isConnectingToInternet(this)) {// 检查网络连接
            ToastView toast = new ToastView(ECJiaMainActivity.this, no_network);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        // 启动server服务连接 ，检测网络的类型
        Intent intent = new Intent(this, NetworkStateService.class);
        startService(intent);

        // 版本更新提醒功能
        UmengUpdateAgent.update(this);// 调用更新方法
        UmengUpdateAgent.setUpdateAutoPopup(true);// 需要更新时主动弹出对话框
        UmengUpdateAgent.setUpdateOnlyWifi(false);// 所有网络都可更新不仅是wifi
        // 自动更新的下载接口，同样可以注册回调消息，这样在自动更新下载APK开始，进行，结束或者失败的时候，可以使用该接口来做出相应的反应，以提供
        // 更友好的用户体验
        UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

            @Override
            public void OnDownloadStart() {
                resources = getBaseContext().getResources();
                String start_load = resources.getString(R.string.main_start_load);
                ToastView toast = new ToastView(ECJiaMainActivity.this, start_load);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void OnDownloadUpdate(int progress) {
            }

            @Override
            public void OnDownloadEnd(int result, String file) {
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        msgfrom = intent.getStringExtra("msgfrom");
        keyword = intent.getStringExtra("keyword");

        if ("search".equals(msgfrom)) {
            TabsFragment.getInstance().msgFragment(2, keyword);
        } else if ("goods_list".equals(msgfrom)) {
//            TabsFragment.getInstance().msgFragment(4, "");
            startActivity(new Intent(ECJiaMainActivity.this, MyGoodsParentActivity.class));
        } else if ("orders_list".equals(msgfrom)) {
//            TabsFragment.getInstance().msgFragment(5, "");
            TabsFragment.getInstance().OnTabSelected("tab_four");
        } else {
            TabsFragment.getInstance().OnTabSelected("tab_one");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    // 退出操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TabsFragment.getInstance().selectfour || TabsFragment.getInstance().selectfive) {
                if (TabsFragment.getInstance().isbeHidden()) {
                    EventBus.getDefault().post(new MyEvent("NEEDCLOSE"));
                }
                TabsFragment.getInstance().toMyFragment(1, "");
                TabsFragment.getInstance().hidden(true);
                return true;
            } else if(TabsFragment.getInstance().select_eight){
//                TabsFragment.getInstance().toServiceFragment();
            }else {

                Resources resource = (Resources) getBaseContext()
                        .getResources();
                String main_exit = resource.getString(R.string.main_exit);
                String main_exit_content = resource.getString(R.string.main_exit_content);
                myDialog = new MyDialog(this, main_exit, main_exit_content);
                myDialog.show();
                myDialog.negative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.positive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
//                        editor.putString("uid", "");
//                        editor.putString("sid", "");
//                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(ECJiaMainActivity.this, NetworkStateService.class);
                        stopService(intent);
                        finish();
                    }
                });

                return true;
            }
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = shared.getString("uid", "");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1) {
//                String sdStatus = Environment.getExternalStorageState();
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            }

        }


    }

    //选择加载的语言
    private void Chooselanguage() {
        Configuration config = resources.getConfiguration();
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        if ("zh".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            config.locale = Locale.CHINA;
        } else if ("en".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            config.locale = Locale.ENGLISH;
        } else {
            sharedPreference.edit().putString("language", "auto").commit();
            config.locale = Locale.getDefault();
        }
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("changelanguage".equals(event.getMsg())) {
            LG.i("运行");
            this.finish();
        }
        if ("userinfo_refresh".equals(event.getMsg())) {
            isRefresh = true;
        }
        if ("exit".equals(event.getMsg())) {
            this.finish();
        }

    }
}
