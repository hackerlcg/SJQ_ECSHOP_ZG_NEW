package com.ecjia.component.network.query;


import com.ecjia.hamster.model.DEVICE;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/2 09:29.
 */

public class PageQuery extends NormalQuery {
    protected PAGINATION pagination;
    public PageQuery() {
        pagination = new PAGINATION();
        requestJsonObject = new JSONObject();
    }

    public String getQuery(int page,int count){
        pagination.setPage(page);
        if(count<=0) {
            pagination.setCount(PAGE_COUNT);
        }else{
            pagination.setCount(count);
        }
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
            e.getMessage();
        }
        return requestJsonObject.toString();
    }
    public String get(){
        return super.get();
    }

    public String getQuery(int page){
        return getQuery(page,0);
    }

    public PAGINATION getPagination(){
        return pagination;
    }

    public String getBussiness(int page,String addTime){
        if(addTime == null){
            addTime = "";
        }
        try {
            requestJsonObject.put("add_time", addTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getQuery(page,30);
    }
}
