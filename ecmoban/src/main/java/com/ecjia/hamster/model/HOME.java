
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HOME
{


    private String key;
    private String label;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static HOME fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        HOME localItem = new HOME();

        localItem.key = jsonObject.optString("key");
        localItem.label = jsonObject.optString("label");
        localItem.value = jsonObject.optString("value");
        return localItem;
    }

}
