package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.QRCodeModel;
import com.ecjia.component.view.ToastView;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


public class QRBindActivity extends BaseActivity implements HttpResponse {

    private QRCodeModel dataModel;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private String code;
    private SESSION session;
    private TextView top_view_text,tv_tips;
    private ImageView top_view_back;
    private Button btn_next,btn_cancel;
    private boolean bindflag=false;
    private String bindtype;
    private FrameLayout fl_bind_succeed;

    public QRBindActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_qrbind);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent=getIntent();
        code=intent.getStringExtra("code");


        if(dataModel==null){
            dataModel=new QRCodeModel(this);
            dataModel.addResponseListener(this);
        }


        initView();

        if(!TextUtils.isEmpty(code)){
            dataModel.QRValidate(session,code, api);
        }


    }

    private void initView() {
        fl_bind_succeed = (FrameLayout) findViewById(R.id.fl_bind_succeed);
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        top_view_text.setText(res.getText(R.string.qrbind_title));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bindflag){
                    bindtype="bind";
                    dataModel.QRBind(session,code,bindtype,api);
                }else{
                    Intent intent=new Intent(QRBindActivity.this,MyCaptureActivity.class);
                    intent.putExtra("startType", 1);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bindtype="unbind";
                dataModel.QRBind(session,code,bindtype,api);
            }
        });

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

        if (url.equals(ProtocolConst.QR_VALIDATE)) {
            if (status.getSucceed() == 1) {
                btn_next.setText(res.getString(R.string.qrbind_confirm));
                bindflag=true;
                btn_cancel.setVisibility(View.VISIBLE);
                btn_cancel.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_in));
                ToastView toast = new ToastView(QRBindActivity.this, res.getString(R.string.qrvalidate_succeed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else{
                btn_next.setText(res.getString(R.string.qrbind_rescan));
                bindflag=false;
                btn_cancel.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_out));
                btn_cancel.setVisibility(View.INVISIBLE);
                tv_tips.setText(status.getError_desc());
                ToastView toast = new ToastView(QRBindActivity.this, res.getString(R.string.qrvalidate_failed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        if (url.equals(ProtocolConst.QR_BIND)) {
            if("bind".equals(bindtype)){
                if (status.getSucceed() == 1) {
                    fl_bind_succeed.setVisibility(View.VISIBLE);
                    fl_bind_succeed.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_in));
                }else {
                    tv_tips.setText(status.getError_desc());
                    btn_next.setText(res.getString(R.string.qrbind_rescan));
                    bindflag=false;
                    btn_cancel.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_out));
                    btn_cancel.setVisibility(View.INVISIBLE);
                    ToastView toast = new ToastView(QRBindActivity.this, res.getString(R.string.qrbind_failed));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            if("unbind".equals(bindtype)){
                if (status.getSucceed() == 1) {
                    btn_next.setText(res.getString(R.string.qrbind_confirm));
                    bindflag=true;
                    btn_cancel.setVisibility(View.VISIBLE);
                    btn_cancel.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_in));
                    ToastView toast = new ToastView(QRBindActivity.this, res.getString(R.string.qrunbind_succeed));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                }else {
                    tv_tips.setText(status.getError_desc());
                    btn_next.setText(res.getString(R.string.qrbind_rescan));
                    bindflag=false;
                    btn_cancel.setAnimation(AnimationUtils.loadAnimation(QRBindActivity.this, R.anim.alpha_out));
                    btn_cancel.setVisibility(View.INVISIBLE);
                    ToastView toast = new ToastView(QRBindActivity.this, res.getString(R.string.qrunbind_failed));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }

    }
}
