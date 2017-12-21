package com.ecjia.component.network.model;

import android.content.Context;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SERVICEINFO;
import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ServiceModel extends BaseModel{
    public SERVICEINFO serviceinfo;
    public ArrayList<SERVICE_MESSAGE> messageArrayList=new ArrayList<SERVICE_MESSAGE>();
    public ServiceModel(Context context){
        super(context);
    }
    public final int PAGE_COUNT = 10;
    public PAGINATED paginated;
    public void getFeedBackList(final String object_type){
        final String Url= ProtocolConst.ADMIN_FEEDBACK_LIST;
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
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
        pd.show();
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
                        messageArrayList.clear();
                        JSONObject service = jo.optJSONObject("data");
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        serviceinfo=SERVICEINFO.fromJson(service);
                        if(serviceinfo!=null&&serviceinfo.getService_messages().size()>0){
                            int size=serviceinfo.getService_messages().size();
                            for(int i=0;i<size;i++){
                                messageArrayList.add(serviceinfo.getService_messages().get(i));
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

    public void getFeedBackListMore(final String object_type){
        final String Url=ProtocolConst.ADMIN_FEEDBACK_LIST;
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            PAGINATION pagination = new PAGINATION();
            pagination.setPage(messageArrayList.size()/PAGE_COUNT+1);
            pagination.setCount(PAGE_COUNT);
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("object_type",object_type);
            requestJsonObject.put("token", SESSION.getInstance().getSid());
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI+Url,params,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                JSONObject jo;
                LG.e("返回参数==" + objectResponseInfo.result);
                try {
                    jo=new JSONObject(objectResponseInfo.result);
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject service = jo.optJSONObject("data");
                        paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                        serviceinfo=SERVICEINFO.fromJson(service);
                        if(serviceinfo!=null&&serviceinfo.getService_messages().size()>0){
                            int size=serviceinfo.getService_messages().size();
                            for(int i=0;i<size;i++){
                                messageArrayList.add(serviceinfo.getService_messages().get(i));
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

}
