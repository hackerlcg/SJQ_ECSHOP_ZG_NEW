package com.ecjia.component.network.requestmodel;

import com.ecjia.hamster.model.DEVICE;
import com.ecjia.hamster.model.SESSION;

import org.json.JSONException;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-10.
 */

public class BaseRequest implements Serializable {
//    public String  session=SESSION.getInstance().toJson().toString();
//    public String  device=DEVICE.getInstance().toJson().toString();

    private String session;
    private String device;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

//    SharedPreferences shared = this.getSharedPreferences("userInfo", 0);
//    uid=shared.getString("uid", "");
//    sid=shared.getString("sid","");
//    api=shared.getString("shopapi","");
//    session=new SESSION();
//    session.setUid(uid);
//    session.setSid(sid);
//
//    DEVICE device1=DEVICE.getInstance();
//    if(TextUtils.isEmpty(device1.getUdid())||TextUtils.isEmpty(device1.getClient())||TextUtils.isEmpty(device1.getCode())){
//        device1.setUdid(DeviceInfoUtil.getUdid(mContext));
//        device1.setClient(DeviceInfoUtil.ANDROID);
//        device1.setCode(DeviceInfoUtil.CODE);
//    }

    public void setInfo() {
        try {
            setSession(SESSION.getInstance().toJson().toString());
            setDevice(DEVICE.getInstance().toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
