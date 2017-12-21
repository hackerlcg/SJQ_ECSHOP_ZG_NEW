package com.ecjia.component.network.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.BRAND;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.HanziToPinyin;
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
import java.util.Collections;
import java.util.Comparator;

public class GoodsBrandModel extends BaseModel {

	public ArrayList<BRAND> brands = new ArrayList<BRAND>();

    public static final int PAGE_COUNT = 10;
    public PAGINATED paginated;

    private String sid;
    private SharedPreferences shared;

    HanziToPinyin toPinyin = HanziToPinyin.getInstance();

	public GoodsBrandModel(Context context)
	{
		super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        sid=shared.getString("sid","");
	}

    Comparator comparator = new Comparator<BRAND>() {
        @Override
        public int compare(BRAND lhs, BRAND rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };


    public void getBrandList(String keywords,String api, final boolean isfalse){
       final String url = ProtocolConst.ADMIN_GOODS_BRAND;
       if(isfalse){
           pd.show();
       }

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("keywords",keywords);
            requestJsonObject.put("pagination",pagination.toJson());

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if(isfalse){
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                brands.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        BRAND brand = BRAND.fromJson(listJsonObject);
                                        brand.setPinyin(toPinyin.getPinYin(brand.getBrand_name()));
                                        brands.add(brand);
                                    }
                                    Collections.sort(brands, comparator);
                                }

                            }
                            GoodsBrandModel.this.OnMessageResponse(url,jo,responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LG.i("返回参数==" + arg0.result);
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void getBrandListMore(String keywords,String api){
        final String url = ProtocolConst.ADMIN_GOODS_BRAND;
      //  pd.show();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)brands.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("keywords",keywords);
            requestJsonObject.put("pagination",pagination.toJson());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                     //   pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        BRAND brand = BRAND.fromJson(listJsonObject);
                                        brand.setPinyin(toPinyin.getPinYin(brand.getBrand_name()));
                                        brands.add(brand);
                                    }
                                    Collections.sort(brands, comparator);
                                }
                            }
                            GoodsBrandModel.this.OnMessageResponse(url,jo,responseStatus);
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
