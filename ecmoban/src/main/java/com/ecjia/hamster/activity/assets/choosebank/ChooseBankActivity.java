package com.ecjia.hamster.activity.assets.choosebank;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.entity.Bank;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ChooseBankActivityBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 选择银行
 * Created by YichenZ on 2017/3/15 15:45.
 */

public class ChooseBankActivity extends NewBaseActivity {
    ChooseBankActivityBinding mBinding;
    ChooseBankAdapter mAdapter;
    List<Bank> mBanks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.choose_bank_activity);
        mBinding.icHead.topViewText.setText("选择开户行");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        initUI();
        loadData();
    }

    private void loadData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIAssets()
                .getBanks("")
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showData(d), e -> showError(e));

    }

    private void initUI() {
        mAdapter = new ChooseBankAdapter(this, mBanks);
        mAdapter.setOnItemClickListener((view, holder, position) -> {
            RxBus.getInstance().post(RxBus.TAG_CHANGE,mBanks.get(position).getName());
            finish();
        });
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvData.setAdapter(mAdapter.adapter());
    }

    public void showData(List<Bank> data) {
        mBanks.clear();
        mBanks.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }
}
