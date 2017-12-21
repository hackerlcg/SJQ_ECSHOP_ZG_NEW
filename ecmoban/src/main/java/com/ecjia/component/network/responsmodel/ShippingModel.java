package com.ecjia.component.network.responsmodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-16.
 */

public class ShippingModel implements Serializable{

    private String support_cod;


    private String shipping_desc;


    private String shipping_id;


    private String format_shipping_fee;


    private String insure;


    private String insure_formated;


    private String shipping_code;


    private String shipping_name;


    private String free_money;


    private String shipping_fee;

    public String getSupport_cod() {
        return support_cod;
    }

    public void setSupport_cod(String support_cod) {
        this.support_cod = support_cod;
    }

    public String getShipping_desc() {
        return shipping_desc;
    }

    public void setShipping_desc(String shipping_desc) {
        this.shipping_desc = shipping_desc;
    }

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getFormat_shipping_fee() {
        return format_shipping_fee;
    }

    public void setFormat_shipping_fee(String format_shipping_fee) {
        this.format_shipping_fee = format_shipping_fee;
    }

    public String getInsure() {
        return insure;
    }

    public void setInsure(String insure) {
        this.insure = insure;
    }

    public String getInsure_formated() {
        return insure_formated;
    }

    public void setInsure_formated(String insure_formated) {
        this.insure_formated = insure_formated;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getFree_money() {
        return free_money;
    }

    public void setFree_money(String free_money) {
        this.free_money = free_money;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }
}
