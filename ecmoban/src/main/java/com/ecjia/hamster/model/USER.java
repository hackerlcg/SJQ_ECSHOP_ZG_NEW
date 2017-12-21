
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class USER
{


    private String id;
    private String username;
    private String api;
    private String email;
    private String last_login;
    private String last_ip;
    private String shop_logo;
    private String shop_name;

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public static USER fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        USER   localItem = new USER();

        JSONArray subItemArray;

        localItem.id = jsonObject.optString("id");
        localItem.username = jsonObject.optString("username");
        localItem.shop_name = jsonObject.optString("shop_name");
        localItem.email = jsonObject.optString("email");
        localItem.last_login = jsonObject.optString("last_login");
        localItem.last_ip=jsonObject.optString("last_ip");
        localItem.shop_logo=jsonObject.optString("shop_logo");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("username", username);
        localItemObject.put("shop_name", shop_name);
        localItemObject.put("email",email);
        localItemObject.put("last_login",last_login);
        localItemObject.put("last_ip", last_ip);
        localItemObject.put("shop_logo", shop_logo);
        return localItemObject;
    }

}
