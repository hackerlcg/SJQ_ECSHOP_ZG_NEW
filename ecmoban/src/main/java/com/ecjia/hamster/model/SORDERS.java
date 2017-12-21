
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SORDERS
{

    private String payed_orders;   //'3000',  付款订单
    private String wait_ship_orders;    //'3000',	待发货订单
    private String shipped_orders;  //'200000',  已发货订单
    private int member_orders;
    private int anonymity_orders;
    private ArrayList<STATS> list=new ArrayList<STATS>();
    private ArrayList<GROUP> grouplist=new ArrayList<GROUP>();

    public ArrayList<GROUP> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(ArrayList<GROUP> grouplist) {
        this.grouplist = grouplist;
    }

    public String getPayed_orders() {
        return payed_orders;
    }

    public void setPayed_orders(String payed_orders) {
        this.payed_orders = payed_orders;
    }

    public String getWait_ship_orders() {
        return wait_ship_orders;
    }

    public void setWait_ship_orders(String wait_ship_orders) {
        this.wait_ship_orders = wait_ship_orders;
    }

    public String getShipped_orders() {
        return shipped_orders;
    }

    public void setShipped_orders(String shipped_orders) {
        this.shipped_orders = shipped_orders;
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

    public static SORDERS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        SORDERS localItem = new SORDERS();

        JSONArray subItemArray,groupArray;

        localItem.payed_orders = jsonObject.optString("payed_orders");
        localItem.wait_ship_orders = jsonObject.optString("wait_ship_orders");
        localItem.shipped_orders = jsonObject.optString("shipped_orders");
        localItem.member_orders=jsonObject.optInt("member_orders");
        localItem.anonymity_orders=jsonObject.optInt("anonymity_orders");
        groupArray=jsonObject.optJSONArray("group");
        subItemArray = jsonObject.optJSONArray("stats");
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
        localItemObject.put("payed_orders", payed_orders);
        localItemObject.put("wait_ship_orders", wait_ship_orders);
        localItemObject.put("shipped_orders", shipped_orders);

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
