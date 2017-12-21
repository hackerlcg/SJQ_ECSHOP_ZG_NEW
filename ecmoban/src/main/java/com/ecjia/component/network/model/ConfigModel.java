package com.ecjia.component.network.model;


import android.content.Context;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.CONFIG;
import com.ecjia.hamster.model.DEVICE;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.DeviceInfoUtil;
import com.ecjia.util.LG;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigModel extends BaseModel {
    public CONFIG config;



    private static ConfigModel instance;

    public static ConfigModel getInstance() {
        return instance;
    }

    public ConfigModel() {
        super();
    }

    public ConfigModel(Context context) {
        super(context);
        instance = this;
    }

    public void getConfig(String api, SESSION session) {
        final String url = ProtocolConst.CONFIG;

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("session", session.toJson());
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params, new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            LG.i("config的返回值==" + jo.toString());
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {

                                JSONObject dataJsonObject = jo.optJSONObject("data");
                                ConfigModel.this.config = CONFIG.fromJson(dataJsonObject);

//                                if (config.getShop_closed() == 1) {
//                                    Intent intent = new Intent(mContext, AppOutActivity.class);
//                                    intent.putExtra("flag", 1);
//                                    mContext.startActivity(intent);
//                                }

                                ConfigModel.this.OnMessageResponse(url, jo, responseStatus);
                            }
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


    public void setDeviceToken(String device_token) {
        DEVICE device = new DEVICE();
        device.setUdid(DeviceInfoUtil.getUdid(mContext));
        device.setClient(DeviceInfoUtil.ANDROID);
        device.setCode("5001");
        final String url = ProtocolConst.SETDEVICE_TOKEN;

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("device", device.toJson());
            if (!TextUtils.isEmpty(device_token)) {
                requestJsonObject.put("device_token", device_token);
            }

        } catch (JSONException e) {

        }
        LG.e("umengMessage===" + requestJsonObject.toString());
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI + url, params, new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        JSONObject jo;
                        try {
                            LG.e("device_token返回值==" + arg0.result);
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            ConfigModel.this.OnMessageResponse(url, jo, responseStatus);

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
