package com.ecjia.component.network.requestmodel;

import java.io.Serializable;

/**
 * 类名介绍：退换货订单提交参数
 * Created by sun
 * Created time 2017-03-10.
 */

public class EditOrderPrice extends BaseRequest implements Serializable {
    public EditOrderPrice() {
        super.setInfo();
    }

   /* order_id:12 //订单iD
    discount:20.00 //折扣金额
    shipping_fee : 5.00 //物流费用*/

    private String order_id;
    private String discount;
    private String shipping_fee;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }
}
