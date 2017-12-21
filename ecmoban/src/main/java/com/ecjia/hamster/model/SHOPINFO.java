
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SHOPINFO
{
    private int id;
    private String seller_name;
    private String seller_logo;
    private String seller_category;
    private String seller_money;
    private String seller_un_money;
    private String seller_telephone;
    private String seller_province;
    private String seller_city;
    private String seller_address;
    private String seller_description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_logo() {
        return seller_logo;
    }

    public void setSeller_logo(String seller_logo) {
        this.seller_logo = seller_logo;
    }

    public String getSeller_category() {
        return seller_category;
    }

    public void setSeller_category(String seller_category) {
        this.seller_category = seller_category;
    }

    public String getSeller_telephone() {
        return seller_telephone;
    }

    public void setSeller_telephone(String seller_telephone) {
        this.seller_telephone = seller_telephone;
    }

    public String getSeller_province() {
        return seller_province;
    }

    public void setSeller_province(String seller_province) {
        this.seller_province = seller_province;
    }

    public String getSeller_city() {
        return seller_city;
    }

    public void setSeller_city(String seller_city) {
        this.seller_city = seller_city;
    }

    public String getSeller_address() {
        return seller_address;
    }

    public void setSeller_address(String seller_address) {
        this.seller_address = seller_address;
    }

    public String getSeller_description() {
        return seller_description;
    }

    public void setSeller_description(String seller_description) {
        this.seller_description = seller_description;
    }

    public String getSeller_money() {
        return seller_money;
    }

    public String getSeller_un_money() {
        return seller_un_money;
    }

    public void setSeller_un_money(String seller_un_money) {
        this.seller_un_money = seller_un_money;
    }

    public void setSeller_money(String seller_money) {
        this.seller_money = seller_money;
    }

    public static SHOPINFO fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SHOPINFO localItem = new SHOPINFO();

        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");
        localItem.seller_name = jsonObject.optString("seller_name");
        localItem.seller_address = jsonObject.optString("seller_address");
        localItem.seller_category = jsonObject.optString("seller_category");
        localItem.seller_money = jsonObject.optString("seller_money");
        localItem.seller_un_money = jsonObject.optString("waiting_account");
        localItem.seller_city = jsonObject.optString("seller_city");
        localItem.seller_logo = jsonObject.optString("seller_logo");
        localItem.seller_province =  jsonObject.optString("seller_province");
        localItem.seller_telephone =  jsonObject.optString("seller_telephone");
        localItem.seller_description  =  jsonObject.optString("seller_description");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("seller_name", seller_name);
        localItemObject.put("seller_address", seller_address);
        localItemObject.put("seller_category", seller_category);
        localItemObject.put("seller_money", seller_money);
        localItemObject.put("waiting_account", seller_un_money);
        localItemObject.put("seller_city", seller_city);
        localItemObject.put("seller_logo", seller_logo);
        localItemObject.put("seller_province", seller_province);
        localItemObject.put("seller_telephone", seller_telephone);
        localItemObject.put("seller_description", seller_description);
        return localItemObject;
    }

}
