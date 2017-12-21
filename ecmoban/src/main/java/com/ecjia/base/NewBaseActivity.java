package com.ecjia.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ecjia.component.network.model.ActivityManagerModel;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 新的baseActivity 替换掉以前baseActivity多余的东西
 * Created by YichenZ on 2017/2/14 12:01.
 */

public class NewBaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    public ECJiaApplication mApp;
    public Activity mActivity;
    public LayoutInflater mInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        mApp = (ECJiaApplication) getApplicationContext();
        PushAgent.getInstance(this).onAppStart();
        ActivityManagerModel.addLiveActivity(this);//暂时使用这个管理类
        mActivity = NewBaseActivity.this;
        mInflater = LayoutInflater.from(NewBaseActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        ActivityManagerModel.addVisibleActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        ActivityManagerModel.removeVisibleActivity(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        ActivityManagerModel.removeLiveActivity(this);
        super.onDestroy();
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 获取屏幕宽度
     */
    public int getDisplayMetricsWidth() {
        int i = getWindowManager().getDefaultDisplay().getWidth();
        int j = getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    // 关闭键盘
    protected void closeKeyBoard(EditText mEditText) {
        mEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }


    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    public final <T> LifecycleTransformer<T> liToDestroy(){
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public void onClick(View v) {

    }
}
