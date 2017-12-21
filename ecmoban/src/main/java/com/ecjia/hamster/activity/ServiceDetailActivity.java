package com.ecjia.hamster.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FeedBackModel;
import com.ecjia.hamster.model.CUSTOMER_SERVICE_ITEM;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.TimeUtil;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ServiceDetailActivity extends BaseActivity {
    private TextView title_text;
    private ImageView back;
    private ImageView service_img;
    private TextView tv_service_name,tv_role,tv_email,tv_time,tv_keep_time;
    private CUSTOMER_SERVICE_ITEM customer_service_item;
    private MyBitmapUtils myBitmapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_service_detail);
        initView();
    }

    private void initView() {
        customer_service_item=FeedBackModel.getIntance().customer_service_item;
        myBitmapUtils=MyBitmapUtils.getInstance(this);
        title_text= (TextView) findViewById(R.id.top_view_text);
        title_text.setText(res.getString(R.string.user_detail));
        back= (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        service_img= (ImageView) findViewById(R.id.service_img);
        tv_service_name= (TextView) findViewById(R.id.service_name);
        tv_role= (TextView) findViewById(R.id.service_role);
        tv_email= (TextView) findViewById(R.id.email_num);
        tv_time= (TextView) findViewById(R.id.logined_time);
        tv_keep_time= (TextView) findViewById(R.id.keep_time);

        myBitmapUtils.displaySmallImage(service_img,customer_service_item.getAvator_img());
        tv_service_name.setText(customer_service_item.getUser_name());
        tv_role.setText(customer_service_item.getRole_name());
        if(TextUtils.isEmpty(customer_service_item.getRole_name())||"null".equals(customer_service_item.getRole_name())){
            tv_role.setText(res.getString(R.string.is_null));
        }else{
            tv_role.setText(customer_service_item.getRole_name());
        }
        tv_email.setText(customer_service_item.getEmail());
        tv_time.setText(customer_service_item.getLast_login());
        tv_keep_time.setText(TimeUtil.getKeepingTime(customer_service_item.getLast_login()));
    }

}
