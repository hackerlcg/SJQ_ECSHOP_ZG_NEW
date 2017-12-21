package com.ecjia.component.network.model;

import android.content.Context;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.FAVOURABLE;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
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
 * Created by Administrator on 2016/3/24.
 */
public class FavourableModel extends BaseModel{
    public  FavourableModel(Context context){
        super(context);
    }
    public static final int PAGE_COUNT = 10;
    public PAGINATED paginated;
    public ArrayList<FAVOURABLE> favourableArrayList=new ArrayList<FAVOURABLE>();

    /**
     *
     * @param status
     */
    public void getFavourableList(String status,String api,final boolean isfalse){
        if(isfalse){
            pd.show();
        }
        final String url = ProtocolConst.ADMIN_FAVOURABLE_LIST;

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("status", status);
            requestJsonObject.put("pagination",pagination.toJson());

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pd.dismiss();
                        LG.e("返回值"+arg0.result);
                        JSONObject jo;
                        try {
                            jo=new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                favourableArrayList.clear();
                                JSONArray data = jo.optJSONArray("data");
                                if(data!=null&&data.length()>0) {
                                    for (int i=0;i<data.length();i++) {
                                        favourableArrayList.add(FAVOURABLE.fromJson(data.optJSONObject(i)));
                                    }
                                }
                            }
                            OnMessageResponse(url,jo,responseStatus);
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

    /**
     *
     * @param status
     */
    public void getFavourableListMore(String status,String api){
        final String url = ProtocolConst.ADMIN_FAVOURABLE_LIST;

        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int)Math.ceil((double)favourableArrayList.size()*1.0/PAGE_COUNT)+1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("token",sid);
            requestJsonObject.put("status", status);
            requestJsonObject.put("pagination",pagination.toJson());

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jo;
                        try {
                            jo=new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray data = jo.optJSONArray("data");
                                if(data!=null&&data.length()>0) {
                                    for (int i=0;i<data.length();i++) {
                                        favourableArrayList.add(FAVOURABLE.fromJson(data.optJSONObject(i)));
                                    }
                                }
                            }
                            OnMessageResponse(url,jo,responseStatus);
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
