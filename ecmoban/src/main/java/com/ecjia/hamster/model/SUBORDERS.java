
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SUBORDERS
{



    private String id;
    private String sn;
    private String total;
    private String status;
    private String consignee;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private String postscript;
    private String amount;
    private String discount;
    private String shipping;
    private String mobile;
    private String number;
    private String time;
    private ArrayList<GOODS> goodslist=new ArrayList<GOODS>();

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

    public static SUBORDERS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SUBORDERS localItem = new SUBORDERS();

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
        localItem.discount = jsonObject.optString("formated_discount");
        localItem.shipping = jsonObject.optString("formated_shipping_fee");
        localItem.total = jsonObject.optString("formated_total_fee");
        localItem.status = jsonObject.optString("status");
        localItem.number = jsonObject.optString("goods_number");
        localItem.time = jsonObject.optString("create_time");

        subItemArray = jsonObject.optJSONArray("goods_items");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GOODS subItem = GOODS.fromJson(subItemObject);
                localItem.goodslist.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
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
        localItemObject.put("formated_discount", discount);
        localItemObject.put("formated_shipping_fee", shipping);
        localItemObject.put("formated_total_fee", total);
        localItemObject.put("status", status);
        localItemObject.put("goods_number", number);
        localItemObject.put("create_time", time);

        for(int i =0; i< goodslist.size(); i++)
        {
            GOODS itemData =goodslist.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("goods_items", itemJSONArray);

        return localItemObject;
    }

}
