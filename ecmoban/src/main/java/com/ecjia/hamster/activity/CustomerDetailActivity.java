package com.ecjia.hamster.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FeedBackModel;
import com.ecjia.hamster.model.FEEDBACK_USER_ITEM;
import com.ecjia.util.MyBitmapUtils;

/**
 * Created by Administrator on 2016/1/7.
 */
public class CustomerDetailActivity extends BaseActivity {
    private TextView title_text;
    private ImageView back;

    private ImageView user_img,user_sex;
    private TextView tv_user_name,tv_level,tv_balance,tv_order_num,tv_red_pager_num,tv_integral,tv_mobile,tv_email;

    private FEEDBACK_USER_ITEM feedback_user_item;
    private MyBitmapUtils myBitmapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer_detail);
        initView();
    }

    private void initView() {
        feedback_user_item= FeedBackModel.getIntance().user_item;
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

        user_img= (ImageView) findViewById(R.id.user_img);
        user_sex= (ImageView) findViewById(R.id.user_sex);
        tv_user_name= (TextView) findViewById(R.id.user_name);
        tv_level= (TextView) findViewById(R.id.user_level);
        tv_balance= (TextView) findViewById(R.id.user_balance);
        tv_order_num= (TextView) findViewById(R.id.orders_num);
        tv_red_pager_num= (TextView) findViewById(R.id.red_pager_num);
        tv_integral= (TextView) findViewById(R.id.integral_num);
        tv_mobile= (TextView) findViewById(R.id.mobile_num);
        tv_email= (TextView) findViewById(R.id.user_email);

        myBitmapUtils.displaySmallImage(user_img,feedback_user_item.getAvatar_img());
        tv_user_name.setText(feedback_user_item.getUser_name());
        tv_level.setText(feedback_user_item.getRank_name());
        tv_balance.setText(feedback_user_item.getFormatted_user_money());
        tv_order_num.setText(feedback_user_item.getOrders()+"");
        tv_red_pager_num.setText(feedback_user_item.getBonus_count()+"");
        tv_integral.setText(feedback_user_item.getUser_points());
        if(TextUtils.isEmpty(feedback_user_item.getMobile_phone())){
            tv_mobile.setText(res.getString(R.string.is_null));
        }else {
            tv_mobile.setText(feedback_user_item.getMobile_phone());
        }
        tv_email.setText(feedback_user_item.getEmail());
        if("1".equals(feedback_user_item.getSex())){
            user_sex.setBackgroundResource(R.drawable.sex1);
        }else if("2".equals(feedback_user_item.getSex())){
            user_sex.setBackgroundResource(R.drawable.sex0);
        }else{
            user_sex.setVisibility(View.GONE);
        }

    }
}
