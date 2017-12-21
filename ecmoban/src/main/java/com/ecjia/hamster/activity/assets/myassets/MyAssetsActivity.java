package com.ecjia.hamster.activity.assets.myassets;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.NormalQuery;
import com.ecjia.entity.Assets;
import com.ecjia.hamster.activity.assets.bank.BindBankCardActivity;
import com.ecjia.hamster.activity.assets.business.BusinessDealListActivity;
import com.ecjia.hamster.activity.assets.cash.CashActivity;
import com.ecjia.hamster.activity.assets.mybankcard.MyBankCardActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.MyAssetsActivityBinding;
import com.google.gson.Gson;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 我的资产
 * Created by YichenZ on 2017/3/14 15:19.
 */

public class MyAssetsActivity extends NewBaseActivity {
    MyAssetsActivityBinding mBinding;

    private Assets mAssets;
    private NormalQuery query = new NormalQuery();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.my_assets_activity);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewText.setText("我的资产");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        RxBus.getInstance().register(this);
        loadData(true);

        Gson gson =new Gson();
        gson.toJsonTree(mAssets);

    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    @Subscribe(tag = RxBus.TAG_UPDATE)
    private void loadData(boolean isUpdate) {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIAssets()
                .getAssets(query.get())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showData(d), e -> showError(e));
    }

    private void showData(Assets data) {
        mAssets = data;
        mBinding.setData(mAssets);
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_cash_list:
                startActivity(new Intent(this, BusinessDealListActivity.class));
                break;
            case R.id.rl_my_bank_card:
                if ("-1".equals(mAssets.getVerifyStatus()) || "2".equals(mAssets.getVerifyStatus())) {
                    startActivity(new Intent(this, BindBankCardActivity.class)
                            .putExtra("assets", mAssets));
                } else {
                    startActivity(new Intent(this, MyBankCardActivity.class)
                            .putExtra("assets", mAssets));
                }
                break;
            case R.id.tv_cash_btn:
                if ("1".equals(mAssets.getVerifyStatus())) {
                    startActivity(new Intent(this, CashActivity.class)
                            .putExtra("assets", mAssets));
                } else {
                    ToastUtil.getInstance().makeShortToast(this, "银行卡通过后才能提现.");
                }
                break;
        }
    }
}
