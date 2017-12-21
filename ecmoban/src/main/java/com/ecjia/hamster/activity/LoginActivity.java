package com.ecjia.hamster.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.DBManager;
import com.ecjia.hamster.adapter.MsgSql;
import com.ecjia.hamster.model.DBUSER;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.update.UpdateManager;
import com.sjq.cn.newmojie.shopkeeper.PushActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

import static android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class LoginActivity extends BaseActivity implements OnClickListener, HttpResponse {

    private ImageView iv_shop, iv_setting;
    private Button login;
    private LinearLayout mRoot, ll_input_area;
    private FrameLayout fl_api;
    private RelativeLayout rl_logo, rl_top, rl_logo2;

    private TextView tv_top;

    private EditText userName;
    private EditText password;
    private EditText shopapi;
    private CheckBox show_password;

    private String name;
    private String pwd;
    private String api;

    private LoginModel loginModel;
    public Handler Intenthandler;
    private TextView getpassword;
    private Boolean Lockin = false;
    private Boolean LockinD = true;
    private DBManager database;
    private SQLiteDatabase db;
    private String intentFlag;
    private String orderid;
    private String goodid;
    private String keyword;
    private String msgid;
    private String webUrl;
    private TextView forget_password;
    private int msgnum;
    private SharedPreferences remdname;
    private SharedPreferences.Editor editor;
    private SharedPreferences remlock;
    private float margin;
    private boolean lockclear = false;//清除手势锁

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EventBus.getDefault().register(this);
        remdname = getSharedPreferences("userInfo", 0);
        editor = remdname.edit();
        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();
        loginModel = new LoginModel(LoginActivity.this);
        loginModel.addResponseListener(this);
        Intent intent = getIntent();
        Lockin = intent.getBooleanExtra("Lockin", false);
        intentFlag = getIntent().getStringExtra("from");
        goodid = getIntent().getStringExtra("goodid");
        orderid = getIntent().getStringExtra("orderid");
        keyword = getIntent().getStringExtra("keyword");
        webUrl = getIntent().getStringExtra("webUrl");
        msgid = getIntent().getStringExtra("msgid");
        remlock = getSharedPreferences("LockInfo", 0);
        boolean lockstate = remlock.getBoolean("lockState", false);
        if (lockstate) {
            Intent intent1 = new Intent(this, LockActivity.class);
            startActivity(intent1);
        }
//        else{
//            name = remdname.getString("uname", "");
//            api = remdname.getString("shopapi", "");
//            pwd = remdname.getString("password", "");
//            if(!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(name)){
//                loginModel.login(name,pwd,api);
//            }else{
//
//            }
//        }
//        new UpdateManager(this).checkUpdate();
        initView();
    }

    private void initView() {
        rl_logo = (RelativeLayout) findViewById(R.id.rl_logo);
        rl_logo2 = (RelativeLayout) findViewById(R.id.rl_logo2);
        tv_top = (TextView) findViewById(R.id.tv_top);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        mRoot = (LinearLayout) findViewById(R.id.ll_root);
        ll_input_area = (LinearLayout) findViewById(R.id.ll_input_area);
        fl_api = (FrameLayout) findViewById(R.id.fl_api);
        login = (Button) findViewById(R.id.login_login);
        userName = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        shopapi = (EditText) findViewById(R.id.login_api);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_setting.setVisibility(View.GONE);
        iv_shop = (ImageView) findViewById(R.id.iv_shop);
        iv_shop.setVisibility(View.GONE);
        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        login.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_shop.setOnClickListener(this);
        margin = res.getDimension(R.dimen.dim30);
        msgnum = remdname.getInt("msgnum", 0);


        if (TextUtils.isEmpty(password.getText().toString())) {
            login.setClickable(false);
            login.setEnabled(false);
        }

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    login.setClickable(false);
                    login.setEnabled(false);
                } else {
                    login.setClickable(true);
                    login.setEnabled(true);
//                    login.setBackgroundDrawable(res.getDrawable(R.drawable.selector_login_button));
                }
            }
        });


        show_password = (CheckBox) findViewById(R.id.login_show_pwd);
        show_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView, final float margin) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int scrollHeight = (location[1] + scrollToView.getHeight() + (int) margin) - rect.bottom;
                    if (scrollHeight > 0) {
                        root.scrollTo(0, scrollHeight);
                    } else {
                        root.scrollTo(0, 0);
                    }
                    rl_logo.setAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.alpha_out));
                    rl_logo.setVisibility(View.INVISIBLE);
                    rl_top.setVisibility(View.INVISIBLE);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                    rl_logo.setVisibility(View.VISIBLE);
                    rl_logo.setAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.alpha_in));
                    rl_top.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        String usern = res.getString(R.string.username_cannot_be_empty);
        String pass = res.getString(R.string.password_cannot_be_empty);
        String noapi = res.getString(R.string.api_cannot_be_empty);
        String check_the_network = res.getString(R.string.check_the_network);
        Intent intent;
        switch (v.getId()) {
            case R.id.login_login:
                name = userName.getText().toString();
                pwd = password.getText().toString();
                api = shopapi.getText().toString();

                if ("".equals(name)) {
                    ToastView toast = new ToastView(this, usern);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(pwd)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(api)) {
                    ToastView toast = new ToastView(this, noapi);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    api = formatApi(api);
                    loginModel.login(name, pwd, api);
                    CloseKeyBoard();
                }
                break;
            case R.id.iv_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_shop:
                intent = new Intent(this, ShopChangeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.forget_password:
                final AlertDialog.Builder log = new AlertDialog.Builder(LoginActivity.this);
                log.setMessage("忘记密码，请联系管理员!");
                log.setCancelable(true);
                log.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                log.create().show();
//                View view = getLayoutInflater().inflate(R.layout.alertdialog_item, null);
////                log.setView(view);
//                log.setCancelable(true);
//                log.create().show();
//                Button button = (Button) view.findViewById(R.id.diagol_button);
//                button.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                    }
//                });
                break;
        }


    }

    private String formatApi(String api) {
        if (!api.contains("http://")) {
            api = "http://" + api;
        }
        return api;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new UpdateManager(this).checkUpdate();
        remlock = getSharedPreferences("LockInfo", 0);
        Lockin = remlock.getBoolean("lockState", false);

        if (Lockin && LockinD) {
            rl_logo.setVisibility(View.GONE);
            rl_logo2.setVisibility(View.VISIBLE);
            ll_input_area.setVisibility(View.GONE);
            tv_top.setVisibility(View.VISIBLE);
            login.setText(res.getString(R.string.login2));
            String name_str = remdname.getString("uname", "");
            String api_str = remdname.getString("shopapi", "");
            String pwd_str = remdname.getString("password", "");
            userName.setText(name_str);
            tv_top.setText(name_str + res.getText(R.string.keeper));
            password.setText(pwd_str);
            shopapi.setText(api_str);
        } else {
            String name_str = remdname.getString("uname", "");
            String api_str = AndroidManager.APPAPI;
//            if(TextUtils.isEmpty(api_str)){
//                fl_api.setVisibility(View.VISIBLE);
//            }else{
//                fl_api.setVisibility(View.GONE);
//            }
            if (!TextUtils.isEmpty(name_str)) {
                userName.setText(name_str);
            }
            shopapi.setText(api_str);
            password.setText("");
            rl_logo.setVisibility(View.VISIBLE);
            rl_logo2.setVisibility(View.GONE);
            ll_input_area.setVisibility(View.VISIBLE);
            tv_top.setVisibility(View.GONE);
            login.setText(res.getString(R.string.login));
            controlKeyboardLayout(mRoot, login, margin);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra("login", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
        return true;
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        userName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid = res.getString(R.string.login_invalid);
        String welcome = res.getString(R.string.login_welcome);

        if (url.equals(ProtocolConst.SIGNIN)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(LoginActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                boolean distinct = database.isDistinct(db, api);
                if (distinct) {
                    database.updateDefault(db, api);
                } else {
                    DBUSER dbuser = new DBUSER(name, pwd, api, 1, 0);
                    database.insertData(db, dbuser);
                }
                Intent intent0 = new Intent(LoginActivity.this, ECJiaMainActivity.class);
                intent0.putExtra("login", true);
                if (lockclear) {
                    intent0.putExtra("lockclear", true);
                }
                if ("search".equals(intentFlag)) {//搜索
                    intent0.putExtra("msgfrom", intentFlag);
                    intent0.putExtra("keyword", keyword);
                } else if ("orders_list".equals(intentFlag)) {//订单列表
                    intent0.putExtra("msgfrom", intentFlag);
                } else if ("goods_list".equals(intentFlag)) {//商品列表
                    intent0.putExtra("msgfrom", intentFlag);
                }
                setResult(Activity.RESULT_OK, intent0);
                startActivity(intent0);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                if (!TextUtils.isEmpty(intentFlag)) {
                    msgnum -= 1;
                    editor.putInt("msgnum", msgnum);
                    editor.commit();
                    MsgSql.getIntence(this).updateData(msgid);
                }

                if ("main".equals(intentFlag)) {


                } else if ("setting".equals(intentFlag)) {//设置
                    Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else if ("webview".equals(intentFlag)) {//浏览器
                    Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                    intent.putExtra("webUrl", webUrl);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("goods_detail".equals(intentFlag)) {//商品详情?
                    Intent intent = new Intent(this, GoodDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(goodid));
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else if ("qrshare".equals(intentFlag)) {//二维码分享
                    Intent intent = new Intent(LoginActivity.this, QRCodeActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else if ("signin".equals(intentFlag)) {//登录
                } else if ("orders_detail".equals(intentFlag)) {//订单详情
                    Intent intent = new Intent(LoginActivity.this, OrderDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(orderid));
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else if ("message".equals(intentFlag)) {//消息中心
                    Intent intent = new Intent(LoginActivity.this, PushActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("login", true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
            } else {
                ToastView toast = new ToastView(LoginActivity.this, invalid);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onEvent(MyEvent event) {
        if ("LOCKFINISH".equals(event.getMsg())) {
            finish();
        } else if ("LOCKDESTROY".equals(event.getMsg())) {
            LockinD = false;
        } else if ("LOCKCLEAR".equals(event.getMsg())) {
            lockclear = true;
        } else if ("gotoMain".equals(event.getMsg())) {
            finish();
        }
    }

}
