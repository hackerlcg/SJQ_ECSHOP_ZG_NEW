package com.ecjia.component.network.query;

import com.ecjia.entity.Assets;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/20 13:51.
 */

public class AssetsQuery extends NormalQuery {
    public AssetsQuery() {

    }

    public String getQuery(Assets assets){
        requestJsonObject =  new JSONObject();
        try {
            requestJsonObject.put("mobile",assets.getMobile());
            requestJsonObject.put("seller_name",assets.getSellerName());
            requestJsonObject.put("bank_name",assets.getBankName());
            requestJsonObject.put("bank_card",assets.getCard());
            requestJsonObject.put("identify",assets.getIdentity());
            requestJsonObject.put("sms_code",assets.getSms());
        } catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    public String getCashQuery(String price){
        requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("money",price);
            requestJsonObject.put("note","");
        } catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    public String getSms(int type, int flag, String phone){
        requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("type",type);
            requestJsonObject.put("flag",flag);
            requestJsonObject.put("value",phone);
        } catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }
}
