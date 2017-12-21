package com.ecjia.component.network.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.REGIONS;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.SHOPINFO;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.LG;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShopModel extends BaseModel {

	public SHOPINFO shopinfo = new SHOPINFO();

    public ArrayList<REGIONS> regionses=new ArrayList<REGIONS>();

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public int privilege;  //0不可见，1只见，3可读可写

	public ShopModel(Context context)
	{
		super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
	}
    public void getShopInfo(SESSION session,String api){
        final String url = ProtocolConst.SHOPINFO;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        LG.i("返回参数==" + arg0.result);
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                shopinfo = SHOPINFO.fromJson(data);
                            }
                            privilege=jo.getInt("privilege");
                            ShopModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    //店铺所在区域
    public void getShopArea(SESSION session,String api,boolean flag){
        final String url = ProtocolConst.SHOPAREA;
        if(flag){
            pd.show();
        }

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("parent_id", -1);
            requestJsonObject.put("session",session.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
//                        LG.i("返回参数==" + arg0.result);
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                JSONArray ordersListJsonArray = data.optJSONArray("regions");
                                regionses.clear();
                                if (null != ordersListJsonArray && ordersListJsonArray.length() > 0)
                                {
                                    for (int i = 0; i < ordersListJsonArray.length(); i++)
                                    {
                                        JSONObject ordersListJsonObject = ordersListJsonArray.getJSONObject(i);
                                        REGIONS regions = REGIONS.fromJson(ordersListJsonObject);
                                        regionses.add(regions);
                                    }
                                }
                                editor.putString("area", ordersListJsonArray.toString());
                                editor.commit();
                            }
                            ShopModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void updateShop(SESSION session,int seller_category,String seller_telephone,int city,int provice,
                           String seller_address,String seller_description,int type,String api){
        final String url = ProtocolConst.UPDATESHOP;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            if(seller_category!=0){
                requestJsonObject.put("seller_category",seller_category);
            }
            if(provice!=0){
                requestJsonObject.put("provice",provice);
            }
            if(city!=0){
                requestJsonObject.put("city",city);
            }

            switch (type){
                case 1:
                  requestJsonObject.put("seller_telephone",seller_telephone);
                break;
                case 2:
                  requestJsonObject.put("seller_address",seller_address);
                break;
                case 3:
                  requestJsonObject.put("seller_description",seller_description);
                break;
            }

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数=="+requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(pd.isShowing()){
                                pd.dismiss();
                            }
                        JSONObject jo;
                        LG.i("返回参数==" + arg0.result);
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            ShopModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


}
