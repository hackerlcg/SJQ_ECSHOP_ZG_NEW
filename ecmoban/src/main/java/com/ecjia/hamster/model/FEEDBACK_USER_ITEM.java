package com.ecjia.hamster.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FEEDBACK_USER_ITEM {
    private String user_id;
    private String user_name;
    private String rank_name;
    private String email;
    private String mobile_phone;
    private String avatar_img;
    private int orders;
    private String formatted_user_money;
    private String user_points;
    private int bonus_count;
    private String sex;

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

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getFormatted_user_money() {
        return formatted_user_money;
    }

    public void setFormatted_user_money(String formatted_user_money) {
        this.formatted_user_money = formatted_user_money;
    }

    public String getUser_points() {
        return user_points;
    }

    public void setUser_points(String user_points) {
        this.user_points = user_points;
    }

    public int getBonus_count() {
        return bonus_count;
    }

    public void setBonus_count(int bonus_count) {
        this.bonus_count = bonus_count;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public static FEEDBACK_USER_ITEM fromJson(JSONObject jsonObject)  throws JSONException {
        if(null == jsonObject){
            return null;
        }
        FEEDBACK_USER_ITEM feedback_user_item=new FEEDBACK_USER_ITEM();

        feedback_user_item.user_id=jsonObject.optString("user_id");
        feedback_user_item.user_name=jsonObject.optString("user_name");
        feedback_user_item.rank_name=jsonObject.optString("rank_name");
        feedback_user_item.email=jsonObject.optString("email");
        feedback_user_item.mobile_phone=jsonObject.optString("mobile_phone");
        feedback_user_item.avatar_img=jsonObject.optString("avatar_img");
        feedback_user_item.orders=jsonObject.optInt("orders");
        feedback_user_item.formatted_user_money=jsonObject.optString("formatted_user_money");
        feedback_user_item.user_points=jsonObject.optString("user_points");
        feedback_user_item.bonus_count=jsonObject.optInt("bonus_count");
        feedback_user_item.sex=jsonObject.optString("sex");
        return  feedback_user_item;
    }
}
