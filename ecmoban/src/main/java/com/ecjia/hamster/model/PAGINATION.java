
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PAGINATION
{


    private int count;


    private int page;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public static PAGINATION fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PAGINATION localItem = new PAGINATION();

        JSONArray subItemArray;

        localItem.count = jsonObject.optInt("count");
        localItem.page = jsonObject.optInt("page");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("count", count);
        localItemObject.put("page", page);
        return localItemObject;
    }

}
