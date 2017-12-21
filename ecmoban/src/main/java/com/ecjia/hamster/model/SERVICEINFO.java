package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/4.
 */
public class SERVICEINFO {
    private ArrayList<SERVICE_MESSAGE> service_messages=new ArrayList<SERVICE_MESSAGE>();
    private String goods_messages;
    private String orders_messages;
    private String common_messages;

    public ArrayList<SERVICE_MESSAGE> getService_messages() {
        return service_messages;
    }

    public void setService_messages(ArrayList<SERVICE_MESSAGE> service_messages) {
        this.service_messages = service_messages;
    }

    public String getGoods_messages() {
        return goods_messages;
    }

    public void setGoods_messages(String goods_messages) {
        this.goods_messages = goods_messages;
    }

    public String getOrders_messages() {
        return orders_messages;
    }

    public void setOrders_messages(String orders_messages) {
        this.orders_messages = orders_messages;
    }

    public String getCommon_messages() {
        return common_messages;
    }

    public void setCommon_messages(String common_messages) {
        this.common_messages = common_messages;
    }

    public static SERVICEINFO fromJson(JSONObject jsonObject)throws JSONException {
        if(null == jsonObject){
            return null;
        }
        SERVICEINFO localItem=new SERVICEINFO();
        localItem.goods_messages=jsonObject.optString("goods_messages");
        localItem.orders_messages=jsonObject.optString("orders_messages");
        localItem.common_messages=jsonObject.optString("common_messages");
        JSONArray jsonArray=jsonObject.optJSONArray("messages");
        localItem.service_messages.clear();
        if(jsonArray!=null&&jsonArray.length()>0){
            for(int i=0;i<jsonArray.length();i++){
                localItem.service_messages.add(SERVICE_MESSAGE.fromJson(jsonArray.optJSONObject(i)));
            }
        }
        return localItem;
    }

}
