package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.USERRANK;
import com.ecjia.hamster.model.VOLUME;
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

public class GoodsDetailModel extends BaseModel {

	public GOODS goods = new GOODS();

    public String goodsWeb;

    public MyProgressDialog pd;

	public GoodsDetailModel(Context context)
	{
		super(context);
        pd= MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
	}
    public void fetchGoodDetail(SESSION session,String id,String api){
        final String url = ProtocolConst.GOODSDETAIL;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("goods_id", id);
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
                        try {
                            LG.i("返回参数==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                goods = GOODS.fromJson(data);
                            }
                            GoodsDetailModel.this.OnMessageResponse(url,jo,responseStatus);
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
    public void fetchGoodDesc(SESSION session,String id,String api){
        final String url = ProtocolConst.GOODSDESC;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("goods_id", id);
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
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("返回参数==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                goodsWeb = jo.optString("data");
                            }
                            GoodsDetailModel.this.OnMessageResponse(url,jo,responseStatus);
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

    //修改价格
    public void updatePrice(SESSION session,String id,String shop_price,String market_price,String promote_price
            ,String promote_start_date,String promote_end_date,String give_integral,String rank_integral,String integral
            ,ArrayList<USERRANK> user_rank,ArrayList<VOLUME> volume_number,String api){
        final String url = ProtocolConst.UPDATEPRICE;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("goods_id", id);
            requestJsonObject.put("type", "add");
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("shop_price", shop_price);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("market_price", market_price);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("promote_price", promote_price);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("promote_start_date", promote_start_date);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("promote_end_date", promote_end_date);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("give_integral", give_integral);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("rank_integral", rank_integral);
            }
            if(!TextUtils.isEmpty(shop_price)){
                requestJsonObject.put("integral", integral);
            }

            if(user_rank.size()>0){
                JSONArray itemJSONArray1 = new JSONArray();
                for(int i =0; i< user_rank.size(); i++)
                {
                    USERRANK itemData =user_rank.get(i);
                    JSONObject itemJSONObject = itemData.toJson();
                    itemJSONArray1.put(itemJSONObject);
                }
                requestJsonObject.put("user_rank", itemJSONArray1);
            }
            if(volume_number.size()>0){
                JSONArray itemJSONArray2 = new JSONArray();
                for(int i =0; i< volume_number.size(); i++)
                {
                    VOLUME itemData =volume_number.get(i);
                    JSONObject itemJSONObject = itemData.toJson();
                    itemJSONArray2.put(itemJSONObject);
                }
                requestJsonObject.put("volume_number", itemJSONArray2);
            }


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
                        try {
                            LG.i("返回参数==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            GoodsDetailModel.this.OnMessageResponse(url,jo,responseStatus);
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
