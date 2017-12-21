package com.ecjia.component.network.API;

import com.ecjia.component.network.base.BaseRes;
import com.ecjia.component.network.responsmodel.ReturnGoodsOrderInfoModel;
import com.ecjia.component.network.responsmodel.ShippingModel;
import com.ecjia.component.network.responsmodel.ShippingStatusModel;
import com.ecjia.component.network.requestmodel.ReturnStatus;
import com.gear.apifinder.annotation.APIService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ecjia.consts.AndroidManager.SERVICE_TAG;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-16.
 */
@APIService
public interface APIOrder {


    /**
     * 修改订单价格
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/change")
    Flowable<BaseRes<String>> getEditOderPrice(@Field("json") String json);

    /**
     * 商品发货
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/delivery")
    Flowable<BaseRes<ShippingStatusModel>> getDeliveryGoods(@Field("json") String json);

    /**
     * 获取物流公司列表
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"order/shipping")
    Flowable<BaseRes<ArrayList<ShippingModel>>> getShipping(@Field("json") String json);

    /**
     * 获取退换货订单详细信息 (卖家)
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/return_detail")
    Flowable<BaseRes<ReturnGoodsOrderInfoModel>> getReturnGoodsOrderInfo(@Field("json") String json);

    /**
     * 退换货 审核同意或拒绝
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/return_audit")
    Flowable<BaseRes<ReturnStatus>> getReturnGoodsOrderVerify(@Field("json") String json);

    /**
     * //订单退款、退货退款，提交退货物流信息
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/return_delivery")
    Flowable<BaseRes<ReturnStatus>> getSendCourier(@Field("json") String json);

    /**
     *  admin/orders/return_received 退换货 收到退回货品
     */
    @FormUrlEncoded
    @POST(SERVICE_TAG+"admin/orders/return_received")
    Flowable<BaseRes<ReturnStatus>> getReturnGoodsReceivedShipping(@Field("json") String json);

}
