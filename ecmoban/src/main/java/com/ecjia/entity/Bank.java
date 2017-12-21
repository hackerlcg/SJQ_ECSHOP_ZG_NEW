package com.ecjia.entity;

import com.google.gson.annotations.SerializedName;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/15 16:32.
 */

public class Bank {
    @SerializedName("id")
    protected String id;
    @SerializedName("bank_image")
    protected String icon;
    @SerializedName("bank_name")
    protected String name;
    @SerializedName("bank_code")
    protected String code;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
