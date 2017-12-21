package com.ecjia.hamster.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BRAND {

    private int brand_id;

    private String brand_name;

    private String brand_logo;

    private int is_show;

    private boolean isClicked;

    private String pinyin;

    public BRAND() {
    }

    public BRAND(int brand_id, String brand_name, String brand_logo, int is_show, boolean isClicked, String pinyin) {
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_logo = brand_logo;
        this.is_show = is_show;
        this.isClicked = isClicked;
        this.pinyin = pinyin;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public static BRAND fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        BRAND localItem = new BRAND();
        JSONArray subItemArray;

        localItem.brand_id = jsonObject.optInt("brand_id");
        localItem.is_show = jsonObject.optInt("is_show");
        localItem.brand_name = jsonObject.optString("brand_name");
        localItem.brand_logo = jsonObject.optString("brand_logo");
        localItem.pinyin = jsonObject.optString("pinyin");
        localItem.isClicked = jsonObject.optBoolean("isClicked");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("brand_id", brand_id);
        localItemObject.put("is_show", is_show);
        localItemObject.put("brand_name", brand_name);
        localItemObject.put("brand_logo", brand_logo);
        localItemObject.put("pinyin", pinyin);
        localItemObject.put("isClicked", isClicked);
        return localItemObject;
    }


}
