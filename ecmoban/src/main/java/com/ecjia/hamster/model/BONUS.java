package com.ecjia.hamster.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BONUS {

    private String bonus_id;


    private int type_id;


    private String type_name;


    private String type_money;


    private String bonus_money_formated;


    private String min_goods_amount;

    private String use_start_date;

    private String use_end_date;

    private String status;

    private String formated_use_start_date;

    private String formated_use_end_date;

    private String formated_min_goods_amount;

    public String getFormated_min_goods_amount() {
        return formated_min_goods_amount;
    }

    public void setFormated_min_goods_amount(String formated_min_goods_amount) {
        this.formated_min_goods_amount = formated_min_goods_amount;
    }

    public String getMin_goods_amount() {
        return min_goods_amount;
    }

    public void setMin_goods_amount(String min_goods_amount) {
        this.min_goods_amount = min_goods_amount;
    }

    public String getUse_start_date() {
        return use_start_date;
    }

    public void setUse_start_date(String use_start_date) {
        this.use_start_date = use_start_date;
    }

    public String getUse_end_date() {
        return use_end_date;
    }

    public void setUse_end_date(String use_end_date) {
        this.use_end_date = use_end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormated_use_start_date() {
        return formated_use_start_date;
    }

    public void setFormated_use_start_date(String formated_use_start_date) {
        this.formated_use_start_date = formated_use_start_date;
    }

    public String getFormated_use_end_date() {
        return formated_use_end_date;
    }

    public void setFormated_use_end_date(String formated_use_end_date) {
        this.formated_use_end_date = formated_use_end_date;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_money() {
        return type_money;
    }

    public void setType_money(String type_money) {
        this.type_money = type_money;
    }

    public String getBonus_id() {
        return bonus_id;
    }

    public void setBonus_id(String bonus_id) {
        this.bonus_id = bonus_id;
    }

    public String getBonus_money_formated() {
        return bonus_money_formated;
    }

    public void setBonus_money_formated(String bonus_money_formated) {
        this.bonus_money_formated = bonus_money_formated;
    }

    public static BONUS fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        BONUS localItem = new BONUS();
        JSONArray subItemArray;

        localItem.type_id = jsonObject.optInt("type_id");
        localItem.type_name = jsonObject.optString("type_name");
        localItem.type_money = jsonObject.optString("type_money");
        localItem.bonus_id = jsonObject.optString("bonus_id");
        localItem.bonus_money_formated = jsonObject.optString("bonus_money_formated");
        localItem.min_goods_amount = jsonObject.optString("min_goods_amount");
        localItem.use_start_date = jsonObject.optString("use_start_date");
        localItem.use_end_date = jsonObject.optString("use_end_date");
        localItem.status = jsonObject.optString("status");
        localItem.formated_use_start_date = jsonObject.optString("formated_use_start_date");
        localItem.formated_use_end_date = jsonObject.optString("formated_use_end_date");
        localItem.formated_min_goods_amount = jsonObject.optString("formated_min_goods_amount");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("type_id", type_id);
        localItemObject.put("type_name", type_name);
        localItemObject.put("type_money", type_money);
        localItemObject.put("bonus_id", bonus_id);
        localItemObject.put("bonus_money_formated", bonus_money_formated);

        localItemObject.put("min_goods_amount", min_goods_amount);
        localItemObject.put("use_start_date", use_start_date);
        localItemObject.put("use_end_date", use_end_date);
        localItemObject.put("status", status);
        localItemObject.put("formated_use_start_date", formated_use_start_date);
        localItemObject.put("formated_use_end_date", formated_use_end_date);
        localItemObject.put("formated_min_goods_amount", formated_min_goods_amount);
        return localItemObject;
    }


}
