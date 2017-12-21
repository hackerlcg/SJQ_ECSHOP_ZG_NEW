
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class USERRANK
{
    private int rank_id;	//会员等级id
    private String rank_name;	//会员等级
    private String discount;		//折扣
    private String price;		//100.00或-1 -1代表按会员折扣率计算
    private int min_points;		//该等级的最低积分
    private int max_points;		//该等级的最高积分
    private boolean checked;

    public USERRANK() {
    }

    public USERRANK(int rank_id, String rank_name, boolean checked) {
        this.rank_id = rank_id;
        this.rank_name = rank_name;
        this.checked = checked;
    }

    public int getRank_id() {
        return rank_id;
    }

    public void setRank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMin_points() {
        return min_points;
    }

    public void setMin_points(int min_points) {
        this.min_points = min_points;
    }

    public int getMax_points() {
        return max_points;
    }

    public void setMax_points(int max_points) {
        this.max_points = max_points;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static USERRANK fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        USERRANK localItem = new USERRANK();

        JSONArray subItemArray;

        localItem.rank_id = jsonObject.optInt("rank_id");
        localItem.rank_name = jsonObject.optString("rank_name");
        localItem.discount = jsonObject.optString("discount");
        localItem.price = jsonObject.optString("price");
        localItem.min_points = jsonObject.optInt("min_points");
        localItem.max_points = jsonObject.optInt("max_points");
        localItem.checked = jsonObject.optBoolean("checked");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("rank_id", rank_id);
        localItemObject.put("rank_name", rank_name);
        localItemObject.put("discount", discount);
        localItemObject.put("price", price);
        localItemObject.put("min_points", min_points);
        localItemObject.put("max_points", max_points);
        localItemObject.put("checked", checked);
        return localItemObject;
    }

}
