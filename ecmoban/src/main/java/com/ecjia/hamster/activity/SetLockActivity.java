package com.ecjia.hamster.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.view.LockPatternView;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.ArrayList;
import java.util.List;


public class SetLockActivity extends BaseActivity implements
        LockPatternView.OnPatternListener  {

    private TextView top_view_text,tv_tips;
    private ImageView top_view_back;
    private LockPatternView lockPatternView;
    SharedPreferences remlock;
    SharedPreferences.Editor edit;
    private Boolean TempLockstate;
    private Boolean ISRESET;
    private List<LockPatternView.Cell> lockPattern;

    public SetLockActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_lock);
        initView();

    }

    private void initView() {
        top_view_text=(TextView)findViewById(R.id.top_view_text);
        tv_tips=(TextView)findViewById(R.id.tv_tips);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        top_view_text.setText(res.getText(R.string.gestruelock));
        tv_tips.setText(res.getText(R.string.set_your_lock));
        remlock=getSharedPreferences("LockInfo", 0);
        edit=remlock.edit();

        TempLockstate=false;
        ISRESET=false;

        top_view_back=(ImageView)findViewById(R.id.top_view_back);

        top_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private Handler Delayhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lockPatternView.clearPattern();
                    lockPatternView.enableInput();
                    break;
                case 2:
                    lockPatternView.clearPattern();
                    lockPatternView.enableInput();
                    tv_tips.setText(res.getText(R.string.set_your_lock));
                    lockPattern.clear();
                    TempLockstate=false;
                    ISRESET=true;
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    public void onPatternStart() {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            lockPatternView.disableInput();
            tv_tips.setText(res.getText(R.string.set_lock_tooshort));
            delay(1);
            return;
        }
        if(TempLockstate){
            if(lockPattern.equals(pattern)){
                tv_tips.setText(res.getText(R.string.set_lock_success));
                edit.putString("myLock",LockPatternView.patternToString(lockPattern));
                edit.putBoolean("ifFirst",false);
                edit.putBoolean("lockState",true);
                edit.commit();
                finish();
            }else {
                tv_tips.setText(res.getText(R.string.set_lock_fail));
                lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                lockPatternView.disableInput();
                delay(2);
                return;
            }

        }else{
            TempLockstate=true;
            if(lockPattern==null||ISRESET){
                lockPattern=new ArrayList<LockPatternView.Cell>(pattern);
                ISRESET=false;
            }
            tv_tips.setText(res.getText(R.string.confirm_your_lock));
            lockPatternView.clearPattern();
            return;
        }

    }

    private void delay(final int i) {
        new Thread(){
            public void run(){

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain(Delayhandler);
                msg.what = i;
                Delayhandler.sendMessage(msg);
            }

        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
