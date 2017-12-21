package com.ecjia.hamster.model;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class ADMIN {


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

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
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

    //    public String getStaff_id() {
    //        return staff_id;
    //    }

    //    public void setStaff_id(String staff_id) {
    //        this.staff_id = staff_id;
    //    }

    public String getRole_name() {
        if (TextUtils.isEmpty(role_name) || role_name.equals("null")) {
            return "收银员";
        }
        return role_name;
    }

    public void setRole_name(String user_level) {
        this.role_name = role_name;
    }

    public Drawable getUser_img() {
        return user_img;
    }

    public void setUser_img(Drawable user_img) {
        this.user_img = user_img;
    }

    private String id;

    private String username;

    private String last_ip;

    private String email;

    private String last_login;

    //    private String staff_id;//工号

    private String role_name;//职位

    public String getAvator_img() {
        return avator_img;
    }

    public void setAvator_img(String avator_img) {
        this.avator_img = avator_img;
    }

    private String avator_img;//头像链接

    private Drawable user_img;


    public static ADMIN fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        ADMIN localItem = new ADMIN();
        localItem.id = jsonObject.optString("id");
        localItem.username = jsonObject.optString("username");
        localItem.last_ip = jsonObject.optString("last_ip");
        localItem.email = jsonObject.optString("email");
        localItem.last_login = jsonObject.optString("last_login");
        //待确定
        //        localItem.staff_id = jsonObject.optString("staff_id");。
        localItem.role_name = jsonObject.optString("role_name");//职位
        localItem.avator_img = jsonObject.optString("avator_img");

        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("id", id);
        localItemObject.put("username", username);
        localItemObject.put("last_ip", last_ip);
        localItemObject.put("email", email);
        localItemObject.put("last_login", last_login);
        //        localItemObject.put("staff_id", staff_id);
        localItemObject.put("role_name", role_name);
        localItemObject.put("avator_img", avator_img);

        return localItemObject;
    }

}
