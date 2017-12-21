package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/24.
 * 满减满赠活动
 */
public class FAVOURABLE {
    private int act_id;
    private String act_name;
    private String label_act_type;
    private String[] rank_nams;
    private String max_amount;
    private String formatted_start_time;
    private String formatted_end_time;
    private String seller_id;
    private String seller_name;

    public int getAct_id() {
        return act_id;
    }

    public void setAct_id(int act_id) {
        this.act_id = act_id;
    }

    public String getAct_name() {
        return act_name;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public String getLabel_act_type() {
        return label_act_type;
    }

    public void setLabel_act_type(String label_act_type) {
        this.label_act_type = label_act_type;
    }

    public String[] getRank_nams() {
        return rank_nams;
    }

    public void setRank_nams(String[] rank_nams) {
        this.rank_nams = rank_nams;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(String max_amount) {
        this.max_amount = max_amount;
    }

    public String getFormatted_start_time() {
        return formatted_start_time;
    }

    public void setFormatted_start_time(String formatted_start_time) {
        this.formatted_start_time = formatted_start_time;
    }

    public String getFormatted_end_time() {
        return formatted_end_time;
    }

    public void setFormatted_end_time(String formatted_end_time) {
        this.formatted_end_time = formatted_end_time;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public static FAVOURABLE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }
        FAVOURABLE favourable = new FAVOURABLE();
        favourable.act_id = jsonObject.optInt("act_id");
        favourable.act_name = jsonObject.optString("act_name");
        favourable.label_act_type = jsonObject.optString("label_act_type");
        JSONArray array = jsonObject.optJSONArray("rank_name");
        if (array.length()>0) {
            favourable.rank_nams = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                favourable.rank_nams[i]=array.getString(i);
            }
        }
        favourable.max_amount=jsonObject.optString("max_amount");
        favourable.formatted_start_time=jsonObject.optString("formatted_start_time");
        favourable.formatted_end_time=jsonObject.optString("formatted_end_time");
        favourable.seller_id=jsonObject.optString("seller_id");
        favourable.seller_name=jsonObject.optString("seller_name");

        return favourable;
    }
}
