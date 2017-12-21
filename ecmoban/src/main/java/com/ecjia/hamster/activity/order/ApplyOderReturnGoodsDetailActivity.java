package com.ecjia.hamster.activity.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.requestmodel.ReturnGoodInfo;
import com.ecjia.component.network.responsmodel.ReturnGoodsOrderInfoModel;
import com.ecjia.hamster.adapter.OderReturnGoodsDetailAdapter;
import com.ecjia.hamster.widgets.MyPullRefreshFrameLayout;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.util.common.T;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：退换货详情页面
 * Created by sun
 * Created time 2017-03-09.
 */

public class ApplyOderReturnGoodsDetailActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;


    @BindView(R.id.null_pager)
    LinearLayout null_pager;

    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_apply_time)
    TextView tv_apply_time;

    @BindView(R.id.my_pullrefresh)
    MyPullRefreshFrameLayout my_pullrefresh;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private OderReturnGoodsDetailAdapter mAdapter;
    private List<ReturnGoodsOrderInfoModel.ReturnLog> datas;
    private ReturnGoodsOrderInfoModel detailDatas;

    @Extra
    String retId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_return_goodsdetail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);
        top_view_text.setText("申请进度");
        if (TextUtils.isEmpty(retId)) {
            finish();
            return;
        }
        datas = new ArrayList<>();
        datas.clear();
        mAdapter = new OderReturnGoodsDetailAdapter(mActivity, datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);

        my_pullrefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //执行刷新操作
                getHttpData(retId);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mRecyclerView == null ? content : mRecyclerView, header);
                //return super.checkCanDoRefresh(frame, 需要刷新的当前View == null ? content : 需要刷新的当前View , header);
            }
        });
        getHttpData(retId);
    }

    private void getHttpData(String ret_id) {
        ReturnGoodInfo info = new ReturnGoodInfo();
        info.setRet_id(ret_id);
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getReturnGoodsOrderInfo(JsonUtil.toString(info))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ReturnGoodsOrderInfoModel>() {
                    @Override
                    public void onNext(ReturnGoodsOrderInfoModel oderreturngoodsdetail) {
                        detailDatas = oderreturngoodsdetail;
                        tv_number.setText("订单编号：  " + oderreturngoodsdetail.getReturn_sn());
                        tv_apply_time.setText("申请时间：   " + oderreturngoodsdetail.getApply_time());
//                        if (detailDatas != null) {
//                            setLy_bottomView();
//                        }
                        datas.clear();
                        if (oderreturngoodsdetail.getLog().size() > 0) {
                            datas.addAll(oderreturngoodsdetail.getLog());
                            mAdapter.notifyDataSetChanged();
                            null_pager.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            null_pager.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        null_pager.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        T.show(mActivity, t.getMessage());
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    @OnClick({R.id.top_view_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(retId)) {
            getHttpData(retId);
        }
    }

    @Override
    public void dispose() {

    }
}
