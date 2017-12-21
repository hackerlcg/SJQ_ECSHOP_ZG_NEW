
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DEVICE
{

    private static DEVICE instance;

    public DEVICE(){
        instance=this;
    }

    public static DEVICE getInstance()
    {
        if (instance == null) {
            instance = new DEVICE();
        }

        return instance;
    }

    public String udid;


    public String client;

    public String code;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static DEVICE fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        DEVICE localItem = DEVICE.getInstance();

        JSONArray subItemArray;

        localItem.udid = jsonObject.optString("udid");

        localItem.client = jsonObject.optString("client");

        localItem.code = jsonObject.optString("code");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("udid", udid);
        localItemObject.put("client", client);
        localItemObject.put("code", code);
        return localItemObject;
    }

}
