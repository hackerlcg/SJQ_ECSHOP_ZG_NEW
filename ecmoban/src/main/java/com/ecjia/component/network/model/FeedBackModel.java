package com.ecjia.component.network.model;

import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.CUSTOMER_SERVICE_ITEM;
import com.ecjia.hamster.model.FEEDBACK_MESSAGE;
import com.ecjia.hamster.model.FEEDBACK_ORDER_ITEM;
import com.ecjia.hamster.model.FEEDBACK_USER_ITEM;
import com.ecjia.hamster.model.FEED_GOODS_ITEM;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/5.
 */
public class FeedBackModel extends BaseModel{
    public final int PAGE_COUNT = 10;
    public PAGINATED paginated;
    public ArrayList<FEEDBACK_MESSAGE> feedback_messages_list=new ArrayList<FEEDBACK_MESSAGE>();
    public FEEDBACK_USER_ITEM user_item;
    public FEEDBACK_ORDER_ITEM feedback_order_item;
    public CUSTOMER_SERVICE_ITEM customer_service_item;
    public FEED_GOODS_ITEM feed_goods_item;
    private static FeedBackModel feedBackModel;
    public static FeedBackModel getIntance(){
        return feedBackModel;
    }
    public FeedBackModel(Context context){
        super(context);
        feedBackModel=this;
    }

    public void getFeedBackMessage(String object_type,String feedback_id){
        final String Url= ProtocolConst.ADMIN_FEEDBACK_MESSAGES;
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            PAGINATION pagination = new PAGINATION();
            pagination.setPage(1);
            pagination.setCount(PAGE_COUNT);
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("object_type",object_type);
            requestJsonObject.put("token", SESSION.getInstance().getSid());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("feedback_id", feedback_id);
        } catch (JSONException e) {
        }
        pd.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI+Url,params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                pd.dismiss();
                JSONObject jo;
                LG.e("返回参数==" + objectResponseInfo.result);
                try {
                    jo=new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        feedback_messages_list.clear();
                        JSONObject dataJson = jo.optJSONObject("data");
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        JSONArray jsonArray=dataJson.optJSONArray("messages");
                        if(jsonArray!=null&&jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                feedback_messages_list.add(FEEDBACK_MESSAGE.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                        customer_service_item=CUSTOMER_SERVICE_ITEM.fromJson(dataJson.optJSONObject("customer_service_item"));
                        user_item=FEEDBACK_USER_ITEM.fromJson(dataJson.optJSONObject("user_item"));
                        feedback_order_item=FEEDBACK_ORDER_ITEM.fromJson(dataJson.optJSONObject("order_item"));
                        feed_goods_item=FEED_GOODS_ITEM.fromJson(dataJson.optJSONObject("goods_item"));
                    }
                    OnMessageResponse(Url,jo,responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
            }
        });
    }


    public void getFeedBackMessageMore(String object_type,String feedback_id){
        final String Url=ProtocolConst.ADMIN_FEEDBACK_MESSAGES;
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            PAGINATION pagination = new PAGINATION();
            pagination.setPage(feedback_messages_list.size()/PAGE_COUNT+1);
            pagination.setCount(PAGE_COUNT);
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("object_type",object_type);
            requestJsonObject.put("token", SESSION.getInstance().getSid());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("feedback_id", feedback_id);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI+Url,params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                pd.dismiss();
                JSONObject jo;
                LG.e("返回参数==" + objectResponseInfo.result);
                try {
                    jo=new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject dataJson = jo.optJSONObject("data");
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        JSONArray jsonArray=dataJson.optJSONArray("messages");
                        if(jsonArray!=null&&jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                feedback_messages_list.add(FEEDBACK_MESSAGE.fromJson(jsonArray.optJSONObject(i)));
                            }
                        }
                    }
                    OnMessageResponse(Url,jo,responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void createFeedBackString(String object_type,String content,String feedback_id){
        final String Url=ProtocolConst.ADMIN_FEEDBACK_REPLY;
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("object_type",object_type);
            requestJsonObject.put("token", SESSION.getInstance().getSid());
            requestJsonObject.put("content", content);
            requestJsonObject.put("feedback_id", feedback_id);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI+Url,params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                pd.dismiss();
                JSONObject jo;
                LG.e("返回参数==" + objectResponseInfo.result);
                try {
                    jo=new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {

                    }
                    OnMessageResponse(Url,jo,responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
