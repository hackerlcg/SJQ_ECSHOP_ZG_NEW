package com.ecjia.hamster.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/6.
 */
public class CUSTOMER_SERVICE_ITEM {
    private String user_id;
    private String user_name;
    private String email;
    private String last_login;
    private String last_ip;
    private String role_name;
    private String avator_img;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getAvator_img() {
        return avator_img;
    }

    public void setAvator_img(String avator_img) {
        this.avator_img = avator_img;
    }

    public static CUSTOMER_SERVICE_ITEM fromJson(JSONObject jsonObject)  throws JSONException {
        if(null == jsonObject){
            return null;
        }

        CUSTOMER_SERVICE_ITEM customer_service_item=new CUSTOMER_SERVICE_ITEM();
        customer_service_item.user_id=jsonObject.optString("user_id");
        customer_service_item.user_name=jsonObject.optString("user_name");
        customer_service_item.email=jsonObject.optString("email");
        customer_service_item.last_login=jsonObject.optString("last_login");
        customer_service_item.last_ip=jsonObject.optString("last_ip");
        customer_service_item.role_name=jsonObject.optString("role_name");
        customer_service_item.avator_img=jsonObject.optString("avator_img");

        return customer_service_item;
    }

}
