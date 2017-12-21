package com.ecjia.hamster.activity.goods.push.goodsinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.hamster.activity.goods.ReleaseGoodActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ActivityGoodsInfoPushBinding;
import com.ybk.intent.inject.annotation.Extra;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;

/**
 * ecjia-shopkeeper-android
 * 提交商品详情
 * Created by YichenZ on 2017/3/21 10:04.
 */

public class GoodsInfoPushActivity extends NewBaseActivity{
    ActivityGoodsInfoPushBinding mBinding;

    @Extra("infoStr")
    String infoStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_info_push);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        mBinding.icHead.topViewText.setText("商品详情");
        mBinding.etInputInfo.setText(infoStr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_cash_btn:
                RxBus.getInstance().post(ReleaseGoodActivity.IN_INFO,
                        mBinding.etInputInfo.getText().toString().trim());
                finish();
                break;
        }
    }
}
