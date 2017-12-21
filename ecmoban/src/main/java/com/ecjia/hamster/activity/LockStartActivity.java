package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;


public class LockStartActivity extends BaseActivity {

    private TextView top_view_text, tv_lock;
    private CheckBox cb_destroy, cb_lock;
    private ImageView top_view_back;
    private LinearLayout bottom_area,reset;
    SharedPreferences remlock;
    SharedPreferences.Editor edit;
    private Boolean isLock;
    private Boolean isProtect;

    public LockStartActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_setting);
        initView();

    }

    private void initView() {
        remlock = getSharedPreferences("LockInfo", 0);
        edit = remlock.edit();

        top_view_text = (TextView) findViewById(R.id.top_view_text);
        tv_lock = (TextView) findViewById(R.id.tv_lock);
        bottom_area = (LinearLayout) findViewById(R.id.bottom_area);
        reset = (LinearLayout) findViewById(R.id.reset);
        top_view_text.setText(res.getText(R.string.setting_gestruelock));

        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        cb_lock = (CheckBox) findViewById(R.id.cb_lock);
        cb_destroy = (CheckBox) findViewById(R.id.cb_destroy);

        isLock = remlock.getBoolean("lockState", false);

        if (isLock) {
            cb_lock.setChecked(true);
            tv_lock.setText(res.getString(R.string.set_to_off));
            bottom_area.setVisibility(View.VISIBLE);
        } else {
            cb_lock.setChecked(false);
            tv_lock.setText(res.getString(R.string.set_to_on));
            bottom_area.setVisibility(View.INVISIBLE);
        }

        cb_lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    edit.putBoolean("lockState", true);
                    tv_lock.setText(res.getString(R.string.set_to_off));
                    bottom_area.setVisibility(View.VISIBLE);
                } else {
                    edit.putBoolean("lockState", false);
                    tv_lock.setText(res.getString(R.string.set_to_on));
                    bottom_area.setVisibility(View.INVISIBLE);
                }
                edit.commit();
            }
        });

        isProtect = remlock.getBoolean("isProtect", false);

        if (isProtect) {
            cb_destroy.setChecked(true);
        } else {
            cb_destroy.setChecked(false);
        }

        cb_destroy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    edit.putBoolean("isProtect", true);
                } else {
                    edit.putBoolean("isProtect", false);
                }
                edit.commit();
            }
        });


        top_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LockStartActivity.this,SetLockActivity.class);
                startActivity(intent);
            }
        });

    }


}
