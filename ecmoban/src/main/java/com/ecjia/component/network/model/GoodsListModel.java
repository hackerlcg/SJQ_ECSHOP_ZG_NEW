package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;

import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecmoban.android.shopkeeper.sijiqing.R;
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

public class GoodsListModel extends BaseModel {

	public ArrayList<GOODS> goodsList = new ArrayList<GOODS>();
//	public ArrayList<GIFT> giftsList = new ArrayList<GIFT>();
	public ArrayList<CATEGORY> categories = new ArrayList<CATEGORY>();

    public MyProgressDialog pd;
    public static final int PAGE_COUNT = 10;

	public GoodsListModel(Context context)
	{
		super(context);
        pd= MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));

	}

    public void getGoodsCategory(SESSION session,String api){
       final String url = ProtocolConst.GOODSCATEGORY;
           pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("device", device.toJson());

        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                                pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray listJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                categories.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        CATEGORY item = CATEGORY.fromJson(listJsonObject);
                                        categories.add(item);
                                    }
                                }

                                for(int i = 0; i < categories.size(); i++){
                                    for(int j = 0; j < categories.size(); j++){
                                        if(categories.get(i).getCat_id()==categories.get(j).getParent_id()){
                                            categories.get(i).setHave_child(true);
                                            break;
                                        }
                                    }
                                }

                            }
                            GoodsListModel.this.OnMessageResponse(url,jo,responseStatus);
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

    public PAGINATED paginated;
    public void fetchPreSearch(SESSION session,String on_sale,String stock,String sort_by,String keywords,
            int categoryid,String api, final boolean isfalse,final boolean isGift){
       final String url = ProtocolConst.GOODS;
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
            requestJsonObject.put("on_sale",on_sale);
            requestJsonObject.put("stock",stock);
            requestJsonObject.put("sort_by",sort_by);
            requestJsonObject.put("keywords",keywords);
            if(categoryid!=0){
            requestJsonObject.put("category_id",categoryid);
            }

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
//                                        if(isGift){
//                                            giftsList.clear();
//                                        }
                                        for (int i = 0; i < listJsonArray.length(); i++)
                                        {
                                            JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                            GOODS goods = GOODS.fromJson(listJsonObject);
                                            goodsList.add(goods);
//                                            if(isGift){
//                                              giftsList.add(new GIFT(goods.getId(),goods.getName(),"",goods.getImg(),false));
//                                            }
                                        }
                                }

                            }
                            GoodsListModel.this.OnMessageResponse(url,jo,responseStatus);
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

    public void fetchPreSearchMore(SESSION session,String on_sale,String stock,String sort_by,String keywords
            ,int categoryid,String api,final boolean isGift){
        final String url = ProtocolConst.GOODS;
      //  pd.show();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)goodsList.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try
        {
            requestJsonObject.put("session",session.toJson());
            requestJsonObject.put("pagination",pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("on_sale",on_sale);
            requestJsonObject.put("stock",stock);
            requestJsonObject.put("sort_by",sort_by);
            requestJsonObject.put("keywords",keywords);
            if(categoryid!=0){
                requestJsonObject.put("category_id",categoryid);
            }
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
//                                            if(isGift){
//                                                giftsList.add(new GIFT(goods.getId(),goods.getName(),"",goods.getImg(),false));
//                                            }
                                        }
                                    }

                            }
                            GoodsListModel.this.OnMessageResponse(url,jo,responseStatus);
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
