package com.ecjia.hamster.activity.order;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.base.RSubscriberAbstract;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.requestmodel.EditOrderPrice;
import com.ecjia.component.view.MyDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.common.JsonUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * 类名介绍：修改订单价格
 * Created by sun
 * Created time 2017-03-15.
 */

public class EditOrderPriceActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;

    @BindView(R.id.tv_goods_amount)
    TextView tv_goods_amount;
    @BindView(R.id.edit_shipping_fee)
    EditText edit_shipping_fee;
    @BindView(R.id.edit_discount)
    EditText edit_discount;


    @Extra
    Float goodsAmount;//"name"is the default key
    @Extra
    Float shippingFee;
    @Extra
    Float discount;
    @Extra
    String orderId;


    @Override
    public int getLayoutId() {
        return R.layout.act_edit_orderprice;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line cod
        tv_goods_amount.setText(goodsAmount + "");
        if (shippingFee > 0) {
            edit_shipping_fee.setText(shippingFee + "");
        } else {
            edit_shipping_fee.setText("0");
        }
        if (discount > 0) {
            edit_discount.setText(discount + "");
        } else {
            edit_discount.setText("");
        }
        top_view_text.setText("订单改价");
        edit_shipping_fee.setHint("运费");
        edit_discount.setHint("优惠金额小于" + goodsAmount);

        edit_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    float n = Float.parseFloat(s.toString());
                    if (n >= goodsAmount) {
                        edit_discount.setText(goodsAmount-0.01 + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.top_view_back, R.id.btn_discount_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.btn_discount_save:
                String shippingStr = edit_shipping_fee.getText().toString();
                String disc = edit_discount.getText().toString();
                EditOrderPrice editOrderPrice = new EditOrderPrice();
                editOrderPrice.setOrder_id(orderId);
                editOrderPrice.setDiscount(TextUtils.isEmpty(disc) ? "0" : disc);
                editOrderPrice.setShipping_fee(TextUtils.isEmpty(shippingStr) ? "0" : shippingStr);
//                LG.d("OkHttp>>>>>>>"+JsonUtil.toString(editOrderPrice));
                backTips(JsonUtil.toHttpFormatString(editOrderPrice));
                break;
        }
    }

    private void editOrderPrice(String jsonStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getEditOderPrice(jsonStr)
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new RSubscriberAbstract<String>() {
                    @Override
                    public void _onNext(String s, BaseRes.Paginated page) {
                        ToastUtil.getInstance().makeLongToast(mActivity, "价格修改成功");
                        EventBus.getDefault().post(new MyEvent("EditOrderPriceActivity"));
                        finish();
                    }

                    @Override
                    public void _onError(Throwable t) {
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }
                });
    }

    private void backTips(String jsonStr) {
        MyDialog myDialog = new MyDialog(this, getResources().getString(R.string.tip), "确认修改价格吗");
        myDialog.show();
        myDialog.negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                editOrderPrice(jsonStr);
            }
        });
    }

    @Override
    public void dispose() {

    }

//    @Extra
//    String name;//"name"is the default key
//    @Extra
//    int age;
//
//    @Extra("price")
//    float price;
//    @Extra("dou")
//    double dou;
//    @Extra("test")//Test need to implements Parcelable or Serializable
//            Test test;
//
//    @ExtraArrayString("datas")
//    ArrayList<String> datas;
//    @ExtraArrayParcelable("tests")
//    ArrayList<Test> tests;
//    @ExtraArrayInt("ints")
//    ArrayList<Integer> ints;


}
