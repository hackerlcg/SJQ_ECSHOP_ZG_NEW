
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class PHOTO implements Serializable
{


    private String small;	//小图


    private String thumb;	//中图


    private String url;		//大图\

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static PHOTO fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PHOTO localItem = new PHOTO();

        JSONArray subItemArray;

        localItem.small = jsonObject.optString("small");
        localItem.thumb = jsonObject.optString("thumb");
        localItem.url = jsonObject.optString("url");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("small", small);
        localItemObject.put("thumb", thumb);
        localItemObject.put("url", url);
        return localItemObject;
    }

}
