
package com.ecjia.hamster.model;

import com.ecjia.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class STATS
{
    private String time;
    private String formatted_time;
    private String value;
    private String orders;
    private String amount;
    private String visitors;
    private String day_formatted_time;
    private String formatted_week_time;
    private String formatted_hour_time;
    private String default_time;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormatted_time() {
        return formatted_time;
    }

    public void setFormatted_time(String formatted_time) {
        this.formatted_time = formatted_time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVisitors() {
        return visitors;
    }

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public String getDay_formatted_time() {
        return day_formatted_time;
    }

    public void setDay_formatted_time(String day_formatted_time) {
        this.day_formatted_time = day_formatted_time;
    }

    public String getFormatted_week_time() {
        return formatted_week_time;
    }

    public void setFormatted_week_time(String formatted_week_time) {
        this.formatted_week_time = formatted_week_time;
    }

    public String getFormatted_hour_time() {
        return formatted_hour_time;
    }

    public void setFormatted_hour_time(String formatted_hour_time) {
        this.formatted_hour_time = formatted_hour_time;
    }

    public String getDefault_time() {
        return default_time;
    }

    public void setDefault_time(String default_time) {
        this.default_time = default_time;
    }

    public static STATS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        STATS localItem = new STATS();
        localItem.time = jsonObject.optString("time");
        String[] date=TimeUtil.getFomartTwoTime(jsonObject.optString("formatted_time"));
        localItem.formatted_week_time=date[0];
        localItem.formatted_hour_time=date[1];
        localItem.default_time = jsonObject.optString("formatted_time");
        localItem.formatted_time = TimeUtil.getFormatDay(jsonObject.optString("formatted_time"));
        localItem.day_formatted_time=TimeUtil.getDayFormatDay(jsonObject.optString("formatted_time"));
        localItem.value = jsonObject.optString("value");
        localItem.amount = jsonObject.optString("amount");
        localItem.orders = jsonObject.optString("orders");
        localItem.visitors = jsonObject.optString("visitors");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("time", time);
        localItemObject.put("formatted_time", formatted_time);
        localItemObject.put("value", value);
        localItemObject.put("amount", amount);
        localItemObject.put("orders", orders);
        localItemObject.put("visitors", visitors);
        return localItemObject;
    }

}
