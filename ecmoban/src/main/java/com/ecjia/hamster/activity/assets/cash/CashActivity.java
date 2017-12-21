package com.ecjia.hamster.activity.assets.cash;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.AssetsQuery;
import com.ecjia.entity.Assets;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.CashActivityBinding;

import java.util.regex.Pattern;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 提现
 * Created by YichenZ on 2017/3/16 09:53.
 */

public class CashActivity extends NewBaseActivity {
    CashActivityBinding mBinding;

    Assets mAssets;
    AssetsQuery query = new AssetsQuery();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.cash_activity);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewText.setText("提现");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());

        initData();
    }

    private void initData() {
        mAssets = (Assets) getIntent().getSerializableExtra("assets");
        if (mAssets == null) {
            ToastUtil.getInstance().makeShortToast(this, "没有资产数据");
            finish();
        }

        mBinding.setData(mAssets);
    }

    private void putCash() {
        String price = getPrice();
        if(price == null){
            return;
        }
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIAssets()
                .putwithdraw(query.getCashQuery(price))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> onSuccess(), e -> showError(e));
    }

    /**
     * 获取提现金额
     * 提现金额不能大于可提现金额
     *
     * @return 提现金额
     */
    private String getPrice() {
        float price = 0.0f;
        try {
            price = Float.valueOf(mBinding.etCash.getText().toString().trim());
        } catch (NumberFormatException e) {
            ToastUtil.getInstance().makeShortToast(this,"请输入正确的提现金额");
            return null;
        }
        String regEx = "[0-9]*\\.[0-9][0-9]?";
        Pattern pa = Pattern.compile(regEx);
        if(!pa.matcher(String.valueOf(price)).matches()){
            ToastUtil.getInstance().makeLongToast(this,"请输入正确的提现金额");
            return null;
        }
        if (price <= 0) {
            ToastUtil.getInstance().makeShortToast(this,"提现金额不能小于0");
            return null;
        }
        return String.valueOf(price);
    }

    public void onSuccess() {
        ToastUtil.getInstance().makeShortToast(this, "申请提现成功,三个工作日内到账");
        RxBus.getInstance().post(RxBus.TAG_UPDATE, true);
        finish();
    }

    public void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_all:
                mBinding.etCash.setText(mAssets.getCanWithdrawMoney() + "");
                break;
            case R.id.tv_cash_btn:
                putCash();
                break;
        }
    }

}
