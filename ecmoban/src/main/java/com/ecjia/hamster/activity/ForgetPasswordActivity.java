package com.ecjia.hamster.activity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyCheckDialog;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.DBManager;
import com.ecjia.hamster.adapter.ForgetCheckAdapter;
import com.ecjia.hamster.model.DBUSER;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ForgetPasswordActivity extends BaseActivity implements HttpResponse {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_next;
    private ListView listView;
    private DBManager database;
    private SQLiteDatabase db;
    private ArrayList<DBUSER> dbusers;
    private ForgetCheckAdapter listAdapter;
    private LoginModel loginModel;
    private String name;
    private String pwd;
    private String api;
    public Handler Intenthandler;
    private MyCheckDialog myCDialog;
    private MyDialog myDialog;

    public ForgetPasswordActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwd);
        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();
        dbusers=new ArrayList<DBUSER>();
        dbusers.addAll(database.selectAll(db));

        loginModel = new LoginModel(ForgetPasswordActivity.this);
        loginModel.addResponseListener(this);

        initView();

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.shop_check));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        btn_next = (Button) findViewById(R.id.btn_next);
        listView = (ListView) findViewById(R.id.shop_list);

        checkCheck();

        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String tips = res.getString(R.string.tips);
                String tips_content = res.getString(R.string.tips_content);
                myDialog = new MyDialog(ForgetPasswordActivity.this, tips, tips_content);
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
                        database.deletNoCheck(db);
                        dbusers.clear();
                        dbusers.addAll(database.selectAll(db));
                        if(dbusers.size()>0){
                            name=dbusers.get(0).getName();
                            pwd=dbusers.get(0).getPwd();
                            api=dbusers.get(0).getApi();
                            api=formatApi(api);
                            loginModel.login(name, pwd,api);
                        }
                        myDialog.dismiss();
                    }
                });
            }
        });

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetPasswordActivity.this,LockActivity.class);
                startActivity(intent);
                resetCheck();
                finish();
            }
        });

        if (listAdapter == null) {
            listAdapter = new ForgetCheckAdapter(this, dbusers);
        }

        listAdapter.setOnCheckItemClickListener(new ForgetCheckAdapter.onCheckItemClickListener() {
            @Override
            public void onCheckItemClick(View v, int position) {
                final String checkpwd=dbusers.get(position).getPwd();
                final String checkapi=dbusers.get(position).getApi();
                String title = res.getString(R.string.enter_password);
                String edit = res.getString(R.string.shop_password);
                myCDialog = new MyCheckDialog(ForgetPasswordActivity.this, title, edit);
                myCDialog.show();
                myCDialog.negative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myCDialog.dismiss();

                    }
                });
                myCDialog.positive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enter=myCDialog.getEnter();
                        if(!TextUtils.isEmpty(enter)) {
                            if (enter.equals(checkpwd)) {
                                database.updateIsCheck(db, checkapi);
                                dbusers.clear();
                                dbusers.addAll(database.selectAll(db));
                                listAdapter.notifyDataSetChanged();
                                checkCheck();
                            }else{
                                ToastView toast = new ToastView(ForgetPasswordActivity.this, res.getString(R.string.fgcheck_fail));
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            myCDialog.dismiss();
                        }else{
                            ToastView toast = new ToastView(ForgetPasswordActivity.this, res.getString(R.string.password_cannot_be_empty));
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                });

            }
        });


        listView.setAdapter(listAdapter);

    }

    private void checkCheck() {
        boolean check=false;
        for (int i=0;i<dbusers.size();i++){
            if(dbusers.get(i).getIsCheck()==1){
                check=true;
                break;
            }else{
                check=false;
            }
        }
        if(check){
            btn_next.setEnabled(true);
        }else{
            btn_next.setEnabled(false);
        }

    }

    private void resetCheck(){
        database.resetCheck(db);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ForgetPasswordActivity.this,LockActivity.class);
        startActivity(intent);
        resetCheck();
        finish();
    }

    private String formatApi(String api) {
        if (!api.contains("http://")){
            api="http://"+api;
        }
        return api;
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid = res.getString(R.string.login_invalid);
        String welcome = res.getString(R.string.login_welcome);

        if (url.equals(ProtocolConst.SIGNIN)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(ForgetPasswordActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                boolean distinct=database.isDistinct(db,api);
                if(distinct){
                    database.updateDefault(db, api);
                }else{
                    DBUSER dbuser=new DBUSER(name, pwd,api,1,0);
                    database.insertData(db,dbuser);
                }
                Intent intent = new Intent(ForgetPasswordActivity.this,SettingActivity.class);
                intent.putExtra("fromForget", true);
                startActivity(intent);
                resetCheck();
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } else {
                ToastView toast = new ToastView(ForgetPasswordActivity.this, invalid);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
