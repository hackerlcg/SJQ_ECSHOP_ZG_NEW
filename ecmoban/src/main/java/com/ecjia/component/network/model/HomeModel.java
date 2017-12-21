package com.ecjia.component.network.model;

import android.content.Context;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.HOME;
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

public class HomeModel extends BaseModel {

    public ArrayList<HOME> homes=new ArrayList<HOME>();

	public HomeModel(Context context) {
		super(context);
	}

    public void getHomeData(SESSION session,final String shopapi,boolean flag){
        final String url = ProtocolConst.HOMEDATA;
        if(flag){
            pd.show();
        }

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
        httpUtils.send(HttpRequest.HttpMethod.POST, shopapi + url, params,
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
                        LG.i("返回值==" + arg0.result);
                        try {
                            jo = new JSONObject(arg0.result);
                            HomeModel.this.callback(jo);
                            STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                JSONArray listJsonArray = data.optJSONArray("order_stats");
                                homes.clear();
                                if (null != listJsonArray && listJsonArray.length() > 0)
                                {
                                    homes.clear();
                                    for (int i = 0; i < listJsonArray.length(); i++)
                                    {
                                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                                        HOME home = HOME.fromJson(listJsonObject);
                                        homes.add(home);
                                    }
                                }
                            }
                            HomeModel.this.OnMessageResponse(url,jo,responseStatus);
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
