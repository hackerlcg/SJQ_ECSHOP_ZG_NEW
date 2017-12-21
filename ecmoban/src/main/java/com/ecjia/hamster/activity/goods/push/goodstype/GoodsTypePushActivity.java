package com.ecjia.hamster.activity.goods.push.goodstype;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.NormalQuery;
import com.ecjia.entity.Species;
import com.ecjia.hamster.activity.goods.push.spec.GoodsSpecPushActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ActivityGoodsTypePushBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 商品类型
 * Created by YichenZ on 2017/3/21 10:37.
 */

public class GoodsTypePushActivity extends NewBaseActivity{
    ActivityGoodsTypePushBinding mBinding;

    GoodsTypePushAdapter mAdapter;
    ArrayList<Species.SpeciBean> speci = new ArrayList<>();
    NormalQuery mQuery = new NormalQuery();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_type_push);
        mBinding.icHead.topViewText.setText("商品类型");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());

        intData();
        initUI();
        loadData();
    }

    private void intData() {
        mAdapter = new GoodsTypePushAdapter(this,speci);
        mAdapter.setOnItemClickListener((view, data) -> {
            startActivity(new Intent(this, GoodsSpecPushActivity.class)
            .putExtra("spec", ((Species.SpeciBean)data)));
        });
    }

    private void initUI() {
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvData.setAdapter(mAdapter);
    }


    private void loadData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIPush()
                .getAttribute(mQuery.get())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showData(d),e -> showError(e));
    }

    private void showData(Species data){
        if(data != null && data.getSpeci() != null) {
            speci.clear();
            speci.addAll(data.getSpeci());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showError(Throwable e){
        ToastUtil.getInstance().makeShortToast(this,e.getMessage());
    }
}
