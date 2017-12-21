package com.ecjia.hamster.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/7.
 */
public class FEED_GOODS_ITEM {
    private String id;
    private String name;
    private String goods_sn;
    private String shop_price;
    private String market_price;
    private PHOTO photo;

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

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public PHOTO getPhoto() {
        return photo;
    }

    public void setPhoto(PHOTO photo) {
        this.photo = photo;
    }

    public static FEED_GOODS_ITEM fromJson(JSONObject jsonObject)  throws JSONException {
        if(null == jsonObject){
            return null;
        }
        FEED_GOODS_ITEM feed_goods_item=new FEED_GOODS_ITEM();
        feed_goods_item.id=jsonObject.optString("id");
        feed_goods_item.name=jsonObject.optString("name");
        feed_goods_item.goods_sn=jsonObject.optString("goods_sn");
        feed_goods_item.shop_price=jsonObject.optString("shop_price");
        feed_goods_item.market_price=jsonObject.optString("market_price");
        feed_goods_item.photo=PHOTO.fromJson(jsonObject.optJSONObject("img"));
        return  feed_goods_item;
    }
}
