package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.DataCleanManager;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FileSizeUtil;

import de.greenrobot.event.EventBus;


public class SettingActivity extends BaseActivity implements OnClickListener {

    private TextView top_view_text,tv_state,tv_version,tv_cache;
    private ImageView top_view_back;
    private LinearLayout set_lock,clear_cache,ll_app_info;
    SharedPreferences remlock;
    SharedPreferences userInfo;
    private Boolean isFirst=true;
    private Boolean lockState=true;
    private Boolean fromForget=false;
    private Boolean dbcheck=false;
    private MyDialog myDialog;
    private Button exit_login;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor edit;

    public SettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        EventBus.getDefault().register(this);
        userInfo=getSharedPreferences("userInfo",0);
        editor=userInfo.edit();
        String name=userInfo.getString("uname","");
        if(TextUtils.isEmpty(name)){
            dbcheck=false;
        }else{
            dbcheck=true;
        }
        Intent intent=getIntent();
        fromForget=intent.getBooleanExtra("fromForget",false);

        initView();

    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_cache.setText(msg.obj.toString());
        }
    };


    private void initView() {
        top_view_text=(TextView)findViewById(R.id.top_view_text);
        tv_state=(TextView)findViewById(R.id.tv_state);
        tv_version=(TextView)findViewById(R.id.tv_version);
        tv_cache=(TextView)findViewById(R.id.tv_cache);

        top_view_text.setText(res.getText(R.string.setting));

        PackageManager manager;
        PackageInfo info = null;
        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }
        tv_version.setText(info.versionName);

        top_view_back=(ImageView)findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(this);

        set_lock=(LinearLayout)findViewById(R.id.set_lock);
        set_lock.setOnClickListener(this);

        clear_cache=(LinearLayout)findViewById(R.id.clear_cache);
        clear_cache.setOnClickListener(this);

        ll_app_info=(LinearLayout)findViewById(R.id.ll_app_info);
        ll_app_info.setOnClickListener(this);

        exit_login=(Button)findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);

        new Thread() {
            public void run() {
                Message msg = new Message();

                try {
                    String size = FileSizeUtil.getAutoFileOrFilesSize(AndroidManager.CACHEPATH);
                    msg.obj = size;
                } catch (Exception e) {
                }
                myHandler.sendMessage(msg);
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                if(fromForget){
                    Intent intent =new Intent(this,ECJiaMainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }
                break;
            case R.id.set_lock:
                if(dbcheck){
                    if(isFirst){
                        Intent intent =new Intent(this,LockFStartActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent =new Intent(this,LockStartActivity.class);
                        startActivity(intent);
                    }
                }else{
                    String content=res.getString(R.string.please_logoradd);
                    ToastView toast = new ToastView(SettingActivity.this,content );
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                break;
            case R.id.ll_app_info:
                        Intent intent =new Intent(this,APPInfoActivity.class);
                        startActivity(intent);

                break;
            case R.id.exit_login:
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

                        Intent intent =new Intent(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);

                        edit.putBoolean("lockState", false);
                        edit.commit();
//
//                        intent.setAction("com.ecjia.component.service.NetworkStateService");
//                        stopService(intent);

                        EventBus.getDefault().post(new MyEvent("exit"));

                        finish();
                    }
                });


                break;
            case R.id.clear_cache:
                if("0KB".equals(tv_cache.getText().toString())||"0B".equals(tv_cache.getText().toString())){

                }else {
                    String tips = res.getString(R.string.tip);
                    String tips_content = res.getString(R.string.clear_cache_content);
                    tips_content = tips_content.replace("0KB", tv_cache.getText().toString());
                    myDialog = new MyDialog(SettingActivity.this, tips, tips_content);
                    myDialog.show();
                    myDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();

                        }
                    });
                    myDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Thread() {
                                public void run() {
                                    Message msg = new Message();
                                    try {
                                        DataCleanManager.deleteFolderFile(AndroidManager.CACHEPATH, false);
                                        String size = FileSizeUtil.getAutoFileOrFilesSize(AndroidManager.CACHEPATH);
                                        msg.obj = size;
                                    } catch (Exception e) {
                                    }
                                    myHandler.sendMessage(msg);
                                }
                            }.start();
                            myDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        remlock=getSharedPreferences("LockInfo", 0);
        edit = remlock.edit();
        isFirst=remlock.getBoolean("ifFirst",true);
        lockState=remlock.getBoolean("lockState",true);
        if(isFirst){
            tv_state.setText(res.getString(R.string.state_default));
        }else{
            if(lockState){
                tv_state.setText(res.getString(R.string.state_on));
            }else{
                tv_state.setText(res.getString(R.string.state_off));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromForget){
            Intent intent =new Intent(this,ECJiaMainActivity.class);
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }
}
