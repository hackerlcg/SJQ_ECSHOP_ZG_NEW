package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
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

public class PromotionModel extends BaseModel {

	public ArrayList<GOODS> goodsList = new ArrayList<GOODS>();
    public GOODS goods = new GOODS();

    public MyProgressDialog pd;
    public static final int PAGE_COUNT = 10;

	public PromotionModel(Context context)
	{
		super(context);
        pd= MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));

	}


    public PAGINATED paginated;
    public void getPromotionList(String status,String keywords,String api, final boolean isfalse){
       final String url = ProtocolConst.ADMIN_PROMOTION_LIST;
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
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("status",status);
            requestJsonObject.put("keywords",keywords);

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
                            LG.i("返回参数==" + jo.toString());
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                    goodsList.clear();
                                    if (null != listJsonArray && listJsonArray.length() > 0)
                                    {
                                        goodsList.clear();
                                        for (int i = 0; i < listJsonArray.length(); i++)
                                        {
                                            JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                            GOODS goods = GOODS.fromJson(listJsonObject);
                                            goodsList.add(goods);
                                        }
                                }

                            }
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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

    public void getPromotionListMore(String status,String keywords,String api){
        final String url = ProtocolConst.ADMIN_PROMOTION_LIST;
      //  pd.show();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)goodsList.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("status",status);
            requestJsonObject.put("keywords",keywords);
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
                                            GOODS goods = GOODS.fromJson(listJsonObject);
                                            goodsList.add(goods);
                                        }
                                    }

                            }
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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


    public void getPromotionDetail(String goods_id,String api){
        final String url = ProtocolConst.ADMIN_PROMOTION_DETAIL;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("goods_id", goods_id);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===传入参数===" + requestJsonObject.toString());
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
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                goods = GOODS.fromJson(data);
                            }
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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


    public void addPromotion(String goods_id,String start_time,String end_time,String promote_price,String api){
        final String url = ProtocolConst.ADMIN_PROMOTION_ADD;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("goods_id",goods_id);
            requestJsonObject.put("start_time",start_time);
            requestJsonObject.put("end_time",end_time);
            requestJsonObject.put("promote_price",promote_price);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
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
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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

    public void updatePromotion(String goods_id,String start_time,String end_time,String promote_price,String api){
        final String url = ProtocolConst.ADMIN_PROMOTION_UPDATE;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("goods_id",goods_id);
            requestJsonObject.put("start_time",start_time);
            requestJsonObject.put("end_time",end_time);
            requestJsonObject.put("promote_price",promote_price);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
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
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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

    public void deletePromotion(String goods_id,String api){
        final String url = ProtocolConst.ADMIN_PROMOTION_DELETE;
        pd.show();
        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("token",sid);
            requestJsonObject.put("goods_id",goods_id);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("传入参数==" + requestJsonObject.toString());
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
                        try {
                            String result=arg0.result;
                            jo = new JSONObject(result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            PromotionModel.this.OnMessageResponse(url,jo,responseStatus);
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

}
