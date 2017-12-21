package com.ecjia.hamster.activity.assets.mybankcard;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.entity.Assets;
import com.ecjia.hamster.activity.assets.bank.BindBankCardActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.MyBankCardActivityBinding;

import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 我的银行卡
 * Created by YichenZ on 2017/3/15 17:11.
 */

public class MyBankCardActivity extends NewBaseActivity {
    MyBankCardActivityBinding mBinding;

    Assets mAssets;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.my_bank_card_activity);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewText.setText("我的银行卡");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        TextView v =new TextView(this);
        v.setText("");
        initUI();
    }

    private void initUI() {
        mAssets = (Assets) getIntent().getSerializableExtra("assets");
        if (mAssets == null) {
            ToastUtil.getInstance().makeShortToast(this, "暂无银行卡信息");
            finish();
        }

        mBinding.setData(mAssets);
    }
    Dialog dia;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cash_btn:
                if("0".equals(mAssets.getVerifyStatus())) {
                    startActivity(new Intent(this, BindBankCardActivity.class)
                            .putExtra("assets", mAssets));
                } else {
                    if ("1".equals(mAssets.getVerifyStatus())) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyBankCardActivity.this);
                        alertDialog.setTitle("联系客服")
                                .setMessage("审核通过的银行卡变更需要联系客服4006805876进行处理")
                                .setNegativeButton("取消", (dialog, which) -> dia.dismiss())
                                .setPositiveButton("确定", (dialog, which) -> {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + "4006805876");
                                    intent.setData(data);
                                    startActivity(intent);
                                });
                        dia = alertDialog.show();
                    }
                }
                break;
        }
    }
}
