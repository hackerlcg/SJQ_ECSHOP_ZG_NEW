package com.ecjia.hamster.activity;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.DBManager;
import com.ecmoban.android.shopkeeper.sijiqing.R;


public class EditShopActivity extends BaseActivity implements OnClickListener {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button login;

    private EditText userName;
    private EditText password;
    private EditText shopapi;
    private CheckBox show_password;

    private String name;
    private String pwd;
    private String api;

    private String orname;
    private String orpwd;
    private String orapi;

    private DBManager database;
    private SQLiteDatabase db;

    public EditShopActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editshop);

        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();

        Intent intent=getIntent();
        orname=intent.getStringExtra("orname");
        orpwd=intent.getStringExtra("orpwd");
        orapi=intent.getStringExtra("orapi");

        initView();

    }

    private void initView() {
        login = (Button) findViewById(R.id.login_login);
        userName = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        shopapi = (EditText) findViewById(R.id.login_api);
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_back=(ImageView)findViewById(R.id.top_view_back);
        login.setOnClickListener(this);
        top_view_back.setOnClickListener(this);

        userName.setText(orname);
        password.setText(orpwd);
        shopapi.setText(orapi);

        top_view_text.setText(res.getText(R.string.shopedit));

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


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String usern = res.getString(R.string.username_cannot_be_empty);
        String pass = res.getString(R.string.password_cannot_be_empty);
        String noapi = res.getString(R.string.api_cannot_be_empty);
        String success = res.getString(R.string.editsuccess);
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
                } else if("".equals(api)){
                    ToastView toast = new ToastView(this, noapi);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    api=formatApi(api);
                    database.updateData(db,name,pwd,api,orapi);

                    ToastView toast = new ToastView(this, success);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    CloseKeyBoard();
                    finish();
                }
                break;
            case R.id.top_view_back:
                finish();
                break;
        }


}

    private String formatApi(String api) {
        if (!api.contains("http://")){
            api="http://"+api;
        }
        return api;
    }


    // 关闭键盘
    public void CloseKeyBoard() {
        userName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
    }

}
