package com.ecjia.hamster.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;


public class LockFStartActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_set_lock;

    public LockFStartActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_set_lock);
        initView();

    }

    private void initView() {
        top_view_text=(TextView)findViewById(R.id.top_view_text);
        btn_set_lock=(Button)findViewById(R.id.btn_set_lock);

        top_view_text.setText(res.getText(R.string.set_gestruelock));

        top_view_back=(ImageView)findViewById(R.id.top_view_back);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_set_lock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LockFStartActivity.this,SetLockActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });

    }



}
