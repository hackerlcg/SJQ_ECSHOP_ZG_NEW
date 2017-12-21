package com.ecjia.hamster.activity;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;


public class APPInfoActivity extends BaseActivity {

    private TextView top_view_text,tv_version;
    private ImageView top_view_back;

    public APPInfoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_appinfo);

        initView();

    }


    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        tv_version = (TextView) findViewById(R.id.tv_version);

        top_view_text.setText(res.getText(R.string.about).toString()+res.getText(R.string.app_name).toString());

        PackageManager manager;
        PackageInfo info = null;
        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }
        tv_version.setText(res.getText(R.string.app_name).toString()+info.versionName+"\n"+info.versionCode);

        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });

    }

}
