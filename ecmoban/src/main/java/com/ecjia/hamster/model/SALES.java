
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class SALES
{


    private String maximum_sales_volume;   //'30000.00',  单日最高
    private String total_sales_volume;    //'200000.00',	总收入
    private String average_sales_volume;  //'20000.00',  日均销量
    private int member_orders;
    private int anonymity_orders;
    private ArrayList<STATS> list=new ArrayList<STATS>();
    private ArrayList<GROUP> grouplist=new ArrayList<GROUP>();

    public String getMaximum_sales_volume() {
        return maximum_sales_volume;
    }

    public void setMaximum_sales_volume(String maximum_sales_volume) {
        this.maximum_sales_volume = maximum_sales_volume;
    }

    public String getTotal_sales_volume() {
        return total_sales_volume;
    }

    public void setTotal_sales_volume(String total_sales_volume) {
        this.total_sales_volume = total_sales_volume;
    }

    public String getAverage_sales_volume() {
        return average_sales_volume;
    }

    public void setAverage_sales_volume(String average_sales_volume) {
        this.average_sales_volume = average_sales_volume;
    }

    public ArrayList<STATS> getList() {
        return list;
    }

    public void setList(ArrayList<STATS> list) {
        this.list = list;
    }

    public int getMember_orders() {
        return member_orders;
    }

    public void setMember_orders(int member_orders) {
        this.member_orders = member_orders;
    }

    public int getAnonymity_orders() {
        return anonymity_orders;
    }

    public void setAnonymity_orders(int anonymity_orders) {
        this.anonymity_orders = anonymity_orders;
    }

    public ArrayList<GROUP> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(ArrayList<GROUP> grouplist) {
        this.grouplist = grouplist;
    }

    public static SALES fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SALES localItem = new SALES();

        JSONArray subItemArray,groupArray;
        DecimalFormat decimalFormat = new DecimalFormat("#######0.00");
        localItem.maximum_sales_volume = jsonObject.optString("maximum_sales_volume");
        localItem.total_sales_volume = decimalFormat.format(jsonObject.optDouble("total_sales_volume"))+"";
        localItem.average_sales_volume = decimalFormat.format(jsonObject.optDouble("average_sales_volume"))+"";
        localItem.anonymity_orders=jsonObject.optInt("anonymity_orders");
        localItem.member_orders=jsonObject.optInt("member_orders");
        subItemArray = jsonObject.optJSONArray("stats");
        groupArray=jsonObject.optJSONArray("group");
        localItem.list.clear();
        if(null != subItemArray) {
            for (int i = 0; i < subItemArray.length(); i++) {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                STATS subItem = STATS.fromJson(subItemObject);
                localItem.list.add(subItem);
            }
        }
        localItem.grouplist.clear();
        if(null != groupArray) {
            for (int i = 0; i < groupArray.length(); i++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                GROUP subItem = GROUP.fromJson(groupObject);
                localItem.grouplist.add(subItem);
            }
        }

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("maximum_sales_volume", maximum_sales_volume);
        localItemObject.put("total_sales_volume", total_sales_volume);
        localItemObject.put("average_sales_volume", average_sales_volume);

        for(int i =0; i< list.size(); i++)
        {
            STATS itemData =list.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("stats", itemJSONArray);

        return localItemObject;
    }

}
