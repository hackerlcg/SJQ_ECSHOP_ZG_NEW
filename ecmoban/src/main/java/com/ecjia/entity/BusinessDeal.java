package com.ecjia.entity;

import com.google.gson.annotations.SerializedName;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/15 09:19.
 */

public class BusinessDeal {
    @SerializedName("id")
    protected String id;
    @SerializedName("time_str")
    protected String year;
    @SerializedName("type")
    protected String tag;
    @SerializedName("note")
    protected String name;
    @SerializedName("add_time")
    protected String time;
    @SerializedName("money")
    protected String price;
    @SerializedName("cer_img")
    protected String cerImg;
    @SerializedName("is_paid")
    protected String paid;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCerImg() {
        return cerImg;
    }

    public void setCerImg(String cerImg) {
        this.cerImg = cerImg;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String showTag(){
        String str = "支";
        if(tag()){
            str = "收";
        }
        return str;
    }

    public String showPrice(){
        if(tag()){
            return "+"+price;
        }
        return "-"+price;
    }

    public boolean tag(){
        if("1".equals(tag)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BusinessDeal{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", cerImg='" + cerImg + '\'' +
                ", paid='" + paid + '\'' +
                '}';
    }
}
