package com.ecjia.hamster.model;

import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/12.
 */
public class GROUP {
    private String time;
    private String formatted_time;
    private String day_formatted_time;
    private String hour_formatted_time;

    public String getHour_formatted_time() {
        return hour_formatted_time;
    }

    public void setHour_formatted_time(String hour_formatted_time) {
        this.hour_formatted_time = hour_formatted_time;
    }

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

    public String getDay_formatted_time() {
        return day_formatted_time;
    }

    public void setDay_formatted_time(String day_formatted_time) {
        this.day_formatted_time = day_formatted_time;
    }
    public static GROUP fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        GROUP localItem = new GROUP();
        localItem.formatted_time = jsonObject.optString("formatted_time");
        localItem.hour_formatted_time = TimeUtil.getDayFormatDay(jsonObject.optString("formatted_time"));
        localItem.day_formatted_time = TimeUtil.getFormatDay(jsonObject.optString("formatted_time"));

        localItem.time = jsonObject.optString("time");

        return localItem;
    }
}
