package com.ecjia.hamster.activity.assets.business;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.PageQuery;
import com.ecjia.entity.BusinessDeal;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.BusinessDealListActivityBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 交易记录
 * Created by YichenZ on 2017/3/14 17:07.
 */

public class BusinessDealListActivity extends NewBaseActivity {
    BusinessDealListActivityBinding mBinding;
    List<BusinessDeal> mDeals = new ArrayList<>();
    BusinessDealAdapter mAdapter;

    PageQuery mQuery = new PageQuery();
    int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.business_deal_list_activity);
        mBinding.icHead.topViewText.setText("交易记录");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        initView();
        loadData();
    }

    private void loadData() {
        String addTime = "";
        if(mDeals.size() > 0){
            addTime=mDeals.get(mDeals.size()-1).getTime();
        }
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIAssets()
                .getBusinessDeal(mQuery.getBussiness(page,addTime))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {
                    pageHandler(d.getPaginated());
                    showData(d.getData());
                }, e -> showError(e));
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            mAdapter.noMore(true);
        } else {
            mAdapter.noMore(false);
        }
    }

    private void showData(List<BusinessDeal> data) {
        if (page == 1) {
            mDeals.clear();
        }
        mDeals.addAll(data);
        mAdapter.notifyDataSetChanged();
        page++;
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    private void initView() {
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BusinessDealAdapter(this, mDeals);
        mAdapter.setOnLoadMoreListener(() -> loadData());
        mBinding.rvData.setAdapter(mAdapter.adapter());
    }
}
