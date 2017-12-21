package com.ecjia.component.network.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.USER;
import com.ecjia.util.LG;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel extends BaseModel {

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	public LoginModel(Context context) {
		super(context);
		shared = context.getSharedPreferences("userInfo", 0);
		editor = shared.edit();
	}

    public void login(String name, final String password,final String shopapi){
        final String url = ProtocolConst.SIGNIN;
        pd.show();

        JSONObject requestJsonObject = new JSONObject();
        try
        {
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("username",name);
            requestJsonObject.put("password",password);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        LG.i("json==" + requestJsonObject.toString());
        params.addBodyParameter("json", requestJsonObject.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, shopapi + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pd.dismiss();
                        try {
                            STATUS responseStatus=new STATUS();
                            responseStatus.setSucceed(-1);
                            LoginModel.this.OnMessageResponse(url, null, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pd.dismiss();
                        JSONObject jo;
                        LG.i("json==" + arg0.result);
                        try {
                            jo = new JSONObject(arg0.result);
                            LoginModel.this.callback(jo);
                            STATUS responseStatus = STATUS.newFromJson(jo.optJSONObject("status"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONObject data = jo.optJSONObject("data");
                                SESSION session = SESSION.fromJson(data.optJSONObject("session"));
                                USER user = USER.fromJson(data.optJSONObject("userinfo"));

                                editor.putString("uid", session.uid);
                                editor.putString("sid", session.sid);
                                editor.putString("uname", user.getUsername());
                                editor.putString("shop_name", user.getShop_name());
                                editor.putString("password", password);
                                editor.putString("shopapi", shopapi);
                                editor.putString("shop_logo",user.getShop_logo());
                                editor.commit();
                            }
                            LoginModel.this.OnMessageResponse(url, jo, responseStatus);
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
