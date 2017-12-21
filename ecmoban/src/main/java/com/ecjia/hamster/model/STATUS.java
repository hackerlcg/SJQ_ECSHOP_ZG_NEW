
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class STATUS
{

    private int succeed;


    private int error_code;

    private String new_error_code;

    private String error_desc;

    public int getSucceed() {
        return succeed;
    }

    public void setSucceed(int succeed) {
        this.succeed = succeed;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getNew_error_code() {
        return new_error_code;
    }

    public void setNew_error_code(String new_error_code) {
        this.new_error_code = new_error_code;
    }

    public static STATUS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        STATUS localItem = new STATUS();

        JSONArray subItemArray;

        localItem.succeed = jsonObject.optInt("succeed");

        localItem.error_code = jsonObject.optInt("error_code");

        localItem.error_desc = jsonObject.optString("error_desc");
        return localItem;
    }

    public static STATUS newFromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        STATUS localItem = new STATUS();

        JSONArray subItemArray;

        localItem.succeed = jsonObject.optInt("succeed");

        localItem.new_error_code = jsonObject.optString("error_code");

        localItem.error_desc = jsonObject.optString("error_desc");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("succeed", succeed);
        localItemObject.put("error_code", error_code);
        localItemObject.put("error_desc", error_desc);
        return localItemObject;
    }

}
