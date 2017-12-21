package com.ecjia.hamster.activity;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.DBManager;
import com.ecjia.hamster.model.DBUSER;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import static android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class AddShopActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_shop,iv_setting;
    private Button login;
    private LinearLayout mRoot;
    private RelativeLayout rl_logo,rl_top;

    private EditText userName;
    private EditText password;
    private EditText shopapi;
    private CheckBox show_password;

    private String name;
    private String psd;
    private String api;

    private DBManager database;
    private SQLiteDatabase db;

    public AddShopActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_addshop);

        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();

        initView();


    }

    private void initView() {
        rl_logo=(RelativeLayout)findViewById(R.id.rl_logo);
        rl_top=(RelativeLayout)findViewById(R.id.rl_top);
        mRoot=(LinearLayout)findViewById(R.id.ll_root);
        login = (Button) findViewById(R.id.login_login);
        userName = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        shopapi = (EditText) findViewById(R.id.login_api);
        iv_setting=(ImageView)findViewById(R.id.iv_setting);
        iv_shop=(ImageView)findViewById(R.id.iv_shop);
        login.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_shop.setOnClickListener(this);

        float margin = res.getDimension(R.dimen.dim30);

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

        controlKeyboardLayout(mRoot, login, margin);

    }

    /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView,final float margin) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {
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
                    int srollHeight = (location[1] + scrollToView.getHeight()+(int)margin) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                    rl_logo.setAnimation(AnimationUtils.loadAnimation(AddShopActivity.this,R.anim.alpha_out));
                    rl_logo.setVisibility(View.INVISIBLE);
                    rl_top.setVisibility(View.INVISIBLE);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                    rl_logo.setVisibility(View.VISIBLE);
                    rl_logo.setAnimation(AnimationUtils.loadAnimation(AddShopActivity.this,R.anim.alpha_in));
                    rl_top.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String usern = res.getString(R.string.username_cannot_be_empty);
        String pass = res.getString(R.string.password_cannot_be_empty);
        String noapi = res.getString(R.string.api_cannot_be_empty);
        String sameapi = res.getString(R.string.api_cannot_be_same);
        String success = res.getString(R.string.add_success);
        String check_the_network = res.getString(R.string.check_the_network);
        Intent intent;
        switch (v.getId()) {
            case R.id.login_login:
                name = userName.getText().toString();
                psd = password.getText().toString();
                api = shopapi.getText().toString();

                if ("".equals(name)) {
                    ToastView toast = new ToastView(this, usern);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(psd)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(api)) {
                    ToastView toast = new ToastView(this, noapi);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    api=formatApi(api);
                    boolean distinct=database.isDistinct(db,api);
                    if(distinct){
                        ToastView toast = new ToastView(this, sameapi);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else{
                        DBUSER dbuser=new DBUSER(name, psd,api,0,0);
                        database.insertData(db,dbuser);
                        ToastView toast = new ToastView(this, success);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        finish();
                    }
                }
                CloseKeyBoard();
                break;
            case R.id.iv_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_shop:
                finish();
                break;
        }
    }



    // 关闭键盘
    public void CloseKeyBoard() {
        userName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
    }

    private String formatApi(String api) {
        if (!api.contains("http://")){
            api="http://"+api;
        }
        return api;
    }

}
