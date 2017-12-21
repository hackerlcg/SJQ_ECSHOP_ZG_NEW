
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ORDERS {


    private String id;
    private String sn;
    private String total;
    private String status;//状态

    private String order_status;//订单状态状态
    private String shipping_status;//物流配送状态
    private String pay_status;//支付状态


    private String consignee;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private String postscript;
    private String amount;//商品金额总计。带符号
    private String discount;//优惠金额。带符号
    private String shipping;//运费。带符号
    //    private String shipping_fee;//运费。带符号
    private String goods_amount;//商品金额总计。不带符号
    private String formated_goods_amount;//商品金额总计。带符号
    private String mobile;
    private String number;
    private String time;
    private String tax;
    private String payment;  //支付方式
    private String shipping_way;  //配送方式
    private String order_amount;  //订单应付价格
    private String formated_coupons;//优惠券减免
    private ArrayList<GOODS> goodslist = new ArrayList<GOODS>();
    private ArrayList<SUBORDERS> suborderses = new ArrayList<SUBORDERS>();
    private ArrayList<ACTION> actions = new ArrayList<ACTION>();

    private String extension_code;//订单类型
    private String extension_id;//订单类型ID

    public String getFormated_coupons() {
        return formated_coupons;
    }

    public void setFormated_coupons(String formated_coupons) {
        this.formated_coupons = formated_coupons;
    }

    public String getExtension_code() {
        return extension_code;
    }

    public void setExtension_code(String extension_code) {
        this.extension_code = extension_code;
    }

    public String getExtension_id() {
        return extension_id;
    }

    public void setExtension_id(String extension_id) {
        this.extension_id = extension_id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getShipping_way() {
        return shipping_way;
    }

    public void setShipping_way(String shipping_way) {
        this.shipping_way = shipping_way;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public ArrayList<SUBORDERS> getSuborderses() {
        return suborderses;
    }

    public void setSuborderses(ArrayList<SUBORDERS> suborderses) {
        this.suborderses = suborderses;
    }

    public ArrayList<ACTION> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ACTION> actions) {
        this.actions = actions;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getFormated_goods_amount() {
        return formated_goods_amount;
    }

    public void setFormated_goods_amount(String formated_goods_amount) {
        this.formated_goods_amount = formated_goods_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<GOODS> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(ArrayList<GOODS> goodslist) {
        this.goodslist = goodslist;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public static ORDERS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        ORDERS localItem = new ORDERS();

        JSONArray subItemArray;

        localItem.id = jsonObject.optString("order_id");
        localItem.sn = jsonObject.optString("order_sn");
        localItem.consignee = jsonObject.optString("consignee");
        localItem.country = jsonObject.optString("country");
        localItem.province = jsonObject.optString("province");
        localItem.city = jsonObject.optString("city");
        localItem.district = jsonObject.optString("district");
        localItem.address = jsonObject.optString("address");
        localItem.mobile = jsonObject.optString("mobile");
        localItem.postscript = jsonObject.optString("postscript");
        localItem.amount = jsonObject.optString("formated_goods_amount");
        localItem.goods_amount = jsonObject.optString("goods_amount");
        localItem.formated_goods_amount = jsonObject.optString("formated_goods_amount");
        localItem.discount = jsonObject.optString("formated_discount");
        localItem.shipping = jsonObject.optString("formated_shipping_fee");
        localItem.total = jsonObject.optString("formated_total_fee");
        localItem.status = jsonObject.optString("status");
        localItem.order_status = jsonObject.optString("order_status");
        localItem.shipping_status = jsonObject.optString("shipping_status");
        localItem.pay_status = jsonObject.optString("pay_status");
        localItem.number = jsonObject.optString("goods_number");
        localItem.time = jsonObject.optString("create_time");
        localItem.shipping_way = jsonObject.optString("shipping_way");
        localItem.order_amount = jsonObject.optString("formated_order_amount");
        localItem.payment = jsonObject.optString("pay_name");
        localItem.tax = jsonObject.optString("formated_tax");
        localItem.formated_coupons = jsonObject.optString("formated_coupons");


        subItemArray = jsonObject.optJSONArray("goods_items");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODS subItem = GOODS.fromJson(subItemObject);
                localItem.goodslist.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("sub_orders");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                SUBORDERS subItem = SUBORDERS.fromJson(subItemObject);
                localItem.suborderses.add(subItem);
            }
        }
        subItemArray = jsonObject.optJSONArray("action_logs");
        if (null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ACTION subItem = ACTION.fromJson(subItemObject);
                localItem.actions.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray1 = new JSONArray();
        JSONArray itemJSONArray2 = new JSONArray();
        JSONArray itemJSONArray3 = new JSONArray();
        localItemObject.put("order_id", id);
        localItemObject.put("order_sn", sn);
        localItemObject.put("consignee", consignee);
        localItemObject.put("country", country);
        localItemObject.put("province", province);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("address", address);
        localItemObject.put("mobile", mobile);
        localItemObject.put("postscript", postscript);
        localItemObject.put("formated_goods_amount", amount);
        localItemObject.put("goods_amount", goods_amount);
        localItemObject.put("formated_goods_amount", formated_goods_amount);
        localItemObject.put("formated_discount", discount);
        localItemObject.put("formated_shipping_fee", shipping);
        localItemObject.put("formated_total_fee", total);
        localItemObject.put("status", status);
        localItemObject.put("order_status", order_status);
        localItemObject.put("shipping_status", shipping_status);
        localItemObject.put("pay_status", pay_status);
        localItemObject.put("goods_number", number);
        localItemObject.put("create_time", time);
        localItemObject.put("pay_name", payment);
        localItemObject.put("order_amount", order_amount);
        localItemObject.put("shipping_way", shipping_way);
        localItemObject.put("formated_tax", tax);
        localItemObject.put("formated_coupons", formated_coupons);

        for (int i = 0; i < goodslist.size(); i++) {
            GOODS itemData = goodslist.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray1.put(itemJSONObject);
        }
        localItemObject.put("goods_list", itemJSONArray1);

        for (int i = 0; i < actions.size(); i++) {
            ACTION itemData = actions.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray2.put(itemJSONObject);
        }
        localItemObject.put("action_logs", itemJSONArray2);

        for (int i = 0; i < suborderses.size(); i++) {
            SUBORDERS itemData = suborderses.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray3.put(itemJSONObject);
        }
        localItemObject.put("sub_orders", itemJSONArray3);

        return localItemObject;
    }

}
