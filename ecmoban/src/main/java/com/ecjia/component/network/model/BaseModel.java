package com.ecjia.component.network.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.DEVICE;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.DeviceInfoUtil;
import com.lidroid.xutils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseModel implements  HttpResponse{

    public SharedPreferences.Editor editor;
    public DEVICE device;
    protected Context mContext;

    public Resources resources;
    public MyProgressDialog pd;
    public String sid;
    protected SharedPreferences shared;
    protected ArrayList<HttpResponse > businessResponseArrayList = new ArrayList<HttpResponse>();
    public HttpUtils httpUtils;
    public BaseModel()
    {

    }

    public BaseModel(Context context)
    {
        mContext = context;
        resources = AppConst.getResources(context);
        httpUtils=((ECJiaApplication)context.getApplicationContext()).getHttpUtils();
        pd=MyProgressDialog.createDialog(context);
        pd.setMessage(resources.getString(R.string.loading));
        shared = context.getSharedPreferences("userInfo", 0);
        editor=shared.edit();
        sid=shared.getString("sid", "");
        device=DEVICE.getInstance();
        if(TextUtils.isEmpty(device.getUdid())||TextUtils.isEmpty(device.getClient())||TextUtils.isEmpty(device.getCode())){
            device.setUdid(DeviceInfoUtil.getUdid(mContext));
            device.setClient(DeviceInfoUtil.ANDROID);
            device.setCode(DeviceInfoUtil.CODE);
        }
    }
    protected void saveCache()
    {
        return ;
    }

    protected void cleanCache()
    {
        return ;
    }


    //公共的错误处理
    public void callback(JSONObject jo)
    {

    }
    public void addResponseListener(HttpResponse listener)
    {
        if (!businessResponseArrayList.contains(listener))
        {
            businessResponseArrayList.add(listener);
        }
    }

    public void removeResponseListener(HttpResponse listener)
    {
        businessResponseArrayList.remove(listener);
    }

    public  void OnMessageResponse(String url, JSONObject jo,STATUS status) throws JSONException
    {
        for (HttpResponse iterable_element : businessResponseArrayList)
        {
            iterable_element.OnMessageResponse(url,jo,status);
        }
    }
}
