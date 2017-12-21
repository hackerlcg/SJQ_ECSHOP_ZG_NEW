package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;

import com.ecjia.consts.ProtocolConst;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SALES;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.SORDERS;
import com.ecjia.hamster.model.STATS;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.VISITOR;
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

public class StatsModel extends BaseModel {

	public SALES sales = new SALES();
	public SORDERS sorders = new SORDERS();
	public VISITOR visitor = new VISITOR();
    public ArrayList<STATS> salesList = new ArrayList<STATS>();

    public MyProgressDialog pd;
    public static final int PAGE_COUNT = 20;


	public StatsModel(Context context)
	{
		super(context);
        pd= MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
	}

    //销售金额
    public void fetchSales(SESSION session,String start_date,String end_date,String api){
        final String url = ProtocolConst.STATS_SALES;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("start_date", start_date);
            requestJsonObject.put("end_date", end_date);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("=======request========"+requestJsonObject.toString());
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
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                sales = SALES.fromJson(data);
                            }
                            StatsModel.this.OnMessageResponse(url,jo,responseStatus);
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


    //订单数量
    public void fetchOrders(SESSION session,String start_date,String end_date,String api){
        final String url = ProtocolConst.STATS_ORDERS;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("start_date", start_date);
            requestJsonObject.put("end_date", end_date);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("=======request========"+requestJsonObject.toString());
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
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                sorders = SORDERS.fromJson(data);
                            }
                            StatsModel.this.OnMessageResponse(url,jo,responseStatus);
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


    //访客数量
    public void fetchVisitor(SESSION session,String start_date,String end_date,String api){
        final String url = ProtocolConst.STATS_VISITOR;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("start_date", start_date);
            requestJsonObject.put("end_date", end_date);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.e("=======request========"+requestJsonObject.toString());
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
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                visitor = VISITOR.fromJson(data);
                            }
                            StatsModel.this.OnMessageResponse(url,jo,responseStatus);
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



    //收益明细
    public PAGINATED paginated;
    public void fetchSaleDetail(SESSION session,String start_date,String end_date,String api, final boolean isfalse){
        final String url = ProtocolConst.STATS_SALESDETAIL;
        if(isfalse){
            pd.show();
        }

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("start_date", start_date);
            requestJsonObject.put("end_date", end_date);

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
                            LG.i("返回参数==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                salesList.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    salesList.clear();
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        STATS stats = STATS.fromJson(listJsonObject);
                                        salesList.add(stats);
                                    }
                                }

                            }
                            StatsModel.this.OnMessageResponse(url,jo,responseStatus);
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

    //收益明细(加载)
    public void fetchSaleDetailMore(SESSION session,String start_date,String end_date,String api){
        final String url = ProtocolConst.STATS_SALESDETAIL;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)salesList.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("start_date", start_date);
            requestJsonObject.put("end_date", end_date);
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
                                        STATS stats = STATS.fromJson(listJsonObject);
                                        salesList.add(stats);
                                    }
                                }
                            }
                            StatsModel.this.OnMessageResponse(url,jo,responseStatus);
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
