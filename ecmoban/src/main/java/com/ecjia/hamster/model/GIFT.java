package com.ecjia.hamster.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;


public class GIFT {

    private String id;

    private String name;

    private String price;

    private String origin_price;

    private String  image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public GIFT() {
    }

    public GIFT(String id, String name, String price,String origin_price,String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.origin_price = origin_price;
        this.image = image;
    }

    public static GIFT fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        GIFT localItem = new GIFT();
        JSONArray subItemArray;

        localItem.id = jsonObject.optString("id");
        localItem.name = jsonObject.optString("name");
        localItem.price = jsonObject.optString("price");
        localItem.origin_price = jsonObject.optString("formatted_shop_price");
        localItem.image = jsonObject.optString("image");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("price", price);
        localItemObject.put("formatted_shop_price", origin_price);
        localItemObject.put("image", image);

        return localItemObject;
    }


}
