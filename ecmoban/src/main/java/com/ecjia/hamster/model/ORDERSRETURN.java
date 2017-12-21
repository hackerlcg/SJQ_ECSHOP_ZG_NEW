package com.ecjia.hamster.model;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-17.
 */

public class ORDERSRETURN implements Serializable {

    /*"ret_id": "53",//退换货ID
            "return_sn": "2017030314125223253",//退换货流水号
            "apply_time": "2017-03-03 14:05:09",//申请时间
            "should_return": "0.01",//退款金额
            "return_shipping_fee": "0.00",//退邮费金额
            "return_status": "0",//订单状态
            "refound_status": "0",//退款状态
            "return_type": "0",//退换货方式           0仅退款 1退货 2换货
            "goods_name": "测试0.01商品 包邮",//退换货商品名称
            "goods_thumb": "http://test.sjq.cn/images/201702/thumb_img/577_thumb_G_1487899198782.jpg",//退换货商品图片
            "goods_id": "585",//商品ID
            "return_number": "1", //退货数量
            "ff_return_status": "已申请",//订单状态-文本
            "ff_refound_status": "未退款",//退款状态-文本
            "ff_return_type": "仅退款"//退换货方式-文本*/

    private String ret_id;
    private String return_sn;
    private String apply_time;
    private String should_return;
    private String return_shipping_fee;
    private String return_status;
    private String refound_status;
    private String return_type;
    private String goods_name;
    private String goods_thumb;
    private String goods_id;
    private String return_number;
    private String ff_return_status;
    private String ff_refound_status;
    private String ff_return_type;

    public String getRet_id() {
        return ret_id;
    }

    public void setRet_id(String ret_id) {
        this.ret_id = ret_id;
    }

    public String getReturn_sn() {
        return return_sn;
    }

    public void setReturn_sn(String return_sn) {
        this.return_sn = return_sn;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getShould_return() {
        return should_return;
    }

    public void setShould_return(String should_return) {
        this.should_return = should_return;
    }

    public String getReturn_shipping_fee() {
        return return_shipping_fee;
    }

    public void setReturn_shipping_fee(String return_shipping_fee) {
        this.return_shipping_fee = return_shipping_fee;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getRefound_status() {
        return refound_status;
    }

    public void setRefound_status(String refound_status) {
        this.refound_status = refound_status;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getReturn_number() {
        return return_number;
    }

    public void setReturn_number(String return_number) {
        this.return_number = return_number;
    }

    public String getFf_return_status() {
        return ff_return_status;
    }

    public void setFf_return_status(String ff_return_status) {
        this.ff_return_status = ff_return_status;
    }

    public String getFf_refound_status() {
        return ff_refound_status;
    }

    public void setFf_refound_status(String ff_refound_status) {
        this.ff_refound_status = ff_refound_status;
    }

    public String getFf_return_type() {
        return ff_return_type;
    }

    public void setFf_return_type(String ff_return_type) {
        this.ff_return_type = ff_return_type;
    }

    public String getAllReturn() {
        return  (Float.parseFloat(should_return)+Float.parseFloat(return_shipping_fee))+"";
    }
}
