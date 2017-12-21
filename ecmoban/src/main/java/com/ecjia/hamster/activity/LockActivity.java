package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.LockPatternView;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.DBManager;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.DBUSER;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;


public class LockActivity extends BaseActivity implements LockPatternView.OnPatternListener ,HttpResponse {

    private TextView tv_tips, tv_forget;
    private LockPatternView lockPatternView;
    private SharedPreferences remlock;
    private SharedPreferences.Editor edit;
    private DBManager database;
    private SQLiteDatabase db;
    private List<LockPatternView.Cell> lockPattern;
    private Boolean isReset = false;
    private Boolean isProtect = false;
    private int failcount = 0;
    private boolean lockclear=false;//清除手势锁
    private LoginModel loginModel;
    public LockActivity() {
    }
    private String name;
    private String pwd;
    private String api;
    private SharedPreferences remdname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);

        EventBus.getDefault().register(this);
        remdname = getSharedPreferences("userInfo", 0);
        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();
        loginModel = new LoginModel(this);
        loginModel.addResponseListener(this);
        initView();

    }

    private void initView() {
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);

        Intent intent = getIntent();
        isReset = intent.getBooleanExtra("reset", false);

        remlock = getSharedPreferences("LockInfo", 0);
        String patternString = remlock.getString("myLock", "");
        isProtect = remlock.getBoolean("isProtect", false);
        edit = remlock.edit();

        lockPattern = LockPatternView.stringToPattern(patternString);

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.clear();
                edit.commit();

                EventBus.getDefault().post(new MyEvent("LOCKCLEAR"));
                lockclear=true;
                finish();
            }
        });

    }


    private Handler Delayhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lockPatternView.clearPattern();
                    lockPatternView.enableInput();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onPatternStart() {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        if (pattern.equals(lockPattern)) {
            if (isReset) {
                Intent intent = new Intent(LockActivity.this, SetLockActivity.class);
                startActivity(intent);
                finish();
            } else {
                name = remdname.getString("uname", "");
                api = remdname.getString("shopapi", "");
                pwd = remdname.getString("password", "");
                loginModel.login(name,pwd,api);
                EventBus.getDefault().post(new MyEvent("gotoMain"));//去首页
            }
        } else {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            lockPatternView.disableInput();
            failcount += 1;
            if (isProtect) {
                tv_tips.setText(res.getText(R.string.lock_fail) + "还能输" + (5 - failcount) + "次");
                if (failcount >= 5) {
                    SharedPreferences s1 = null;
                    SharedPreferences.Editor editor1 = null;

                    s1 = getSharedPreferences("userInfo", 0);
                    editor1 = s1.edit();
                    editor1.clear();
                    editor1.commit();

                    edit.clear();
                    edit.commit();

                    database.destroy(db);
                    EventBus.getDefault().post(new MyEvent("LOCKDESTROY"));
                    finish();
                }
            } else {
                tv_tips.setText(res.getText(R.string.lock_fail));
            }
            delay();
            return;
        }
    }

    private void delay() {
        new Thread() {
            public void run() {

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain(Delayhandler);
                msg.what = 1;
                Delayhandler.sendMessage(msg);
            }

        }.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new MyEvent("LOCKFINISH"));
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        db.close();
    }

    public void onEvent(MyEvent event) {

    }
    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid = res.getString(R.string.login_invalid);
        String welcome = res.getString(R.string.login_welcome);

        if (url.equals(ProtocolConst.SIGNIN)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(LockActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                boolean distinct = database.isDistinct(db, api);
                if (distinct) {
                    database.updateDefault(db, api);
                } else {
                    DBUSER dbuser = new DBUSER(name, pwd, api, 1, 0);
                    database.insertData(db, dbuser);
                }
                Intent intent0 = new Intent(LockActivity.this, ECJiaMainActivity.class);
                intent0.putExtra("login", true);
                if (lockclear) {
                    intent0.putExtra("lockclear", true);
                }
                startActivity(intent0);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }

        }
    }
}
