package com.ecjia.hamster.activity.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.ecjia.base.LibActivity;
import com.ecjia.hamster.fragment.MyGoodsFragment;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名介绍：我的宝贝，activity
 * Created by sun
 * Created time 2017-03-15.
 */

public class MyGoodsParentActivity extends LibActivity {
    @BindView(R.id.btn_onsale)
    Button btn_onsale;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mygoods_parent;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        replaceWithAnim(new MyGoodsFragment());
    }

    public void setBottomShow(boolean result){
        if(result){
            btn_onsale.setVisibility(View.VISIBLE);
        }else {
            btn_onsale.setVisibility(View.GONE);
        }

    }

    private void replaceWithAnim(Fragment fragment) {

        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (fragment instanceof ServiceFragment) {
//            localFragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
//        } else {
//            localFragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
//        }
        localFragmentTransaction.replace(R.id.ly_fragment, fragment);
        localFragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.btn_onsale})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_onsale:
                ReleaseGoodActivity_Builder.intent(mActivity).goodId("0").start();
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
