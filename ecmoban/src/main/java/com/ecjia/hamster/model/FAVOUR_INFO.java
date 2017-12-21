
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FAVOUR_INFO
{

    private int act_id;
    private String act_name;
    private String formatted_start_time;
    private String formatted_end_time;
    private String label_act_range;  //优惠活动范围类型对应中文说明（全部商品、指定分类、指定品牌、指定商品）
    private int act_range;
    private String min_amount;  //可参与活动的最小额度
    private String max_amount;
    private String formatted_min_amount;  //可参与活动的最小额度
    private String formatted_max_amount;
    private String label_act_type;  //活动类型中文说明（特惠品、现金减免、价格折扣）
    private String act_type;   //活动类型，（0：享受赠品、1：享受现金减免、2：享受价格折扣）
    private String act_type_ext;    //活动优惠力度,当优惠方式为“享受赠品（特惠品）”时，请输入允许买家选择赠品（特惠品）的最大数量，数量为0表示不限数量；当优惠方式为“享受现金减免”时，请输入现金减免的金额；当优惠方式为“享受价格折扣”时，请输入折扣（1－99），如：打9折，就输入90。

    private ArrayList<ACT> act_range_ext=new ArrayList<ACT>();
    private ArrayList<GIFT> gift_items=new ArrayList<GIFT>();
    private ArrayList<USERRANK> user_rank_list=new ArrayList<USERRANK>();

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

    public String getLabel_act_range() {
        return label_act_range;
    }

    public void setLabel_act_range(String label_act_range) {
        this.label_act_range = label_act_range;
    }

    public int getAct_range() {
        return act_range;
    }

    public void setAct_range(int act_range) {
        this.act_range = act_range;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(String max_amount) {
        this.max_amount = max_amount;
    }

    public String getFormatted_min_amount() {
        return formatted_min_amount;
    }

    public void setFormatted_min_amount(String formatted_min_amount) {
        this.formatted_min_amount = formatted_min_amount;
    }

    public String getFormatted_max_amount() {
        return formatted_max_amount;
    }

    public void setFormatted_max_amount(String formatted_max_amount) {
        this.formatted_max_amount = formatted_max_amount;
    }

    public String getLabel_act_type() {
        return label_act_type;
    }

    public void setLabel_act_type(String label_act_type) {
        this.label_act_type = label_act_type;
    }

    public String getAct_type() {
        return act_type;
    }

    public void setAct_type(String act_type) {
        this.act_type = act_type;
    }

    public String getAct_type_ext() {
        return act_type_ext;
    }

    public void setAct_type_ext(String act_type_ext) {
        this.act_type_ext = act_type_ext;
    }

    public ArrayList<ACT> getAct_range_ext() {
        return act_range_ext;
    }

    public void setAct_range_ext(ArrayList<ACT> act_range_ext) {
        this.act_range_ext = act_range_ext;
    }

    public ArrayList<GIFT> getGift_items() {
        return gift_items;
    }

    public void setGift_items(ArrayList<GIFT> gift_items) {
        this.gift_items = gift_items;
    }

    public ArrayList<USERRANK> getUser_rank_list() {
        return user_rank_list;
    }

    public void setUser_rank_list(ArrayList<USERRANK> user_rank_list) {
        this.user_rank_list = user_rank_list;
    }

    public static FAVOUR_INFO fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        FAVOUR_INFO localItem = new FAVOUR_INFO();

        JSONArray subItemArray;

        localItem.act_id = jsonObject.optInt("act_id");
        localItem.act_name = jsonObject.optString("act_name");
        localItem.formatted_start_time = jsonObject.optString("formatted_start_time");
        localItem.formatted_end_time = jsonObject.optString("formatted_end_time");
        localItem.label_act_range = jsonObject.optString("label_act_range");
        localItem.act_range = jsonObject.optInt("act_range");
        localItem.min_amount = jsonObject.optString("min_amount");
        localItem.max_amount = jsonObject.optString("max_amount");
        localItem.formatted_min_amount = jsonObject.optString("formatted_min_amount");
        localItem.formatted_max_amount = jsonObject.optString("formatted_max_amount");
        localItem.label_act_type = jsonObject.optString("label_act_type");
        localItem.act_type = jsonObject.optString("act_type");
        localItem.act_type_ext = jsonObject.optString("act_type_ext");

        subItemArray = jsonObject.optJSONArray("user_rank_list");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                USERRANK subItem = USERRANK.fromJson(subItemObject);
                localItem.user_rank_list.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("act_range_ext");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                ACT subItem = ACT.fromJson(subItemObject);
                localItem.act_range_ext.add(subItem);
            }
        }

        subItemArray = jsonObject.optJSONArray("gift_items");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                GIFT subItem = GIFT.fromJson(subItemObject);
                localItem.gift_items.add(subItem);
            }
        }


        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray1 = new JSONArray();
        JSONArray itemJSONArray2 = new JSONArray();
        JSONArray itemJSONArray3 = new JSONArray();
        localItemObject.put("act_id", act_id);
        localItemObject.put("act_name", act_name);
        localItemObject.put("formatted_start_time", formatted_start_time);
        localItemObject.put("formatted_end_time", formatted_end_time);
        localItemObject.put("label_act_range", label_act_range);
        localItemObject.put("act_range", act_range);
        localItemObject.put("min_amount", min_amount);
        localItemObject.put("max_amount", max_amount);
        localItemObject.put("formatted_min_amount", formatted_min_amount);
        localItemObject.put("formatted_max_amount", formatted_max_amount);
        localItemObject.put("label_act_type", label_act_type);
        localItemObject.put("act_type", act_type);
        localItemObject.put("act_type_ext", act_type_ext);

        for(int i =0; i< user_rank_list.size(); i++)
        {
            USERRANK itemData =user_rank_list.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray1.put(itemJSONObject);
        }
        localItemObject.put("user_rank_list", itemJSONArray1);

        for(int i =0; i< act_range_ext.size(); i++)
        {
            ACT itemData =act_range_ext.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray2.put(itemJSONObject);
        }
        localItemObject.put("act_range_ext", itemJSONArray2);

        for(int i =0; i< gift_items.size(); i++)
        {
            GIFT itemData =gift_items.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray3.put(itemJSONObject);
        }
        localItemObject.put("gift_items", itemJSONArray3);

        return localItemObject;
    }

}
