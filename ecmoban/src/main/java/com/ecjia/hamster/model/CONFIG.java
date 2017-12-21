
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CONFIG
{

    private String service_phone;


    private String site_url;


    private String shop_desc;


    private int shop_closed;


    private String close_comment;


    private String shop_reg_closed;


    private String goods_url;

    private String shop_address;

    private String shop_name;


    private String alipay_notify_url ;

    private String get_password_url;

    public String getGet_password_url() {
        return get_password_url;
    }

    public void setGet_password_url(String get_password_url) {
        this.get_password_url = get_password_url;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getAlipay_notify_url() {
        return alipay_notify_url;
    }

    public void setAlipay_notify_url(String alipay_notify_url) {
        this.alipay_notify_url = alipay_notify_url;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }

    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }

    public int getShop_closed() {
        return shop_closed;
    }

    public void setShop_closed(int shop_closed) {
        this.shop_closed = shop_closed;
    }

    public String getClose_comment() {
        return close_comment;
    }

    public void setClose_comment(String close_comment) {
        this.close_comment = close_comment;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }
    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_reg_closed() {
        return shop_reg_closed;
    }

    public void setShop_reg_closed(String shop_reg_closed) {
        this.shop_reg_closed = shop_reg_closed;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public static CONFIG fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        CONFIG localItem = new CONFIG();

        JSONArray subItemArray;

        localItem.service_phone   = jsonObject.optString("service_phone");

        localItem.site_url         = jsonObject.optString("site_url");

        localItem.shop_desc        = jsonObject.optString("shop_desc");

        localItem.shop_closed = jsonObject.optInt("shop_closed");

        localItem.close_comment = jsonObject.optString("close_comment");

        localItem.shop_address = jsonObject.optString("shop_address");

        localItem.shop_name = jsonObject.optString("shop_name");

        localItem.shop_reg_closed = jsonObject.optString("shop_reg_closed");

        localItem.goods_url = jsonObject.optString("goods_url");

        localItem.alipay_notify_url=jsonObject.optString("alipay_notify_url");

        localItem.get_password_url=jsonObject.optString("get_password_url");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();

        localItemObject.put("service_phone",service_phone);
        localItemObject.put("site_url",site_url);
        localItemObject.put("shop_desc",shop_desc);
        localItemObject.put("shop_closed", shop_closed);
        localItemObject.put("close_comment", close_comment);
        localItemObject.put("shop_address", shop_address);
        localItemObject.put("shop_name", shop_name);
        localItemObject.put("shop_reg_closed", shop_reg_closed);
        localItemObject.put("goods_url", goods_url);
        localItemObject.put("alipay_notify_url",alipay_notify_url );
        localItemObject.put("get_password_url",get_password_url );
        return localItemObject;
    }

}
