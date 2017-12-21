package com.ecjia.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/22 18:40.
 */

public class ValuePrice implements Parcelable{
    @SerializedName("price")
    private String price;
    @SerializedName("num")
    private String num;

    public ValuePrice() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ValuePrice{" +
                "price='" + price + '\'' +
                ", num='" + num + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeString(num);
    }

    public static final Parcelable.Creator<ValuePrice> CREATOR = new Creator<ValuePrice>()
    {
        @Override
        public ValuePrice[] newArray(int size)
        {
            return new ValuePrice[size];
        }

        @Override
        public ValuePrice createFromParcel(Parcel in)
        {
            return new ValuePrice(in);
        }
    };

    public ValuePrice(Parcel in)
    {
        price = in.readString();
        num = in.readString();
    }
}
