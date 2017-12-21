package com.ecjia.hamster.activity.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.requestmodel.ReturnGoodInfo;
import com.ecjia.component.network.requestmodel.ReturnStatus;
import com.ecjia.component.network.responsmodel.ReturnGoodsOrderInfoModel;
import com.ecjia.component.view.MyDialog;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.util.common.T;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：获取退换货订单详细信息 (卖家)
 * Created by sun
 * Created time 2017-03-20.
 */

public class OrderReutrnInfoActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;

    @BindView(R.id.tv_status)
    TextView tv_status;//售后单状态
    @BindView(R.id.tv_return_type)
    TextView tv_return_type;//退款类型
    @BindView(R.id.tv_amount)
    TextView tv_amount;//退款金额
    @BindView(R.id.tv_retsn)
    TextView tv_retsn;//流水号
    @BindView(R.id.tv_orderno)
    TextView tv_orderno;//订单号
    @BindView(R.id.tv_return_time)
    TextView tv_return_time;//退货时间
    @BindView(R.id.tv_send_time)
    TextView tv_send_time;//发货时间
    @BindView(R.id.tv_cause)
    TextView tv_cause;//退换货原因
    @BindView(R.id.tv_return_brief)
    TextView tv_return_brief;//问题描述

    @BindView(R.id.ly_img)
    LinearLayout ly_img;
    @BindView(R.id.img1)
    ImageView img1;//
    @BindView(R.id.img2)
    ImageView img2;//
    @BindView(R.id.img3)
    ImageView img3;//

    @BindView(R.id.ll_log)
    LinearLayout ll_log;//协商记录

    @BindView(R.id.ly_bottom)
    LinearLayout ly_bottom;//底部按钮
    @BindView(R.id.btn_left)
    Button btn_left;//同意申请
    @BindView(R.id.btn_right)
    Button btn_right;//拒绝申请
    @BindView(R.id.tv_contact)
    TextView tv_contact;//确认收到商品
    @BindView(R.id.tv_writeinfo)
    TextView tv_writeinfo;//填写物流信息


    private ReturnGoodsOrderInfoModel data;


    @Extra
    String retId;

    @Override
    public int getLayoutId() {
        return R.layout.act_order_returninfo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);
        top_view_text.setText("退换货申请详情");
        ReturnGoodInfo info = new ReturnGoodInfo();
        if (TextUtils.isEmpty(retId)) {
            finish();
        }
        info.setRet_id(retId);
        LG.d("OkHttp" + JsonUtil.toString(info));
        getReturnGoodsOrderInfo(JsonUtil.toString(info));
    }

    private void initView() {
        //"return_type":1,//0为仅退款,1为退款退货,2为换货
        tv_status.setText(data.getReturn_statusStr());
        tv_return_type.setText(data.getReturn_typeStr());
        tv_amount.setText(data.getAllReturn() + "(商品金额：" + data.getShould_return() + ",运费：" + data.getReturn_shipping_fee() + ")");
        tv_retsn.setText(data.getReturn_sn());
        tv_orderno.setText(data.getOrder_sn());
        tv_return_time.setText(data.getApply_time());
        tv_send_time.setText(data.getShipping_time());
        tv_cause.setText(data.getCause_name());
        tv_return_brief.setText(data.getReturn_brief());

        if (data.getImages().size() > 0) {
            ly_img.setVisibility(View.VISIBLE);
            ImageLoaderForLV.getInstance(mActivity).setImageRes(img1, data.getImages().get(0));
            if (data.getImages().size() > 1) {
                ImageLoaderForLV.getInstance(mActivity).setImageRes(img1, data.getImages().get(1));
                if (data.getImages().size() > 2) {
                    ImageLoaderForLV.getInstance(mActivity).setImageRes(img1, data.getImages().get(2));
                }
            }
        } else {
//            ly_img.setVisibility(View.GONE);
            ly_img.setVisibility(View.VISIBLE);
        }
        setLy_bottomView();
    }

    private void getReturnGoodsOrderInfo(String jsonStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getReturnGoodsOrderInfo(jsonStr)
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ReturnGoodsOrderInfoModel>() {

                    @Override
                    public void onNext(ReturnGoodsOrderInfoModel returnGoodsOrderInfoModel) {
                        data = returnGoodsOrderInfoModel;
                        initView();
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //审核
    private void getVerify(String jsonStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getReturnGoodsOrderVerify(jsonStr)
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ReturnStatus>() {

                    @Override
                    public void onNext(ReturnStatus returnStatus) {
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //确认收货卖家
    private void getReturnGoodsReceivedShipping(String ret_id) {
        ReturnGoodInfo info = new ReturnGoodInfo();
        info.setRet_id(ret_id);
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getReturnGoodsReceivedShipping(JsonUtil.toString(info))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ReturnStatus>() {
                    @Override
                    public void onNext(ReturnStatus returnstatus) {

                        if ("1".equals(returnstatus.getReturn_status())) {//1为卖家收到
                            //0为仅退款,1为退款退货,2为换货
                            if ("2".equals(data.getReturn_type())) {
                                ly_bottom.setVisibility(View.GONE);
                                tv_writeinfo.setVisibility(View.VISIBLE);
                                tv_contact.setVisibility(View.GONE);
                            }else{
                                ly_bottom.setVisibility(View.GONE);
                                tv_writeinfo.setVisibility(View.GONE);
                                tv_contact.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.top_view_back, R.id.btn_left, R.id.btn_right, R.id.ll_log, R.id.tv_writeinfo, R.id.tv_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.btn_left://同意5
                ReturnStatus returnStatus = new ReturnStatus();
                returnStatus.setRet_id(retId);
                returnStatus.setReturn_status("5");
                backTips(JsonUtil.toHttpFormatString(returnStatus), "同意申请");
                break;
            case R.id.btn_right://9为拒绝
                ReturnStatus returnStatus2 = new ReturnStatus();
                returnStatus2.setRet_id(retId);
                returnStatus2.setReturn_status("9");
                backTips(JsonUtil.toHttpFormatString(returnStatus2), "拒绝申请");
                break;
            case R.id.ll_log:
                ApplyOderReturnGoodsDetailActivity_Builder.intent(mActivity).retId(retId).start();
                break;
            case R.id.tv_writeinfo://填写物流信息
                ApplyAddShippingInfoActivity_Builder.intent(mActivity).retId(retId).start();
                break;
            case R.id.tv_contact://确认收货
                getReturnGoodsReceivedShipping(retId);
                break;
        }
    }

    private void backTips(String jsonStr, String showStr) {
        MyDialog myDialog = new MyDialog(this, getResources().getString(R.string.tip), "确认" + showStr + "吗");
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
                getVerify(jsonStr);
            }
        });
    }

    private void setLy_bottomView() {
        if ("0".equals(data.getReturn_status())) {//0为已申请
            ly_bottom.setVisibility(View.VISIBLE);
            btn_left.setVisibility(View.VISIBLE);
            btn_right.setVisibility(View.VISIBLE);
            tv_writeinfo.setVisibility(View.GONE);
            tv_contact.setVisibility(View.GONE);
        } else if ("6".equals(data.getReturn_status())) { //0为已申请,1为卖家收到，2为卖家寄出（分单），3为卖家寄出，4为 完成，5为同意申请，6为买家寄出，9为卖家拒绝
            ly_bottom.setVisibility(View.VISIBLE);
            btn_left.setVisibility(View.GONE);
            btn_right.setVisibility(View.GONE);
            tv_writeinfo.setVisibility(View.GONE);
            tv_contact.setVisibility(View.VISIBLE);
        } else if ("1".equals(data.getReturn_status())) {//1为卖家收到
            btn_left.setVisibility(View.GONE);
            btn_right.setVisibility(View.GONE);
            //0为仅退款,1为退款退货,2为换货
            if ("2".equals(data.getReturn_type())) {
                ly_bottom.setVisibility(View.GONE);
                tv_writeinfo.setVisibility(View.VISIBLE);
                tv_contact.setVisibility(View.GONE);
            }else{
                ly_bottom.setVisibility(View.GONE);
                tv_writeinfo.setVisibility(View.GONE);
                tv_contact.setVisibility(View.GONE);
            }
        } else {
            ly_bottom.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(retId)) {
            ReturnGoodInfo info = new ReturnGoodInfo();
            if (TextUtils.isEmpty(retId)) {
                finish();
            }
            info.setRet_id(retId);
            getReturnGoodsOrderInfo(JsonUtil.toString(info));
        }
    }

    @Override
    public void dispose() {

    }
}
