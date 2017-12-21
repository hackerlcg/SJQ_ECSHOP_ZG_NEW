package com.ecjia.hamster.activity.goods.push.setprice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.entity.ValuePrice;
import com.ecjia.hamster.activity.goods.ReleaseGoodActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ActivitySetPriceBinding;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.annotation.ExtraArrayParcelable;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.ui.custom.GearRecyclerItemDecoration;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/22 16:40.
 */

public class SetPriceActivity extends NewBaseActivity{
    ActivitySetPriceBinding mBinding;

    SetPriceAdapter mAdapter;

//    @ExtraArrayParcelable("data")
    ArrayList<ValuePrice> data;
//    @Extra("price")
    String price;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data=getIntent().getParcelableArrayListExtra("GoodPriceList");
        price=getIntent().getStringExtra("price");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_price);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewText.setText("价格设置");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());

        init();
    }

    private void init() {
//        IntentInject.inject(this);
        if(data == null){
            data = new ArrayList<>();
            data.add(new ValuePrice());
        }
        if(price == null){
            price = "";
        }
        mBinding.etPrice.setText(price);
        mAdapter = new SetPriceAdapter(this,data);

        mBinding.rvPrice.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvPrice.addItemDecoration(new GearRecyclerItemDecoration(this,LinearLayoutManager.VERTICAL,1));
        mBinding.rvPrice.setAdapter(mAdapter);
    }

    /**
     * 检测输入价格是否合法
     */
    private void onVerification(){
        String regEx = "[0-9]*\\.[0-9][0-9]?";
        Pattern pa = Pattern.compile(regEx);
        float price = 0.0f;//售价
        List<Integer> num = new ArrayList<>();//阶梯数量
        List<Float> sPrices = new ArrayList<>();//阶梯价
        //数据赋值并检测
        try{
            price = Float.valueOf(mBinding.etPrice.getText().toString().trim());
        } catch (Exception e){
            ToastUtil.getInstance().makeShortToast(this,"请输入正确的售价");
            return;
        }
        try{
            num.clear();
            sPrices.clear();
            for (int i = 0; i < data.size(); i++) {
                num.add(Integer.valueOf(data.get(i).getNum()));
                sPrices.add(Float.valueOf(data.get(i).getPrice()));
            }
        } catch (Exception e){
            ToastUtil.getInstance().makeShortToast(this,"请输入正确阶梯数量或价格");
            return;
        }
        //判断是否为正确价格
        if(!pa.matcher(String.valueOf(price)).matches()){
            ToastUtil.getInstance().makeShortToast(this,"请输入正确的售价");
            return;
        }
        if(price <= 0){
            ToastUtil.getInstance().makeShortToast(this,"售价不能小于0");
            return;
        }
        //判断阶梯数量
        for (int i = 0; i < num.size(); i++) {
            if(i == 0 && num.get(0) <= 0){
                ToastUtil.getInstance().makeShortToast(this,i+1+"阶梯数量不能为小于0");
                return;
            }
            if(i > 0 && num.get(i) <= num.get(i-1)){
                ToastUtil.getInstance().makeShortToast(this,i+1+"阶梯数量要大于上一个数量");
                return;
            }
        }
        //判断价格
        for (int i = 0; i < sPrices.size(); i++) {
            //判断是否为正确价格
            if(!pa.matcher(String.valueOf(sPrices.get(i))).matches()){
                ToastUtil.getInstance().makeShortToast(this,i+1+"阶梯价格输入有误");
                return;
            }
            if(i == 0 && sPrices.get(0) <= 0){
                ToastUtil.getInstance().makeShortToast(this,i+1+"阶梯价格不能为小于0");
                return;
            }
            if(i > 0 && sPrices.get(i) >= sPrices.get(i-1)){
                ToastUtil.getInstance().makeShortToast(this,i+1+"阶梯价格不能大于上一个阶梯价格");
                return;
            }
        }

        RxBus.getInstance().post(ReleaseGoodActivity.IN_PRICE,price);//给价格
        RxBus.getInstance().post(ReleaseGoodActivity.IN_PRICE_VOLUME,data);//给价格
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_add_price:
                if(mAdapter.getData().size() <= 2){
                    mAdapter.getData().add(new ValuePrice());
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_cash_btn:
                data = mAdapter.getData();
                onVerification();
                break;
        }
    }
}
