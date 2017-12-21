package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FEEDBACK_ORDER_ITEM {
    private String order_id;
    private String order_sn;
    private String formatted_create_time;
    private String formatted_order_status;
    private ArrayList<GOODS> goodsArrayList = new ArrayList<GOODS>();

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getFormatted_create_time() {
        return formatted_create_time;
    }

    public void setFormatted_create_time(String formatted_create_time) {
        this.formatted_create_time = formatted_create_time;
    }

    public String getFormatted_order_status() {
        return formatted_order_status;
    }

    public void setFormatted_order_status(String formatted_order_status) {
        this.formatted_order_status = formatted_order_status;
    }

    public ArrayList<GOODS> getGoodsArrayList() {
        return goodsArrayList;
    }

    public void setGoodsArrayList(ArrayList<GOODS> goodsArrayList) {
        this.goodsArrayList = goodsArrayList;
    }

    public static FEEDBACK_ORDER_ITEM fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        FEEDBACK_ORDER_ITEM feedback_order_item = new FEEDBACK_ORDER_ITEM();
        feedback_order_item.order_id = jsonObject.optString("order_id");
        feedback_order_item.order_sn = jsonObject.optString("order_sn");
        feedback_order_item.formatted_create_time = jsonObject.optString("formatted_create_time");
        feedback_order_item.formatted_order_status = jsonObject.optString("formatted_order_status");
        JSONArray jsonArray = jsonObject.optJSONArray("goods_items");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++)
                feedback_order_item.goodsArrayList.add(GOODS.fromJson(jsonArray.optJSONObject(i)));
        }

        return feedback_order_item;
    }
}
