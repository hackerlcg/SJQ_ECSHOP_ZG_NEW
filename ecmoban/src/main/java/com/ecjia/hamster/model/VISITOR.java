
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class VISITOR
{

    private String total_visitors;   //'3000',  访客人数
    private String visit_times;    //'3000',	浏览次数
    private String mobile_visitors;  //'3000',  移动app人数
    private String web_visitors;  //'3000',  网页人数

    private ArrayList<STATS> list=new ArrayList<STATS>();
    private ArrayList<GROUP> grouplist=new ArrayList<GROUP>();

    public ArrayList<GROUP> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(ArrayList<GROUP> grouplist) {
        this.grouplist = grouplist;
    }

    public String getTotal_visitors() {
        return total_visitors;
    }

    public void setTotal_visitors(String total_visitors) {
        this.total_visitors = total_visitors;
    }

    public String getVisit_times() {
        return visit_times;
    }

    public void setVisit_times(String visit_times) {
        this.visit_times = visit_times;
    }

    public String getMobile_visitors() {
        return mobile_visitors;
    }

    public void setMobile_visitors(String mobile_visitors) {
        this.mobile_visitors = mobile_visitors;
    }

    public String getWeb_visitors() {
        return web_visitors;
    }

    public void setWeb_visitors(String web_visitors) {
        this.web_visitors = web_visitors;
    }

    public ArrayList<STATS> getList() {
        return list;
    }

    public void setList(ArrayList<STATS> list) {
        this.list = list;
    }

    public static VISITOR fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        VISITOR localItem = new VISITOR();

        JSONArray subItemArray,groupArray;

        localItem.total_visitors = jsonObject.optString("total_visitors");
        localItem.visit_times = jsonObject.optString("visit_times");
        localItem.mobile_visitors = jsonObject.optString("mobile_visitors");
        localItem.web_visitors = jsonObject.optString("web_visitors");
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
        localItemObject.put("total_visitors", total_visitors);
        localItemObject.put("visit_times", visit_times);
        localItemObject.put("mobile_visitors", mobile_visitors);
        localItemObject.put("web_visitors", web_visitors);

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
