
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FAVOUR_LIST
{

    private int act_id;
    private String act_name;
    private String formatted_start_time;
    private String formatted_end_time;
    private int seller_id; //店家id，0表示自营
    private String seller_name;  //	 店家名称，如果自营则返回空值
    private String max_amount; //可参与活动的最高额度
    private String label_act_type;  //活动类型中文说明（特惠品、现金减免、价格折扣）

    private ArrayList<String> rank_name=new ArrayList<String>();


    public static FAVOUR_LIST fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        FAVOUR_LIST localItem = new FAVOUR_LIST();

        JSONArray subItemArray;

        localItem.act_id = jsonObject.optInt("act_id");
        localItem.act_name = jsonObject.optString("act_name");
        localItem.formatted_start_time = jsonObject.optString("formatted_start_time");
        localItem.formatted_end_time = jsonObject.optString("formatted_end_time");
        localItem.max_amount = jsonObject.optString("max_amount");
        localItem.label_act_type = jsonObject.optString("label_act_type");
        localItem.seller_name = jsonObject.optString("seller_name");
        localItem.seller_id = jsonObject.optInt("seller_id");

        subItemArray = jsonObject.optJSONArray("rank_name");
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                String subItem = subItemObject.toString();
                localItem.rank_name.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray1 = new JSONArray();
        localItemObject.put("act_id", act_id);
        localItemObject.put("act_name", act_name);
        localItemObject.put("formatted_start_time", formatted_start_time);
        localItemObject.put("formatted_end_time", formatted_end_time);
        localItemObject.put("max_amount", max_amount);
        localItemObject.put("label_act_type", label_act_type);
        localItemObject.put("seller_name", seller_name);
        localItemObject.put("seller_id", seller_id);


        for(int i =0; i< rank_name.size(); i++)
        {
            String itemData =rank_name.get(i);
            JSONObject itemJSONObject = new JSONObject(itemData);
            itemJSONArray1.put(itemJSONObject);
        }
        localItemObject.put("act_range_ext", itemJSONArray1);


        return localItemObject;
    }

}
