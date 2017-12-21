package com.ecjia.hamster.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;


public class QRCodeActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;

    public QRCodeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_qrcode);
        initView();

    }

    private void initView() {
        top_view_text=(TextView)findViewById(R.id.top_view_text);

        top_view_text.setText(res.getText(R.string.shop_zxing));

        top_view_back=(ImageView)findViewById(R.id.top_view_back);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



}
