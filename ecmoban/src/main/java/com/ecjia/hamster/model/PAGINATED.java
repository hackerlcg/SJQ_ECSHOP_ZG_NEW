
package com.ecjia.hamster.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PAGINATED
{

    private int total;

    private int more;

    private int count;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static PAGINATED fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        PAGINATED localItem = new PAGINATED();

        JSONArray subItemArray;

        localItem.total = jsonObject.optInt("total");

        localItem.more = jsonObject.optInt("more");

        localItem.count = jsonObject.optInt("count");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("total", total);
        localItemObject.put("more", more);
        localItemObject.put("count", count);
        return localItemObject;
    }

}
