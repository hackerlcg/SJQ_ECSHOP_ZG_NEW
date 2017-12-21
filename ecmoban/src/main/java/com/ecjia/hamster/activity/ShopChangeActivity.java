package com.ecjia.hamster.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.SwipeListView;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.DBManager;
import com.ecjia.hamster.adapter.ShopListAdapter;
import com.ecjia.hamster.model.DBUSER;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShopChangeActivity extends BaseActivity implements HttpResponse {

    private TextView top_view_text;
    private ImageView top_view_back, right_view;
    private SwipeListView listView;
    private FrameLayout ll_null,ll_notnull;
    private DBManager database;
    private SQLiteDatabase db;
    private ArrayList<DBUSER> dbusers;
    private ShopListAdapter shopListAdapter;
    private LoginModel loginModel;
    private String name;
    private String pwd;
    private String api;
    private boolean isInner;
    public Handler Intenthandler;
    private MyDialog myDialog;

    public ShopChangeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_shop);
        database = new DBManager(this);
        db = null;
        db = database.getReadableDatabase();
        dbusers=new ArrayList<DBUSER>();
        dbusers.addAll(database.selectAll(db));
        Intent intent=getIntent();
        isInner=intent.getBooleanExtra("fromInner",false);

        loginModel = new LoginModel(ShopChangeActivity.this);
        loginModel.addResponseListener(this);

        initView();

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.shop_list));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        ll_null = (FrameLayout) findViewById(R.id.ll_null);
        ll_notnull = (FrameLayout) findViewById(R.id.ll_notnull);
        listView = (SwipeListView) findViewById(R.id.shop_list);
        right_view = (ImageView) findViewById(R.id.right_view);
        right_view.setVisibility(View.VISIBLE);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInner){
                    finish();
                }else {
                    Intent intent = new Intent(ShopChangeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        });
        right_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.hiddenRight(SwipeListView.mCurrentItemView);
                listView.flag=0;
                Intent intent = new Intent(ShopChangeActivity.this, AddShopActivity.class);
                startActivity(intent);
            }
        });

        if (shopListAdapter == null) {
            shopListAdapter = new ShopListAdapter(this, dbusers, listView.getRightViewWidth());
        }

        shopListAdapter.setOnRightItemClickListener(new ShopListAdapter.onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, final int position) {
                if(v.getId()==R.id.shop_item_left){
                    if(!listView.isIsShown()){
                        if(dbusers.get(position).getIsDefault()!=1||!isInner) {
                            api = formatApi(dbusers.get(position).getApi());
                            name = dbusers.get(position).getName();
                            pwd = dbusers.get(position).getPwd();
                            loginModel.login(name, pwd, api);
                        }
                        }
                }
                if (v.getId() == R.id.ll_delete) {
                    if(isInner&&dbusers.get(position).getIsDefault()==1){
                        ToastView toast = new ToastView(ShopChangeActivity.this, res.getString(R.string.now_in_shop));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        listView.hiddenRight(SwipeListView.mCurrentItemView);
                        listView.flag = 0;
                    }else {
                        String tips = res.getString(R.string.tip);
                        String tips_content = res.getString(R.string.tips_content_del);
                        myDialog = new MyDialog(ShopChangeActivity.this, tips, tips_content);
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

                                database.deleteData(db, dbusers.get(position).getApi());
                                listView.deleteItem(SwipeListView.mCurrentItemView);
                                listView.flag = 0;
                                dbusers.clear();
                                dbusers.addAll(database.selectAll(db));
                                if (dbusers.size() == 0) {
                                    isInner = false;
                                    destory();
                                    checkNull();
                                }
                                shopListAdapter.notifyDataSetChanged();
                                myDialog.dismiss();
                            }
                        });
                    }
                } else if (v.getId() == R.id.ll_edit) {
                    if (isInner && dbusers.get(position).getIsDefault() == 1) {
                        ToastView toast = new ToastView(ShopChangeActivity.this, res.getString(R.string.now_in_shop));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        listView.hiddenRight(SwipeListView.mCurrentItemView);
                        listView.flag = 0;
                    } else {
                        api = formatApi(dbusers.get(position).getApi());
                        name = dbusers.get(position).getName();
                        pwd = dbusers.get(position).getPwd();
                        Intent intent = new Intent(ShopChangeActivity.this, EditShopActivity.class);
                        intent.putExtra("orname", name);
                        intent.putExtra("orpwd", pwd);
                        intent.putExtra("orapi", api);
                        startActivity(intent);
                        listView.hiddenRight(SwipeListView.mCurrentItemView);
                        listView.flag = 0;
                    }
                }
            }
        });

        listView.setAdapter(shopListAdapter);
        listView.flag=0;

        checkNull();

    }


    private void checkNull() {
        if (dbusers.size()==0) {
            ll_null.setVisibility(View.VISIBLE);
            ll_notnull.setVisibility(View.GONE);
        } else {
            ll_null.setVisibility(View.GONE);
            ll_notnull.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        database = new DBManager(this);
        db = database.getReadableDatabase();
        dbusers.clear();
        dbusers.addAll(database.selectAll(db));
        shopListAdapter.notifyDataSetChanged();
        checkNull();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isInner){
            finish();
        }else {
            Intent intent = new Intent(ShopChangeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    private String formatApi(String api) {
        if (!api.contains("http://")){
            api="http://"+api;
        }
        return api;
    }

    private void destory() {
        SharedPreferences s1 = null;
        SharedPreferences.Editor editor1 = null;
        SharedPreferences s2 = null;
        SharedPreferences.Editor editor2 = null;

        s1 = getSharedPreferences("userInfo",0);
        editor1 = s1.edit();
        editor1.clear();
        editor1.commit();

        s2 = getSharedPreferences("LockInfo",0);
        editor2 = s2.edit();
        editor2.clear();
        editor2.commit();

        database.destroy(db);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid = res.getString(R.string.login_invalid);
        String welcome = res.getString(R.string.login_welcome);

        if (url.equals(ProtocolConst.SIGNIN)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(ShopChangeActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                boolean distinct=database.isDistinct(db,api);
                if(distinct){
                    database.updateDefault(db, api);
                }else{
                    DBUSER dbuser=new DBUSER(name, pwd,api,1,0);
                    database.insertData(db,dbuser);
                }
                Intent intent = new Intent(ShopChangeActivity.this,ECJiaMainActivity.class);
                intent.putExtra("login", true);
                intent.putExtra("fromchange", true);
                setResult(Activity.RESULT_OK, intent);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } else {

                ToastView toast = new ToastView(ShopChangeActivity.this, invalid);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
