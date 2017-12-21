package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.UserModel;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


public class CustomerCenterActivity extends BaseActivity implements OnClickListener,HttpResponse {

    private TextView top_view_text,tv_center_name,tv_center_mail,tv_center_last_login;
    private ImageView top_view_back;
    private MyDialog myDialog;
    private Button exit_login;
    private SharedPreferences remlock;
    private SharedPreferences remdname;
    private SharedPreferences.Editor edit,editor;
    private UserModel dataModel;

    public CustomerCenterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_center);
        EventBus.getDefault().register(this);
        remdname = getSharedPreferences("userInfo", 0);
        editor = remdname.edit();
        remlock=getSharedPreferences("LockInfo", 0);
        edit = remlock.edit();

        if(null==dataModel){
            dataModel=new UserModel(this);
            dataModel.addResponseListener(this);
        }

        initView();

        dataModel.getUserInfo(api);
    }


    private void initView() {
        top_view_text=(TextView)findViewById(R.id.top_view_text);

        tv_center_name=(TextView)findViewById(R.id.tv_center_name);
        tv_center_mail=(TextView)findViewById(R.id.tv_center_mail);
        tv_center_last_login=(TextView)findViewById(R.id.tv_center_last_login);

        top_view_text.setText(res.getText(R.string.custom_center));


        top_view_back=(ImageView)findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(this);


        exit_login=(Button)findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                    finish();
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

                        dataModel.signOut(api);

                    }
                });


                break;
        }
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
        if (url.equals(ProtocolConst.ADMIN_USER_USERINFO)) {
            if (status.getSucceed() == 1) {
               tv_center_name.setText(dataModel.admin.getUsername());
               tv_center_mail.setText(dataModel.admin.getEmail());
               tv_center_last_login.setText(dataModel.admin.getLast_login());
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_USER_SIGNOUT)) {
            if (status.getSucceed() == 1) {
                Intent intent =new Intent(CustomerCenterActivity.this,LoginActivity.class);
                startActivity(intent);
                edit.putBoolean("lockState", false);
                editor.putString("password", "");
                editor.commit();
                edit.commit();
                EventBus.getDefault().post(new MyEvent("exit"));
                finish();
            }else{
                ToastView toast = new ToastView(CustomerCenterActivity.this, res.getString(R.string.center_signout_toast));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
