package com.ecjia.hamster.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 2014/12/30.
 */
public interface HttpResponse {
    public abstract void OnMessageResponse(String url, JSONObject jo,STATUS status) throws JSONException;
}
