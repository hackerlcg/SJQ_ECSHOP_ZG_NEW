package com.ecjia.hamster.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.ShopModel;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;


public class ShopEditActivity extends BaseActivity implements HttpResponse {

    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private ShopModel dataModel;
    private TextView top_view_text, top_right_tv;
    private EditText et_content;
    private ImageView top_view_back, iv_del_content;
    private LinearLayout ll_content;
    private String content;
    private int type;
    private String backcontent;
    private MyDialog myDialog;
    private int privilege;

    public ShopEditActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shop_edit);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        type = intent.getIntExtra("type", 0);
        privilege = intent.getIntExtra("privilege", 1);

        if (dataModel == null) {
            dataModel = new ShopModel(this);
            dataModel.addResponseListener(this);
        }
        initView();
    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_right_tv = (TextView) findViewById(R.id.top_right_tv);
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        iv_del_content = (ImageView) findViewById(R.id.iv_del_content);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setText(content);
        if (privilege < 2) {
            top_right_tv.setVisibility(View.GONE);
            et_content.setEnabled(false);
        } else {
            et_content.setSelection(content.length());
            et_content.setFocusable(true);
            et_content.setFocusableInTouchMode(true);
            et_content.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et_content, 0);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) et_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(et_content, 0);
                               }
                           },
                    400);
        }
        switch (type) {
            case 1:
                top_view_text.setText(res.getString(R.string.edit_phone));
                break;
            case 2:
                top_view_text.setText(res.getString(R.string.edit_address));
                ll_content.setMinimumHeight((int) (res.getDimension(R.dimen.dim280)));
                break;
            case 3:
                top_view_text.setText(res.getString(R.string.edit_description));
                ll_content.setMinimumHeight((int) (res.getDimension(R.dimen.dim280)));
                break;
        }
        top_right_tv.setText(res.getText(R.string.save));
        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard();
                finish();
            }
        });

        iv_del_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
            }
        });

        top_right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backcontent = et_content.getText().toString();

                if (backcontent.equals(content)) {
                    ToastView toast = new ToastView(ShopEditActivity.this, res.getString(R.string.not_edit_shop));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                } else if (TextUtils.isEmpty(backcontent)) {

                    String tips = res.getString(R.string.tip);
                    String tips_content = res.getString(R.string.edit_shop_dialog);
                    myDialog = new MyDialog(ShopEditActivity.this, tips, tips_content);
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
                            Update();
                            myDialog.dismiss();
                        }


                    });

                } else {
                    Update();
                }
            }
        });

    }

    public void Update() {
        switch (type) {
            case 1:
                dataModel.updateShop(session, 0, backcontent, 0, 0, "", "", 1, api);
                break;
            case 2:
                dataModel.updateShop(session, 0, "", 0, 0, backcontent, "", 2, api);
                break;
            case 3:
                dataModel.updateShop(session, 0, "", 0, 0, "", backcontent, 3, api);
                break;
        }
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        et_content.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.UPDATESHOP)) {
            if (status.getSucceed() == 1) {
                EventBus.getDefault().post(new MyEvent("EDITSHOP"));
                finish();
            }
        }

    }
}
