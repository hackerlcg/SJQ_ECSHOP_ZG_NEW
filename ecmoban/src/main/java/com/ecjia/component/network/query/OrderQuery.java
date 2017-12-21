package com.ecjia.component.network.query;

import org.json.JSONException;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-16.
 */

public class OrderQuery extends PageQuery {

    protected static OrderQuery instance;

    public static OrderQuery getInstance() {
        if (instance == null) {
            synchronized (OrderQuery.class) {
                if (instance == null) {
                    instance = new OrderQuery();
                }
            }
        }
        return instance;
    }

    public OrderQuery() {
    }

    //获取退换货原因
    public String getShipping(){
        return super.get();
    }

    //订单退款、退货退款，提交退货物流信息
    public String getSendCourier(String ret_id,String shipping_id,String invoice_no,String images){
        try {
            requestJsonObject.put("ret_id", ret_id);
            requestJsonObject.put("shipping_id", shipping_id);
            requestJsonObject.put("invoice_no", invoice_no);
            requestJsonObject.put("images", images);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

}
